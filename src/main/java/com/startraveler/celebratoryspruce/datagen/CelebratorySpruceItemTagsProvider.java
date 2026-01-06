package com.startraveler.celebratoryspruce.datagen;

import com.startraveler.celebratoryspruce.CelebratorySpruce;
import com.startraveler.celebratoryspruce.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CelebratorySpruceItemTagsProvider extends ItemTagsProvider {

    public CelebratorySpruceItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CelebratorySpruce.MODID);
    }


    @Override
    protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {
        this.tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).add(ModItems.FRUIT_CAKE.get(), ModItems.YULE_LOG_CAKE.get());
    }
}
