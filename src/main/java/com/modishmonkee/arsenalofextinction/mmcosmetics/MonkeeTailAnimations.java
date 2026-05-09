package com.modishmonkee.arsenalofextinction.mmcosmetics;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MonkeeTailAnimations {

    public static final AnimationDefinition ANIM_TAIL_IDLE = AnimationDefinition.Builder.withLength(2.0F).looping()
            .addAnimation("bone7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F,  KeyframeAnimations.degreeVec(21.0805F, 20.5347F, 11.317F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(14.8351F, -1.2521F, 1.9364F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F,  KeyframeAnimations.degreeVec(10.0845F, 7.3855F, 1.3096F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(4.9764F, -6.4438F, -1.3007F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F,  KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F),           AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(5.0058F, -8.9343F, -1.5202F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F,  KeyframeAnimations.degreeVec(10.0845F, -7.3855F, -1.3096F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(14.915F, 5.9972F, 3.862F),     AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F,  KeyframeAnimations.degreeVec(21.0805F, 20.5347F, 11.317F),  AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("bone1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F,  KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, -12.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F,  KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -12.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F,  KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 12.5F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F,  KeyframeAnimations.degreeVec(0.0F, 20.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 12.5F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F,  KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("bone2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F,  KeyframeAnimations.degreeVec(0.0F, 12.5F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F,  KeyframeAnimations.degreeVec(0.0F, -12.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F,  KeyframeAnimations.degreeVec(0.0F, -12.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F,  KeyframeAnimations.degreeVec(0.0F, 12.5F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 20.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F,  KeyframeAnimations.degreeVec(0.0F, 12.5F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("bone3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F,  KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F,  KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F,  KeyframeAnimations.degreeVec(20.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F,  KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F,  KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("bone4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F,  KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F,  KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F,  KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(20.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F,  KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F,  KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("bone5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F,  KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F,  KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F,  KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F,  KeyframeAnimations.degreeVec(20.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F,  KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("bone6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F,  KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F,  KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F,  KeyframeAnimations.degreeVec(5.0F, 8.5F, 0.0F),    AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F),   AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F,  KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(20.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F,  KeyframeAnimations.degreeVec(15.0F, -8.5F, 0.0F),  AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
}
