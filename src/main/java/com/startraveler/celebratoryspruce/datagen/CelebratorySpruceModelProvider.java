package com.startraveler.celebratoryspruce.datagen;

import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quadrant;
import com.startraveler.celebratoryspruce.*;
import com.startraveler.celebratoryspruce.block.*;
import com.startraveler.celebratoryspruce.datagen.model.CelebratorySpruceModelTemplates;
import com.startraveler.celebratoryspruce.datagen.model.CelebratorySpruceTextureMapping;
import com.startraveler.celebratoryspruce.datagen.model.CelebratorySpruceTexturedModel;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CelebratorySpruceModelProvider extends ModelProvider {


    private BlockModelGenerators blockModels;
    private ItemModelGenerators itemModels;

    public CelebratorySpruceModelProvider(PackOutput output) {
        super(output, CelebratorySpruce.MODID);
    }

    public static MultiPartGenerator createDoubleSidedLogBlock(Block block, Identifier model) {

        return MultiPartGenerator.multiPart(block)
                .with(
                        new ConditionBuilder().term(RotatedPillarBlock.AXIS, Direction.Axis.Y),
                        BlockModelGenerators.variants(new Variant(model))
                )
                .with(
                        new ConditionBuilder().term(RotatedPillarBlock.AXIS, Direction.Axis.X),
                        BlockModelGenerators.variants(new Variant(model))
                                .with(VariantMutator.X_ROT.withValue(Quadrant.R90))
                                .with(VariantMutator.Y_ROT.withValue(Quadrant.R90))
                )
                .with(
                        new ConditionBuilder().term(RotatedPillarBlock.AXIS, Direction.Axis.Z),
                        BlockModelGenerators.variants(new Variant(model))
                                .with(VariantMutator.X_ROT.withValue(Quadrant.R90))
                );
    }


    public static MultiVariantGenerator createMirroredColumnGenerator(Block columnBlock, BiConsumer<Identifier, ModelInstance> modelOutput, TextureMapping[] mappings, String[] suffixes) {
        Stream<Identifier> mirrored = Util.zip(
                Arrays.stream(mappings),
                Arrays.stream(suffixes),
                (mapping, suffix) -> ModelTemplates.CUBE_COLUMN_MIRRORED.createWithSuffix(
                        columnBlock,
                        suffix,
                        mapping,
                        modelOutput
                )
        );
        Stream<Identifier> normal = Util.zip(
                Arrays.stream(mappings),
                Arrays.stream(suffixes),
                (mapping, suffix) -> ModelTemplates.CUBE_COLUMN.createWithSuffix(
                        columnBlock,
                        suffix,
                        mapping,
                        modelOutput
                )
        );
        Stream<Identifier> merged = Streams.concat(mirrored, normal);
        return createRotatedVariant(
                columnBlock,
                merged.toArray(Identifier[]::new)
        ).with(BlockModelGenerators.createRotatedPillar());
    }

    public static MultiVariantGenerator createRotatedVariant(Block block, Identifier... models) {
        return MultiVariantGenerator.dispatch(
                block, BlockModelGenerators.variants(Stream.concat(
                        Arrays.stream(models).map(Variant::new),
                        Arrays.stream(models)
                                .map(model -> new Variant(model).with(VariantMutator.Y_ROT.withValue(Quadrant.R180)))


                ).toArray(Variant[]::new))
        );
    }

    public static MultiVariantGenerator createTumbledBlock(Block block, Identifier model) {
        int numDirections = Direction.values().length;
        Variant[] variants = new Variant[numDirections * numDirections];
        for (int i = 0; i < numDirections; i++) {
            for (int j = 0; j < numDirections; j++) {
                variants[i + numDirections * j] = VariantMutator.X_ROT.withValue(Quadrant.values()[i])
                        .then(VariantMutator.Y_ROT.withValue(Quadrant.values()[i]))
                        .apply(new Variant(model));
            }
        }
        return MultiVariantGenerator.dispatch(block, BlockModelGenerators.variants(variants));
    }

    public static MultiPartGenerator createBoxPile(Block block, Function<Integer, Identifier> modelFunction) {
        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
        for (int i : BoxPileBlock.BOXES.getPossibleValues()) {
            Identifier model = modelFunction.apply(i);
            generator = generator.with(
                    new ConditionBuilder().term(BoxPileBlock.FACING, Direction.EAST).term(BoxPileBlock.BOXES, i),
                    BlockModelGenerators.variants(new Variant(model)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))
            ).with(
                    new ConditionBuilder().term(BoxPileBlock.FACING, Direction.SOUTH).term(BoxPileBlock.BOXES, i),
                    BlockModelGenerators.variants(new Variant(model))
                            .with(VariantMutator.Y_ROT.withValue(Quadrant.R180))
            ).with(
                    new ConditionBuilder().term(BoxPileBlock.FACING, Direction.WEST).term(BoxPileBlock.BOXES, i),
                    BlockModelGenerators.variants(new Variant(model))
                            .with(VariantMutator.Y_ROT.withValue(Quadrant.R270))
            ).with(
                    new ConditionBuilder().term(BoxPileBlock.FACING, Direction.NORTH).term(BoxPileBlock.BOXES, i),
                    BlockModelGenerators.variants(new Variant(model))
            );
        }
        return generator;
    }

    public static MultiPartGenerator createBooleanPropertyBoxPile(Block block, BiFunction<Integer, Boolean, Identifier> modelFunction, BooleanProperty property) {
        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
        for (int i : BoxPileBlock.BOXES.getPossibleValues()) {
            for (boolean b : property.getPossibleValues()) {
                Identifier model = modelFunction.apply(i, b);
                generator = generator.with(
                        new ConditionBuilder().term(BoxPileBlock.FACING, Direction.EAST)
                                .term(BoxPileBlock.BOXES, i)
                                .term(ItemHoldingBoxPileBlock.CLOSED, b),
                        BlockModelGenerators.variants(new Variant(model))
                                .with(VariantMutator.Y_ROT.withValue(Quadrant.R90))
                ).with(
                        new ConditionBuilder().term(BoxPileBlock.FACING, Direction.SOUTH)
                                .term(BoxPileBlock.BOXES, i)
                                .term(ItemHoldingBoxPileBlock.CLOSED, b),
                        BlockModelGenerators.variants(new Variant(model))
                                .with(VariantMutator.Y_ROT.withValue(Quadrant.R180))
                ).with(
                        new ConditionBuilder().term(BoxPileBlock.FACING, Direction.WEST)
                                .term(BoxPileBlock.BOXES, i)
                                .term(ItemHoldingBoxPileBlock.CLOSED, b),
                        BlockModelGenerators.variants(new Variant(model))
                                .with(VariantMutator.Y_ROT.withValue(Quadrant.R270))
                ).with(
                        new ConditionBuilder().term(BoxPileBlock.FACING, Direction.NORTH)
                                .term(BoxPileBlock.BOXES, i)
                                .term(ItemHoldingBoxPileBlock.CLOSED, b),
                        BlockModelGenerators.variants(new Variant(model))
                );
            }
        }
        return generator;
    }

    @SuppressWarnings("SameParameterValue")
    protected void booleanPropertyBoxPile(Block block, TriFunction<Integer, Boolean, Identifier, TexturedModel.Provider> texturedModel, @Nullable Identifier overrideBlockPathWith) {
        blockModels.blockStateOutput.accept(createBooleanPropertyBoxPile(
                block,
                (i, b) -> texturedModel.apply(i, b, overrideBlockPathWith)
                        .get(block)
                        .updateTemplate(template -> template.extend().renderType("cutout").build())
                        .createWithSuffix(block, "_stack" + i + (b ? "_closed" : ""), blockModels.modelOutput),
                ItemHoldingBoxPileBlock.CLOSED
        ));
    }

    protected MultiVariantGenerator createRotatedTopOverlaidBlock(Block block, Function<String, TexturedModel.Provider> model, String[] overlays) {
        Variant[] variants = new Variant[4 * overlays.length];
        for (int o = 0; o < overlays.length; o++) {
            String overlay = overlays[o];
            Identifier modelidentifier = model.apply(overlay).createWithSuffix(
                    block,
                    (overlays.length == 1 || overlay.equals("default") || overlay.equals("overlay_default") ? "" : "_" + overlay),
                    blockModels.modelOutput
            );
            for (int j = 0; j < 4; j++) {
                variants[o * 4 + j] = new Variant(modelidentifier).with(VariantMutator.Y_ROT.withValue(Quadrant.values()[j]));
            }
        }
        return MultiVariantGenerator.dispatch(block, BlockModelGenerators.variants(variants));
    }

    protected MultiVariantGenerator createOverlaidBlock(Block block, Function<String, TexturedModel.Provider> model, String[] overlays) {
        Variant[] variants = new Variant[overlays.length];
        for (int o = 0; o < overlays.length; o++) {
            String overlay = overlays[o];
            String trimmedOverlay = overlay.substring(overlay.lastIndexOf('/') + 1);
            Identifier modelidentifier = model.apply(overlay).createWithSuffix(
                    block,
                    (overlays.length == 1 || trimmedOverlay.equals("default") || trimmedOverlay.equals("overlay_default")) ? "" : "_" + trimmedOverlay,
                    blockModels.modelOutput
            );
            variants[o] = new Variant(modelidentifier);

        }
        return MultiVariantGenerator.dispatch(block, BlockModelGenerators.variants(variants));
    }

    protected MultiVariantGenerator createTumbledOverlaidBlock(Block block, Function<String, TexturedModel.Provider> model, String[] overlays) {
        Variant[] variants = new Variant[4 * 4 * overlays.length];
        for (int o = 0; o < overlays.length; o++) {
            String overlay = overlays[o];
            String trimmedOverlay = overlay.substring(overlay.lastIndexOf('/') + 1);
            Identifier modelidentifier = model.apply(overlay).createWithSuffix(
                    block,
                    (overlays.length == 1 || trimmedOverlay.equals("default") || trimmedOverlay.equals("overlay_default")) ? "" : "_" + trimmedOverlay,
                    blockModels.modelOutput
            );
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    variants[o * 16 + i * 4 + j] = new Variant(modelidentifier).with(VariantMutator.X_ROT.withValue(
                            Quadrant.values()[i])).with(VariantMutator.Y_ROT.withValue(Quadrant.values()[j]));
                }
            }
        }
        return MultiVariantGenerator.dispatch(block, BlockModelGenerators.variants(variants));
    }

    @SuppressWarnings("unused")
    protected void boxPile(Block block, @Nullable Identifier overrideBlockPathWith) {
        blockModels.blockStateOutput.accept(createBoxPile(
                block,
                (i) -> CelebratorySpruceTexturedModel.BOX_PILE.apply(i, false, overrideBlockPathWith)
                        .get(block)
                        .updateTemplate(template -> template.extend().renderType("cutout").build())
                        .createWithSuffix(block, "_stack" + i, blockModels.modelOutput)
        ));
    }

    @SuppressWarnings("unused")
    public void candleCake(Block candleBlock, Block cakeBlock, Block candleCakeBlock) {

        this.blockModels.registerSimpleFlatItemModel(candleBlock.asItem());
        Identifier candleCakeBase = ModelTemplates.CANDLE_CAKE.create(
                candleCakeBlock,
                CelebratorySpruceTextureMapping.candleCake(cakeBlock, candleBlock, false),
                this.blockModels.modelOutput
        );
        Identifier candleCakeLit = ModelTemplates.CANDLE_CAKE.createWithSuffix(
                candleCakeBlock,
                "_lit",
                CelebratorySpruceTextureMapping.candleCake(cakeBlock, candleBlock, true),
                this.blockModels.modelOutput
        );
        this.blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(candleCakeBlock)
                .with(BlockModelGenerators.createBooleanModelDispatch(
                        BlockStateProperties.LIT,
                        BlockModelGenerators.variant(new Variant(candleCakeLit)),
                        BlockModelGenerators.variant(new Variant(candleCakeBase))
                )));
    }

    @SuppressWarnings("unused")
    public void cakeBlock(Block cake, Item cakeItem) {
        this.blockModels.registerSimpleFlatItemModel(cakeItem);

        this.blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(cake)
                .with(PropertyDispatch.initial(BlockStateProperties.BITES)
                        .generate(n -> BlockModelGenerators.variant(new Variant(ModelLocationUtils.getModelLocation(
                                cake,
                                "_slice" + n
                        ))))));
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {

        this.blockModels = blockModels;
        this.itemModels = itemModels;

        basicItem(ModItems.ORNAMENT.get());
        basicItem(ModItems.FESTIVE_LIGHT.get());
        createOverlaidTintedLowEmissiveLeaves(
                ModBlocks.DECORATED_SPRUCE_LEAVES.get(),
                CelebratorySpruceClient.SPRUCE_LEAVES_TINT,
                DecoratedLeavesBlock.VARIANT
        );
        createOverlaidTintedEmissiveLeaves(
                ModBlocks.FESTIVE_SPRUCE_LEAVES.get(),
                CelebratorySpruceClient.SPRUCE_LEAVES_TINT,
                DecoratedLeavesBlock.VARIANT
        );
        blockModels.createPlantWithDefaultItem(
                ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get(),
                ModBlocks.POTTED_CELEBRATORY_SPRUCE_SAPLING.get(),
                BlockModelGenerators.PlantType.NOT_TINTED
        );
        blockModels.registerSimpleFlatItemModel(ModItems.DIAMOND_STAR.asItem());
        blockModels.registerSimpleFlatItemModel(ModItems.COPPER_STAR.asItem());
        blockModels.registerSimpleFlatItemModel(ModItems.IRON_STAR.asItem());
        blockModels.registerSimpleFlatItemModel(ModItems.GOLD_STAR.asItem());
        blockModels.createParticleOnlyBlock(ModBlocks.DIAMOND_STAR.get());
        blockModels.createParticleOnlyBlock(ModBlocks.WALL_DIAMOND_STAR.get());
        blockModels.createParticleOnlyBlock(ModBlocks.COPPER_STAR.get());
        blockModels.createParticleOnlyBlock(ModBlocks.WALL_COPPER_STAR.get());
        blockModels.createParticleOnlyBlock(ModBlocks.IRON_STAR.get());
        blockModels.createParticleOnlyBlock(ModBlocks.WALL_IRON_STAR.get());
        blockModels.createParticleOnlyBlock(ModBlocks.GOLD_STAR.get());
        blockModels.createParticleOnlyBlock(ModBlocks.WALL_GOLD_STAR.get());
        blockModels.registerSimpleFlatItemModel(ModItems.ITEM_DISPLAY.asItem());
        blockModels.createParticleOnlyBlock(ModBlocks.ITEM_DISPLAY.get());
        blockModels.createParticleOnlyBlock(ModBlocks.WALL_ITEM_DISPLAY.get());
        // blockModels.createMultiface(ModBlocks.LIGHT_NET.get());
        createDecoratedMultifaceBlock(
                ModBlocks.LIGHT_NET.get(),
                CelebratorySpruceTexturedModel.MULTIFACE_FACE_EMISSIVE,
                LightNetBlock.VARIANT
        );

        createWreath(ModBlocks.WREATH.get(), ModBlocks.WALL_WREATH.get());
        createOverlaidWreath(
                ModBlocks.DECORATED_WREATH.get(),
                ModBlocks.DECORATED_WALL_WREATH.get(),
                DecoratedWreathBlock.VARIANT
        );

        basicItem(ModItems.BLANK_CAROL_DISC.get());
        basicItem(ModItems.MUSIC_DISC_SILENT_NIGHT.get());
        basicItem(ModItems.MUSIC_DISC_WHAT_CHILD.get());
        basicItem(ModItems.MUSIC_DISC_CHRISTMAS_DAY_BELLS.get());
        basicItem(ModItems.MUSIC_DISC_CAROL_OF_THE_BELLS.get());

        ModItems.PRESENTS_BY_COLOR.forEach((color, blockItem) -> {
            booleanPropertyBoxPile(
                    blockItem.get().getBlock(),
                    CelebratorySpruceTexturedModel.TINTED_BOX_PILE,
                    CelebratorySpruce.id("block/present_pile")
            );
            generateItemWithTintedBaseLayerAndOverlay(
                    blockItem.get(),
                    CelebratorySpruce.id("item/present"),
                    "_overlay",
                    new Constant(CelebratorySpruceClient.getPresentBaseTint(color)),
                    new Constant(CelebratorySpruceClient.getPresentOverlayTint(color))
            );
        });

        itemModels.generateItemWithTintedOverlay(ModItems.STOCKING.get(), new Dye(9180700 | 0xFF000000));

        cakeBlock(ModBlocks.YULE_LOG_CAKE.get(), ModItems.YULE_LOG_CAKE.get());
        cakeBlock(ModBlocks.FRUIT_CAKE.get(), ModItems.FRUIT_CAKE.get());
        ModBlocks.CANDLE_FRUIT_CAKES.stream()
                .map(Supplier::get)
                .forEach(cake -> candleCake(cake.getCandle().get(), cake.getBaseCake().get(), cake));

        createLogFires(ModBlocks.LOG_FIRE.get());
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "ConstantConditions"})
    @Override
    protected @NotNull Stream<? extends Holder<Block>> getKnownBlocks() {
        List<Block> excluded = new ArrayList<>();
        return super.getKnownBlocks().filter(entry -> !excluded.contains(entry.value()));
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "ConstantConditions"})
    @Override
    protected @NotNull Stream<? extends Holder<Item>> getKnownItems() {
        List<Item> excluded = new ArrayList<>();

        return super.getKnownItems().filter(entry -> !excluded.contains(entry.value()));
    }

    public void generateItemWithTintedBaseLayerAndOverlay(Item item, Identifier overrideTexture, String suffix, ItemTintSource baseTintSource, ItemTintSource overlayTintSource) {
        Identifier texturePath = overrideTexture == null ? TextureMapping.getItemTexture(item) : overrideTexture;
        Identifier identifier = this.itemModels.generateLayeredItem(item, texturePath, texturePath.withSuffix(suffix));
        this.itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.tintedModel(identifier, baseTintSource, overlayTintSource)
        );
    }

    @SuppressWarnings("deprecation")
    public void createLogFires(Block... logFireBlocks) {
        MultiVariant offVariant = BlockModelGenerators.plainVariant(ModelLocationUtils.decorateBlockModelLocation(
                "campfire_off"));

        for (Block block : logFireBlocks) {
            MultiVariant onVariant = BlockModelGenerators.plainVariant(CelebratorySpruceModelTemplates.LOG_FIRE.create(
                    block,
                    CelebratorySpruceTextureMapping.logFire(block),
                    blockModels.modelOutput
            ));
            blockModels.registerSimpleFlatItemModel(block.asItem());
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                    .with(BlockModelGenerators.createBooleanModelDispatch(
                            BlockStateProperties.LIT,
                            onVariant,
                            offVariant
                    ))
                    .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
        }

    }

    public void createOverlaidWreath(Block wreathBlock, Block wallWreathBlock, IntegerProperty intProperty) {

        this.blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(wreathBlock)
                .with(PropertyDispatch.initial(intProperty).generate((property) -> {

                    Identifier modelidentifier = this.blockModels.createSuffixedVariant(
                            wreathBlock,
                            "_" + intProperty.getName() + property,
                            CelebratorySpruceTexturedModel.OVERLAID_WREATH.get(wreathBlock)
                                    .getTemplate()
                                    .extend()
                                    .renderType(ChunkSectionLayer.CUTOUT.label())
                                    .build(),
                            CelebratorySpruceTextureMapping::overlaidWreath
                    );
                    return BlockModelGenerators.plainVariant(modelidentifier);
                })));

        Int2ObjectMap<Identifier> alreadyCreated = new Int2ObjectOpenHashMap<>();
        Map<Direction, VariantMutator> propertyMap = BlockModelGenerators.ROTATION_TORCH.getEntries()
                .entrySet()
                .stream()
                .map(entry -> Pair.of(
                        entry.getKey()
                                .values()
                                .stream()
                                .map(Property.Value::value)
                                .map(value -> value instanceof Direction direction ? direction : null)
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse(null), entry.getValue()
                ))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        this.blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(wallWreathBlock)
                .with(PropertyDispatch.initial(intProperty, WallWreathBlock.FACING).generate((property, rotation) -> {

                    Identifier modelIdentifier = alreadyCreated.computeIfAbsent(
                            property, (i) -> this.blockModels.createSuffixedVariant(
                                    wallWreathBlock,
                                    "_" + intProperty.getName() + i,
                                    CelebratorySpruceTexturedModel.OVERLAID_WREATH_WALL.get(wreathBlock)
                                            .getTemplate()
                                            .extend()
                                            .renderType(ChunkSectionLayer.CUTOUT.label())
                                            .build(),
                                    CelebratorySpruceTextureMapping::overlaidWreath
                            )
                    );
                    return BlockModelGenerators.plainVariant(modelIdentifier).with(propertyMap.get(rotation));
                })));

        Identifier identifier = this.blockModels.createFlatItemModelWithBlockTextureAndOverlay(
                wreathBlock.asItem(),
                wreathBlock,
                "_overlay"
        );

        this.blockModels.registerSimpleTintedItemModel(
                wreathBlock,
                identifier,
                ItemModelUtils.constantTint(CelebratorySpruceClient.SPRUCE_LEAVES_TINT)
        );
    }


    public void createWreath(Block wreathBlock, Block wallWreathBlock) {
        TextureMapping textureMapping = CelebratorySpruceTextureMapping.wreath(wreathBlock);
        this.blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                wreathBlock,
                BlockModelGenerators.plainVariant(CelebratorySpruceModelTemplates.WREATH.create(
                        wreathBlock,
                        textureMapping,
                        this.blockModels.modelOutput
                ))
        ));
        this.blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(
                wallWreathBlock,
                BlockModelGenerators.plainVariant(CelebratorySpruceModelTemplates.WALL_WREATH.create(
                        wallWreathBlock,
                        textureMapping,
                        this.blockModels.modelOutput
                ))
        ).with(BlockModelGenerators.ROTATION_TORCH));
        Identifier identifier = this.blockModels.createFlatItemModelWithBlockTexture(wreathBlock.asItem(), wreathBlock);

        this.blockModels.registerSimpleTintedItemModel(
                wreathBlock,
                identifier,
                ItemModelUtils.constantTint(CelebratorySpruceClient.SPRUCE_LEAVES_TINT)
        );
    }

    @SuppressWarnings("unused")
    public void createOverlaidTintedLeaves(Block block, int tint, IntegerProperty intProperty) {
        createOverlaidTintedLeaves(block, CelebratorySpruceTexturedModel.OVERLAID_LEAVES, tint, intProperty);
    }

    public void createOverlaidTintedLowEmissiveLeaves(Block block, int tint, IntegerProperty intProperty) {
        createOverlaidTintedLeaves(
                block,
                CelebratorySpruceTexturedModel.OVERLAID_LEAVES_LOW_EMISSIVE,
                tint,
                intProperty
        );
    }

    public void createOverlaidTintedEmissiveLeaves(Block block, int tint, IntegerProperty intProperty) {
        createOverlaidTintedLeaves(block, CelebratorySpruceTexturedModel.OVERLAID_LEAVES_EMISSIVE, tint, intProperty);
    }


    public void createDecoratedMultifaceBlock(Block block, Function<Integer, TexturedModel.Provider> provider, IntegerProperty intProperty) {
        String intPropertyName = intProperty.getName();
        int firstIntValue = intProperty.getPossibleValues().getFirst();
        Map<Property<@NotNull Boolean>, VariantMutator> map = BlockModelGenerators.selectMultifaceProperties(
                block.defaultBlockState(),
                MultifaceBlock::getFaceProperty
        );

        // Holds defaults for all the properties.
        ConditionBuilder conditionBuilder = BlockModelGenerators.condition();

        Map<Integer, Map<BooleanProperty, VariantMutator>> decorationToSidesMap = intProperty.getPossibleValues()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        (i) -> BlockModelGenerators.selectMultifaceProperties(
                                block.defaultBlockState(),
                                MultifaceBlock::getFaceProperty
                        )
                ));

        map.forEach((booleanProperty, variantMutator) -> conditionBuilder.term(booleanProperty, false));
        conditionBuilder.term(intProperty, firstIntValue);


        // Root part of the blockstates file
        MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);

        decorationToSidesMap.forEach((decorationValue, sidesMap) -> {

            Identifier modelIdentifier = this.blockModels.createSuffixedVariant(
                    block,
                    "_" + intPropertyName + decorationValue,
                    provider.apply(decorationValue)
                            .get(block)
                            .getTemplate()
                            .extend()
                            .renderType(ChunkSectionLayer.CUTOUT.label())
                            .build(),
                    CelebratorySpruceTextureMapping::multifaceFace
            );
            MultiVariant multiVariant = BlockModelGenerators.plainVariant(modelIdentifier);

            sidesMap.forEach((booleanProperty, variantMutator) -> {
                // Adds each conditional value to the blockstates property.
                MultiVariant withVariantMutator = multiVariant.with(variantMutator);
                multiPartGenerator.with(
                        BlockModelGenerators.condition().term(booleanProperty, true).term(intProperty, decorationValue),
                        withVariantMutator
                );


            });
        });

        this.blockModels.blockStateOutput.accept(multiPartGenerator);
        this.basicItem(block.asItem());
    }

    public void createOverlaidTintedLeaves(Block block, Function<Integer, TexturedModel.Provider> provider, int tint, IntegerProperty intProperty) {
        int minPropertyValue = intProperty.getPossibleValues().stream().mapToInt(i -> i).min().orElse(0);

        Mutable<Identifier> itemModelIdentifier = new MutableObject<>();

        Int2ObjectMap<Identifier> intToObjectMap = new Int2ObjectOpenHashMap<>();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(intProperty)
                        .generate((property) -> BlockModelGenerators.plainVariant(intToObjectMap.computeIfAbsent(
                                property, (stage) -> {
                                    Identifier modelidentifier = this.blockModels.createSuffixedVariant(
                                            block,
                                            "_" + intProperty.getName() + stage,
                                            provider.apply(stage)
                                                    .get(block)
                                                    .getTemplate()
                                                    .extend()
                                                    .renderType(ChunkSectionLayer.CUTOUT.label())
                                                    .build(),
                                            CelebratorySpruceTextureMapping::overlaidLeaves
                                    );
                                    if (stage == minPropertyValue) {
                                        itemModelIdentifier.setValue(modelidentifier);
                                    }
                                    return modelidentifier;
                                }
                        )))));
        blockModels.registerSimpleTintedItemModel(block, itemModelIdentifier.get(), ItemModelUtils.constantTint(tint));
    }

    private void basicItem(Item item) {
        itemModels.generateFlatItem(item, item, ModelTemplates.FLAT_ITEM);
    }

    @SuppressWarnings("unused")
    private void handheldItem(Item item) {
        itemModels.generateFlatItem(item, item, ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    @SuppressWarnings("unused")
    public void tippedArrow(Item arrowItem) {
        Identifier Identifier = itemModels.generateLayeredItem(
                arrowItem,
                ModelLocationUtils.getModelLocation(arrowItem, "_head"),
                ModelLocationUtils.getModelLocation(arrowItem, "_base")
        );
        itemModels.addPotionTint(arrowItem, Identifier);
    }

    @SuppressWarnings("unused")
    protected void simpleBlockWithItem(Block block) {
        blockModels.createTrivialBlock(block, TexturedModel.CUBE);
    }

    @SuppressWarnings("unused")
    protected void simpleBlockWithItem(Block block, String renderType) {
        blockModels.createTrivialBlock(
                block,
                TexturedModel.CUBE.updateTemplate(template -> template.extend().renderType(renderType).build())
        );
    }

    @SuppressWarnings("unused")
    protected void nonrotatablePillarBlock(Block block) {
        TexturedModel model = TexturedModel.COLUMN.get(block)
                .updateTextures(p_387400_ -> p_387400_.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block)));
        this.blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                block,
                BlockModelGenerators.variant(new Variant(model.create(block, this.blockModels.modelOutput)))
        ));
    }

    @SuppressWarnings("unused")
    protected void tumbledBlockWithItem(Block block) {
        tumbledBlockWithItem(block, null);
    }

    @SuppressWarnings("unused")
    protected void mirroredColumnBlock(Block block) {
        TextureMapping mapping = TextureMapping.column(block);
        TextureMapping altMapping = CelebratorySpruceTextureMapping.columnAlt(block);


        TextureMapping[] mappings = new TextureMapping[]{mapping, altMapping};
        String[] suffixes = new String[]{"", "_alt"};

        blockModels.blockStateOutput.accept(createMirroredColumnGenerator(
                block,
                blockModels.modelOutput,
                mappings,
                suffixes
        ));
    }

    @SuppressWarnings("SameParameterValue")
    protected void tumbledBlockWithItem(Block block, @Nullable String renderType) {
        TexturedModel.Provider model = TexturedModel.CUBE;

        if (renderType != null) {
            model = model.updateTemplate(template -> template.extend().renderType(renderType).build());
        }

        blockModels.blockStateOutput.accept(createTumbledBlock(block, model.create(block, blockModels.modelOutput)));
    }

    @SuppressWarnings("unused")
    protected void tumbledOverlaidBlockWithItem(Block block, Block base, String... overlays) {
        BiFunction<String, Block, TexturedModel.Provider> baseModel = CelebratorySpruceTexturedModel.OVERLAID_CUBE;

        Function<String, TexturedModel.Provider> model = (lambdaOverlay) -> baseModel.apply(lambdaOverlay, base)
                .updateTemplate(template -> template.extend().renderType("cutout").build());

        blockModels.blockStateOutput.accept(createTumbledOverlaidBlock(block, model, overlays));
    }

    @SuppressWarnings("unused")
    protected void overlaidBlockWithItem(Block block, Block base, String... overlays) {
        BiFunction<String, Block, TexturedModel.Provider> baseModel = CelebratorySpruceTexturedModel.OVERLAID_CUBE;

        Function<String, TexturedModel.Provider> model = (lambdaOverlay) -> baseModel.apply(lambdaOverlay, base)
                .updateTemplate(template -> template.extend().renderType("cutout").build());

        blockModels.blockStateOutput.accept(createOverlaidBlock(block, model, overlays));
    }

    @SuppressWarnings("unused")
    protected void doubleSidedLogBlockWithItem(Block block) {
        blockModels.blockStateOutput.accept(createDoubleSidedLogBlock(
                block,
                CelebratorySpruceTexturedModel.DOUBLE_SIDED_LOG.create(block, blockModels.modelOutput)
        ));
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    protected void rotatedTopOverlaidBlockWithItem(Block block, Block base, String topOverlay, @NotNull String[] overlays) {
        TriFunction<String, String, Block, TexturedModel.Provider> baseModel = CelebratorySpruceTexturedModel.TOP_OVERLAID_CUBE;

        Function<String, TexturedModel.Provider> model = (lambdaOverlay) -> baseModel.apply(
                        lambdaOverlay,
                        topOverlay,
                        base
                )
                .updateTemplate(template -> template.extend().renderType("cutout").build());

        blockModels.blockStateOutput.accept(createRotatedTopOverlaidBlock(block, model, overlays));
    }


    @SuppressWarnings("unused")
    public void createPlantWithDefaultItem(Block block, Block pottedBlock, BlockModelGenerators.PlantType plantType, String renderType) {
        blockModels.registerSimpleItemModel(block.asItem(), plantType.createItemModel(blockModels, block));
        createPlant(block, pottedBlock, plantType, renderType);
    }

    @SuppressWarnings("unused")
    public void createPlantWithDefaultItemWithCustomPottedTexture(Block block, Block pottedBlock, Identifier customTexture, BlockModelGenerators.PlantType plantType, String renderType) {
        blockModels.registerSimpleItemModel(block.asItem(), plantType.createItemModel(blockModels, block));
        createPlantWithCustomPottedTexture(block, pottedBlock, customTexture, plantType, renderType);
    }

    public void createPlant(Block block, Block pottedBlock, BlockModelGenerators.PlantType plantType, String renderType) {
        createCrossBlock(block, plantType, renderType);
        TextureMapping texturemapping = plantType.getPlantTextureMapping(block);
        Identifier Identifier = plantType.getCrossPot()
                .extend()
                .renderType(renderType)
                .build()
                .create(pottedBlock, texturemapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                pottedBlock,
                BlockModelGenerators.plainVariant(Identifier)
        ));
    }

    public void createPlantWithCustomPottedTexture(Block block, Block pottedBlock, Identifier customPottedTexture, BlockModelGenerators.PlantType plantType, String renderType) {
        createCrossBlock(block, plantType, renderType);
        TextureMapping texturemapping = TextureMapping.singleSlot(TextureSlot.PLANT, customPottedTexture);

        Identifier Identifier = plantType.getCrossPot()
                .extend()
                .renderType(renderType)
                .build()
                .create(pottedBlock, texturemapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                pottedBlock,
                BlockModelGenerators.plainVariant(Identifier)
        ));
    }

    @SuppressWarnings("unused")
    public void createPottedOnly(Block block, Block pottedBlock, BlockModelGenerators.PlantType plantType, String renderType) {
        TextureMapping texturemapping = plantType.getPlantTextureMapping(block);
        Identifier Identifier = plantType.getCrossPot()
                .extend()
                .renderType(renderType)
                .build()
                .create(pottedBlock, texturemapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                pottedBlock,
                BlockModelGenerators.plainVariant(Identifier)
        ));
    }

    public void createCrossBlock(Block block, BlockModelGenerators.PlantType plantType, String renderType) {
        TextureMapping texturemapping = plantType.getTextureMapping(block);
        createCrossBlock(block, plantType, texturemapping, renderType);
    }

    public void createCrossBlock(Block block, BlockModelGenerators.PlantType plantType, TextureMapping textureMapping, String renderType) {
        Identifier Identifier = plantType.getCross()
                .extend()
                .renderType(renderType)
                .build()
                .create(block, textureMapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                block,
                BlockModelGenerators.plainVariant(Identifier)
        ));
    }

    public void createCrossBlockWithoutItem(Block block, BlockModelGenerators.PlantType plantType, String renderType, Property<@NotNull Integer> ageProperty, int... possibleValues) {
        if (ageProperty.getPossibleValues().size() != possibleValues.length) {
            throw new IllegalArgumentException("missing values for property: " + ageProperty);
        } else {
            PropertyDispatch<MultiVariant> propertydispatch = PropertyDispatch.initial(ageProperty)
                    .generate(p_388685_ -> {
                        String s = "_stage" + possibleValues[p_388685_];
                        TextureMapping texturemapping = TextureMapping.cross(TextureMapping.getBlockTexture(block, s));
                        Identifier Identifier = plantType.getCross()
                                .extend()
                                .renderType(renderType)
                                .build()
                                .createWithSuffix(block, s, texturemapping, blockModels.modelOutput);
                        return BlockModelGenerators.plainVariant(Identifier);
                    });
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(propertydispatch));
        }
    }

    @SuppressWarnings("unused")
    public void createCropBlock(Block cropBlock, String renderType, Property<@NotNull Integer> ageProperty, int... ageToVisualStageMapping) {
        this.blockModels.registerSimpleFlatItemModel(cropBlock.asItem());
        createCropBlockWithoutItem(cropBlock, renderType, ageProperty, ageToVisualStageMapping);
    }

    public void createCropBlockWithoutItem(Block cropBlock, String renderType, Property<@NotNull Integer> ageProperty, int... ageToVisualStageMapping) {
        if (ageProperty.getPossibleValues().size() != ageToVisualStageMapping.length) {
            throw new IllegalArgumentException();
        } else {
            Int2ObjectMap<Identifier> int2objectmap = new Int2ObjectOpenHashMap<>();
            this.blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(cropBlock)
                    .with(PropertyDispatch.initial(ageProperty).generate((p_408977_) -> {
                        int i = ageToVisualStageMapping[p_408977_];
                        return BlockModelGenerators.plainVariant(int2objectmap.computeIfAbsent(
                                i, (stage) -> this.blockModels.createSuffixedVariant(
                                        cropBlock,
                                        "_stage" + stage,
                                        ModelTemplates.CROP.extend().renderType(renderType).build(),
                                        TextureMapping::crop
                                )
                        ));
                    })));
        }
    }

    @SuppressWarnings("unused")
    public void createCrossBlock(Block block, BlockModelGenerators.PlantType plantType, String renderType, Property<@NotNull Integer> ageProperty, int... possibleValues) {
        createCrossBlockWithoutItem(block, plantType, renderType, ageProperty, possibleValues);
        blockModels.registerSimpleFlatItemModel(block.asItem());

    }

    @SuppressWarnings("unused")
    public void createAsteriskBlockWithoutItem(Block block, BlockModelGenerators.PlantType plantType, String renderType, Property<@NotNull Integer> ageProperty, int... possibleValues) {
        if (ageProperty.getPossibleValues().size() != possibleValues.length) {
            throw new IllegalArgumentException("missing values for property: " + ageProperty);
        } else {
            PropertyDispatch<MultiVariant> propertydispatch = PropertyDispatch.initial(ageProperty).generate(index -> {
                String s = "_stage" + possibleValues[index];
                TextureMapping texture = CelebratorySpruceTextureMapping.asterisk(
                        TextureMapping.getBlockTexture(block),
                        TextureMapping.getBlockTexture(block, s)
                );

                Identifier Identifier = CelebratorySpruceModelTemplates.ASTERISK.extend()
                        .renderType(renderType)
                        .build()
                        .createWithSuffix(block, s, texture, blockModels.modelOutput);
                return BlockModelGenerators.plainVariant(Identifier);
            });
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(propertydispatch));
        }
    }

    @SuppressWarnings("unused")
    public void generateSimpleSpecialItemModel(Block block, SpecialModelRenderer.Unbaked specialModel) {
        Item item = block.asItem();
        Identifier Identifier = ModelLocationUtils.getModelLocation(item);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.specialModel(Identifier, specialModel));
    }
}
