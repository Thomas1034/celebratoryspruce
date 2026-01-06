/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * If you modify this file, please include a notice stating the changes:
 * Example: "Modified by [Your Name] on [Date] - [Short Description of Changes]"
 */
package com.startraveler.celebratoryspruce.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ExtensibleCakeBlock extends Block {

    public static final Codec<@NotNull ExtensibleCakeBlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            propertiesCodec(),
            FoodProperties.DIRECT_CODEC.fieldOf("food_properties")
                    .forGetter((ExtensibleCakeBlock cake) -> cake.foodProperties),
            Consumable.CODEC.fieldOf("consumable")
                    .forGetter((ExtensibleCakeBlock cake) -> cake.consumable)
    ).apply(instance, ExtensibleCakeBlock::new));
    public static final MapCodec<@NotNull ExtensibleCakeBlock> MAP_CODEC = CODEC.fieldOf("extensible_cake");
    public static final int MAX_BITES = 6;
    public static final IntegerProperty BITES;
    public static final int FULL_CAKE_SIGNAL;
    protected static final VoxelShape[] SHAPE_BY_BITE;

    static {
        BITES = BlockStateProperties.BITES;
        FULL_CAKE_SIGNAL = getOutputSignal(0);
        SHAPE_BY_BITE = new VoxelShape[]{Block.box(1.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(3.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(5.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(7.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(9.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(11.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
                Block.box(13.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F)};
    }

    protected final FoodProperties foodProperties;
    protected final Map<Block, Block> byCandle;
    protected final Consumable consumable;

    public ExtensibleCakeBlock(Properties properties, FoodProperties foodProperties, Consumable consumable) {
        super(properties);
        this.foodProperties = foodProperties;
        this.consumable = consumable;
        this.byCandle = new HashMap<>();
    }

    public static int getOutputSignal(int eaten) {
        return (1 + MAX_BITES - eaten) * 2;
    }

    @Override
    public @NotNull MapCodec<@NotNull ExtensibleCakeBlock> codec() {
        return MAP_CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, @NotNull BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess tickAccess, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
        return direction == Direction.DOWN && !state.canSurvive(
                level,
                pos
        ) ? Blocks.AIR.defaultBlockState() : super.updateShape(
                state,
                level,
                tickAccess,
                pos,
                direction,
                neighborPos,
                neighborState,
                random
        );
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide()) {
            if (this.eatCustom(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return eatCustom(level, pos, state, player);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        Item item = stack.getItem();
        if (stack.is(ItemTags.CANDLES) && state.getValue(BITES) == 0) {
            Block var10 = Block.byItem(item);
            if (var10 instanceof CandleBlock candleblock) {
                stack.consume(1, player);
                level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlockAndUpdate(pos, this.byCandle.get(candleblock).defaultBlockState());
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                player.awardStat(Stats.ITEM_USED.get(item));
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        return level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {

        return ExtensibleCakeBlock.getOutputSignal(blockState.getValue(BITES));
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_BITE[state.getValue(BITES)];
    }

    protected InteractionResult eatCustom(LevelAccessor level, BlockPos pos, BlockState state, Player player) {

        if (!player.canEat(this.foodProperties.canAlwaysEat())) {
            return InteractionResult.PASS;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            Level playerLevel = player.level();
            ItemStack defaultStack = this.asItem().getDefaultInstance();
            this.foodProperties.onConsume(playerLevel, player, defaultStack, this.consumable);
            this.consumable.onConsume(playerLevel, player, defaultStack);
            int i = state.getValue(BITES);

            level.gameEvent(player, GameEvent.EAT, pos);
            if (i < MAX_BITES) {
                level.setBlock(pos, state.setValue(BITES, i + 1), 3);
            } else {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    public void addCandleCake(Block candle, Block cake) {
        this.byCandle.put(candle, cake);
    }

}

