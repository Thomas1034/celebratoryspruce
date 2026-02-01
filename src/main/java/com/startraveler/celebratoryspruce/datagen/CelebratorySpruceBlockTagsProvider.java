package com.startraveler.celebratoryspruce.datagen;

import com.startraveler.celebratoryspruce.CelebratorySpruce;
import com.startraveler.celebratoryspruce.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CelebratorySpruceBlockTagsProvider extends BlockTagsProvider {

    public CelebratorySpruceBlockTagsProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CelebratorySpruce.MODID);
    }


    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookupProvider) {

        ModBlocks.CANDLE_FRUIT_CAKES.stream().map(Supplier::get).forEach(this.tag(BlockTags.CANDLE_CAKES)::add);
        this.tag(BlockTags.CAMPFIRES).add(ModBlocks.LOG_FIRE.get());
        this.tag(BlockTags.LEAVES).add(ModBlocks.DECORATED_SPRUCE_LEAVES.get());
        this.tag(BlockTags.LEAVES).add(ModBlocks.FESTIVE_SPRUCE_LEAVES.get());
        this.tag(BlockTags.SAPLINGS).add(ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.DIAMOND_STAR.get(),
                ModBlocks.WALL_DIAMOND_STAR.get(),
                ModBlocks.COPPER_STAR.get(),
                ModBlocks.WALL_COPPER_STAR.get(),
                ModBlocks.IRON_STAR.get(),
                ModBlocks.WALL_IRON_STAR.get(),
                ModBlocks.GOLD_STAR.get(),
                ModBlocks.WALL_GOLD_STAR.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.LOG_FIRE.get());
        this.tag(Tags.Blocks.DYED).addAll(ModBlocks.PRESENT_PILES_BY_COLOR.values().stream().map(Supplier::get));
    }
}
