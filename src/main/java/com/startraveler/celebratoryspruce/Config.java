package com.startraveler.celebratoryspruce;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("unused")
public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SNOW_MELTS_IN_LIGHT = BUILDER
            .comment("Whether or not snow melts in light.")
            .define("snowMeltsInLight", true);

    public static final ModConfigSpec.IntValue SNOW_MELTING_THRESHOLD = BUILDER
            .comment(
                    "The light level above which snow melts, if snow melting in light is enabled. Typically one more than the level at which snow lands.")
            .defineInRange("snowMeltingLightThreshold", 11, 0, 15);

    public static final ModConfigSpec.IntValue SNOW_ACCUMULATION_THRESHOLD = BUILDER
            .comment("The light level below which snow accumulates. Typically one less than the level at which snow melts.")
            .defineInRange("snowBuildupLightThreshold", 10, 0, 15);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(Identifier.parse(itemName));
    }
}
