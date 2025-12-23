package com.startraveler.celebratoryspruce.datagen;

import com.startraveler.celebratoryspruce.CelebratorySpruce;
import com.startraveler.celebratoryspruce.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CelebratorySpruceBlockTagsProvider extends BlockTagsProvider {

    public CelebratorySpruceBlockTagsProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CelebratorySpruce.MODID);
    }


    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookupProvider) {

        this.tag(BlockTags.LEAVES).add(ModBlocks.DECORATED_SPRUCE_LEAVES.get());
        this.tag(BlockTags.LEAVES).add(ModBlocks.FESTIVE_SPRUCE_LEAVES.get());
        this.tag(BlockTags.SAPLINGS).add(ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.GOLD_STAR.get(), ModBlocks.WALL_GOLD_STAR.get());
    }
}
