package com.startraveler.celebratoryspruce.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class RandomizedDecoratedLeavesBlock extends DecoratedLeavesBlock {

    public RandomizedDecoratedLeavesBlock(float particleProbability, @NotNull Properties properties, @NotNull Supplier<Ingredient> cycleIngredient) {
        super(particleProbability, properties, cycleIngredient);
    }

    @SuppressWarnings("deprecation")
    public static BlockState applyRandomVariant(final BlockState state, final BlockPos pos, final IntegerProperty variant) {
        List<Integer> values = variant.getPossibleValues();
        int selectedVariantIndex = Mth.abs((int) (Mth.getSeed(pos) % values.size()));
        return state.trySetValue(variant, values.get(selectedVariantIndex));
    }

    @Override
    public @NotNull BlockState getStateForPlacement(final @NotNull BlockPlaceContext context) {
        return applyRandomVariant(super.getStateForPlacement(context), context.getClickedPos(), VARIANT);
    }
}
