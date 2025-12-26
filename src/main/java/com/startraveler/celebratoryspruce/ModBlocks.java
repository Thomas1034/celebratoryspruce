package com.startraveler.celebratoryspruce;

import com.google.common.base.Suppliers;
import com.startraveler.celebratoryspruce.block.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "celebratoryspruce" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CelebratorySpruce.MODID);

    public static final DeferredBlock<@NotNull SaplingBlock> CELEBRATORY_SPRUCE_SAPLING = register(
            "celebratory_spruce_sapling",
            (properties) -> new SaplingBlock(CelebratorySpruceTreeGrowers.CELEBRATORY_SPRUCE, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SAPLING)
    );

    @SuppressWarnings("deprecation")
    public static final DeferredBlock<@NotNull FlowerPotBlock> POTTED_CELEBRATORY_SPRUCE_SAPLING = registerWithoutItem(
            "potted_celebratory_spruce_sapling",
            (properties) -> new FlowerPotBlock(
                    ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get(),
                    properties
            ),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_SPRUCE_SAPLING)
    );

    public static final DeferredBlock<@NotNull LightNetBlock> LIGHT_NET = register(
            "light_net",
            properties -> new LightNetBlock(properties, Suppliers.memoize(() -> Ingredient.of(ModItems.FESTIVE_LIGHT))),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLOW_LICHEN).lightLevel(LightNetBlock.emission(5))
    );

    public static final DeferredBlock<@NotNull StarBlock> GOLD_STAR = registerWithoutItem(
            "gold_star",
            properties -> new StandingStarBlock(ParticleTypes.END_ROD, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK).lightLevel(state -> 15)
    );

    public static final DeferredBlock<@NotNull WallStarBlock> WALL_GOLD_STAR = registerWithoutItem(
            "wall_gold_star",
            properties -> new WallStarBlock(ParticleTypes.END_ROD, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK).lightLevel(state -> 14)
    );


    public static final DeferredBlock<@NotNull DecoratedLeavesBlock> DECORATED_SPRUCE_LEAVES = register(
            "decorated_spruce_leaves",
            (properties) -> new RandomizedDecoratedLeavesBlock(
                    0.005f,
                    properties,
                    Suppliers.memoize(() -> Ingredient.of(ModItems.ORNAMENT))
            ),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES)
    );

    public static final DeferredBlock<@NotNull DecoratedLeavesBlock> FESTIVE_SPRUCE_LEAVES = register(
            "festive_spruce_leaves",
            (properties) -> new DecoratedLeavesBlock(
                    0.005f,
                    properties,
                    Suppliers.memoize(() -> Ingredient.of(ModItems.FESTIVE_LIGHT))
            ),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES).lightLevel(state -> 5)
    );

    public static final DeferredBlock<@NotNull DecoratedWreathBlock> DECORATED_WREATH = registerWithoutItem(
            "decorated_wreath",
            properties -> new DecoratedWreathBlock(
                    properties,
                    () -> Ingredient.of(ModItems.ORNAMENT, ModItems.FESTIVE_LIGHT)
            ),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES)
    );

    public static final DeferredBlock<@NotNull DecoratedWallWreathBlock> DECORATED_WALL_WREATH = registerWithoutItem(
            "decorated_wall_wreath",
            properties -> new DecoratedWallWreathBlock(
                    properties,
                    () -> Ingredient.of(ModItems.ORNAMENT, ModItems.FESTIVE_LIGHT)
            ),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES)
    );

    public static final DeferredBlock<@NotNull WreathBlock> WREATH = registerWithoutItem(
            "wreath",
            WreathBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES)
    );

    public static final DeferredBlock<@NotNull WallWreathBlock> WALL_WREATH = registerWithoutItem(
            "wall_wreath",
            WallWreathBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES)
    );

    public static <T extends Block> DeferredBlock<T> register(final String name, final Function<BlockBehaviour.Properties, T> block, final Supplier<BlockBehaviour.Properties> properties) {
        return register(
                name,
                block,
                properties,
                deferredBlock -> ((itemProperties) -> new BlockItem(
                        deferredBlock.get(),
                        itemProperties.useBlockDescriptionPrefix()
                ))
        );
    }

    protected static <T extends Block> DeferredBlock<T> register(final String name, final Function<BlockBehaviour.Properties, T> block, final Supplier<BlockBehaviour.Properties> properties, final Function<DeferredBlock<T>, Function<Item.Properties, ? extends BlockItem>> item, final Supplier<Item.Properties> itemProperties) {
        var reg = BLOCKS.registerBlock(name, block, properties);
        ModItems.register(name, item.apply(reg), itemProperties);
        return reg;
    }

    protected static <T extends Block> DeferredBlock<T> register(final String name, final Function<BlockBehaviour.Properties, T> block, final Supplier<BlockBehaviour.Properties> properties, final Function<DeferredBlock<T>, Function<Item.Properties, ? extends BlockItem>> item) {
        return register(name, block, properties, item, Item.Properties::new);
    }

    public static <T extends Block> DeferredBlock<T> registerWithoutItem(final String name, final Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
        return BLOCKS.registerBlock(name, block, properties);
    }
}
