package com.startraveler.celebratoryspruce.block;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.startraveler.celebratoryspruce.block.entity.ItemRenderingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class StarBlock extends BaseStarBlock implements SimpleWaterloggedBlock {
    public static final float GLEAM_CHANCE = 0.2f;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD = BuiltInRegistries.PARTICLE_TYPE.byNameCodec()
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
            .fieldOf("particle_options");

    public static final MapCodec<StarBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PARTICLE_OPTIONS_FIELD.forGetter((starBlock) -> starBlock.gleamParticle),
                    propertiesCodec()
            )
            .apply(instance, StarBlock::new));
    protected final SimpleParticleType gleamParticle;

    public StarBlock(SimpleParticleType gleamParticle, Properties properties) {
        super(properties);
        this.gleamParticle = gleamParticle;
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
    }

    @Override
    protected @NotNull List<ItemRenderingBlockEntity.@NotNull Transform<?>> setupDisplayTransforms(BlockState state) {
        @NotNull List<ItemRenderingBlockEntity.@NotNull Transform<?>> transforms = super.setupDisplayTransforms(state);
        transforms.addLast(new ItemRenderingBlockEntity.Translation(0.5f, 0.5f, 0.5f));
        return transforms;
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess tickAccess, @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    public @NotNull MapCodec<? extends StarBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (random.nextFloat() < GLEAM_CHANCE) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(this.gleamParticle, x, y, z, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        ItemRenderingBlockEntity itemRenderingBlockEntity = new ItemRenderingBlockEntity(blockPos, blockState);
        itemRenderingBlockEntity.setDisplayItem(this.asItem());
        itemRenderingBlockEntity.addTransforms(this.setupDisplayTransforms(blockState));
        return itemRenderingBlockEntity;
    }
}
