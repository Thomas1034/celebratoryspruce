package com.startraveler.celebratoryspruce.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class WallWreathBlock extends WreathBlock {
    public static final EnumProperty<@NotNull Direction> FACING = HorizontalDirectionalBlock.FACING;
    protected static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(
            16.0F,
            0.0F,
            16.0F,
            12.0F,
            16.0F
    ));

    public WallWreathBlock(Properties properties) {
        super(properties);
    }

    public static VoxelShape getShape(BlockState state) {
        return SHAPES.get(state.getValue(FACING));
    }

    public static boolean canSurvive(LevelReader level, BlockPos pos, Direction facing) {
        BlockPos oppositePos = pos.relative(facing.getOpposite());
        BlockState oppositeState = level.getBlockState(oppositePos);
        return oppositeState.isFaceSturdy(level, oppositePos, facing) || oppositeState.is(BlockTags.LEAVES);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction[] lookingDirections = context.getNearestLookingDirections();
        FluidState fluidState = level.getFluidState(pos);

        for (Direction direction : lookingDirections) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                state = state.setValue(FACING, opposite);
                if (state.canSurvive(level, pos)) {
                    return state.setValue(
                            WATERLOGGED,
                            fluidState.getType() == Fluids.WATER
                    );
                }
            }
        }

        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess tickAccess, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {

        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(
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
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return canSurvive(level, pos, state.getValue(FACING));
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getShape(state);
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
