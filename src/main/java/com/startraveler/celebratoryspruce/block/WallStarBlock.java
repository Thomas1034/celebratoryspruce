package com.startraveler.celebratoryspruce.block;

import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.startraveler.celebratoryspruce.block.entity.ItemRenderingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class WallStarBlock extends StarBlock {
    public static final EnumProperty<@NotNull Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final MapCodec<WallStarBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                    PARTICLE_OPTIONS_FIELD.forGetter((starBlock) -> starBlock.gleamParticle),
                    propertiesCodec()
            )
            .apply(instance, WallStarBlock::new));
    protected static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(
            12.0F,
            2.0F,
            14.0F,
            14.0F,
            16.0F
    ));

    public WallStarBlock(SimpleParticleType gleamParticle, BlockBehaviour.Properties properties) {
        super(gleamParticle, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
    protected @NotNull List<ItemRenderingBlockEntity.@NotNull Transform<?>> setupDisplayTransforms(BlockState state) {
        @NotNull List<ItemRenderingBlockEntity.@NotNull Transform<?>> transforms = super.setupDisplayTransforms(state);
        Direction facing = state.getValue(FACING);
        transforms.addLast(new ItemRenderingBlockEntity.Rotation(facing.getRotation()));
        transforms.addLast(new ItemRenderingBlockEntity.Rotation(Axis.XN.rotation((float) Math.PI / 2)));
        transforms.addLast(new ItemRenderingBlockEntity.Translation(0, 0, -15f / 32f));
        return transforms;
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
    public @NotNull MapCodec<WallStarBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {

        if (random.nextFloat() < GLEAM_CHANCE) {

            VoxelShape shape = SHAPES.get(state.getValue(FACING));

            AABB box = shape.bounds();

            double x = box.minX + box.getXsize() * random.nextDouble();
            double y = box.minY + box.getYsize() * random.nextDouble();
            double z = box.minZ + box.getZsize() * random.nextDouble();

            level.addParticle(this.gleamParticle, x, y, z, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
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
