package com.startraveler.celebratoryspruce.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.startraveler.celebratoryspruce.block.entity.ItemRenderingBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ItemRenderingBlockEntityRenderer<T extends ItemRenderingBlockEntity> implements BlockEntityRenderer<T, @NotNull ItemRenderingBlockEntityRenderState> {

    private final ItemModelResolver itemModelResolver;

    public ItemRenderingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static void applyTransform(PoseStack stack, ItemRenderingBlockEntity.Transform<?> transform) {
        switch (transform.getType()) {
            case SCALE:
                ItemRenderingBlockEntity.Scale scale = (ItemRenderingBlockEntity.Scale) transform;
                stack.scale(scale.getX(), scale.getY(), scale.getZ());
                break;
            case ROTATION:
                ItemRenderingBlockEntity.Rotation rotation = (ItemRenderingBlockEntity.Rotation) transform;
                stack.mulPose(rotation.quaternion());
                break;
            case TRANSLATION:
                ItemRenderingBlockEntity.Translation translation = (ItemRenderingBlockEntity.Translation) transform;
                stack.translate(translation.getX(), translation.getY(), translation.getZ());
                break;
        }
    }

    public static void applyAllTransforms(PoseStack stack, List<ItemRenderingBlockEntity.Transform<?>> transforms) {
        for (ItemRenderingBlockEntity.Transform<?> transform : transforms) {
            applyTransform(stack, transform);
        }
    }

    @Override
    public @NotNull ItemRenderingBlockEntityRenderState createRenderState() {
        return new ItemRenderingBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, ItemRenderingBlockEntityRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity,
                renderState,
                partialTick,
                cameraPosition,
                breakProgress
        );

        renderState.item = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(
                renderState.item,
                blockEntity.getStackForDisplay(),
                ItemDisplayContext.FIXED,
                blockEntity.getLevel(),
                null,
                (int) blockEntity.getBlockPos().asLong()
        );
        renderState.transforms = blockEntity.getTransforms();
    }

    @Override
    public void submit(ItemRenderingBlockEntityRenderState itemRenderingBlockEntityRenderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        applyAllTransforms(poseStack, itemRenderingBlockEntityRenderState.transforms);

        itemRenderingBlockEntityRenderState.item.submit(
                poseStack,
                submitNodeCollector,
                itemRenderingBlockEntityRenderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }
}
