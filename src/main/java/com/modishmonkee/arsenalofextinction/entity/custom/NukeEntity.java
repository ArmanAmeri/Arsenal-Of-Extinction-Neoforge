package com.modishmonkee.arsenalofextinction.entity.custom;

import com.modishmonkee.arsenalofextinction.entity.NukeManager;
import com.modishmonkee.arsenalofextinction.explosion.CustomExplosion;
import com.modishmonkee.arsenalofextinction.network.ModNetwork;
import com.modishmonkee.arsenalofextinction.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class NukeEntity extends Projectile {

    private static final EntityDataAccessor<Float> TARGET_X =
            SynchedEntityData.defineId(NukeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_Y =
            SynchedEntityData.defineId(NukeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_Z =
            SynchedEntityData.defineId(NukeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> START_X =
            SynchedEntityData.defineId(NukeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> START_Y =
            SynchedEntityData.defineId(NukeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> START_Z =
            SynchedEntityData.defineId(NukeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> CURRENT_TICK =
            SynchedEntityData.defineId(NukeEntity.class, EntityDataSerializers.INT);

    private int totalTicks = 75;
    private int currentTick = 0;
    private final Set<ChunkPos> forcedChunks = new HashSet<>();

    private float clientSmoothTick = 0f;
    private int lastKnownServerTick = 0;

    public NukeEntity(EntityType<? extends NukeEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (!this.level().isClientSide) {
            NukeManager.register(this);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(TARGET_X, 0f);
        builder.define(TARGET_Y, 0f);
        builder.define(TARGET_Z, 0f);
        builder.define(START_X, 0f);
        builder.define(START_Y, 0f);
        builder.define(START_Z, 0f);
        builder.define(CURRENT_TICK, 0);
    }

    public void setTarget(double targetX, double targetY, double targetZ) {
        this.entityData.set(START_X, (float) this.getX());
        this.entityData.set(START_Y, (float) this.getY());
        this.entityData.set(START_Z, (float) this.getZ());
        this.entityData.set(TARGET_X, (float) targetX);
        this.entityData.set(TARGET_Y, (float) targetY);
        this.entityData.set(TARGET_Z, (float) targetZ);

        double dx = targetX - this.getX();
        double dy = targetY - this.getY();
        double dz = targetZ - this.getZ();
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDist));
        this.setYRot(yaw);
        this.setXRot(pitch);
        this.yRotO = yaw;
        this.xRotO = pitch;
    }

    private Vec3 getStartPos() {
        return new Vec3(entityData.get(START_X), entityData.get(START_Y), entityData.get(START_Z));
    }

    private Vec3 getTargetPos() {
        return new Vec3(entityData.get(TARGET_X), entityData.get(TARGET_Y), entityData.get(TARGET_Z));
    }

    private Vec3 getControlPos() {
        Vec3 start = getStartPos();
        Vec3 target = getTargetPos();
        return new Vec3(
                (start.x + target.x) / 2,
                (start.y + target.y) / 2 + 80,
                (start.z + target.z) / 2);
    }

    public Vec3 getBezierPoint(float t) {
        Vec3 start = getStartPos();
        Vec3 control = getControlPos();
        Vec3 end = getTargetPos();
        double x = (1 - t) * (1 - t) * start.x + 2 * (1 - t) * t * control.x + t * t * end.x;
        double y = (1 - t) * (1 - t) * start.y + 2 * (1 - t) * t * control.y + t * t * end.y;
        double z = (1 - t) * (1 - t) * start.z + 2 * (1 - t) * t * control.z + t * t * end.z;
        return new Vec3(x, y, z);
    }

    public void preloadPathChunks(ServerLevel serverLevel, Vec3 start, Vec3 target) {
        Vec3 control = new Vec3(
                (start.x + target.x) / 2,
                (start.y + target.y) / 2 + 80,
                (start.z + target.z) / 2);

        Set<ChunkPos> chunks = new HashSet<>();
        for (int i = 0; i <= 500; i++) {
            float t = (float) i / 500;
            double x = (1 - t) * (1 - t) * start.x + 2 * (1 - t) * t * control.x + t * t * target.x;
            double z = (1 - t) * (1 - t) * start.z + 2 * (1 - t) * t * control.z + t * t * target.z;
            chunks.add(new ChunkPos((int) x >> 4, (int) z >> 4));
        }
        for (ChunkPos chunk : chunks) {
            serverLevel.getChunkSource().updateChunkForced(chunk, true);
            forcedChunks.add(chunk);
        }
    }

    public void unloadPathChunks(ServerLevel serverLevel) {
        for (ChunkPos chunk : forcedChunks) {
            serverLevel.getChunkSource().updateChunkForced(chunk, false);
        }
        forcedChunks.clear();
    }

    public int getCurrentTick() { return currentTick; }
    public int getTotalTicks()  { return totalTicks; }

    private void applyPositionFromT(float t) {
        if (t < 0f) t = 0f;
        if (t > 1f) t = 1f;

        Vec3 pos = getBezierPoint(t);
        this.setPos(pos.x, pos.y, pos.z);

        if (t > 0f) {
            float prevT = Math.max(0f, t - 0.01f);
            Vec3 prev = getBezierPoint(prevT);
            double dx = pos.x - prev.x;
            double dy = pos.y - prev.y;
            double dz = pos.z - prev.z;
            double horizontalDist = Math.sqrt(dx * dx + dz * dz);
            float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
            float pitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDist));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
            this.setYRot(wrapDegrees(yaw));
            this.setXRot(wrapDegrees(pitch));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            serverTick();
        } else {
            clientTick();
        }
    }

    private void serverTick() {
        if (currentTick == 0) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    ModSounds.NUKE_FALLING.get(), SoundSource.HOSTILE, 8.0f, 1.0f);
        }

        if (currentTick >= totalTicks) {
            onReachTarget();
            return;
        }

        currentTick++;
        this.entityData.set(CURRENT_TICK, currentTick);
        float t = (float) currentTick / totalTicks;
        applyPositionFromT(t);
    }

    private void clientTick() {
        int serverTick = this.entityData.get(CURRENT_TICK);
        if (serverTick != lastKnownServerTick) {
            if (Math.abs(serverTick - clientSmoothTick) > 5) {
                clientSmoothTick = serverTick;
            }
            lastKnownServerTick = serverTick;
        }

        clientSmoothTick += 1.0f;
        clientSmoothTick = Math.min(clientSmoothTick, totalTicks - 1f);

        float t = clientSmoothTick / totalTicks;
        applyPositionFromT(t);
    }

    private float wrapDegrees(float degrees) {
        degrees = degrees % 360.0f;
        if (degrees >= 180.0f)  degrees -= 360.0f;
        if (degrees < -180.0f) degrees += 360.0f;
        return degrees;
    }

    private boolean hasExploded = false;

    private void onReachTarget() {
        if (hasExploded) return;
        hasExploded = true;

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            double targetX = this.entityData.get(TARGET_X);
            double targetY = this.entityData.get(TARGET_Y);
            double targetZ = this.entityData.get(TARGET_Z);

            CustomExplosion.explode(this.level(), targetX, targetY, targetZ, 80.0f, 1.6f, 1000.0f);

            serverLevel.getEntitiesOfClass(Player.class,
                            new AABB(targetX - 150, targetY - 150, targetZ - 150,
                                    targetX + 150, targetY + 150, targetZ + 150))
                    .forEach(player -> {
                        if (player instanceof ServerPlayer serverPlayer) {
                            ModNetwork.sendFlashbang(serverPlayer);
                            ModNetwork.sendScreenShake(serverPlayer);
                        }
                    });

            for (ServerPlayer serverPlayer : serverLevel.players()) {
                ModNetwork.sendNukeExplosionSound(serverPlayer);
            }

            unloadPathChunks(serverLevel);
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        onReachTarget();
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            unloadPathChunks(serverLevel);
        }
        super.remove(reason);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        Vec3 target = getTargetPos();
        Vec3 start = getStartPos();
        tag.putDouble("targetX", target.x);
        tag.putDouble("targetY", target.y);
        tag.putDouble("targetZ", target.z);
        tag.putDouble("startX", start.x);
        tag.putDouble("startY", start.y);
        tag.putDouble("startZ", start.z);
        tag.putInt("currentTick", currentTick);
        tag.putInt("totalTicks", totalTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("targetX")) {
            this.entityData.set(TARGET_X, (float) tag.getDouble("targetX"));
            this.entityData.set(TARGET_Y, (float) tag.getDouble("targetY"));
            this.entityData.set(TARGET_Z, (float) tag.getDouble("targetZ"));
        }
        if (tag.contains("startX")) {
            this.entityData.set(START_X, (float) tag.getDouble("startX"));
            this.entityData.set(START_Y, (float) tag.getDouble("startY"));
            this.entityData.set(START_Z, (float) tag.getDouble("startZ"));
        }
        this.currentTick = tag.getInt("currentTick");
        this.totalTicks  = tag.getInt("totalTicks");
        this.entityData.set(CURRENT_TICK, this.currentTick);
        this.clientSmoothTick    = this.currentTick;
        this.lastKnownServerTick = this.currentTick;
    }
}
