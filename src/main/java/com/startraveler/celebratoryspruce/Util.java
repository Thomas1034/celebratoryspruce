package com.startraveler.celebratoryspruce;


import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Util {

    public static final @NotNull ImmutableList<@NotNull DyeColor> DYES = ImmutableList.<@NotNull DyeColor>builder().add(
            DyeColor.WHITE,
            DyeColor.LIGHT_GRAY,
            DyeColor.GRAY,
            DyeColor.BLACK,
            DyeColor.BROWN,
            DyeColor.RED,
            DyeColor.ORANGE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.GREEN,
            DyeColor.CYAN,
            DyeColor.LIGHT_BLUE,
            DyeColor.BLUE,
            DyeColor.PURPLE,
            DyeColor.MAGENTA,
            DyeColor.PINK
    ).build();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, @NotNull DyeColor> PRESENT_ACCENT_COLOR = ImmutableBiMap.<@NotNull DyeColor, @NotNull DyeColor>builder()
            .put(DyeColor.WHITE, DyeColor.GRAY)
            .put(DyeColor.ORANGE, DyeColor.BLUE)
            .put(DyeColor.MAGENTA, DyeColor.PINK)
            .put(DyeColor.LIGHT_BLUE, DyeColor.BROWN)
            .put(DyeColor.LIME, DyeColor.RED)
            .put(DyeColor.PINK, DyeColor.WHITE)
            .put(DyeColor.GRAY, DyeColor.ORANGE)
            .put(DyeColor.YELLOW, DyeColor.PURPLE)
            .put(DyeColor.LIGHT_GRAY, DyeColor.CYAN)
            .put(DyeColor.CYAN, DyeColor.GREEN)
            .put(DyeColor.PURPLE, DyeColor.LIGHT_GRAY)
            .put(DyeColor.BLUE, DyeColor.LIGHT_BLUE)
            .put(DyeColor.BROWN, DyeColor.BLACK)
            .put(DyeColor.GREEN, DyeColor.LIME)
            .put(DyeColor.RED, DyeColor.YELLOW)
            .put(DyeColor.BLACK, DyeColor.MAGENTA)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> CANDLES_BY_COLOR = ImmutableBiMap.<DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_CANDLE)
            .put(DyeColor.ORANGE, Blocks.ORANGE_CANDLE)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_CANDLE)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CANDLE)
            .put(DyeColor.LIME, Blocks.LIME_CANDLE)
            .put(DyeColor.PINK, Blocks.PINK_CANDLE)
            .put(DyeColor.GRAY, Blocks.GRAY_CANDLE)
            .put(DyeColor.YELLOW, Blocks.YELLOW_CANDLE)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CANDLE)
            .put(DyeColor.CYAN, Blocks.CYAN_CANDLE)
            .put(DyeColor.PURPLE, Blocks.PURPLE_CANDLE)
            .put(DyeColor.BLUE, Blocks.BLUE_CANDLE)
            .put(DyeColor.BROWN, Blocks.BROWN_CANDLE)
            .put(DyeColor.GREEN, Blocks.GREEN_CANDLE)
            .put(DyeColor.RED, Blocks.RED_CANDLE)
            .put(DyeColor.BLACK, Blocks.BLACK_CANDLE)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_CANDLE = CANDLES_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> CANDLE_CAKES_BY_COLOR = CANDLES_BY_COLOR.entrySet()
            .stream()
            .collect(ImmutableBiMap.toImmutableBiMap(
                    Map.Entry::getKey,
                    ((Function<? super Map.Entry<DyeColor, Block>, ? extends Block>) Map.Entry::getValue).andThen(Util.safeCast(
                            CandleBlock.class)).andThen(CandleCakeBlock::byCandle).andThen(BlockState::getBlock)
            ));
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_CANDLE_CAKE = CANDLE_CAKES_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> WOOL_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_WOOL)
            .put(DyeColor.ORANGE, Blocks.ORANGE_WOOL)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL)
            .put(DyeColor.LIME, Blocks.LIME_WOOL)
            .put(DyeColor.PINK, Blocks.PINK_WOOL)
            .put(DyeColor.GRAY, Blocks.GRAY_WOOL)
            .put(DyeColor.YELLOW, Blocks.YELLOW_WOOL)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL)
            .put(DyeColor.CYAN, Blocks.CYAN_WOOL)
            .put(DyeColor.PURPLE, Blocks.PURPLE_WOOL)
            .put(DyeColor.BLUE, Blocks.BLUE_WOOL)
            .put(DyeColor.BROWN, Blocks.BROWN_WOOL)
            .put(DyeColor.GREEN, Blocks.GREEN_WOOL)
            .put(DyeColor.RED, Blocks.RED_WOOL)
            .put(DyeColor.BLACK, Blocks.BLACK_WOOL)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_WOOL = WOOL_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> CARPETS_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_CARPET)
            .put(DyeColor.ORANGE, Blocks.ORANGE_CARPET)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_CARPET)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CARPET)
            .put(DyeColor.LIME, Blocks.LIME_CARPET)
            .put(DyeColor.PINK, Blocks.PINK_CARPET)
            .put(DyeColor.GRAY, Blocks.GRAY_CARPET)
            .put(DyeColor.YELLOW, Blocks.YELLOW_CARPET)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CARPET)
            .put(DyeColor.CYAN, Blocks.CYAN_CARPET)
            .put(DyeColor.PURPLE, Blocks.PURPLE_CARPET)
            .put(DyeColor.BLUE, Blocks.BLUE_CARPET)
            .put(DyeColor.BROWN, Blocks.BROWN_CARPET)
            .put(DyeColor.GREEN, Blocks.GREEN_CARPET)
            .put(DyeColor.RED, Blocks.RED_CARPET)
            .put(DyeColor.BLACK, Blocks.BLACK_CARPET)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_CARPET = CARPETS_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> BANNERS_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_BANNER)
            .put(DyeColor.ORANGE, Blocks.ORANGE_BANNER)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_BANNER)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_BANNER)
            .put(DyeColor.LIME, Blocks.LIME_BANNER)
            .put(DyeColor.PINK, Blocks.PINK_BANNER)
            .put(DyeColor.GRAY, Blocks.GRAY_BANNER)
            .put(DyeColor.YELLOW, Blocks.YELLOW_BANNER)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_BANNER)
            .put(DyeColor.CYAN, Blocks.CYAN_BANNER)
            .put(DyeColor.PURPLE, Blocks.PURPLE_BANNER)
            .put(DyeColor.BLUE, Blocks.BLUE_BANNER)
            .put(DyeColor.BROWN, Blocks.BROWN_BANNER)
            .put(DyeColor.GREEN, Blocks.GREEN_BANNER)
            .put(DyeColor.RED, Blocks.RED_BANNER)
            .put(DyeColor.BLACK, Blocks.BLACK_BANNER)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_BANNER = BANNERS_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> WALL_BANNERS_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_WALL_BANNER)
            .put(DyeColor.ORANGE, Blocks.ORANGE_WALL_BANNER)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_WALL_BANNER)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WALL_BANNER)
            .put(DyeColor.LIME, Blocks.LIME_WALL_BANNER)
            .put(DyeColor.PINK, Blocks.PINK_WALL_BANNER)
            .put(DyeColor.GRAY, Blocks.GRAY_WALL_BANNER)
            .put(DyeColor.YELLOW, Blocks.YELLOW_WALL_BANNER)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WALL_BANNER)
            .put(DyeColor.CYAN, Blocks.CYAN_BANNER)
            .put(DyeColor.PURPLE, Blocks.PURPLE_WALL_BANNER)
            .put(DyeColor.BLUE, Blocks.BLUE_WALL_BANNER)
            .put(DyeColor.BROWN, Blocks.BROWN_WALL_BANNER)
            .put(DyeColor.GREEN, Blocks.GREEN_WALL_BANNER)
            .put(DyeColor.RED, Blocks.RED_WALL_BANNER)
            .put(DyeColor.BLACK, Blocks.BLACK_WALL_BANNER)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_WALL_BANNER = WALL_BANNERS_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> BEDS_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_BED)
            .put(DyeColor.ORANGE, Blocks.ORANGE_BED)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_BED)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_BED)
            .put(DyeColor.LIME, Blocks.LIME_BED)
            .put(DyeColor.PINK, Blocks.PINK_BED)
            .put(DyeColor.GRAY, Blocks.GRAY_BED)
            .put(DyeColor.YELLOW, Blocks.YELLOW_BED)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_BED)
            .put(DyeColor.CYAN, Blocks.CYAN_BED)
            .put(DyeColor.PURPLE, Blocks.PURPLE_BED)
            .put(DyeColor.BLUE, Blocks.BLUE_BED)
            .put(DyeColor.BROWN, Blocks.BROWN_BED)
            .put(DyeColor.GREEN, Blocks.GREEN_BED)
            .put(DyeColor.RED, Blocks.RED_BED)
            .put(DyeColor.BLACK, Blocks.BLACK_BED)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_BED = BEDS_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> STAINED_GLASS_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_STAINED_GLASS)
            .put(DyeColor.ORANGE, Blocks.ORANGE_STAINED_GLASS)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_STAINED_GLASS)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_STAINED_GLASS)
            .put(DyeColor.LIME, Blocks.LIME_STAINED_GLASS)
            .put(DyeColor.PINK, Blocks.PINK_STAINED_GLASS)
            .put(DyeColor.GRAY, Blocks.GRAY_STAINED_GLASS)
            .put(DyeColor.YELLOW, Blocks.YELLOW_STAINED_GLASS)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_STAINED_GLASS)
            .put(DyeColor.CYAN, Blocks.CYAN_STAINED_GLASS)
            .put(DyeColor.PURPLE, Blocks.PURPLE_STAINED_GLASS)
            .put(DyeColor.BLUE, Blocks.BLUE_STAINED_GLASS)
            .put(DyeColor.BROWN, Blocks.BROWN_STAINED_GLASS)
            .put(DyeColor.GREEN, Blocks.GREEN_STAINED_GLASS)
            .put(DyeColor.RED, Blocks.RED_STAINED_GLASS)
            .put(DyeColor.BLACK, Blocks.BLACK_STAINED_GLASS)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_STAINED_GLASS = STAINED_GLASS_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> STAINED_GLASS_PANES_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_STAINED_GLASS_PANE)
            .put(DyeColor.ORANGE, Blocks.ORANGE_STAINED_GLASS_PANE)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_STAINED_GLASS_PANE)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE)
            .put(DyeColor.LIME, Blocks.LIME_STAINED_GLASS_PANE)
            .put(DyeColor.PINK, Blocks.PINK_STAINED_GLASS_PANE)
            .put(DyeColor.GRAY, Blocks.GRAY_STAINED_GLASS_PANE)
            .put(DyeColor.YELLOW, Blocks.YELLOW_STAINED_GLASS_PANE)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE)
            .put(DyeColor.CYAN, Blocks.CYAN_STAINED_GLASS_PANE)
            .put(DyeColor.PURPLE, Blocks.PURPLE_STAINED_GLASS_PANE)
            .put(DyeColor.BLUE, Blocks.BLUE_STAINED_GLASS_PANE)
            .put(DyeColor.BROWN, Blocks.BROWN_STAINED_GLASS_PANE)
            .put(DyeColor.GREEN, Blocks.GREEN_STAINED_GLASS_PANE)
            .put(DyeColor.RED, Blocks.RED_STAINED_GLASS_PANE)
            .put(DyeColor.BLACK, Blocks.BLACK_STAINED_GLASS_PANE)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_STAINED_GLASS_PANE = STAINED_GLASS_PANES_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> CONCRETE_POWDERS_BY_COLOR = ImmutableBiMap.<DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_CONCRETE_POWDER)
            .put(DyeColor.ORANGE, Blocks.ORANGE_CONCRETE_POWDER)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_CONCRETE_POWDER)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CONCRETE_POWDER)
            .put(DyeColor.LIME, Blocks.LIME_CONCRETE_POWDER)
            .put(DyeColor.PINK, Blocks.PINK_CONCRETE_POWDER)
            .put(DyeColor.GRAY, Blocks.GRAY_CONCRETE_POWDER)
            .put(DyeColor.YELLOW, Blocks.YELLOW_CONCRETE_POWDER)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CONCRETE_POWDER)
            .put(DyeColor.CYAN, Blocks.CYAN_CONCRETE_POWDER)
            .put(DyeColor.PURPLE, Blocks.PURPLE_CONCRETE_POWDER)
            .put(DyeColor.BLUE, Blocks.BLUE_CONCRETE_POWDER)
            .put(DyeColor.BROWN, Blocks.BROWN_CONCRETE_POWDER)
            .put(DyeColor.GREEN, Blocks.GREEN_CONCRETE_POWDER)
            .put(DyeColor.RED, Blocks.RED_CONCRETE_POWDER)
            .put(DyeColor.BLACK, Blocks.BLACK_CONCRETE_POWDER)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_CONCRETE_POWDER = CONCRETE_POWDERS_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> CONCRETE_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_CONCRETE)
            .put(DyeColor.ORANGE, Blocks.ORANGE_CONCRETE)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_CONCRETE)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CONCRETE)
            .put(DyeColor.LIME, Blocks.LIME_CONCRETE)
            .put(DyeColor.PINK, Blocks.PINK_CONCRETE)
            .put(DyeColor.GRAY, Blocks.GRAY_CONCRETE)
            .put(DyeColor.YELLOW, Blocks.YELLOW_CONCRETE)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CONCRETE)
            .put(DyeColor.CYAN, Blocks.CYAN_CONCRETE)
            .put(DyeColor.PURPLE, Blocks.PURPLE_CONCRETE)
            .put(DyeColor.BLUE, Blocks.BLUE_CONCRETE)
            .put(DyeColor.BROWN, Blocks.BROWN_CONCRETE)
            .put(DyeColor.GREEN, Blocks.GREEN_CONCRETE)
            .put(DyeColor.RED, Blocks.RED_CONCRETE)
            .put(DyeColor.BLACK, Blocks.BLACK_CONCRETE)
            .build();
    public static final ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_CONCRETE = CONCRETE_BY_COLOR.inverse();
    public static final ImmutableBiMap<@NotNull DyeColor, Block> TERRACOTTA_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_TERRACOTTA)
            .put(DyeColor.ORANGE, Blocks.ORANGE_TERRACOTTA)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_TERRACOTTA)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_TERRACOTTA)
            .put(DyeColor.LIME, Blocks.LIME_TERRACOTTA)
            .put(DyeColor.PINK, Blocks.PINK_TERRACOTTA)
            .put(DyeColor.GRAY, Blocks.GRAY_TERRACOTTA)
            .put(DyeColor.YELLOW, Blocks.YELLOW_TERRACOTTA)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_TERRACOTTA)
            .put(DyeColor.CYAN, Blocks.CYAN_TERRACOTTA)
            .put(DyeColor.PURPLE, Blocks.PURPLE_TERRACOTTA)
            .put(DyeColor.BLUE, Blocks.BLUE_TERRACOTTA)
            .put(DyeColor.BROWN, Blocks.BROWN_TERRACOTTA)
            .put(DyeColor.GREEN, Blocks.GREEN_TERRACOTTA)
            .put(DyeColor.RED, Blocks.RED_TERRACOTTA)
            .put(DyeColor.BLACK, Blocks.BLACK_TERRACOTTA)
            .build();
    public static final ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_TERRACOTTA = TERRACOTTA_BY_COLOR.inverse();
    public static final ImmutableBiMap<@NotNull DyeColor, Block> GLAZED_TERRACOTTA_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_GLAZED_TERRACOTTA)
            .put(DyeColor.ORANGE, Blocks.ORANGE_GLAZED_TERRACOTTA)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_GLAZED_TERRACOTTA)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA)
            .put(DyeColor.LIME, Blocks.LIME_GLAZED_TERRACOTTA)
            .put(DyeColor.PINK, Blocks.PINK_GLAZED_TERRACOTTA)
            .put(DyeColor.GRAY, Blocks.GRAY_GLAZED_TERRACOTTA)
            .put(DyeColor.YELLOW, Blocks.YELLOW_GLAZED_TERRACOTTA)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA)
            .put(DyeColor.CYAN, Blocks.CYAN_GLAZED_TERRACOTTA)
            .put(DyeColor.PURPLE, Blocks.PURPLE_GLAZED_TERRACOTTA)
            .put(DyeColor.BLUE, Blocks.BLUE_GLAZED_TERRACOTTA)
            .put(DyeColor.BROWN, Blocks.BROWN_GLAZED_TERRACOTTA)
            .put(DyeColor.GREEN, Blocks.GREEN_GLAZED_TERRACOTTA)
            .put(DyeColor.RED, Blocks.RED_GLAZED_TERRACOTTA)
            .put(DyeColor.BLACK, Blocks.BLACK_GLAZED_TERRACOTTA)
            .build();
    public static final ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_GLAZED_TERRACOTTA = GLAZED_TERRACOTTA_BY_COLOR.inverse();
    public static final @NotNull ImmutableBiMap<@NotNull DyeColor, Block> SHULKER_BOX_BY_COLOR = ImmutableBiMap.<@NotNull DyeColor, Block>builder()
            .put(DyeColor.WHITE, Blocks.WHITE_SHULKER_BOX)
            .put(DyeColor.ORANGE, Blocks.ORANGE_SHULKER_BOX)
            .put(DyeColor.MAGENTA, Blocks.MAGENTA_SHULKER_BOX)
            .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_SHULKER_BOX)
            .put(DyeColor.LIME, Blocks.LIME_SHULKER_BOX)
            .put(DyeColor.PINK, Blocks.PINK_SHULKER_BOX)
            .put(DyeColor.GRAY, Blocks.GRAY_SHULKER_BOX)
            .put(DyeColor.YELLOW, Blocks.YELLOW_SHULKER_BOX)
            .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_SHULKER_BOX)
            .put(DyeColor.CYAN, Blocks.CYAN_SHULKER_BOX)
            .put(DyeColor.PURPLE, Blocks.PURPLE_SHULKER_BOX)
            .put(DyeColor.BLUE, Blocks.BLUE_SHULKER_BOX)
            .put(DyeColor.BROWN, Blocks.BROWN_SHULKER_BOX)
            .put(DyeColor.GREEN, Blocks.GREEN_SHULKER_BOX)
            .put(DyeColor.RED, Blocks.RED_SHULKER_BOX)
            .put(DyeColor.BLACK, Blocks.BLACK_SHULKER_BOX)
            .build();
    public static final @NotNull ImmutableBiMap<@NotNull Block, DyeColor> COLOR_BY_SHULKER_BOX = SHULKER_BOX_BY_COLOR.inverse();

    static {
        DyeColor[] dyes = DyeColor.values();
        if (DYES.size() != dyes.length) {
            CelebratorySpruce.LOGGER.warn(
                    "Strange things did happen here...\nEncountered unexpected dye colors: {}\nThis may cause problems.",
                    Arrays.stream(dyes).filter(DYES::contains).map(DyeColor::getName).collect(Collectors.joining(", "))
            );
        }
    }

    @SuppressWarnings("unused")
    Map<@NotNull Block, DyeColor> COLOR_BY_BLOCK = ImmutableMap.<@NotNull Block, DyeColor>builder()
            .putAll(COLOR_BY_CANDLE)
            .putAll(COLOR_BY_CANDLE_CAKE)
            .putAll(COLOR_BY_CONCRETE)
            .putAll(COLOR_BY_CONCRETE_POWDER)
            .putAll(COLOR_BY_STAINED_GLASS)
            .putAll(COLOR_BY_STAINED_GLASS_PANE)
            .putAll(COLOR_BY_WOOL)
            .putAll(COLOR_BY_CARPET)
            .putAll(COLOR_BY_BED)
            .putAll(COLOR_BY_BANNER)
            .putAll(COLOR_BY_WALL_BANNER)
            .putAll(COLOR_BY_TERRACOTTA)
            .putAll(COLOR_BY_GLAZED_TERRACOTTA)
            .putAll(COLOR_BY_SHULKER_BOX)
            .build();
    @SuppressWarnings("unused")
    Map<@NotNull DyeColor, @NotNull DyeItem> DYE_ITEM_BY_COLOR = Arrays.stream(DyeColor.values())
            .collect(ImmutableBiMap.toImmutableBiMap(Function.identity(), DyeItem::byColor));

    public static <S, T extends S> Function<@Nullable S, @Nullable T> safeCast(@NotNull Class<T> cls) {
        return (S s) -> cls.isInstance(s) ? cls.cast(s) : null;
    }

    @SuppressWarnings("unused")
    public static <S, T extends S> @Nullable T safeCast(@NotNull Class<T> cls, @Nullable S s) {
        return Util.<S, T>safeCast(cls).apply(s);
    }

    public static <S, T extends S> Function<@Nullable S, @NotNull T> castOrThrow(@NotNull Class<T> cls) {
        return Util.<S, T>safeCast(cls).andThen(Objects::requireNonNull);
    }

    @SuppressWarnings("unused")
    public static <S, T extends S> @NotNull T castOrThrow(@NotNull Class<T> cls, @Nullable S s) {
        return Util.<S, T>castOrThrow(cls).apply(s);
    }

    public static <A, B, R> Stream<R> zip(Stream<A> stream1, Stream<B> stream2, BiFunction<? super A, ? super B, ? extends R> zipper) {
        Iterator<A> iterator1 = stream1.iterator();
        Iterator<B> iterator2 = stream2.iterator();
        Iterable<R> iterable = () -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator1.hasNext() && iterator2.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(iterator1.next(), iterator2.next());
            }
        };
        return StreamSupport.stream(iterable.spliterator(), false).onClose(stream1::close).onClose(stream2::close);
    }


}
