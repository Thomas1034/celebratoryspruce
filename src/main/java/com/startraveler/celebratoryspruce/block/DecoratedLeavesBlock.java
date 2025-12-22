package com.startraveler.celebratoryspruce.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class DecoratedLeavesBlock extends TintedParticleLeavesBlock {

    public static final int MIN_VARIANTS = 0;
    public static final int MAX_VARIANTS = 7;
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", MIN_VARIANTS, MAX_VARIANTS);
    protected final @NotNull Supplier<Ingredient> cycleIngredient;

    public DecoratedLeavesBlock(float particleProbability, @NotNull Properties properties, @NotNull Supplier<Ingredient> cycleIngredient) {
        super(particleProbability, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(VARIANT, MIN_VARIANTS));
        this.cycleIngredient = cycleIngredient;
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (stack.is(this.cycleIngredient.get()::acceptsItem)) {
            List<Integer> values = VARIANT.getPossibleValues();
            int currentValueIndex = values.indexOf(state.getValue(VARIANT));
            int numValues = values.size();
            int newValueIndex = (currentValueIndex + 1) % numValues;
            level.setBlockAndUpdate(pos, state.trySetValue(VARIANT, values.get(newValueIndex)));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VARIANT);
    }
}
