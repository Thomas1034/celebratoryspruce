package com.startraveler.celebratoryspruce;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public class CelebratorySpruceTreeGrowers {

    public static final TreeGrower CELEBRATORY_SPRUCE = new TreeGrower(
            "celebratory_spruce",
            0.5F,
            Optional.of(CelebratorySpruceTreeFeatures.MEGA_SPRUCE),
            Optional.of(CelebratorySpruceTreeFeatures.MEGA_PINE),
            Optional.of(CelebratorySpruceTreeFeatures.SPRUCE),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );

    public static class CelebratorySpruceTreeFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_SPRUCE = createKey("mega_celebratory_spruce");
        public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_PINE = createKey("mega_celebratory_pine");
        public static final ResourceKey<ConfiguredFeature<?, ?>> SPRUCE = createKey("celebratory_spruce");

        public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, CelebratorySpruce.id(name));
        }
    }
}
