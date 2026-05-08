package com.modishmonkee.arsenalofextinction.client.screen;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.modishmonkee.arsenalofextinction.network.LaunchNukePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@OnlyIn(Dist.CLIENT)
public class NukeTargetMapScreen extends Screen {

    // ── Map world coverage ────────────────────────────────────────────────────
    private static final int MAP_PIXEL_SIZE   = 384;
    private static final int BLOCKS_PER_PIXEL = 1;

    // ── Display size ──────────────────────────────────────────────────────────
    private static final int DISPLAY_SIZE = (int) (MAP_PIXEL_SIZE * 0.75f);

    // ── Crosshair WASD movement ───────────────────────────────────────────────
    private static final float CROSSHAIR_SPEED  = 0.6f;

    // ── Crosshair appearance ──────────────────────────────────────────────────
    private static final float CH_LINE_LENGTH   = 10f;
    private static final float CH_GAP           = 8f;
    private static final float CH_THICKNESS     = 0f;
    private static final int   CH_FILL_COLOR    = 0xFF_66CCFF;
    private static final int   CH_OUTLINE_COLOR = 0xFF_0033AA;

    // ── Player arrow appearance ───────────────────────────────────────────────
    private static final int   ARROW_FILL_COLOR = 0xFF_66CCFF;
    private static final int   ARROW_SCALE      = 1;

    private float crosshairScreenX;
    private float crosshairScreenZ;
    private boolean keyW, keyA, keyS, keyD;

    private final double centerX;
    private final double centerY;
    private final double centerZ;
    private final float  playerYaw;

    private volatile int[]      packedColors = null;
    private final AtomicBoolean samplingDone = new AtomicBoolean(false);
    private final AtomicBoolean textureReady = new AtomicBoolean(false);

    private DynamicTexture   mapTexture;
    private ResourceLocation mapTextureId;

    private int loadingDots = 0;
    private int loadingTick = 0;

    private static final int MAP_BORDER_OUTER = 0xFF_000107;
    private static final int MAP_BORDER_INNER = 0xFF_101010;

    private static final int BORDER_OUTER_TB   = 8;
    private static final int BORDER_OUTER_SIDE = 8;
    private static final int BORDER_INNER_TB   = 4;
    private static final int BORDER_INNER_SIDE = 4;

    private static final int PARCHMENT_A = 0xFF_C8A46A;
    private static final int PARCHMENT_B = 0xFF_B8944E;

    private static final float[] SHADE_MULT = { 180f/255f, 220f/255f, 1.0f, 135f/255f };

    public NukeTargetMapScreen(double playerX, double playerY, double playerZ, float playerYaw) {
        super(Component.literal("Nuke Targeting System"));
        this.centerX   = playerX;
        this.centerY   = playerY;
        this.centerZ   = playerZ;
        this.playerYaw = playerYaw;
        this.crosshairScreenX = DISPLAY_SIZE / 2.0f;
        this.crosshairScreenZ = DISPLAY_SIZE / 2.0f;

        Level level = Minecraft.getInstance().level;
        CompletableFuture.runAsync(() -> {
            int[] colors = new int[MAP_PIXEL_SIZE * MAP_PIXEL_SIZE];
            sampleMapColors(level, colors);
            packedColors = colors;
            samplingDone.set(true);
        });
    }

    private void uploadTexture() {
        int[] colors = packedColors;
        if (colors == null) return;

        mapTexture = new DynamicTexture(MAP_PIXEL_SIZE, MAP_PIXEL_SIZE, false);
        var pixels = mapTexture.getPixels();
        if (pixels == null) return;

        for (int pz = 0; pz < MAP_PIXEL_SIZE; pz++) {
            for (int px = 0; px < MAP_PIXEL_SIZE; px++) {
                int argb = colors[pz * MAP_PIXEL_SIZE + px];
                if (argb == 0) argb = ((px ^ pz) & 1) == 0 ? PARCHMENT_A : PARCHMENT_B;

                // NativeImage uses ABGR, convert from ARGB
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >>  8) & 0xFF;
                int b = (argb      ) & 0xFF;
                int abgr = (a << 24) | (b << 16) | (g << 8) | r;
                pixels.setPixelRGBA(px, pz, abgr);
            }
        }

