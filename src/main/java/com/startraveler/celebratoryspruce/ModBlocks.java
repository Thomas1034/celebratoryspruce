package com.startraveler.celebratoryspruce;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.startraveler.celebratoryspruce.block.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

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

    public static final DeferredBlock<@NotNull StandingItemDisplayingBlock> DIAMOND_STAR = registerWithoutItem(
            "diamond_star",
            properties -> new StandingItemDisplayingBlock(Optional.of(ParticleTypes.END_ROD), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).lightLevel(state -> 15).noOcclusion()
    );
    public static final DeferredBlock<@NotNull WallItemDisplayingBlock> WALL_DIAMOND_STAR = registerWithoutItem(
            "wall_diamond_star",
            properties -> new WallItemDisplayingBlock(Optional.of(ParticleTypes.END_ROD), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).lightLevel(state -> 14).noOcclusion()
    );
    public static final DeferredBlock<@NotNull StandingItemDisplayingBlock> COPPER_STAR = registerWithoutItem(
            "copper_star",
            properties -> new StandingItemDisplayingBlock(Optional.of(ParticleTypes.END_ROD), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK).lightLevel(state -> 15).noOcclusion()
    );
    public static final DeferredBlock<@NotNull WallItemDisplayingBlock> WALL_COPPER_STAR = registerWithoutItem(
            "wall_copper_star",
            properties -> new WallItemDisplayingBlock(Optional.of(ParticleTypes.END_ROD), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK).lightLevel(state -> 14).noOcclusion()
    );
    public static final DeferredBlock<@NotNull StandingItemDisplayingBlock> IRON_STAR = registerWithoutItem(
            "iron_star",
            properties -> new StandingItemDisplayingBlock(Optional.of(ParticleTypes.END_ROD), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK).lightLevel(state -> 15).noOcclusion()
    );
    public static final DeferredBlock<@NotNull WallItemDisplayingBlock> WALL_IRON_STAR = registerWithoutItem(
            "wall_iron_star",
            properties -> new WallItemDisplayingBlock(Optional.of(ParticleTypes.END_ROD), properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK).lightLevel(state -> 14).noOcclusion()
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
    public static final ImmutableBiMap<@NotNull DyeColor, @NotNull DeferredBlock<@NotNull ItemHoldingBoxPileBlock>> PRESENT_PILES_BY_COLOR = blockForEachColor(
            Util.DYES,
            ModBlocks::presentPileForColor
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
    public static final DeferredBlock<@NotNull ExtensibleCandleCakeBlock> CANDLE_FRUIT_CAKE = registerWithoutItem(
            "candle_fruit_cake",
            (properties) -> new

                    ExtensibleCandleCakeBlock(() -> Blocks.CANDLE, ModBlocks.FRUIT_CAKE, properties),
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)
    );
    public static final ImmutableBiMap<DyeColor, DeferredBlock<@NotNull ExtensibleCandleCakeBlock>> CANDLE_FRUIT_CAKES_BY_COLOR = blockForEachColor(
            Util.DYES,
            ModBlocks.candleFruitCakeForColor(FRUIT_CAKE)
    );
    public static final ImmutableSet<DeferredBlock<@NotNull ExtensibleCandleCakeBlock>> CANDLE_FRUIT_CAKES = ImmutableSet.<DeferredBlock<@NotNull ExtensibleCandleCakeBlock>>builder()
            .add(CANDLE_FRUIT_CAKE)
            .addAll(CANDLE_FRUIT_CAKES_BY_COLOR.values())
            .build();

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
    public static final DeferredBlock<@NotNull LogFireBlock> LOG_FIRE = register(
            "log_fire",
            properties -> new LogFireBlock(true, 1, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PODZOL)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .lightLevel(litBlockEmission(15))
                    .noOcclusion()
                    .ignitedByLava()
    );

    static {
        BLOCKS.addAlias(CelebratorySpruce.id("present_pile"), CelebratorySpruce.id("red_present_pile"));
    }

    public static DeferredBlock<@NotNull ItemHoldingBoxPileBlock> presentPileForColor(DyeColor color) {

        return registerWithoutItem(
                color.getName() + "_present_pile", (properties) -> new ItemHoldingBoxPileBlock(
                        properties,
                        Suppliers.memoize(() -> ModItems.PRESENTS_BY_COLOR.get(color) instanceof ItemLike item ? Ingredient.of(
                                item) : Ingredient.of())
                ), () -> BlockBehaviour.Properties.ofFullCopy(Objects.requireNonNull(
                        Util.WOOL_BY_COLOR.get(color),
                        "Wool color '" + color.getName() + "' is not stored in the wool colors map (encountered when registering a present pile)."
                )).noOcclusion()
        );
    }

    public static Function<DyeColor, DeferredBlock<@NotNull ExtensibleCandleCakeBlock>> candleFruitCakeForColor(Supplier<ExtensibleCakeBlock> baseCake) {
        return (color) -> candleFruitCakeForColor(color, baseCake);
    }

    public static DeferredBlock<@NotNull ExtensibleCandleCakeBlock> candleFruitCakeForColor(DyeColor color, Supplier<ExtensibleCakeBlock> baseCake) {
        return registerWithoutItem(
                color.getName() + "_candle_fruit_cake", (properties) -> new ExtensibleCandleCakeBlock(
                        () -> Objects.requireNonNull(
                                Util.CANDLES_BY_COLOR.get(color),
                                "Candle color '" + color.getName() + "' is not stored in the candle colors map (encountered when registering a candle fruit cake)."
                        ), baseCake, properties
                ), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)

        );
    }

    public static <T extends Block> ImmutableBiMap<DyeColor, DeferredBlock<@NotNull T>> blockForEachColor(Collection<DyeColor> colors, Function<DyeColor, DeferredBlock<@NotNull T>> generator) {
        ImmutableBiMap.Builder<DyeColor, DeferredBlock<@NotNull T>> builder = ImmutableBiMap.builder();

        for (DyeColor color : colors) {
            builder.put(color, generator.apply(color));
        }

        return builder.build();
    }

    public static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> (Boolean) state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
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
