package com.modishmonkee.arsenalofextinction.effect;

import com.modishmonkee.arsenalofextinction.damage.ModDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadiatedEffect extends MobEffect {

    private static final float DAMAGE_PER_LEVEL = 2.0f;
    private static final int DAMAGE_INTERVAL_TICKS = 200;
    public static final int EFFECT_DURATION_TICKS = 20 * 60 * 7 + 15;
    public static final int TICKS_PER_LEVEL_GAIN = 20;

    public RadiatedEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        float damage = (amplifier + 1) * DAMAGE_PER_LEVEL;

        double motionX = entity.getDeltaMovement().x;
        double motionY = entity.getDeltaMovement().y;
        double motionZ = entity.getDeltaMovement().z;

        entity.hurt(ModDamageTypes.radiated(entity.level()), damage);

        entity.setDeltaMovement(motionX, motionY, motionZ);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % DAMAGE_INTERVAL_TICKS == 0;
    }
}
