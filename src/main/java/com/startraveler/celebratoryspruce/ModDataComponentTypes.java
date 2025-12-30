package com.startraveler.celebratoryspruce;

import com.mojang.serialization.Codec;
import com.startraveler.celebratoryspruce.item.component.GoodieBagContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(
            Registries.DATA_COMPONENT_TYPE,
            CelebratorySpruce.MODID
    );

    public static final DeferredHolder<DataComponentType<?>, @NotNull DataComponentType<Integer>> DEFAULT_GOODIE_BAG_SIZE = DATA_COMPONENT_TYPES.register(
            "default_goodie_bag_size",
            () -> DataComponentType.<Integer>builder()
                    .networkSynchronized(ByteBufCodecs.INT)
                    .persistent(Codec.INT)
                    .build()
    );

    public static final DeferredHolder<DataComponentType<?>, @NotNull DataComponentType<GoodieBagContents>> GOODIE_BAG_CONTENTS = DATA_COMPONENT_TYPES.register(
            "goodie_bag_contents",
            () -> DataComponentType.<GoodieBagContents>builder()
                    .networkSynchronized(GoodieBagContents.STREAM_CODEC)
                    .persistent(GoodieBagContents.CODEC)
                    .build()
    );

}
