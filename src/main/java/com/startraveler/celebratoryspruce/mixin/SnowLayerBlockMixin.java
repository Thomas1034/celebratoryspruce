package com.startraveler.celebratoryspruce.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.startraveler.celebratoryspruce.Config;
import net.minecraft.world.level.block.SnowLayerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin {

    @Expression("11")
    @ModifyExpressionValue(method = "randomTick", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private int setSnowLayerMeltingLevel(int i) {
        return Config.SNOW_MELTING_THRESHOLD.get();
    }

    @Definition(id = "getBrightness", method = "Lnet/minecraft/server/level/ServerLevel;getBrightness(Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/BlockPos;)I")
    @Expression("?.getBrightness(?, ?) > 11")
    @ModifyExpressionValue(method = "randomTick", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private boolean checkSnowLayerMelting(boolean b) {

        return b && Config.SNOW_MELTS_IN_LIGHT.getAsBoolean();
    }
}