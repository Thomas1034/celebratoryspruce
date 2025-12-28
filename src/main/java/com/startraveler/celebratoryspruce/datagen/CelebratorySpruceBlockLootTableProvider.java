package com.startraveler.celebratoryspruce.datagen;

import com.startraveler.celebratoryspruce.ModBlocks;
import com.startraveler.celebratoryspruce.ModItems;
import com.startraveler.celebratoryspruce.block.BoxPileBlock;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CelebratorySpruceBlockLootTableProvider extends BlockLootSubProvider {
    protected static final float[] EXTRA_LEAVES_SAPLING_CHANCES = new float[]{0.05F * 2f,
            0.0625F * 2f,
            0.083333336F * 2f,
            0.1F * 2f};

    protected final Set<Block> knownBlocks;

    public CelebratorySpruceBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        this.knownBlocks = new HashSet<>();
    }

    @SuppressWarnings("unused")
    protected LootTable.Builder createChanceDrops(Block block, Item item, float chance) {
        return createShearsDispatchTable(
                block, this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(item)
                                .when(LootItemRandomChanceCondition.randomChance(chance))
                                .apply(ApplyBonusCount.addUniformBonusCount(
                                        registries.lookup(Registries.ENCHANTMENT)
                                                .orElseThrow()
                                                .getOrThrow(Enchantments.FORTUNE), 2
                                ))
                )
        );
    }

    public LootTable.Builder createSingleItemTable(ItemLike item, List<Integer> range) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(
                item,
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(
                                        range.get(0),
                                        range.get(1)
                                ))))
        ));
    }

    @Override
    protected void generate() {

        this.knownBlocks.addAll(ModBlocks.BLOCKS.getEntries()
                .stream()
                .map(DeferredHolder::get)
                .collect(Collectors.toSet()));

        this.add(
                ModBlocks.DECORATED_SPRUCE_LEAVES.get(), createItemLeavesDrops(
                        ModBlocks.DECORATED_SPRUCE_LEAVES.get(),
                        ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get(),
                        ModItems.ORNAMENT.get()
                )
        );
        this.add(
                ModBlocks.FESTIVE_SPRUCE_LEAVES.get(), createItemLeavesDrops(
                        ModBlocks.FESTIVE_SPRUCE_LEAVES.get(),
                        ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get(),
                        ModItems.FESTIVE_LIGHT.get()
                )
        );
        this.dropSelf(ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get());
        this.add(
                ModBlocks.POTTED_CELEBRATORY_SPRUCE_SAPLING.get(),
                this.createPotFlowerItemTable(ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get())
        );
        this.dropOther(ModBlocks.GOLD_STAR.get(), ModItems.GOLD_STAR.get());
        this.dropOther(ModBlocks.WALL_GOLD_STAR.get(), ModItems.GOLD_STAR.get());
        this.add(ModBlocks.LIGHT_NET.get(), this.createMultifaceBlockDrops(ModBlocks.LIGHT_NET.get()));
        this.dropOther(ModBlocks.WREATH.get(), ModItems.WREATH.get());
        this.dropOther(ModBlocks.WALL_WREATH.get(), ModItems.WREATH.get());
        this.dropOther(ModBlocks.DECORATED_WREATH.get(), ModItems.DECORATED_WREATH.get());
        this.dropOther(ModBlocks.DECORATED_WALL_WREATH.get(), ModItems.DECORATED_WREATH.get());
        this.dropOther(ModBlocks.ITEM_DISPLAY.get(), ModItems.ITEM_DISPLAY.get());
        this.dropOther(ModBlocks.WALL_ITEM_DISPLAY.get(), ModItems.ITEM_DISPLAY.get());


        BiFunction<Block, ItemLike, LootTable.Builder> bombPileTableMaker = (block, itemLike) -> LootTable.lootTable()
                .withPool(this.applyExplosionCondition(
                        itemLike,
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))

                                .add(LootItem.lootTableItem(itemLike)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BoxPileBlock.BOXES, 1)))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(itemLike)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BoxPileBlock.BOXES, 2)))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))))

                                .add(LootItem.lootTableItem(itemLike)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BoxPileBlock.BOXES, 3)))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3))))

                                .add(LootItem.lootTableItem(itemLike)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BoxPileBlock.BOXES, 4)))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4))))

                                .add(LootItem.lootTableItem(itemLike)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BoxPileBlock.BOXES, 5)))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(5))))

                                .add(LootItem.lootTableItem(itemLike)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BoxPileBlock.BOXES, 6)))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(6))))

                                .add(LootItem.lootTableItem(itemLike)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BoxPileBlock.BOXES, 7)))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(7))))

                                .add(LootItem.lootTableItem(itemLike)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BoxPileBlock.BOXES, 8)))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(8))))

                ));
        this.add(
                ModBlocks.PRESENT_PILE.get(),
                bombPileTableMaker.apply(ModBlocks.PRESENT_PILE.get(), ModItems.PRESENT.get())
        );
    }

    @Override
    @NotNull
    protected Iterable<Block> getKnownBlocks() {
        return this.knownBlocks;
    }

    @SuppressWarnings("SameParameterValue")
    @NotNull
    protected LootTable.Builder createItemLeavesDrops(@NotNull final Block leavesBlock, @NotNull final Block saplingBlock, @NotNull final ItemLike bonusItem) {
        return createItemLeavesDrops(leavesBlock, saplingBlock, bonusItem, EXTRA_LEAVES_SAPLING_CHANCES);
    }

    @NotNull
    protected LootTable.Builder createItemLeavesDrops(@NotNull final Block leavesBlock, @NotNull final Block saplingBlock, @NotNull final ItemLike bonusItem, final float @NotNull ... chances) {
        return this.createLeavesDrops(leavesBlock, saplingBlock, chances)
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(this.doesNotHaveShearsOrSilkTouch())
                        .add((this.applyExplosionCondition(leavesBlock, LootItem.lootTableItem(bonusItem)))));
    }

    protected LootTable.Builder createOreDrops(Block block, ItemLike item, List<Integer> range) {
        return createSilkTouchDispatchTable(
                block, this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(
                                        range.get(0),
                                        range.get(1)
                                )))
                                .apply(ApplyBonusCount.addOreBonusCount(registries.lookup(Registries.ENCHANTMENT)
                                        .orElseThrow()
                                        .getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }

    @SuppressWarnings("unused")
    protected void dropOther(Block block, ItemLike item, List<Integer> range) {
        this.add(block, this.createSingleItemTable(item, range));
    }

    protected LootTable.Builder createSilkTouchDrop(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)));
    }

    protected LootTable.Builder createSilkTouchOrShearsOreDrops(Block block, Item item, List<Integer> range) {
        return createSilkTouchOrShearsDispatchTable(
                block, this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(
                                        range.get(0),
                                        range.get(1)
                                )))
                                .apply(ApplyBonusCount.addOreBonusCount(registries.lookup(Registries.ENCHANTMENT)
                                        .orElseThrow()
                                        .getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }

    protected LootTable.Builder createSilkTouchOrShearsDrop(Block pBlock, Item item) {
        return createSilkTouchOrShearsDispatchTable(
                pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item))
        );
    }

    protected LootTable.Builder createSilkTouchOrShearsDrop(Block pBlock, Item item, List<Integer> range) {
        return createSilkTouchOrShearsOreDrops(pBlock, item, range);
    }

    @SuppressWarnings("unused")
    protected void requireSilkTouch(Block base, ItemLike withoutSilk) {
        this.add(base, block -> createSilkTouchDrop(base, withoutSilk.asItem()));
    }

    @SuppressWarnings("unused")
    protected void requireSilkTouchOrShears(Block base, ItemLike withoutSilk) {
        this.add(base, block -> createSilkTouchOrShearsDrop(base, withoutSilk.asItem()));
    }

    @SuppressWarnings("unused")
    protected void requireSilkTouchOrShears(Block base, ItemLike withoutSilk, List<Integer> range) {
        this.add(base, block -> createSilkTouchOrShearsDrop(base, withoutSilk.asItem(), range));
    }

    @SuppressWarnings("unused")
    protected void requireSilkTouch(Block base, ItemLike withoutSilk, List<Integer> range) {
        this.add(base, block -> createOreDrops(base, withoutSilk.asItem(), range));
    }

    @SuppressWarnings("unused")
    protected void requireSilkTouchDropsOther(Block base, Block source) {
        Identifier sourceLoc = BuiltInRegistries.BLOCK.getKey(source).withPrefix("blocks/");
        this.add(base, block -> this.createSilkTouchOrOtherDrop(block, sourceLoc));
    }

    @SuppressWarnings("unused")
    protected void oreDrop(Block base, ItemLike drop, List<Integer> range) {
        this.add(base, block -> createOreDrops(base, drop, range));
    }

    protected LootTable.Builder createSilkTouchOrOtherDrop(Block block, Identifier source) {
        return createSilkTouchDispatchTable(
                block,
                this.applyExplosionDecay(
                        block,
                        NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, source))
                )
        );
    }


}
