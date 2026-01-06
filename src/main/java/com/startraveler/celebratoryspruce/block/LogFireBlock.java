package com.startraveler.celebratoryspruce.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class LogFireBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<@NotNull Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.column(16.0F, 0.0F, 7.0F);
    private final boolean spawnParticles;
    private final int fireDamage;

    public LogFireBlock(boolean spawnParticles, int fireDamage, Properties properties) {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.fireDamage = fireDamage;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, true)
                .setValue(SIGNAL_FIRE, false)
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH));
    }

    private boolean isSmokeSource(BlockState state) {
        return state.is(Blocks.HAY_BLOCK);
    }

    @Override
    public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(LIT)) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound(
                        (double) pos.getX() + (double) 0.5F,
                        (double) pos.getY() + (double) 0.5F,
                        (double) pos.getZ() + (double) 0.5F,
                        SoundEvents.CAMPFIRE_CRACKLE,
                        SoundSource.BLOCKS,
                        0.5F + random.nextFloat(),
                        random.nextFloat() * 0.7F + 0.6F,
                        false
                );
            }

            if (this.spawnParticles && random.nextInt(5) == 0) {
                for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                    level.addParticle(
                            ParticleTypes.LAVA,
                            pos.getX() + 0.5F,
                            pos.getY() + 0.5F,
                            pos.getZ() + 0.5F,
                            random.nextFloat() / 2.0F,
                            5.0E-5,
                            random.nextFloat() / 2.0F
                    );
                }
            }
        }

    }

    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        boolean isInWater = levelaccessor.getFluidState(clickedPos).getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(WATERLOGGED, isInWater)
                .setValue(SIGNAL_FIRE, this.isSmokeSource(levelaccessor.getBlockState(clickedPos.below())))
                .setValue(LIT, !isInWater)
                .setValue(FACING, context.getHorizontalDirection());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, @NotNull BlockState> builder) {
        builder.add(LIT, SIGNAL_FIRE, WATERLOGGED, FACING);
    }

    public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, BlockState state, @NotNull FluidState fluidState) {
        if (!(Boolean) state.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            boolean isLit = state.getValue(LIT);
            if (isLit) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                CampfireBlock.dowse(null, level, pos, state);
            }

            level.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(LIT, false), Block.UPDATE_ALL);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess tickAccess, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return direction == Direction.DOWN ? state.setValue(
                SIGNAL_FIRE,
                this.isSmokeSource(neighborState)
        ) : super.updateShape(
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
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity, @NotNull InsideBlockEffectApplier effectApplier, boolean intersects) {
        if (state.getValue(LIT) && entity instanceof LivingEntity && level instanceof ServerLevel serverLevel) {
            entity.hurtServer(serverLevel, level.damageSources().campfire(), (float) this.fireDamage);
        }
        super.entityInside(state, level, pos, entity, effectApplier, intersects);
    }

    @Override
    protected void onProjectileHit(@NotNull Level level, @NotNull BlockState state, BlockHitResult hit, @NotNull Projectile projectile) {
        BlockPos pos = hit.getBlockPos();
        if (level instanceof ServerLevel serverlevel) {
            if (projectile.isOnFire() && projectile.mayInteract(
                    serverlevel,
                    pos
            ) && !(Boolean) state.getValue(LIT) && !(Boolean) state.getValue(WATERLOGGED)) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), Block.UPDATE_ALL_IMMEDIATE);
            }
        }

    }

}
