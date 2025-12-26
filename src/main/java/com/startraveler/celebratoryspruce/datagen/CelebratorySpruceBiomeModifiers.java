package com.startraveler.celebratoryspruce.datagen;

import com.startraveler.celebratoryspruce.CelebratorySpruce;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class CelebratorySpruceBiomeModifiers {
    public static final ResourceKey<PlacedFeature> CELEBRATORY_TREES_GROVE = ResourceKey.create(
            Registries.PLACED_FEATURE, // The registry this key is for
            CelebratorySpruce.id("celebratory_trees_grove") // The registry entry name
    );
    public static final ResourceKey<PlacedFeature> CELEBRATORY_TREES_SNOWY = ResourceKey.create(
            Registries.PLACED_FEATURE, // The registry this key is for
            CelebratorySpruce.id("celebratory_trees_snowy") // The registry entry name
    );
    public static final ResourceKey<PlacedFeature> CELEBRATORY_TREES_TAIGA = ResourceKey.create(
            Registries.PLACED_FEATURE, // The registry this key is for
            CelebratorySpruce.id("celebratory_trees_taiga") // The registry entry name
    );
    public static final ResourceKey<PlacedFeature> CELEBRATORY_TREES_OLD_GROWTH_PINE_TAIGA = ResourceKey.create(
            Registries.PLACED_FEATURE, // The registry this key is for
            CelebratorySpruce.id("celebratory_trees_old_growth_pine_taiga") // The registry entry name
    );
    public static final ResourceKey<PlacedFeature> CELEBRATORY_TREES_OLD_GROWTH_SPRUCE_TAIGA = ResourceKey.create(
            Registries.PLACED_FEATURE, // The registry this key is for
            CelebratorySpruce.id("celebratory_trees_old_growth_spruce_taiga") // The registry entry name
    );
    // Assume we have some PlacedFeature named EXAMPLE_PLACED_FEATURE.
    // Define the ResourceKey for our BiomeModifier.
    public static final ResourceKey<BiomeModifier> ADD_CELEBRATORY_TREES_GROVE = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
            CelebratorySpruce.id("add_celebratory_trees_grove") // The registry name
    );
    public static final ResourceKey<BiomeModifier> ADD_CELEBRATORY_TREES_TAIGA = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
            CelebratorySpruce.id("add_celebratory_trees_taiga") // The registry name
    );
    public static final ResourceKey<BiomeModifier> ADD_CELEBRATORY_TREES_SNOWY = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
            CelebratorySpruce.id("add_celebratory_trees_snowy") // The registry name
    );
    public static final ResourceKey<BiomeModifier> ADD_CELEBRATORY_TREES_OLD_GROWTH_PINE_TAIGA = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
            CelebratorySpruce.id("add_celebratory_trees_old_growth_pine_taiga") // The registry name
    );
    public static final ResourceKey<BiomeModifier> ADD_CELEBRATORY_TREES_OLD_GROWTH_SPRUCE_TAIGA = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
            CelebratorySpruce.id("add_celebratory_trees_old_growth_spruce_taiga") // The registry name
    );

    // BUILDER is a RegistrySetBuilder passed to DatapackBuiltinEntriesProvider
    // in a listener for the `GatherDataEvent`s.
    public static void register(BootstrapContext<BiomeModifier> provider) {
        // Lookup any necessary registries.
        // Static registries only need to be looked up if you need to grab the tag data.
        HolderGetter<Biome> biomes = provider.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = provider.lookup(Registries.PLACED_FEATURE);

        // Register the biome modifiers.
        provider.register(
                ADD_CELEBRATORY_TREES_SNOWY, new BiomeModifiers.AddFeaturesBiomeModifier(
                        // The biome(s) to generate within
                        HolderSet.direct(biomes::getOrThrow, Biomes.SNOWY_PLAINS),
                        // The feature(s) to generate within the biomes
                        HolderSet.direct(placedFeatures::getOrThrow, CELEBRATORY_TREES_SNOWY),
                        // The generation step
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
        provider.register(
                ADD_CELEBRATORY_TREES_GROVE, new BiomeModifiers.AddFeaturesBiomeModifier(
                        // The biome(s) to generate within
                        HolderSet.direct(biomes::getOrThrow, Biomes.GROVE),
                        // The feature(s) to generate within the biomes
                        HolderSet.direct(placedFeatures::getOrThrow, CELEBRATORY_TREES_GROVE),
                        // The generation step
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );

        provider.register(
                ADD_CELEBRATORY_TREES_TAIGA, new BiomeModifiers.AddFeaturesBiomeModifier(
                        // The biome(s) to generate within
                        HolderSet.direct(biomes::getOrThrow, Biomes.TAIGA, Biomes.SNOWY_TAIGA),
                        // The feature(s) to generate within the biomes
                        HolderSet.direct(placedFeatures::getOrThrow, CELEBRATORY_TREES_TAIGA),
                        // The generation step
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );

        provider.register(
                ADD_CELEBRATORY_TREES_OLD_GROWTH_PINE_TAIGA, new BiomeModifiers.AddFeaturesBiomeModifier(
                        // The biome(s) to generate within
                        HolderSet.direct(biomes::getOrThrow, Biomes.OLD_GROWTH_PINE_TAIGA),
                        // The feature(s) to generate within the biomes
                        HolderSet.direct(placedFeatures::getOrThrow, CELEBRATORY_TREES_OLD_GROWTH_PINE_TAIGA),
                        // The generation step
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );

        provider.register(
                ADD_CELEBRATORY_TREES_OLD_GROWTH_SPRUCE_TAIGA, new BiomeModifiers.AddFeaturesBiomeModifier(
                        // The biome(s) to generate within
                        HolderSet.direct(biomes::getOrThrow, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
                        // The feature(s) to generate within the biomes
                        HolderSet.direct(placedFeatures::getOrThrow, CELEBRATORY_TREES_OLD_GROWTH_SPRUCE_TAIGA),
                        // The generation step
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
    }
}
