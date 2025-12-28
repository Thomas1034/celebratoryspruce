package com.startraveler.celebratoryspruce.block;

import com.mojang.math.Axis;
import com.startraveler.celebratoryspruce.block.entity.ItemRenderingBlockEntity;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class StandingItemDisplayingBlock extends ItemDisplayingBlock {
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public StandingItemDisplayingBlock(Optional<SimpleParticleType> gleamParticle, Properties properties) {
        super(gleamParticle, properties);
    }


    @Override
    protected @NotNull List<ItemRenderingBlockEntity.@NotNull Transform<?>> setupDisplayTransforms(BlockState state) {
        @NotNull List<ItemRenderingBlockEntity.@NotNull Transform<?>> transforms = super.setupDisplayTransforms(state);
        float angle = Mth.DEG_TO_RAD * getYRotationDegrees(state);
        transforms.addLast(new ItemRenderingBlockEntity.Rotation(Axis.YN.rotation(angle)));
        transforms.addLast(new ItemRenderingBlockEntity.Rotation(Axis.YN.rotation(Mth.PI)));

        return transforms;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ROTATION);
    }

    public float getYRotationDegrees(BlockState state) {
        return RotationSegment.convertToDegrees(state.getValue(ROTATION));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return (this.defaultBlockState()
                .setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation() + 180.0F))).setValue(
                WATERLOGGED,
                fluidstate.getType() == Fluids.WATER
        );
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(ROTATION, rot.rotate(state.getValue(ROTATION), 16));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), 16));
    }

}