        mapTexture.upload();
        mapTextureId = Minecraft.getInstance().getTextureManager()
                .register("nuke_target_map", mapTexture);
        textureReady.set(true);
    }

    @Override
    public void onClose() {
        if (mapTextureId != null) {
            Minecraft.getInstance().getTextureManager().release(mapTextureId);
            mapTextureId = null;
        }
        if (mapTexture != null) {
            mapTexture.close();
            mapTexture = null;
        }
        super.onClose();
    }

    // ── Exact Forge sampleMapColors: full Multiset/water-depth algorithm, negated rotation ──
    private void sampleMapColors(Level level, int[] out) {
        if (level == null) return;

        double yawRad = Math.toRadians(playerYaw);
        double cosY   = Math.cos(yawRad);
        double sinY   = Math.sin(yawRad);

        int half = MAP_PIXEL_SIZE / 2;
        BlockPos.MutableBlockPos mPos  = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mPos2 = new BlockPos.MutableBlockPos();

        for (int pz = 0; pz < MAP_PIXEL_SIZE; pz++) {
            double d0 = 0.0;
            for (int px = 0; px < MAP_PIXEL_SIZE; px++) {
                int offX = px - half;
                int offZ = pz - half;

                // Exact Forge rotation (negated): -offX*cos + offZ*sin, -offX*sin - offZ*cos
                int worldDX = (int) Math.round(-offX * cosY + offZ * sinY);
                int worldDZ = (int) Math.round(-offX * sinY - offZ * cosY);

                int worldX = (int) centerX + worldDX * BLOCKS_PER_PIXEL;
                int worldZ = (int) centerZ + worldDZ * BLOCKS_PER_PIXEL;

                int idx = pz * MAP_PIXEL_SIZE + px;

                if (!level.hasChunk(worldX >> 4, worldZ >> 4)) {
                    out[idx] = 0;
                    continue;
                }

                Multiset<MapColor> multiset = LinkedHashMultiset.create();
                double d1 = 0.0;
                int waterDepth = 0;

                for (int si = 0; si < BLOCKS_PER_PIXEL; si++) {
                    for (int sj = 0; sj < BLOCKS_PER_PIXEL; sj++) {
                        mPos.set(worldX + si, 0, worldZ + sj);
                        int k = level.getHeight(Heightmap.Types.WORLD_SURFACE, mPos.getX(), mPos.getZ()) + 1;
                        BlockState blockState;
                        if (k <= level.getMinBuildHeight() + 1) {
                            blockState = Blocks.BEDROCK.defaultBlockState();
                        } else {
                            do {
                                mPos.setY(--k);
                                blockState = level.getBlockState(mPos);
                            } while (blockState.getMapColor(level, mPos) == MapColor.NONE && k > level.getMinBuildHeight());

                            if (k > level.getMinBuildHeight() && !blockState.getFluidState().isEmpty()) {
                                int waterY = k - 1;
                                mPos2.set(mPos);
                                BlockState below;
                                do {
                                    mPos2.setY(waterY--);
                                    below = level.getBlockState(mPos2);
                                    waterDepth++;
                                } while (waterY > level.getMinBuildHeight() && !below.getFluidState().isEmpty());
                            }
                        }
                        d1 += (double) k / (double)(BLOCKS_PER_PIXEL * BLOCKS_PER_PIXEL);
                        multiset.add(blockState.getMapColor(level, mPos));
                    }
                }

                waterDepth /= BLOCKS_PER_PIXEL * BLOCKS_PER_PIXEL;
                MapColor mapColor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.NONE);

                MapColor.Brightness brightness;
                if (mapColor == MapColor.WATER) {
                    double d2 = waterDepth * 0.1 + (double)((px + pz & 1)) * 0.2;
                    if      (d2 < 0.5) brightness = MapColor.Brightness.HIGH;
                    else if (d2 > 0.9) brightness = MapColor.Brightness.LOW;
                    else               brightness = MapColor.Brightness.NORMAL;
                } else {
                    double d3 = (d1 - d0) * 4.0 / (double)(BLOCKS_PER_PIXEL + 4)
                            + ((double)((px + pz & 1)) - 0.5) * 0.4;
                    if      (d3 > 0.6)  brightness = MapColor.Brightness.HIGH;
                    else if (d3 < -0.6) brightness = MapColor.Brightness.LOW;
                    else                brightness = MapColor.Brightness.NORMAL;
                }

                d0 = d1;
                out[idx] = (mapColor == MapColor.NONE) ? 0xFF_888888 : mapColorToArgb(mapColor, brightness);
            }
        }
    }

    private static int mapColorToArgb(MapColor color, MapColor.Brightness brightness) {
        float m = SHADE_MULT[brightness.id];
        int r = Math.min(255, (int)(((color.col >> 16) & 0xFF) * m));
        int g = Math.min(255, (int)(((color.col >>  8) & 0xFF) * m));
        int b = Math.min(255, (int)(( color.col        & 0xFF) * m));
        return 0xFF_000000 | (r << 16) | (g << 8) | b;
    }

    @Override
    public void tick() {
        if (!samplingDone.get()) {
            if (++loadingTick % 10 == 0) loadingDots = (loadingDots + 1) % 4;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_W -> { keyW = true; return true; }
            case GLFW.GLFW_KEY_S -> { keyS = true; return true; }
            case GLFW.GLFW_KEY_A -> { keyA = true; return true; }
            case GLFW.GLFW_KEY_D -> { keyD = true; return true; }
            case GLFW.GLFW_KEY_SPACE -> { if (textureReady.get()) launchNuke(); return true; }
            case GLFW.GLFW_KEY_ESCAPE -> { this.onClose(); return true; }
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_W -> { keyW = false; return true; }
            case GLFW.GLFW_KEY_S -> { keyS = false; return true; }
            case GLFW.GLFW_KEY_A -> { keyA = false; return true; }
            case GLFW.GLFW_KEY_D -> { keyD = false; return true; }
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() { return false; }

    // Exact Forge: renderBackground overridden to do nothing (no vanilla darkening)
    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {}

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, width, height, 0x88_000000);

        int mapLeft = (width  - DISPLAY_SIZE) / 2;
        int mapTop  = (height - DISPLAY_SIZE) / 2;

        if (samplingDone.get() && !textureReady.get()) {
            uploadTexture();
        }

        if (textureReady.get() && mapTextureId != null) {
            g.blit(mapTextureId, mapLeft, mapTop, 0, 0, DISPLAY_SIZE, DISPLAY_SIZE,
                    DISPLAY_SIZE, DISPLAY_SIZE);

            drawMapBorder(g, mapLeft, mapTop);

            int centreScreenX = mapLeft + DISPLAY_SIZE / 2;
            int centreScreenZ = mapTop  + DISPLAY_SIZE / 2;
            drawPlayerArrow(g, centreScreenX, centreScreenZ);

            if (keyW) crosshairScreenZ -= CROSSHAIR_SPEED;
            if (keyS) crosshairScreenZ += CROSSHAIR_SPEED;
            if (keyA) crosshairScreenX -= CROSSHAIR_SPEED;
            if (keyD) crosshairScreenX += CROSSHAIR_SPEED;
            crosshairScreenX = Math.max(0, Math.min(DISPLAY_SIZE - 1, crosshairScreenX));
            crosshairScreenZ = Math.max(0, Math.min(DISPLAY_SIZE - 1, crosshairScreenZ));

            // Exact Forge: drawCrosshair takes floats
            drawCrosshair(g, mapLeft + crosshairScreenX, mapTop + crosshairScreenZ);

            double[] wc = crosshairToWorld();
            String coords = String.format("§fX: §e%.0f  §fZ: §e%.0f", wc[0], wc[1]);
            g.drawCenteredString(font, coords, width / 2, mapTop + DISPLAY_SIZE + 6, 0xFFFFFFFF);
            g.drawCenteredString(font, "§7[WASD] Move  [SPACE] Launch  [ESC] Cancel",
                    width / 2, mapTop + DISPLAY_SIZE + 17, 0xFF_AAAAAA);
        } else {
            g.fill(mapLeft, mapTop, mapLeft + DISPLAY_SIZE, mapTop + DISPLAY_SIZE, 0xFF_000000);
            drawMapBorder(g, mapLeft, mapTop);
            String dots = ".".repeat(loadingDots);
            g.drawCenteredString(font, "§7Scanning terrain" + dots,
                    width / 2, mapTop + DISPLAY_SIZE / 2 - 4, 0xFF_AAAAAA);
        }
    }

    private void drawMapBorder(GuiGraphics g, int mapLeft, int mapTop) {
        int s  = DISPLAY_SIZE;
        int ot = BORDER_OUTER_TB;
        int os = BORDER_OUTER_SIDE;
        int it = BORDER_INNER_TB;
        int is = BORDER_INNER_SIDE;

        // Outer border
        g.fill(mapLeft - os, mapTop - ot,      mapLeft + s + os, mapTop,           MAP_BORDER_OUTER);
        g.fill(mapLeft - os, mapTop + s,        mapLeft + s + os, mapTop + s + ot,  MAP_BORDER_OUTER);
        g.fill(mapLeft - os, mapTop,            mapLeft,           mapTop + s,      MAP_BORDER_OUTER);
        g.fill(mapLeft + s,  mapTop,            mapLeft + s + os,  mapTop + s,      MAP_BORDER_OUTER);

        // Inner border
        g.fill(mapLeft - is, mapTop - it,      mapLeft + s + is, mapTop,           MAP_BORDER_INNER);
        g.fill(mapLeft - is, mapTop + s,        mapLeft + s + is, mapTop + s + it,  MAP_BORDER_INNER);
        g.fill(mapLeft - is, mapTop,            mapLeft,           mapTop + s,      MAP_BORDER_INNER);
        g.fill(mapLeft + s,  mapTop,            mapLeft + s + is,  mapTop + s,      MAP_BORDER_INNER);
    }

    // Exact Forge pixel-art arrow: ARROW_FILL_COLOR=0xFF_66CCFF, ARROW_SCALE=1
    private void drawPlayerArrow(GuiGraphics g, int cx, int cz) {
        int f = ARROW_FILL_COLOR;
        int S = ARROW_SCALE;
        int top = cz - 5 * S;

        // 8 solid rows (the arrowhead/body)
        int[][] solid = { {0,0},{-1,1},{-1,1},{-2,2},{-2,2},{-3,3},{-3,3},{-4,4} };
        for (int row = 0; row < solid.length; row++) {
            g.fill(cx + solid[row][0]*S, top + row*S, cx + solid[row][1]*S + S, top + row*S + S, f);
        }
        // 6 more rows (tail)
        g.fill(cx - 4*S, top + 8*S,  cx,        top + 9*S,  f);
        g.fill(cx + S,   top + 8*S,  cx + 5*S,  top + 9*S,  f);
        g.fill(cx - 4*S, top + 9*S,  cx - S,    top + 10*S, f);
        g.fill(cx + 2*S, top + 9*S,  cx + 5*S,  top + 10*S, f);
        g.fill(cx - 4*S, top + 10*S, cx - 2*S,  top + 11*S, f);
        g.fill(cx + 3*S, top + 10*S, cx + 5*S,  top + 11*S, f);
    }

    // Exact Forge crosshair: outline+fill, CH_LINE_LENGTH=10, CH_GAP=8, CH_THICKNESS=0
    private void drawCrosshair(GuiGraphics g, float cx, float cz) {
        float lineLen = CH_LINE_LENGTH;
        float gap     = CH_GAP;
        float thick   = CH_THICKNESS;
        int fill    = CH_FILL_COLOR;
        int outline = CH_OUTLINE_COLOR;

        // Horizontal left arm
        fillF(g, cx - gap - lineLen, cz - thick - 1, cx - gap, cz + thick + 2, outline);
        fillF(g, cx - gap - lineLen, cz - thick,     cx - gap, cz + thick + 1, fill);
        // Horizontal right arm
        fillF(g, cx + gap, cz - thick - 1, cx + gap + lineLen, cz + thick + 2, outline);
        fillF(g, cx + gap, cz - thick,     cx + gap + lineLen, cz + thick + 1, fill);
        // Vertical top arm
        fillF(g, cx - thick - 1, cz - gap - lineLen, cx + thick + 2, cz - gap, outline);
        fillF(g, cx - thick,     cz - gap - lineLen, cx + thick + 1, cz - gap, fill);
        // Vertical bottom arm
        fillF(g, cx - thick - 1, cz + gap, cx + thick + 2, cz + gap + lineLen, outline);
        fillF(g, cx - thick,     cz + gap, cx + thick + 1, cz + gap + lineLen, fill);
    }

    private static void fillF(GuiGraphics g, float x0, float y0, float x1, float y1, int color) {
        g.fill(Math.round(x0), Math.round(y0), Math.round(x1), Math.round(y1), color);
    }

    // Exact Forge crosshairToWorld: mapPx scale, negated rotation
    private double[] crosshairToWorld() {
        double yawRad = Math.toRadians(playerYaw);
        double cosY   = Math.cos(yawRad);
        double sinY   = Math.sin(yawRad);

        float mapPx = crosshairScreenX * MAP_PIXEL_SIZE / (float) DISPLAY_SIZE;
        float mapPz = crosshairScreenZ * MAP_PIXEL_SIZE / (float) DISPLAY_SIZE;
        float offX  = mapPx - MAP_PIXEL_SIZE / 2.0f;
        float offZ  = mapPz - MAP_PIXEL_SIZE / 2.0f;

        // Exact Forge negated rotation
        double worldDX = -offX * cosY + offZ * sinY;
        double worldDZ = -offX * sinY - offZ * cosY;

        return new double[]{ centerX + worldDX * BLOCKS_PER_PIXEL, centerZ + worldDZ * BLOCKS_PER_PIXEL };
    }

    private void launchNuke() {
        double[] wc = crosshairToWorld();
        Minecraft mc = Minecraft.getInstance();
        double targetY = centerY;
        if (mc.level != null) {
            targetY = mc.level.getHeight(Heightmap.Types.WORLD_SURFACE, (int) wc[0], (int) wc[1]);
        }
        PacketDistributor.sendToServer(new LaunchNukePacket(wc[0], targetY, wc[1]));
        this.onClose();
    }
}
