package com.modishmonkee.arsenalofextinction.explosion;

import com.modishmonkee.arsenalofextinction.damage.ModDamageTypes;
import com.modishmonkee.arsenalofextinction.entity.NukeManager;
import com.modishmonkee.arsenalofextinction.particle.MushroomCloudManager;
import com.modishmonkee.arsenalofextinction.particle.RadiationZoneManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomExplosion {

    private static final int BLOCKS_PER_TICK = 26000;
    private static final int CLOUD_Y_OFFSET = -20;

    public static void explode(Level level, double x, double y, double z, float radius, float Hmultiplier, float pdamage) {
        if (level.isClientSide) return;

        ServerLevel serverLevel = (ServerLevel) level;
        int r = (int) radius;

        // Pre-calculate all blocks to destroy
        List<BlockPos> toDestroy = new ArrayList<>();
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    double distSq = dx * dx + (dy * Hmultiplier) * (dy * Hmultiplier) + dz * dz;
                    double noise = (0.85 + Math.random() * 0.15) * r;
                    if (distSq <= noise * noise) {
                        BlockPos pos = new BlockPos((int) x + dx, (int) y + dy, (int) z + dz);
                        if (!level.getBlockState(pos).is(Blocks.BEDROCK)) {
                            toDestroy.add(pos);
                        }
                    }
                }
            }
        }

        toDestroy.sort(Comparator.comparingDouble(pos ->
                pos.distSqr(new BlockPos((int) x, (int) y, (int) z))));

        int totalBatches = (int) Math.ceil((double) toDestroy.size() / BLOCKS_PER_TICK);
        for (int batch = 0; batch < totalBatches; batch++) {
            int start = batch * BLOCKS_PER_TICK;
            int end = Math.min(start + BLOCKS_PER_TICK, toDestroy.size());
            List<BlockPos> batchBlocks = new ArrayList<>(toDestroy.subList(start, end));
            int delayTicks = batch;

            NukeManager.scheduleTask(delayTicks, () -> {
                for (BlockPos pos : batchBlocks) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
            });
        }

        NukeManager.scheduleTask(totalBatches, () -> {
            for (int dx = -(r - 1); dx <= r - 1; dx++) {
                for (int dz = -(r - 1); dz <= r - 1; dz++) {
                    for (int dy = -(r - 1); dy <= r - 1; dy++) {
                        double distSq = dx * dx + (dy * Hmultiplier) * (dy * Hmultiplier) + dz * dz;
                        if (distSq > r * r) continue;

                        BlockPos pos = new BlockPos((int) x + dx, (int) y + dy, (int) z + dz);
                        BlockPos above = pos.above();

                        if (!level.getBlockState(pos).isAir()
                                && !level.getBlockState(pos).is(Blocks.BEDROCK)
                                && level.getBlockState(above).isAir()) {
                            if (Math.random() < 0.15) {
                                level.setBlock(above, Blocks.FIRE.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
        });

        // Immediate effects
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, (int) x, (int) z);
        MushroomCloudManager.spawnCloud(serverLevel, x, surfaceY + CLOUD_Y_OFFSET, z);

        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)
        );
        for (LivingEntity entity : entities) {
            double dist = entity.position().distanceTo(new Vec3(x, y, z));
            if (dist <= radius) {
                float damage = (float) (pdamage * (1.0 - dist / radius));
                entity.hurt(ModDamageTypes.nukeExplosion(level), damage);
            }
        }

        RadiationZoneManager.addZone(serverLevel, x, y, z, radius, 20 * 60 * 7);
    }
}
