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
    private static final float CROSSHAIR_SPEED = 1.5f;
    private boolean keyW, keyS, keyA, keyD;
    private float crosshairScreenX;
    private float crosshairScreenZ;

    // ── Player position ───────────────────────────────────────────────────────
    private final double centerX;
    private final double centerY;
    private final double centerZ;
    private final float  playerYaw;

    // ── Map texture ───────────────────────────────────────────────────────────
    private volatile int[]        packedColors = null;
    private final AtomicBoolean   samplingDone = new AtomicBoolean(false);
    private final AtomicBoolean   textureReady = new AtomicBoolean(false);
    private DynamicTexture        mapTexture;
    private ResourceLocation      mapTextureId;

    private int loadingDots = 0;
    private int loadingTick = 0;

    // ── Border colours ────────────────────────────────────────────────────────
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

                double worldDX = offX * BLOCKS_PER_PIXEL;
                double worldDZ = offZ * BLOCKS_PER_PIXEL;

                double rotX = worldDX * cosY - worldDZ * sinY;
                double rotZ = worldDX * sinY + worldDZ * cosY;

                int worldX = (int)(centerX + rotX);
                int worldZ = (int)(centerZ + rotZ);

                int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ) - 1;
                mPos.set(worldX, y, worldZ);
                BlockState state = level.getBlockState(mPos);
                MapColor mapColor = state.getMapColor(level, mPos);

                int baseColor = mapColor.col;
                if (baseColor == 0) {
                    out[pz * MAP_PIXEL_SIZE + px] = 0;
                    continue;
                }

                // Shade based on height difference
                mPos2.set(worldX, y, worldZ - 1);
                int nY = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ - 1) - 1;
                int shade;
                if (y > nY) shade = 2;
                else if (y < nY) shade = 0;
                else shade = 1;

                float mult = SHADE_MULT[shade];
                int r = (int)(((baseColor >> 16) & 0xFF) * mult);
                int g = (int)(((baseColor >>  8) & 0xFF) * mult);
                int b = (int)((baseColor & 0xFF) * mult);
                out[pz * MAP_PIXEL_SIZE + px] = 0xFF000000 | (r << 16) | (g << 8) | b;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_W -> { keyW = true; return true; }
            case GLFW.GLFW_KEY_S -> { keyS = true; return true; }
            case GLFW.GLFW_KEY_A -> { keyA = true; return true; }
            case GLFW.GLFW_KEY_D -> { keyD = true; return true; }
            case GLFW.GLFW_KEY_SPACE -> {
                launchNuke();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_W -> { keyW = false; return true; }
            case GLFW.GLFW_KEY_S -> { keyS = false; return true; }
            case GLFW.GLFW_KEY_A -> { keyA = false; return true; }
            case GLFW.GLFW_KEY_D -> { keyD = false; return true; }
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() { return false; }

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
            drawCrosshair(g, mapLeft + (int)crosshairScreenX, mapTop + (int)crosshairScreenZ);

            double[] wc = crosshairToWorld();
            String coords = String.format("§fX: §e%.0f  §fZ: §e%.0f", wc[0], wc[1]);
            g.drawCenteredString(font, coords, width / 2, mapTop + DISPLAY_SIZE + 6, 0xFFFFFFFF);
            g.drawCenteredString(font, "§7[WASD] Move  [SPACE] Launch  [ESC] Cancel",
                    width / 2, mapTop + DISPLAY_SIZE + 17, 0xFF_AAAAAA);
        } else {
            g.fill(mapLeft, mapTop, mapLeft + DISPLAY_SIZE, mapTop + DISPLAY_SIZE, 0xFF_000000);
            drawMapBorder(g, mapLeft, mapTop);
            loadingTick++;
            if (loadingTick % 10 == 0) loadingDots = (loadingDots + 1) % 4;
            String dots = ".".repeat(loadingDots);
            g.drawCenteredString(font, "§7Scanning terrain" + dots,
                    width / 2, mapTop + DISPLAY_SIZE / 2 - 4, 0xFF_AAAAAA);
        }
    }

    private void drawMapBorder(GuiGraphics g, int mapLeft, int mapTop) {
        int x0 = mapLeft - BORDER_OUTER_SIDE;
        int y0 = mapTop  - BORDER_OUTER_TB;
        int x1 = mapLeft + DISPLAY_SIZE + BORDER_OUTER_SIDE;
        int y1 = mapTop  + DISPLAY_SIZE + BORDER_OUTER_TB;

        g.fill(x0, y0, x1, y0 + BORDER_OUTER_TB, MAP_BORDER_OUTER);
        g.fill(x0, y1 - BORDER_OUTER_TB, x1, y1, MAP_BORDER_OUTER);
        g.fill(x0, y0, x0 + BORDER_OUTER_SIDE, y1, MAP_BORDER_OUTER);
        g.fill(x1 - BORDER_OUTER_SIDE, y0, x1, y1, MAP_BORDER_OUTER);

        int ix0 = mapLeft - BORDER_INNER_SIDE;
        int iy0 = mapTop  - BORDER_INNER_TB;
        int ix1 = mapLeft + DISPLAY_SIZE + BORDER_INNER_SIDE;
        int iy1 = mapTop  + DISPLAY_SIZE + BORDER_INNER_TB;

        g.fill(ix0, iy0, ix1, iy0 + BORDER_INNER_TB, MAP_BORDER_INNER);
        g.fill(ix0, iy1 - BORDER_INNER_TB, ix1, iy1, MAP_BORDER_INNER);
        g.fill(ix0, iy0, ix0 + BORDER_INNER_SIDE, iy1, MAP_BORDER_INNER);
        g.fill(ix1 - BORDER_INNER_SIDE, iy0, ix1, iy1, MAP_BORDER_INNER);
    }

    private void drawPlayerArrow(GuiGraphics g, int cx, int cy) {
        g.fill(cx - 1, cy - 4, cx + 1, cy + 4, 0xFF_FFFF00);
        g.fill(cx - 3, cy - 1, cx + 3, cy + 1, 0xFF_FFFF00);
    }

    private void drawCrosshair(GuiGraphics g, int cx, int cy) {
        int size = 6;
        g.fill(cx - size, cy - 1, cx + size, cy + 1, 0xFF_FF0000);
        g.fill(cx - 1, cy - size, cx + 1, cy + size, 0xFF_FF0000);
    }

    private double[] crosshairToWorld() {
        float offX = crosshairScreenX - DISPLAY_SIZE / 2.0f;
        float offZ = crosshairScreenZ - DISPLAY_SIZE / 2.0f;

        double worldDX = offX * BLOCKS_PER_PIXEL;
        double worldDZ = offZ * BLOCKS_PER_PIXEL;

        double yawRad = Math.toRadians(playerYaw);
        double cosY = Math.cos(yawRad);
        double sinY = Math.sin(yawRad);

        double rotX = worldDX * cosY - worldDZ * sinY;
        double rotZ = worldDX * sinY + worldDZ * cosY;

        return new double[]{ centerX + rotX, centerZ + rotZ };
    }

    private void launchNuke() {
        double[] wc = crosshairToWorld();
        Level level = Minecraft.getInstance().level;
        double targetY = centerY;
        if (level != null) {
            int surfY = level.getHeight(Heightmap.Types.WORLD_SURFACE, (int) wc[0], (int) wc[1]);
            targetY = surfY;
        }
        PacketDistributor.sendToServer(new LaunchNukePacket(wc[0], targetY, wc[1]));
        this.onClose();
        Minecraft.getInstance().setScreen(null);
    }
}
