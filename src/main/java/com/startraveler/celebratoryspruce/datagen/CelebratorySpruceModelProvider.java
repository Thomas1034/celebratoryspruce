package com.startraveler.celebratoryspruce.datagen;

import com.google.common.collect.Streams;
import com.mojang.math.Quadrant;
import com.startraveler.celebratoryspruce.*;
import com.startraveler.celebratoryspruce.block.BoxPileBlock;
import com.startraveler.celebratoryspruce.block.DecoratedLeavesBlock;
import com.startraveler.celebratoryspruce.datagen.model.CelebratorySpruceModelTemplates;
import com.startraveler.celebratoryspruce.datagen.model.CelebratorySpruceTextureMapping;
import com.startraveler.celebratoryspruce.datagen.model.CelebratorySpruceTexturedModel;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
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

    protected void boxPile(Block block) {
        blockModels.blockStateOutput.accept(createBoxPile(
                block,
                (i) -> CelebratorySpruceTexturedModel.BOX_PILE.apply(i)
                        .get(block)
                        .updateTemplate(template -> template.extend().renderType("cutout").build())
                        .createWithSuffix(block, "_stack" + i, blockModels.modelOutput)
        ));
    }

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
        blockModels.createCrossBlock(ModBlocks.GOLD_STAR.get(), BlockModelGenerators.PlantType.EMISSIVE_NOT_TINTED);
        blockModels.registerSimpleItemModel(
                ModBlocks.GOLD_STAR.asItem(),
                BlockModelGenerators.PlantType.EMISSIVE_NOT_TINTED.createItemModel(
                        blockModels,
                        ModBlocks.GOLD_STAR.get()
                )
        );

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

    private String name(Block block) {
        return this.key(block).getPath();
    }

    private Identifier key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

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

    private void handheldItem(Item item) {
        itemModels.generateFlatItem(item, item, ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    public void tippedArrow(Item arrowItem) {
        Identifier Identifier = itemModels.generateLayeredItem(
                arrowItem,
                ModelLocationUtils.getModelLocation(arrowItem, "_head"),
                ModelLocationUtils.getModelLocation(arrowItem, "_base")
        );
        itemModels.addPotionTint(arrowItem, Identifier);
    }

    protected void simpleBlockWithItem(Block block) {
        blockModels.createTrivialBlock(block, TexturedModel.CUBE);
    }

    protected void simpleBlockWithItem(Block block, String renderType) {
        blockModels.createTrivialBlock(
                block,
                TexturedModel.CUBE.updateTemplate(template -> template.extend().renderType(renderType).build())
        );
    }

    protected void nonrotatablePillarBlock(Block block) {
        TexturedModel model = TexturedModel.COLUMN.get(block)
                .updateTextures(p_387400_ -> p_387400_.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block)));
        this.blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                block,
                BlockModelGenerators.variant(new Variant(model.create(block, this.blockModels.modelOutput)))
        ));
    }

    protected void tumbledBlockWithItem(Block block) {
        tumbledBlockWithItem(block, null);
    }

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

    protected void tumbledOverlaidBlockWithItem(Block block, Block base, String... overlays) {
        BiFunction<String, Block, TexturedModel.Provider> baseModel = CelebratorySpruceTexturedModel.OVERLAID_CUBE;

        Function<String, TexturedModel.Provider> model = (lambdaOverlay) -> baseModel.apply(lambdaOverlay, base)
                .updateTemplate(template -> template.extend().renderType("cutout").build());

        blockModels.blockStateOutput.accept(createTumbledOverlaidBlock(block, model, overlays));
    }

    protected void overlaidBlockWithItem(Block block, Block base, String... overlays) {
        BiFunction<String, Block, TexturedModel.Provider> baseModel = CelebratorySpruceTexturedModel.OVERLAID_CUBE;

        Function<String, TexturedModel.Provider> model = (lambdaOverlay) -> baseModel.apply(lambdaOverlay, base)
                .updateTemplate(template -> template.extend().renderType("cutout").build());

        blockModels.blockStateOutput.accept(createOverlaidBlock(block, model, overlays));
    }

    protected void doubleSidedLogBlockWithItem(Block block) {
        blockModels.blockStateOutput.accept(createDoubleSidedLogBlock(
                block,
                CelebratorySpruceTexturedModel.DOUBLE_SIDED_LOG.create(block, blockModels.modelOutput)
        ));
    }

    @SuppressWarnings("SameParameterValue")
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


    public void createPlantWithDefaultItem(Block block, Block pottedBlock, BlockModelGenerators.PlantType plantType, String renderType) {
        blockModels.registerSimpleItemModel(block.asItem(), plantType.createItemModel(blockModels, block));
        createPlant(block, pottedBlock, plantType, renderType);
    }

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

    public void createCrossBlock(Block block, BlockModelGenerators.PlantType plantType, String renderType, Property<@NotNull Integer> ageProperty, int... possibleValues) {
        createCrossBlockWithoutItem(block, plantType, renderType, ageProperty, possibleValues);
        blockModels.registerSimpleFlatItemModel(block.asItem());

    }

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


    public void generateSimpleSpecialItemModel(Block block, SpecialModelRenderer.Unbaked specialModel) {
        Item item = block.asItem();
        Identifier Identifier = ModelLocationUtils.getModelLocation(item);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.specialModel(Identifier, specialModel));
    }
}
