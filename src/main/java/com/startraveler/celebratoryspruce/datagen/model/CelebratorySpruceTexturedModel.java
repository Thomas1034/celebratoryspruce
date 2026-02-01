package com.startraveler.celebratoryspruce.datagen.model;

import com.startraveler.celebratoryspruce.CelebratorySpruce;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CelebratorySpruceTexturedModel {

    @SuppressWarnings("unused")
    public static final TexturedModel.Provider WREATH = TexturedModel.createDefault(
            CelebratorySpruceTextureMapping::wreath,
            CelebratorySpruceModelTemplates.WREATH
    );

    @SuppressWarnings("unused")
    public static final TexturedModel.Provider WREATH_WALL = TexturedModel.createDefault(
            CelebratorySpruceTextureMapping::wreath,
            CelebratorySpruceModelTemplates.WALL_WREATH
    );

    public static final TexturedModel.Provider OVERLAID_WREATH = TexturedModel.createDefault(
            CelebratorySpruceTextureMapping::overlaidWreath,
            CelebratorySpruceModelTemplates.OVERLAID_WREATH
    );

    public static final TexturedModel.Provider OVERLAID_WREATH_WALL = TexturedModel.createDefault(
            CelebratorySpruceTextureMapping::overlaidWreath,
            CelebratorySpruceModelTemplates.OVERLAID_WALL_WREATH
    );

    @SuppressWarnings("unused")
    public static final Function<Integer, TexturedModel.Provider> MULTIFACE_FACE = variant -> TexturedModel.createDefault(
            block -> CelebratorySpruceTextureMapping.multifaceFace(TextureMapping.getBlockTexture(block)),
            CelebratorySpruceModelTemplates.MULTIFACE_FACE
    );

    public static final Function<Integer, TexturedModel.Provider> MULTIFACE_FACE_EMISSIVE = variant -> TexturedModel.createDefault(
            block -> CelebratorySpruceTextureMapping.multifaceFace(TextureMapping.getBlockTexture(block)),
            CelebratorySpruceModelTemplates.MULTIFACE_FACE_EMISSIVE
    );

    public static final Function<Integer, TexturedModel.Provider> OVERLAID_LEAVES = age -> TexturedModel.createDefault(
            block -> CelebratorySpruceTextureMapping.overlaidLeaves(TextureMapping.getBlockTexture(block)),
            CelebratorySpruceModelTemplates.OVERLAID_LEAVES
    );

    public static final Function<Integer, TexturedModel.Provider> OVERLAID_LEAVES_LOW_EMISSIVE = age -> TexturedModel.createDefault(
            block -> CelebratorySpruceTextureMapping.overlaidLeavesLowEmissive(TextureMapping.getBlockTexture(block)),
            CelebratorySpruceModelTemplates.OVERLAID_LEAVES_LOW_EMISSIVE
    );


    public static final Function<Integer, TexturedModel.Provider> OVERLAID_LEAVES_EMISSIVE = age -> TexturedModel.createDefault(
            block -> CelebratorySpruceTextureMapping.overlaidLeavesEmissive(TextureMapping.getBlockTexture(block)),
            CelebratorySpruceModelTemplates.OVERLAID_LEAVES_EMISSIVE
    );

    public static final TexturedModel.Provider DOUBLE_SIDED_LOG = TexturedModel.createDefault(
            TextureMapping::logColumn,
            CelebratorySpruceModelTemplates.DOUBLE_SIDED_CUBE_COLUMN
    );

    public static final TriFunction<Integer, Boolean, Identifier, TexturedModel.Provider> BOX_PILE = (boxes, closed, overrideBlockPathWith) -> TexturedModel.createDefault(
            (block) -> CelebratorySpruceTextureMapping.boxPile(block, boxes, closed, overrideBlockPathWith),
            CelebratorySpruceModelTemplates.boxPile(boxes)
    );

    public static final TriFunction<Integer, Boolean, Identifier, TexturedModel.Provider> TINTED_BOX_PILE = (boxes, closed, overrideBlockPathWith) -> TexturedModel.createDefault(
            (block) -> CelebratorySpruceTextureMapping.tintedBoxPile(block, boxes, closed, overrideBlockPathWith),
            CelebratorySpruceModelTemplates.tintedBoxPile(boxes)
    );


    public static final BiFunction<String, Block, TexturedModel.Provider> OVERLAID_CUBE = (overlay, base) -> TexturedModel.createDefault(
            (block) -> CelebratorySpruceTextureMapping.overlaidCubeBlock(
                    block,
                    base,
                    Identifier.fromNamespaceAndPath(CelebratorySpruce.MODID, overlay).withPrefix("block/")
            ), CelebratorySpruceModelTemplates.OVERLAID_CUBE
    );

    public static final TriFunction<String, String, Block, TexturedModel.Provider> TOP_OVERLAID_CUBE = (overlay, topOverlay, base) -> TexturedModel.createDefault(
            (block) -> CelebratorySpruceTextureMapping.topOverlaidCubeBlock(
                    block,
                    base,
                    Identifier.fromNamespaceAndPath(CelebratorySpruce.MODID, overlay).withPrefix("block/"),
                    Identifier.fromNamespaceAndPath(CelebratorySpruce.MODID, topOverlay).withPrefix("block/")
            ), CelebratorySpruceModelTemplates.TOP_OVERLAID_CUBE
    );

}
