package com.startraveler.celebratoryspruce.datagen;

import com.google.common.base.Suppliers;
import com.startraveler.celebratoryspruce.*;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class CelebratorySpruceEnglishUSLanguageProvider extends LanguageProvider {

    private final Set<Block> excludedBlocks;
    private final Set<Item> excludedItems;
    private final Set<TagKey<?>> excludedTags;
    private final Set<MobEffect> excludedEffects;
    private final Set<EntityType<?>> excludedEntityTypes;
    private final Set<Potion> excludedPotions;

    public CelebratorySpruceEnglishUSLanguageProvider(PackOutput output) {
        super(output, CelebratorySpruce.MODID, "en_us");
        this.excludedBlocks = new HashSet<>();
        this.excludedItems = new HashSet<>();
        this.excludedTags = new HashSet<>();
        this.excludedEffects = new HashSet<>();
        this.excludedEntityTypes = new HashSet<>();
        this.excludedPotions = new HashSet<>();
    }

    @Override
    protected void addTranslations() {

        this.exclude(ModItems.MUSIC_DISC_SILENT_NIGHT.get(), "Carol Disc", this.excludedItems);
        this.exclude(ModItems.MUSIC_DISC_WHAT_CHILD.get(), "Carol Disc", this.excludedItems);
        this.exclude(ModItems.MUSIC_DISC_CHRISTMAS_DAY_BELLS.get(), "Carol Disc", this.excludedItems);
        this.exclude(ModItems.MUSIC_DISC_CAROL_OF_THE_BELLS.get(), "Carol Disc", this.excludedItems);
        this.add("jukebox_song.celebratoryspruce.silent_night", "Franz Gruber - Silent Night");
        this.add("jukebox_song.celebratoryspruce.what_child", "William Chatterton Dix - What Child Is This?");
        this.add(
                "jukebox_song.celebratoryspruce.christmas_day_bells",
                "Henry Wadsworth Longfellow / John Baptiste Calkin - I Heard the Bells on Christmas Day"
        );
        this.add(
                "jukebox_song.celebratoryspruce.carol_of_the_bells",
                "Mykola Leontovych / Jason Shaw - Shchedryk (Carol of the Bells)"
        );

        this.add(Config.ICE_MELTS_IN_LIGHT);
        this.add(Config.ICE_MELTING_THRESHOLD);
        this.add(Config.ICE_FREEZING_THRESHOLD);
        this.add(Config.SNOW_MELTS_IN_LIGHT);
        this.add(Config.SNOW_MELTING_THRESHOLD);
        this.add(Config.SNOW_ACCUMULATION_THRESHOLD);
        this.add(Config.SNOW_TEMPERATURE_THRESHOLD);
        this.add("item.celebratoryspruce.goodie_bag.for", "For: ");
        this.add("item.celebratoryspruce.goodie_bag.contains_one", "Contains a gift! Use to open.");
        this.add("item.celebratoryspruce.goodie_bag.contains_n", "Contains %s gifts! Use to open.");
        this.add("item.celebratoryspruce.goodie_bag.empty", "Add a stack of items!");
        // Now, do all the rest automagically.
        this.add(ModCreativeModeTabs.ITEMS_NAME, "Celebratory Spruce Items");
        this.addTranslations(ModBlocks.BLOCKS.getEntries(), this.excludedBlocks);
        this.addTranslations(ModItems.ITEMS.getEntries(), this.excludedItems);
        this.addTranslations(Set.of(), this.excludedEffects);
        this.addTranslations(Set.of(), this.excludedEntityTypes);
        this.addPotionTranslations(Set.of(), this.excludedPotions);
    }

    @SuppressWarnings("deprecation")
    public <T> void add(ModConfigSpec.ConfigValue<T> configValue) {
        List<String> rawName = configValue.getPath();
        List<String> splitRawName = List.of(rawName.getLast().split("(?=\\p{Lu})"));
        String name = splitRawName.stream()
                .map(WordUtils::capitalize)
                .flatMap(string -> Stream.of(string, " "))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        String rawNameJoinedByDots = String.join(".", rawName);
        String generatedTranslationKey = CelebratorySpruce.MODID + ".configuration." + rawNameJoinedByDots;

        // This crashes.
        this.add(generatedTranslationKey, name);
    }

    @SuppressWarnings("unused")
    public <T extends HasDescriptionId> void exclude(T key, String name, Set<T> values) {
        values.add(key);
        super.add(key.getDescriptionId(), name);
    }

    @SuppressWarnings("unused")
    public void excludeTag(TagKey<?> tagKey, String name) {
        this.excludedTags.add(tagKey);
        super.add(tagKey, name);
    }

    @SuppressWarnings({"deprecation", "unused"})
    protected void addTagTranslations(Iterable<TagKey<?>> tags) {
        tags.forEach(tag -> {
            if (!excludedTags.contains(tag)) {
                this.addTag(
                        Suppliers.ofInstance(tag),
                        WordUtils.capitalize(tag.location().getPath().replace('/', ' ').replace('_', ' '))
                );
            }
        });
    }

    @SuppressWarnings("deprecation")
    protected <T extends HasDescriptionId> void addTranslations(Iterable<? extends Holder<T>> toTranslate, Set<T> addTo) {
        toTranslate.forEach(holder -> {
            String id = holder.value().getDescriptionId();

            if (!(addTo.contains(holder.value()))) {
                try {
                    this.add(
                            id,
                            WordUtils.capitalize(Objects.requireNonNull(holder.getKey())
                                    .identifier()
                                    .getPath()
                                    .replace('/', ' ')
                                    .replace('_', ' '))
                    );
                } catch (IllegalStateException e) {
                    CelebratorySpruce.LOGGER.warn("Skipping duplicate translation key {}", id);
                }
            } else {
                CelebratorySpruce.LOGGER.warn("Skipping manually entered translation key {}", id);
            }
        });
    }

    @SuppressWarnings("deprecation")
    protected void addPotionTranslations(Iterable<Holder<Potion>> potions, Set<Potion> excludedPotions) {
        potions.forEach(holder -> {
            if (!excludedPotions.contains(holder.value())) {
                String rawId = Objects.requireNonNull(holder.getKey()).identifier().getPath();
                String id = rawId.replace("long_", "").replace("strong_", "").replace("_", " ");
                String name = WordUtils.capitalize(id);
                this.add("item.minecraft.potion.effect." + rawId, "Potion of " + name);
                this.add("item.minecraft.splash_potion.effect." + rawId, "Splash Potion of " + name);
                this.add("item.minecraft.lingering_potion.effect." + rawId, "Lingering Potion of " + name);
                this.add("item.minecraft.tipped_arrow.effect." + rawId, "Arrow of " + name);
            }
        });
    }

}