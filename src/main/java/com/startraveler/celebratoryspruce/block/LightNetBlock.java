package com.startraveler.celebratoryspruce.block;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class LightNetBlock extends MultifaceSpreadeableBlock {

    public static final MapCodec<LightNetBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            Ingredient.CODEC.xmap(Suppliers::ofInstance, Supplier::get)
                    .fieldOf("ingredient")
                    .forGetter(lightNetBlock -> lightNetBlock.cycleIngredient)
    ).apply(instance, LightNetBlock::new));
    public static final int MIN_VARIANTS = 0;
    public static final int MAX_VARIANTS = 7;
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", MIN_VARIANTS, MAX_VARIANTS);
    protected final @NotNull Supplier<@NotNull Ingredient> cycleIngredient;
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public LightNetBlock(@NotNull Properties properties, @NotNull Supplier<@NotNull Ingredient> cycleIngredient) {
        super(properties);
        this.cycleIngredient = cycleIngredient;
    }

    public static ToIntFunction<BlockState> emission(int light) {
        return (state) -> MultifaceBlock.hasAnyFace(state) ? light : 0;
    }

    @Override
    public @NotNull MapCodec<LightNetBlock> codec() {
        return CODEC;
    }

    public @NotNull MultifaceSpreader getSpreader() {
        return this.spreader;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VARIANT);

    }

    @Override
    public @Nullable BlockState getStateForPlacement(final @NotNull BlockPlaceContext context) {
        return RandomizedDecoratedLeavesBlock.applyRandomVariant(
                super.getStateForPlacement(context),
                context.getClickedPos(),
                VARIANT
        );
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

    protected boolean propagatesSkylightDown(BlockState state) {
        return state.getFluidState().isEmpty();
    }
}

