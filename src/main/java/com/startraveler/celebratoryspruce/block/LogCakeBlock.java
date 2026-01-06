package com.startraveler.celebratoryspruce.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class LogCakeBlock extends ExtensibleCakeBlock {

    protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[]{Block.boxZ(10.0F, 0.0F, 10.0F, 1.0F, 15.0F),
            Block.boxZ(10.0F, 0.0F, 10.0F, 3.0F, 15.0F),
            Block.boxZ(10.0F, 0.0F, 10.0F, 5.0F, 15.0F),
            Block.boxZ(10.0F, 0.0F, 10.0F, 7.0F, 15.0F),
            Block.boxZ(10.0F, 0.0F, 10.0F, 9.0F, 15.0F),
            Block.boxZ(10.0F, 0.0F, 10.0F, 11.0F, 15.0F),
            Block.boxZ(10.0F, 0.0F, 10.0F, 13.0F, 15.0F)};

    public LogCakeBlock(Properties properties, FoodProperties foodProperties, Consumable consumable) {
        super(properties, foodProperties, consumable);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_BITE[state.getValue(BITES)];
    }
}
