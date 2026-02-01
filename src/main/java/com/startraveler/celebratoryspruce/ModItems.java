package com.startraveler.celebratoryspruce;

import com.google.common.collect.ImmutableBiMap;
import com.startraveler.celebratoryspruce.item.GoodieBagItem;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    // Create a Deferred Register to hold Items which will all be registered under the "celebratoryspruce" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CelebratorySpruce.MODID);
    public static final DeferredItem<@NotNull Item> ORNAMENT = register("ornament");
    public static final DeferredItem<@NotNull Item> FESTIVE_LIGHT = register("festive_light");
    public static final DeferredItem<@NotNull StandingAndWallBlockItem> DIAMOND_STAR = register(
            "diamond_star",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.DIAMOND_STAR.get(),
                    ModBlocks.WALL_DIAMOND_STAR.get(),
                    Direction.DOWN,
                    properties
            )
    );
    public static final DeferredItem<@NotNull StandingAndWallBlockItem> COPPER_STAR = register(
            "copper_star",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.COPPER_STAR.get(),
                    ModBlocks.WALL_COPPER_STAR.get(),
                    Direction.DOWN,
                    properties
            )
    );
    public static final DeferredItem<@NotNull StandingAndWallBlockItem> IRON_STAR = register(
            "iron_star",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.IRON_STAR.get(),
                    ModBlocks.WALL_IRON_STAR.get(),
                    Direction.DOWN,
                    properties
            )
    );
    public static final DeferredItem<@NotNull StandingAndWallBlockItem> GOLD_STAR = register(
            "gold_star",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.GOLD_STAR.get(),
                    ModBlocks.WALL_GOLD_STAR.get(),
                    Direction.DOWN,
                    properties
            )
    );
    public static final DeferredItem<@NotNull StandingAndWallBlockItem> ITEM_DISPLAY = register(
            "item_display",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.ITEM_DISPLAY.get(),
                    ModBlocks.WALL_ITEM_DISPLAY.get(),
                    Direction.DOWN,
                    properties
            )
    );
    public static final DeferredItem<@NotNull StandingAndWallBlockItem> WREATH = register(
            "wreath",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.WREATH.get(),
                    ModBlocks.WALL_WREATH.get(),
                    Direction.DOWN,
                    properties
            )
    );

    public static final ImmutableBiMap<DyeColor, DeferredItem<@NotNull BlockItem>> PRESENTS_BY_COLOR = itemForEachColor(
            Util.DYES,
            ModItems::presentForColor
    );
    public static final DeferredItem<@NotNull Item> DECORATED_WREATH = register(
            "decorated_wreath",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.DECORATED_WREATH.get(),
                    ModBlocks.DECORATED_WALL_WREATH.get(),
                    Direction.DOWN,
                    properties
            )
    );
    public static final DeferredItem<@NotNull Item> BLANK_CAROL_DISC = register(
            "blank_carol_disc",
            Item::new,
            () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)
    );
    public static final DeferredItem<@NotNull Item> MUSIC_DISC_SILENT_NIGHT = register(
            "music_disc_silent_night",
            Item::new,
            () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(
                    DataComponents.JUKEBOX_PLAYABLE,
                    new JukeboxPlayable(new EitherHolder<>(ResourceKey.create(
                            Registries.JUKEBOX_SONG,
                            CelebratorySpruce.id("silent_night")
                    )))
            )
    );
    public static final DeferredItem<@NotNull Item> MUSIC_DISC_WHAT_CHILD = register(
            "music_disc_what_child",
            Item::new,
            () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(
                    DataComponents.JUKEBOX_PLAYABLE,
                    new JukeboxPlayable(new EitherHolder<>(ResourceKey.create(
                            Registries.JUKEBOX_SONG,
                            CelebratorySpruce.id("what_child")
                    )))
            )
    );
    public static final DeferredItem<@NotNull Item> MUSIC_DISC_CHRISTMAS_DAY_BELLS = register(
            "music_disc_christmas_day_bells",
            Item::new,
            () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(
                    DataComponents.JUKEBOX_PLAYABLE,
                    new JukeboxPlayable(new EitherHolder<>(ResourceKey.create(
                            Registries.JUKEBOX_SONG,
                            CelebratorySpruce.id("christmas_day_bells")
                    )))
            )
    );
    public static final DeferredItem<@NotNull Item> MUSIC_DISC_CAROL_OF_THE_BELLS = register(
            "music_disc_carol_of_the_bells",
            Item::new,
            () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(
                    DataComponents.JUKEBOX_PLAYABLE,
                    new JukeboxPlayable(new EitherHolder<>(ResourceKey.create(
                            Registries.JUKEBOX_SONG,
                            CelebratorySpruce.id("carol_of_the_bells")
                    )))
            )
    );

    public static final DeferredItem<@NotNull GoodieBagItem> STOCKING = register(
            "stocking",
            GoodieBagItem::new,
            () -> new Item.Properties().component(ModDataComponentTypes.DEFAULT_GOODIE_BAG_SIZE, 3)
                    .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                    .component(
                            DataComponents.EQUIPPABLE,
                            Equippable.builder(EquipmentSlot.HEAD)
                                    .setAsset(ModArmorMaterials.CHRISTMAS_ASSET)
                                    .setDispensable(true)
                                    .setDamageOnHurt(false)
                                    .setSwappable(false)
                                    .build()
                    )
    );
    public static final DeferredItem<@NotNull BlockItem> FRUIT_CAKE = register(
            "fruit_cake",
            (properties) -> new BlockItem(ModBlocks.FRUIT_CAKE.get(), properties.stacksTo(1))
    );
    public static final DeferredItem<@NotNull BlockItem> YULE_LOG_CAKE = register(
            "yule_log_cake",
            (properties) -> new BlockItem(ModBlocks.YULE_LOG_CAKE.get(), properties.stacksTo(1))
    );

    static {
        ITEMS.addAlias(CelebratorySpruce.id("present"), CelebratorySpruce.id("red_present"));
    }

    public static DeferredItem<@NotNull BlockItem> presentForColor(DyeColor color) {
        return register(
                color.getName() + "_present", (properties) -> new BlockItem(
                        Objects.requireNonNull(
                                ModBlocks.PRESENT_PILES_BY_COLOR.get(color),
                                "Attempting to register present item for color '" + color.getName() + "' that lacks a present pile block."
                        ).get(), properties
                )
        );
    }

    public static <T extends Item> ImmutableBiMap<DyeColor, DeferredItem<@NotNull T>> itemForEachColor(Collection<DyeColor> colors, Function<DyeColor, DeferredItem<@NotNull T>> generator) {
        ImmutableBiMap.Builder<DyeColor, DeferredItem<@NotNull T>> builder = ImmutableBiMap.builder();

        for (DyeColor color : colors) {
            builder.put(color, generator.apply(color));
        }

        return builder.build();
    }

    public static DeferredItem<@NotNull Item> register(String name) {
        return register(name, Item::new);
    }

    public static <T extends Item> DeferredItem<@NotNull T> register(String name, Function<Item.Properties, T> supplier) {
        return register(name, supplier, Item.Properties::new);
    }

    public static <T extends Item> DeferredItem<@NotNull T> register(String name, Function<Item.Properties, T> supplier, Supplier<Item.Properties> properties) {
        return ITEMS.registerItem(name, supplier, properties);
    }

}
