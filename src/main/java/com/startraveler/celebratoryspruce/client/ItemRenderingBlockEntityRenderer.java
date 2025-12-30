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
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ItemRenderingBlockEntityRenderer<T extends ItemRenderingBlockEntity> implements BlockEntityRenderer<@NotNull T, @NotNull ItemRenderingBlockEntityRenderState> {

    private final ItemModelResolver itemModelResolver;

    public ItemRenderingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static void applyAllTransforms(PoseStack stack, List<ItemRenderingBlockEntity.Transform<?>> transforms) {
        for (ItemRenderingBlockEntity.Transform<?> transform : transforms) {
            transform.apply(stack.last().pose());
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
        ItemStack stack = blockEntity.getStackForDisplay();
        Boolean isNormallyFoil = stack.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        renderState.item = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(
                renderState.item,
                blockEntity.getStackForDisplay(),
                ItemDisplayContext.FIXED, // FIXED
                blockEntity.getLevel(),
                blockEntity,
                (int) blockEntity.getBlockPos().asLong()
        );

        if (isNormallyFoil != null) {
            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, isNormallyFoil);
        }
        renderState.transforms = blockEntity.getTransforms();
    }

    @Override
    public void submit(ItemRenderingBlockEntityRenderState itemRenderingBlockEntityRenderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        // TODO refactor to use baked transforms.
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
