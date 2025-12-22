package com.startraveler.celebratoryspruce.mixin;

import com.startraveler.celebratoryspruce.HasDescriptionId;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements HasDescriptionId {
}
