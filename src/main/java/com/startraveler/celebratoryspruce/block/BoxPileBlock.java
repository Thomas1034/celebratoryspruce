package com.startraveler.celebratoryspruce.block;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import com.startraveler.celebratoryspruce.XFactHDShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BoxPileBlock extends Block {

    public static final int MIN_BOXES = 1;
    public static final int MAX_BOXES = 8;
    public static final IntegerProperty BOXES = IntegerProperty.create("boxes", MIN_BOXES, MAX_BOXES);
    public static final EnumProperty<@NotNull Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final Table<Integer, Direction, VoxelShape> SHAPES = ArrayTable.create(
            BOXES.getPossibleValues(),
            FACING.getPossibleValues()
    );
    protected final Supplier<Ingredient> canIncreaseSize;

    public BoxPileBlock(Properties properties, Supplier<Ingredient> canIncreaseSize) {
        super(properties);
        this.canIncreaseSize = canIncreaseSize;
        this.registerDefaultState(this.getStateDefinition().any().setValue(BOXES, MIN_BOXES));
    }

    @Override
    protected @NotNull InteractionResult useItemOn(ItemStack stack, BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        int numBombs = state.getValue(BOXES);
        Item item = stack.getItem();
        if (numBombs < MAX_BOXES && this.canIncreaseSize.get().test(stack)) {
            level.setBlockAndUpdate(pos, state.setValue(BOXES, numBombs + 1));
            player.awardStat(Stats.ITEM_USED.get(item));
            stack.consume(1, player);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, result);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockState attachedTo = level.getBlockState(pos.below());
        return attachedTo.isFaceSturdy(level, pos, Direction.UP) && super.canSurvive(state, level, pos);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        int count = state.getValue(BOXES);
        Direction facing = state.getValue(FACING);
        VoxelShape shape = SHAPES.get(count, facing);
        if (shape == null) {
            shape = makeShape(count);
            shape = XFactHDShapeUtils.rotateShapeAroundY(Direction.NORTH, facing, shape);
            SHAPES.put(count, facing, shape);
        }
        return shape;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
        return this.canSurvive(state, context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, @NotNull BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, BOXES);
    }

    public VoxelShape makeShape(int number) {

        VoxelShape shape = Shapes.empty();
        switch (number) {
            case 1:
                shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75), BooleanOp.OR);
                break;
            case 2:
                shape = Shapes.join(shape, Shapes.box(0, 0, 0.125, 0.5, 0.5, 0.625), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.375, 1, 0.5, 0.875), BooleanOp.OR);
                break;
            case 3:
                shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.5, 1, 0.5, 1), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0, 0, 0.5, 0.5, 0.5, 1), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.25, 0, 0, 0.75, 0.5, 0.5), BooleanOp.OR);
                break;
            case 4:
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.5, 1), BooleanOp.OR);
                break;
            case 5:
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.5, 1), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.25, 0.5, 0.25, 0.75, 1, 0.75), BooleanOp.OR);
                break;
            case 6:
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.5, 1), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.4375, 0.5, 0, 0.9375, 1, 0.5), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.0625, 0.5, 0.5, 0.5625, 1, 1), BooleanOp.OR);
                break;
            case 7:
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.5, 1), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.25, 0.5, 0.5, 0.75, 1, 1), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.5, 0.5, 0, 1, 1, 0.5), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0, 0.5, 0, 0.5, 1, 0.5), BooleanOp.OR);
                break;
            case 8:
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 1, 1), BooleanOp.OR);
                break;
        }

        return shape;
    }

}
