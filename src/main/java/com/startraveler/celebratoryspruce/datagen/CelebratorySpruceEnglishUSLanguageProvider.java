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
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CelebratorySpruceEnglishUSLanguageProvider extends LanguageProvider {

    private final Set<Block> excludedBlocks;
    private final Set<Item> excludedItems;
    private final Set<TagKey<?>> excludedTags;
    private final Set<MobEffect> excludedEffects;
    private final Set<EntityType<?>> excludedEntityTypes;
    private final Set<Potion> excludedPotions;

    public CelebratorySpruceEnglishUSLanguageProvider(PackOutput output) {
        super(output, CelebratorySpruce.MODID, "en_us");
        excludedBlocks = new HashSet<>();
        excludedItems = new HashSet<>();
        excludedTags = new HashSet<>();
        excludedEffects = new HashSet<>();
        excludedEntityTypes = new HashSet<>();
        excludedPotions = new HashSet<>();
    }


    @Override
    protected void addTranslations() {
        // Now, do all the rest automagically.

        this.add(ModCreativeModeTabs.ITEMS_NAME, "Celebratory Spruce Items");
        this.addTranslations(ModBlocks.BLOCKS.getEntries(), excludedBlocks);
        this.addTranslations(ModItems.ITEMS.getEntries(), excludedItems);
        this.addTranslations(Set.of(), excludedEffects);
        this.addTranslations(Set.of(), excludedEntityTypes);
        this.addPotionTranslations(Set.of());

    }

    private void exclude(EntityType<?> key, String name) {
        this.excludedEntityTypes.add(key);
        super.add(key, name);
    }

    public <T extends HasDescriptionId> void exclude(T key, String name, Set<T> values) {
        values.add(key);
        super.add(key.getDescriptionId(), name);
    }

    public void exclude(TagKey<?> tagKey, String name) {
        this.excludedTags.add(tagKey);
        super.add(tagKey, name);
    }

    @SuppressWarnings("deprecation")
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
    protected <T extends HasDescriptionId> void addTranslations(Iterable<? extends Holder<T>> blocks, Set<T> addTo) {
        blocks.forEach(holder -> {
            String id = holder.value().getDescriptionId();

            if (!(addTo.contains(holder.value()))) {
                try {
                    this.add(
                            id,
                            WordUtils.capitalize(Objects.requireNonNull(holder.getKey())
                                    .identifier().getPath().replace('/', ' ').replace('_', ' '))
                    );
                } catch (IllegalStateException e) {
                    CelebratorySpruce.LOGGER.warn("Skipping duplicate translation key {}", id);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    protected void addPotionTranslations(Iterable<Holder<Potion>> potions) {
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