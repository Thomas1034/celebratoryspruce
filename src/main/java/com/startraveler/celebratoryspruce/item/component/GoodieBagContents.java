package com.startraveler.celebratoryspruce.item.component;

import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Stream;

public record GoodieBagContents(List<ItemStack> stacks, int maxSize) {
    public static final Codec<GoodieBagContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(GoodieBagContents::stacks),
            Codec.INT.fieldOf("max_size").forGetter(GoodieBagContents::maxSize)
    ).apply(instance, GoodieBagContents::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GoodieBagContents> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            GoodieBagContents::stacks,
            ByteBufCodecs.INT,
            GoodieBagContents::maxSize,
            GoodieBagContents::new
    );

    @SuppressWarnings("unused")
    public GoodieBagContents() {
        this(0);
    }

    public GoodieBagContents(int maxSize) {
        this(List.of(), maxSize);
    }

    public GoodieBagContents {
        if (stacks.size() > maxSize) {
            throw new IllegalArgumentException("ItemStack list exceeds maximum size.");
        }
    }

    public List<ItemStack> copyStacks() {
        return this.stacks.stream().map(ItemStack::copy).toList();
    }

    public GoodieBagContents withAdditionalOrNull(ItemStack stack) {
        if (this.maxSize > this.stacks.size()) {
            return new GoodieBagContents(Streams.concat(this.stacks.stream(), Stream.of(stack)).toList(), this.maxSize);
        }
        return null;
    }


}
