package com.startraveler.celebratoryspruce.block;

import com.startraveler.celebratoryspruce.block.entity.ItemHoldingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class ItemHoldingBoxPileBlock extends BoxPileBlock implements EntityBlock {
    public static final BooleanProperty CLOSED = BooleanProperty.create("closed");

    public ItemHoldingBoxPileBlock(Properties properties, Supplier<Ingredient> canIncreaseSize) {
        super(properties, canIncreaseSize);
        this.registerDefaultState(this.defaultBlockState().setValue(CLOSED, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ItemHoldingBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (this.canChangeItem(state, level, pos)) {
            if (!level.isClientSide()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof ItemHoldingBlockEntity itemHoldingBlockEntity) {
                    ItemStack stack = itemHoldingBlockEntity.getStoredItemStack().copyAndClear();
                    if (!stack.isEmpty()) {
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
                    }
                    player.addItem(stack);
                    itemHoldingBlockEntity.setStoredItemStack(ItemStack.EMPTY);

                }
            }
            return InteractionResult.CONSUME;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected boolean triggerEvent(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity != null && blockentity.triggerEvent(id, param);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (this.canChangeItem(state, level, pos) && !stack.is(this.canIncreaseSize.get().getValues())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ItemHoldingBlockEntity itemHoldingBlockEntity && !level.isClientSide()) {
                ItemStack oldStack = itemHoldingBlockEntity.getStoredItemStack();
                if (oldStack.isEmpty()) {
                    ItemStack newStack = stack.copyAndClear();
                    itemHoldingBlockEntity.setStoredItemStack(newStack);
                    level.setBlockAndUpdate(pos, state.trySetValue(CLOSED, true));
                    if (!newStack.isEmpty()) {
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
                        return InteractionResult.SUCCESS_SERVER;
                    }
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CLOSED);
    }

    @SuppressWarnings("unused")
    public boolean canChangeItem(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return !state.getValueOrElse(CLOSED, false);
    }

}
