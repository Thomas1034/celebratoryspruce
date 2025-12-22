package com.startraveler.celebratoryspruce;

import net.minecraft.world.item.Item;
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

    public static DeferredItem<@NotNull Item> register(String name) {
        return register(name, Item::new);
    }

    public static <T extends Item> DeferredItem<@NotNull T> register(String name, Function<Item.Properties, T> supplier) {
        return register(name, supplier, Item.Properties::new);
    }

    public static <T extends Item> DeferredItem<@NotNull T> register(String name, Function<Item.Properties, T> supplier, Supplier<Item.Properties> properties) {
        return ITEMS.registerItem(
                name,
                supplier, properties
        );
    }
}
