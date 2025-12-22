package com.startraveler.celebratoryspruce.mixin;

import com.startraveler.celebratoryspruce.HasDescriptionId;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin implements HasDescriptionId {
}
