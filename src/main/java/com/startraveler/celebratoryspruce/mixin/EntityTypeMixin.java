package com.startraveler.celebratoryspruce.mixin;

import com.startraveler.celebratoryspruce.HasDescriptionId;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements HasDescriptionId {
}
