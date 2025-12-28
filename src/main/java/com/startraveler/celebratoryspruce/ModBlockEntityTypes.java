package com.startraveler.celebratoryspruce;

import com.startraveler.celebratoryspruce.block.entity.ItemHoldingBlockEntity;
import com.startraveler.celebratoryspruce.block.entity.ItemRenderingBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockEntityTypes {
    // Create a Deferred Register to hold Blocks which will all be registered under the "celebratoryspruce" namespace
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
            Registries.BLOCK_ENTITY_TYPE,
            CelebratorySpruce.MODID
    );
    public static final DeferredHolder<BlockEntityType<?>, @NotNull BlockEntityType<@NotNull ItemHoldingBlockEntity>> ITEM_HOLDING_BLOCK = BLOCK_ENTITY_TYPES.register(
            "item_holding_block",
            () -> new BlockEntityType<>(
                    ItemHoldingBlockEntity::new,
                    Set.of(
                            ModBlocks.PRESENT_PILE.get()
                    )
            )
    );
    public static final DeferredHolder<BlockEntityType<?>, @NotNull BlockEntityType<@NotNull ItemRenderingBlockEntity>> ITEM_RENDERING_BLOCK = BLOCK_ENTITY_TYPES.register(
            "item_rendering_block",
            () -> new BlockEntityType<>(
                    ItemRenderingBlockEntity::new,
                    Set.of(
                            ModBlocks.GOLD_STAR.get(),
                            ModBlocks.WALL_GOLD_STAR.get(),
                            ModBlocks.ITEM_DISPLAY.get(),
                            ModBlocks.WALL_ITEM_DISPLAY.get()
                    )
            )
    );


}
