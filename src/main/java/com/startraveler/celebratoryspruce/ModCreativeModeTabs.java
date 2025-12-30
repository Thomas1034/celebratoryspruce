package com.startraveler.celebratoryspruce;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class ModCreativeModeTabs {
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "celebratoryspruce" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            CelebratorySpruce.MODID
    );
    // Creates a creative tab with the id "celebratoryspruce:celebratory_spruce_items" for the example item, that is placed after the combat tab
    public static final String PREFIX = "itemGroup.";
    public static final String ITEMS_ID = "celebratory_spruce_items";
    public static final String ITEMS_NAME = PREFIX + ITEMS_ID;
    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, @NotNull CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register(
            ITEMS_ID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable(ITEMS_NAME)) //The language key for the title of your CreativeModeTab
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(ModBlocks.CELEBRATORY_SPRUCE_SAPLING::toStack)
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.CELEBRATORY_SPRUCE_SAPLING);
                        output.accept(ModItems.ORNAMENT);
                        output.accept(ModItems.FESTIVE_LIGHT);
                        output.accept(ModBlocks.DECORATED_SPRUCE_LEAVES);
                        output.accept(ModBlocks.FESTIVE_SPRUCE_LEAVES);
                        output.accept(ModItems.WREATH);
                        output.accept(ModItems.DECORATED_WREATH);
                        output.accept(ModBlocks.LIGHT_NET);
                        output.accept(ModItems.GOLD_STAR);
                        output.accept(ModItems.PRESENT);
                        output.accept(ModItems.STOCKING);
                        output.accept(ModItems.ITEM_DISPLAY);
                        output.accept(ModItems.BLANK_CAROL_DISC);
                        output.accept(ModItems.MUSIC_DISC_CHRISTMAS_DAY_BELLS);
                        output.accept(ModItems.MUSIC_DISC_SILENT_NIGHT);
                        output.accept(ModItems.MUSIC_DISC_WHAT_CHILD);
                        output.accept(ModItems.MUSIC_DISC_CAROL_OF_THE_BELLS);
                    })
                    .build()
    );
}
