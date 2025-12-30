package com.startraveler.celebratoryspruce;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("unused")
public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ICE_MELTS_IN_LIGHT = BUILDER.comment(
            "Whether or not ice melts in light.").define("iceMeltsInLight", true);

    public static final ModConfigSpec.IntValue ICE_MELTING_THRESHOLD = BUILDER.comment(
                    "The light level above which ice melts, if ice melting in light is enabled. Typically one more than the level at which ice freezes.")
            .defineInRange("iceMeltingLightThreshold", 11, 0, 15);

    public static final ModConfigSpec.IntValue ICE_FREEZING_THRESHOLD = BUILDER.comment(
                    "The light level below which ice freezes. Typically one less than the level at which ice melts.")
            .defineInRange("iceFreezingLightThreshold", 10, 0, 15);

    public static final ModConfigSpec.BooleanValue SNOW_MELTS_IN_LIGHT = BUILDER.comment(
            "Whether or not snow melts in light.").define("snowMeltsInLight", true);

    public static final ModConfigSpec.IntValue SNOW_MELTING_THRESHOLD = BUILDER.comment(
                    "The light level above which snow melts, if snow melting in light is enabled. Typically one more than the level at which snow lands.")
            .defineInRange("snowMeltingLightThreshold", 11, 0, 15);

    public static final ModConfigSpec.IntValue SNOW_ACCUMULATION_THRESHOLD = BUILDER.comment(
                    "The light level below which snow accumulates. Typically one less than the level at which snow melts.")
            .defineInRange("snowBuildupLightThreshold", 10, 0, 15);


    public static final ModConfigSpec.DoubleValue SNOW_TEMPERATURE_THRESHOLD = BUILDER.comment(
                    "The temperature threshold required for rain to turn to snow. Higher values will make warm biomes have snow; lower values will make cold biomes have rain.")
            .defineInRange("snowTemperatureThreshold", 0.15, 0.0, 1.0);


    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(Identifier.parse(itemName));
    }
}
