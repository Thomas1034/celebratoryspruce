package com.startraveler.celebratoryspruce.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.joml.Matrix4f;

public class ItemRenderingBlockEntityRenderState extends
        BlockEntityRenderState {
    public ItemStackRenderState item;
    public Matrix4f bakedTransforms;
}
