package com.startraveler.celebratoryspruce;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class ModArmorMaterials {

    // The resource key of the equipment asset used to link
    // the `EquipmentClientInfo` JSON.
    // Points to assets/celebratoryspruce/equipment/christmas.json
    public static final ResourceKey<EquipmentAsset> CHRISTMAS_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            CelebratorySpruce.id("christmas")
    );
}
