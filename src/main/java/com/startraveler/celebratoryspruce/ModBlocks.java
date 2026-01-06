package com.startraveler.celebratoryspruce;

import com.google.common.base.Suppliers;
import com.startraveler.celebratoryspruce.block.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "celebratoryspruce" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CelebratorySpruce.MODID);
    public static final Set<Supplier<ExtensibleCandleCakeBlock>> CANDLE_CAKES = new HashSet<>();
    public static final DeferredBlock<@NotNull SaplingBlock> CELEBRATORY_SPRUCE_SAPLING = register(
            "celebratory_spruce_sapling",
            (properties) -> new SaplingBlock(CelebratorySpruceTreeGrowers.CELEBRATORY_SPRUCE, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SAPLING)
    );
    @SuppressWarnings("deprecation")
    public static final DeferredBlock<@NotNull FlowerPotBlock> POTTED_CELEBRATORY_SPRUCE_SAPLING = registerWithoutItem(
            "potted_celebratory_spruce_sapling",
            (properties) -> new FlowerPotBlock(ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get(), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_SPRUCE_SAPLING).noOcclusion()
    );
    public static final DeferredBlock<@NotNull LightNetBlock> LIGHT_NET = register(
            "light_net",
            properties -> new LightNetBlock(properties, Suppliers.memoize(() -> Ingredient.of(ModItems.FESTIVE_LIGHT))),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLOW_LICHEN)
                    .lightLevel(LightNetBlock.emission(5))
                    .noOcclusion()
    );
    public static final DeferredBlock<@NotNull StandingItemDisplayingBlock> GOLD_STAR = registerWithoutItem(
            "gold_star",
            properties -> new StandingItemDisplayingBlock(Optional.of(ParticleTypes.END_ROD), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK).lightLevel(state -> 15).noOcclusion()
    );
    public static final DeferredBlock<@NotNull WallItemDisplayingBlock> WALL_GOLD_STAR = registerWithoutItem(
            "wall_gold_star",
            properties -> new WallItemDisplayingBlock(Optional.of(ParticleTypes.END_ROD), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK).lightLevel(state -> 14).noOcclusion()
    );
    public static final DeferredBlock<@NotNull InteractableStandingItemDisplayingBlock> ITEM_DISPLAY = registerWithoutItem(
            "item_display",
            properties -> new InteractableStandingItemDisplayingBlock(Optional.empty(), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).noOcclusion()
    );
    public static final DeferredBlock<@NotNull InteractableWallItemDisplayingBlock> WALL_ITEM_DISPLAY = registerWithoutItem(
            "wall_item_display",
            properties -> new InteractableWallItemDisplayingBlock(Optional.empty(), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).noOcclusion()
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
    public static final DeferredBlock<@NotNull ItemHoldingBoxPileBlock> PRESENT_PILE = registerWithoutItem(
            "present_pile",
            (properties) -> new ItemHoldingBoxPileBlock(
                    properties,
                    Suppliers.memoize(() -> Ingredient.of(ModItems.PRESENT))
            ),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL).noOcclusion()
    );

    public static final DeferredBlock<@NotNull ExtensibleCakeBlock> FRUIT_CAKE = registerWithoutItem(
            "fruit_cake", properties -> new ExtensibleCakeBlock(
                    properties,
                    new FoodProperties.Builder().nutrition(2).saturationModifier(0.5f).build(),
                    Consumable.builder()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(
                                            MobEffects.SLOWNESS,
                                            200,
                                            0
                                    ), 1.0f
                            ))
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(
                                            MobEffects.RESISTANCE,
                                            200,
                                            0
                                    ), 1.0f
                            ))
                            .build()
            ), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)
    );

    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "candle_fruit_cake",
                    (properties) -> new

                            ExtensibleCandleCakeBlock(() -> Blocks.CANDLE, ModBlocks.FRUIT_CAKE, properties),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> WHITE_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "white_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.WHITE_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> ORANGE_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "orange_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.ORANGE_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> MAGENTA_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "magenta_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.MAGENTA_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> LIGHT_BLUE_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "light_blue_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.LIGHT_BLUE_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> YELLOW_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "yellow_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.YELLOW_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> LIME_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "lime_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.LIME_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> PINK_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "pink_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.PINK_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> GRAY_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "gray_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.GRAY_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> LIGHT_GRAY_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "light_gray_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.LIGHT_GRAY_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> CYAN_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "cyan_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.CYAN_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> PURPLE_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "purple_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.PURPLE_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> BLUE_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "blue_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.BLUE_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> BROWN_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "brown_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.BROWN_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> GREEN_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "green_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.GREEN_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> RED_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "red_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.RED_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );
    @SuppressWarnings("unused")
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> BLACK_CANDLE_FRUIT_CAKE = addToSet(
            registerWithoutItem(
                    "black_candle_fruit_cake",
                    (properties) -> new ExtensibleCandleCakeBlock(
                            () -> Blocks.BLACK_CANDLE,
                            ModBlocks.FRUIT_CAKE,
                            properties
                    ),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

            ), CANDLE_CAKES
    );


    public static final DeferredBlock<@NotNull ExtensibleCakeBlock> YULE_LOG_CAKE = registerWithoutItem(
            "yule_log_cake", properties -> new LogCakeBlock(
                    properties,
                    new FoodProperties.Builder().nutrition(2).saturationModifier(0.5f).build(),
                    Consumable.builder()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(
                                            MobEffects.HASTE,
                                            200,
                                            0
                                    ), 1.0f
                            ))
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(
                                            MobEffects.JUMP_BOOST,
                                            200,
                                            0
                                    ), 1.0f
                            ))
                            .build()
            ), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)
    );


    @SuppressWarnings("unused")
    public static <T extends Block> Supplier<@NotNull T> addToSet(Supplier<@NotNull T> cake, Set<Supplier<T>> set) {
        set.add(cake);
        return cake;
    }

    public static <T extends Block> DeferredBlock<@NotNull T> addToSet(DeferredBlock<@NotNull T> cake, Set<Supplier<T>> set) {
        set.add(cake);
        return cake;
    }

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
