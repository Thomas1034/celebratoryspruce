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

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ExtensibleCandleCakeBlock extends AbstractCandleBlock {
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
    protected static final VoxelShape CAKE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
    protected static final VoxelShape CANDLE_SHAPE = Block.box(7.0D, 8.0D, 7.0D, 9.0D, 14.0D, 9.0D);
    protected static final VoxelShape SHAPE = Shapes.or(CAKE_SHAPE, CANDLE_SHAPE);
    private static final Iterable<Vec3> PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5D, 1.0D, 0.5D));

    protected final Supplier<@NotNull ExtensibleCakeBlock> baseCake;
    protected final Supplier<@NotNull Block> candle;

    public ExtensibleCandleCakeBlock(Supplier<@NotNull Block> candle, Supplier<@NotNull ExtensibleCakeBlock> baseCake, Properties properties) {
        super(properties);
        this.candle = candle;
        this.baseCake = baseCake;
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
    }

    private static boolean candleHit(BlockHitResult hit) {
        return (hit.getLocation().y - hit.getBlockPos().getY()) > 0.5;
    }

    public Supplier<ExtensibleCakeBlock> getBaseCake() {
        return this.baseCake;
    }

    public Supplier<Block> getCandle() {
        return this.candle;
    }

    @Override
    protected @NotNull MapCodec<? extends AbstractCandleBlock> codec() {
        throw new IllegalStateException("Codecs aren't implementing yet.");
    }

    @Override
    protected @NotNull Iterable<Vec3> getParticleOffsets(@NotNull BlockState state) {
        return PARTICLE_OFFSETS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
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
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        InteractionResult interactionresult = (this.baseCake.get()).eatCustom(
                level,
                pos,
                this.baseCake.get().defaultBlockState(),
                player
        );
        if (interactionresult.consumesAction()) {
            dropResources(state, level, pos);
        }

        return interactionresult;
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
            if (candleHit(hitResult) && stack.isEmpty() && state.getValue(LIT)) {
                extinguish(player, state, level, pos);
                return InteractionResult.SUCCESS;
            } else {
                return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(@NotNull BlockState p_152909_) {
        return true;
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        return level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    protected int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return CakeBlock.FULL_CAKE_SIGNAL;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean includeData, @NotNull Player player) {
        return new ItemStack(this.baseCake.get());
    }
}

