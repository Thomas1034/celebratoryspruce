package com.startraveler.celebratoryspruce;

import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    // Create a Deferred Register to hold Items which will all be registered under the "celebratoryspruce" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CelebratorySpruce.MODID);

    public static final DeferredItem<@NotNull Item> ORNAMENT = register("ornament");
    public static final DeferredItem<@NotNull Item> FESTIVE_LIGHT = register("festive_light");
    public static final DeferredItem<@NotNull Item> GOLD_STAR = register(
            "gold_star",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.GOLD_STAR.get(),
                    ModBlocks.WALL_GOLD_STAR.get(),
                    Direction.DOWN,
                    properties
            )
    );

    public static final DeferredItem<@NotNull Item> WREATH = register(
            "wreath",
            (properties) -> new StandingAndWallBlockItem(
                    ModBlocks.WREATH.get(),
                    ModBlocks.WALL_WREATH.get(),
                    Direction.DOWN,
                    properties
            )
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
            "blank_carol_disc", Item::new, () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)
    );

    public static final DeferredItem<@NotNull Item> MUSIC_DISC_SILENT_NIGHT = register(
            "music_disc_silent_night", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(
                    DataComponents.JUKEBOX_PLAYABLE,
                    new JukeboxPlayable(new EitherHolder<>(ResourceKey.create(
                            Registries.JUKEBOX_SONG,
                            CelebratorySpruce.id("silent_night")
                    )))
            )
    );

    public static final DeferredItem<@NotNull Item> MUSIC_DISC_WHAT_CHILD = register(
            "music_disc_what_child", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(
                    DataComponents.JUKEBOX_PLAYABLE,
                    new JukeboxPlayable(new EitherHolder<>(ResourceKey.create(
                            Registries.JUKEBOX_SONG,
                            CelebratorySpruce.id("what_child")
                    )))
            )
    );

    public static final DeferredItem<@NotNull Item> MUSIC_DISC_CHRISTMAS_DAY_BELLS = register(
            "music_disc_christmas_day_bells", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(
                    DataComponents.JUKEBOX_PLAYABLE,
                    new JukeboxPlayable(new EitherHolder<>(ResourceKey.create(
                            Registries.JUKEBOX_SONG,
                            CelebratorySpruce.id("christmas_day_bells")
                    )))
            )
    );

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
