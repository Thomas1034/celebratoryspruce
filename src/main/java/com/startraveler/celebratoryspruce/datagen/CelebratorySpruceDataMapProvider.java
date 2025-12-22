package com.startraveler.celebratoryspruce.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CelebratorySpruceDataMapProvider extends DataMapProvider {
    public CelebratorySpruceDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        // TODO add leaves here as fuel and compost
    }

    @SuppressWarnings("unused")
    public void addCompostable(ItemLike item, float compostChance) {
        this.builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()), new Compostable(compostChance, true), false);
    }

    @SuppressWarnings("unused")
    public void addFurnaceFuel(ItemLike itemLike, int burnTime) {
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(itemLike.asItem()), new FurnaceFuel(burnTime), false);
    }
}
