package com.startraveler.celebratoryspruce.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.startraveler.celebratoryspruce.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IceBlock.class)
public class IceBlockMixin {

    @Expression("11")
    @ModifyExpressionValue(method = "randomTick", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private int setSnowLayerMeltingLevel(int i) {
        return Config.SNOW_MELTING_THRESHOLD.get();
    }

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/IceBlock;melt(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void checkSnowLayerMelting(IceBlock instance, BlockState state, Level level, BlockPos pos, Operation<Void> original) {
        if (Config.SNOW_MELTS_IN_LIGHT.getAsBoolean()) {
            original.call(instance, state, level, pos);
        }
    }
}
