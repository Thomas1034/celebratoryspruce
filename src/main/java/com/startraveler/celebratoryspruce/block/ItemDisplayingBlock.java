package com.startraveler.celebratoryspruce.block;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.startraveler.celebratoryspruce.block.entity.ItemHoldingBlockEntity;
import com.startraveler.celebratoryspruce.block.entity.ItemRenderingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDisplayingBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final float GLEAM_CHANCE = 0.2f;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final MapCodec<Optional<SimpleParticleType>> PARTICLE_OPTIONS_FIELD = BuiltInRegistries.PARTICLE_TYPE.byNameCodec()
            .comapFlatMap(
                    (type) -> {
                        DataResult<SimpleParticleType> dataResult;
                        if (type instanceof SimpleParticleType simpleParticleType) {
                            dataResult = DataResult.success(simpleParticleType);
                        } else {
                            dataResult = DataResult.error(() -> "Not a SimpleParticleType: " + type);
                        }
                        return dataResult;
                    }, (particleType) -> particleType
            )
            .optionalFieldOf("particle_options");
    public static final MapCodec<ItemDisplayingBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                    PARTICLE_OPTIONS_FIELD.forGetter((starBlock) -> starBlock.gleamParticle),
                    propertiesCodec()
            )
            .apply(instance, ItemDisplayingBlock::new));
    private static final VoxelShape SHAPE = Block.column(12.0F, 0.0F, 16.0F);
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected final Optional<SimpleParticleType> gleamParticle;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public ItemDisplayingBlock(Optional<SimpleParticleType> gleamParticle, Properties properties) {
        super(properties);
        this.gleamParticle = gleamParticle;
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
    }

    protected @NotNull List<ItemRenderingBlockEntity.@NotNull Transform<?>> setupDisplayTransforms(BlockState state) {
        @NotNull List<ItemRenderingBlockEntity.@NotNull Transform<?>> transforms = new ArrayList<>();
        transforms.addLast(new ItemRenderingBlockEntity.Translation(0.5f, 0.5f, 0.5f));
        return transforms;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean includeData, @NotNull Player player) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ItemHoldingBlockEntity itemHoldingBlockEntity) {
                ItemStack cloneItemStack = itemHoldingBlockEntity.getStoredItemStack();
                if (!cloneItemStack.isEmpty()) {
                    return cloneItemStack;
                }
            }
        }
        return state.getCloneItemStack(level, pos, includeData);
    }


    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess tickAccess, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return direction == Direction.DOWN && !this.canSurvive(
                state,
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
        if (this.canChangeItem(state, level, pos)) {
            if (!level.isClientSide()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof ItemRenderingBlockEntity itemRenderingBlockEntity) {
                    ItemStack stack = itemRenderingBlockEntity.getStoredItemStack();
                    if (!stack.isEmpty() && player.addItem(stack)) {
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
                        itemRenderingBlockEntity.setStoredItemStack(ItemStack.EMPTY);
                    }
                }
            }
            return InteractionResult.CONSUME;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (this.canChangeItem(state, level, pos)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ItemRenderingBlockEntity itemRenderingBlockEntity && !level.isClientSide()) {
                ItemStack oldStack = itemRenderingBlockEntity.getStoredItemStack();
                if (oldStack.isEmpty()) {
                    ItemStack newStack = stack.copy();
                    if (!player.getAbilities().instabuild) {
                        stack.setCount(0);
                    }
                    itemRenderingBlockEntity.setStoredItemStack(newStack);
                    if (!newStack.isEmpty()) {
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
                        return InteractionResult.SUCCESS_SERVER;
                    }
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP) || this.alsoSurvivesWhen(state, level, pos);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    public boolean canChangeItem(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return false;
    }

    public @NotNull MapCodec<? extends ItemDisplayingBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {

        if (this.gleamParticle.isPresent()) {
            if (random.nextFloat() < GLEAM_CHANCE) {
                AABB shape = this.getShape(state, level, pos, CollisionContext.empty()).bounds();

                double x = pos.getX() + shape.minX + random.nextDouble() * shape.getXsize();
                double y = pos.getY() + shape.minY + random.nextDouble() * shape.getYsize();
                double z = pos.getZ() + shape.minZ + random.nextDouble() * shape.getZsize();
                level.addParticle(this.gleamParticle.get(), x, y, z, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        ItemRenderingBlockEntity itemRenderingBlockEntity = new ItemRenderingBlockEntity(blockPos, blockState);
        itemRenderingBlockEntity.setDefaultDisplayStack(this.asItem().getDefaultInstance());
        itemRenderingBlockEntity.addTransforms(this.setupDisplayTransforms(blockState));
        return itemRenderingBlockEntity;
    }

    @SuppressWarnings("unused")
    protected boolean alsoSurvivesWhen(@NotNull BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(BlockTags.LEAVES);
    }

}
