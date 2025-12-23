package com.startraveler.celebratoryspruce.client;

import com.startraveler.celebratoryspruce.block.entity.ItemRenderingBlockEntity;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

import java.util.List;

public class ItemRenderingBlockEntityRenderState extends
        BlockEntityRenderState {
    public ItemStackRenderState item;
    public List<ItemRenderingBlockEntity.Transform<?>> transforms;
}
