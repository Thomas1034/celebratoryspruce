package com.startraveler.celebratoryspruce.block.entity;

import com.startraveler.celebratoryspruce.CelebratorySpruce;
import com.startraveler.celebratoryspruce.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class ItemHoldingBlockEntity extends BlockEntity {
    public static final String ITEM_KEY = "item";
    protected @NotNull ItemStack stack;

    public ItemHoldingBlockEntity(BlockPos pos, BlockState blockState) {
        this(ModBlockEntityTypes.ITEM_HOLDING_BLOCK.get(), pos, blockState);
    }

    public ItemHoldingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.stack = ItemStack.EMPTY;
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        // load item and transforms
        this.stack = input.read(ITEM_KEY, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    // Save values into the passed CompoundTag here.
    @Override
    public void saveAdditional(@NotNull ValueOutput output) {

        super.saveAdditional(output);

        output.store(ITEM_KEY, ItemStack.OPTIONAL_CODEC, this.stack);
    }

    @Override
    public Packet<@NotNull ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(
                this::toString,
                CelebratorySpruce.LOGGER
        )) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
            this.saveAdditional(output);
            tag.merge(output.buildResult());
        }
        return tag;
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.level != null) {
            Containers.dropContents(this.level, pos, NonNullList.of(ItemStack.EMPTY, this.stack));
        }
    }


    public @NotNull ItemStack getStoredItemStack() {
        return this.stack;
    }

    public void setStoredItemStack(ItemStack stack) {
        this.stack = stack;
        this.markAsChanged();
    }

    public void markAsChanged() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(
                    this.getBlockPos(),
                    this.getBlockState(),
                    this.getBlockState(),
                    Block.UPDATE_ALL
            );
        }
    }
}
