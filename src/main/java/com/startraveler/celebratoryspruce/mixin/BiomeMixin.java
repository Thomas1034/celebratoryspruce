package com.startraveler.celebratoryspruce.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.startraveler.celebratoryspruce.Config;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
public class BiomeMixin {

    @Expression("10")
    @ModifyExpressionValue(method = "shouldSnow", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private int lightLevelAtWhichShouldSnow(int i) {
        return Config.SNOW_ACCUMULATION_THRESHOLD.get();
    }

    @Expression("10")
    @ModifyExpressionValue(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private int lightLevelAtWhichShouldFreeze(int i) {
        return Config.ICE_MELTING_THRESHOLD.get();
    }

    @Expression("0.15")
    @ModifyExpressionValue(method = "warmEnoughToRain", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private float temperatureForRain(float original) {
        return Config.SNOW_TEMPERATURE_THRESHOLD.get().floatValue();
    }
}
