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
}
