package com.startraveler.celebratoryspruce.datagen.model;


import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public class CelebratorySpruceTextureMapping {

    public static TextureMapping overlaidLeaves(Identifier block) {
        return TextureMapping.cube(block).put(CelebratorySpruceTextureSlot.OVERLAY, block.withSuffix("_overlay"));
    }

    public static TextureMapping overlaidLeavesLowEmissive(Identifier block) {
        return TextureMapping.cube(block).put(CelebratorySpruceTextureSlot.OVERLAY, block.withSuffix("_overlay"));
    }

    public static TextureMapping overlaidLeavesEmissive(Identifier block) {
        return TextureMapping.cube(block).put(CelebratorySpruceTextureSlot.OVERLAY, block.withSuffix("_overlay"));
    }

    public static TextureMapping columnAlt(Block block) {
        return (new TextureMapping()).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_alt"))
                .put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_top"));
    }

    public static TextureMapping asterisk(Identifier plus, Identifier cross) {
        return new TextureMapping().put(CelebratorySpruceTextureSlot.PLUS, plus).put(TextureSlot.CROSS, cross);
    }

    @SuppressWarnings("unused")
    public static TextureMapping logFire(Block block) {
        return new TextureMapping().put(CelebratorySpruceTextureSlot.LOG, TextureMapping.getBlockTexture(block, "_log"))
                .put(CelebratorySpruceTextureSlot.BASE, TextureMapping.getBlockTexture(block, "_base"))
                .put(TextureSlot.LIT_LOG, TextureMapping.getBlockTexture(block, "_log_lit"))
                .put(TextureSlot.FIRE, TextureMapping.getBlockTexture(block, "_fire"));
    }


    @SuppressWarnings("unused")
    public static TextureMapping asterisk(Block block) {
        return new TextureMapping().put(
                        CelebratorySpruceTextureSlot.PLUS,
                        TextureMapping.getBlockTexture(block, "_plus")
                )
                .put(TextureSlot.CROSS, TextureMapping.getBlockTexture(block, "_cross"));
    }

    @SuppressWarnings("unused")
    public static TextureMapping boxPile(Block block, int boxes, boolean closed, Identifier overrideBlockPathWith) {
        Identifier basePath = overrideBlockPathWith == null ? TextureMapping.getBlockTexture(block) : overrideBlockPathWith;
        return new TextureMapping().put(
                        CelebratorySpruceTextureSlot.FLOWER,
                        basePath.withSuffix(closed ? "_closed" : "")
                )
                .put(TextureSlot.PARTICLE, basePath.withSuffix("_side"))
                .put(TextureSlot.SIDE, basePath.withSuffix("_side"));
    }

    @SuppressWarnings("unused")
    public static TextureMapping tintedBoxPile(Block block, int boxes, boolean closed, Identifier overrideBlockPathWith) {
        Identifier basePath = overrideBlockPathWith == null ? TextureMapping.getBlockTexture(block) : overrideBlockPathWith;
        return new TextureMapping().put(TextureSlot.PARTICLE, basePath)
                .put(CelebratorySpruceTextureSlot.LAYER0_FLOWER, basePath.withSuffix(closed ? "_closed" : ""))
                .put(CelebratorySpruceTextureSlot.LAYER0_SIDE, basePath.withSuffix("_side"))
                .put(
                        CelebratorySpruceTextureSlot.LAYER1_FLOWER,
                        basePath.withSuffix(closed ? "_overlay_closed" : "_overlay")
                )
                .put(CelebratorySpruceTextureSlot.LAYER1_SIDE, basePath.withSuffix("_overlay_side"));
    }

    public static TextureMapping candleCake(Block cake, Block candle, boolean lit) {
        return new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(cake, "_side"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(cake, "_bottom"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(cake, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(cake, "_side"))
                .put(TextureSlot.CANDLE, TextureMapping.getBlockTexture(candle, lit ? "_lit" : ""));
    }


    @SuppressWarnings("unused")
    public static TextureMapping overlaidCubeBlock(Block block, Block base, Identifier overlay) {
        return new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(base))
                .put(CelebratorySpruceTextureSlot.BASE, TextureMapping.getBlockTexture(base))
                .put(CelebratorySpruceTextureSlot.OVERLAY, overlay);
    }

    @SuppressWarnings("unused")
    public static TextureMapping topOverlaidCubeBlock(Block block, Block base, Identifier overlay, Identifier topOverlay) {
        return new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(base))
                .put(CelebratorySpruceTextureSlot.BASE, TextureMapping.getBlockTexture(base))
                .put(CelebratorySpruceTextureSlot.OVERLAY, overlay)
                .put(TextureSlot.TOP, topOverlay.withSuffix("_top"))
                .put(TextureSlot.SIDE, topOverlay.withSuffix("_side"))
                .put(TextureSlot.BOTTOM, topOverlay.withSuffix("_bottom"));
    }

    public static TextureMapping multifaceFace(Identifier identifier) {
        return new TextureMapping().put(TextureSlot.PARTICLE, identifier)
                .put(CelebratorySpruceTextureSlot.FACE, identifier);
    }

    public static TextureMapping wreath(Block block) {
        Identifier identifier = TextureMapping.getBlockTexture(block);
        return new TextureMapping().put(TextureSlot.PARTICLE, identifier)
                .put(TextureSlot.FRONT, identifier)
                .put(TextureSlot.ALL, identifier);
    }

    public static TextureMapping overlaidWreath(Block block) {
        Identifier identifier = TextureMapping.getBlockTexture(block);
        return overlaidWreath(identifier);
    }

    public static TextureMapping overlaidWreath(Identifier identifier) {
        return new TextureMapping().put(TextureSlot.PARTICLE, identifier)
                .put(TextureSlot.ALL, identifier)
                .put(TextureSlot.FRONT, identifier)
                .put(CelebratorySpruceTextureSlot.OVERLAY_FRONT, identifier.withSuffix("_overlay_front"))
                .put(CelebratorySpruceTextureSlot.OVERLAY, identifier.withSuffix("_overlay"));
    }
}

