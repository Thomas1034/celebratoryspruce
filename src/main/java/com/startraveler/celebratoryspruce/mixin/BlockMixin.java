package com.startraveler.celebratoryspruce.mixin;

import com.startraveler.celebratoryspruce.HasDescriptionId;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public abstract class BlockMixin implements HasDescriptionId {
}
