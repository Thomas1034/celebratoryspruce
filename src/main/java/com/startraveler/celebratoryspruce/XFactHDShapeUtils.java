/*
 * This file contains code originally written by XFactHD for the FramedBlocks project.
 * The code was graciously shared by the author and is used here with explicit permission,
 * overriding the original LGPL-3.0 license.
 *
 * The original code can be found at: https://github.com/XFactHD/FramedBlocks
 *
 * Any faults are mine alone.
 */

package com.startraveler.celebratoryspruce;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class XFactHDShapeUtils {

    private static final Direction[] HORIZONTAL_DIRECTIONS = Direction.Plane.HORIZONTAL.stream()
            .toArray(Direction[]::new);
    private static final int[] DIR_ROT_X_2D_DATA = OtherUtils.make(
            new int[6], arr -> {
                arr[Direction.DOWN.ordinal()] = 2;
                arr[Direction.UP.ordinal()] = 0;
                arr[Direction.NORTH.ordinal()] = 3;
                arr[Direction.SOUTH.ordinal()] = 1;
                arr[Direction.WEST.ordinal()] = -1;
                arr[Direction.EAST.ordinal()] = -1;
            }
    );
    private static final int[] DIR_ROT_Z_2D_DATA = OtherUtils.make(
            new int[6], arr -> {
                arr[Direction.DOWN.ordinal()] = 2;
                arr[Direction.UP.ordinal()] = 0;
                arr[Direction.NORTH.ordinal()] = -1;
                arr[Direction.SOUTH.ordinal()] = -1;
                arr[Direction.WEST.ordinal()] = 3;
                arr[Direction.EAST.ordinal()] = 1;
            }
    );

    private XFactHDShapeUtils() {
    }

    public static VoxelShape orUnoptimized(VoxelShape first, VoxelShape second) {
        return Shapes.joinUnoptimized(first, second, BooleanOp.OR);
    }

    public static VoxelShape orUnoptimized(VoxelShape first, VoxelShape... others) {
        for (VoxelShape shape : others) {
            first = XFactHDShapeUtils.orUnoptimized(first, shape);
        }
        return first;
    }

    public static VoxelShape or(VoxelShape first, VoxelShape second) {
        return orUnoptimized(first, second).optimize();
    }

    public static VoxelShape or(VoxelShape first, VoxelShape... others) {
        return orUnoptimized(first, others).optimize();
    }

    public static VoxelShape andUnoptimized(VoxelShape first, VoxelShape second) {
        return Shapes.joinUnoptimized(first, second, BooleanOp.AND);
    }

    public static VoxelShape andUnoptimized(VoxelShape first, VoxelShape... others) {
        for (VoxelShape shape : others) {
            first = XFactHDShapeUtils.andUnoptimized(first, shape);
        }
        return first;
    }

    public static VoxelShape and(VoxelShape first, VoxelShape second) {
        return andUnoptimized(first, second).optimize();
    }

    public static VoxelShape and(VoxelShape first, VoxelShape... others) {
        return andUnoptimized(first, others).optimize();
    }

    public static VoxelShape rotateShapeAroundY(Direction from, Direction to, VoxelShape shape) {
        return rotateShapeUnoptimizedAroundY(from, to, shape).optimize();
    }

    public static VoxelShape rotateShapeUnoptimizedAroundY(Direction from, Direction to, VoxelShape shape) {
        if (OtherUtils.isY(from) || OtherUtils.isY(to)) {
            throw new IllegalArgumentException("Invalid Direction!");
        }
        if (from == to) {
            return shape;
        }

        List<AABB> sourceBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (AABB box : sourceBoxes) {
            for (int i = 0; i < times; i++) {
                box = new AABB(1 - box.maxZ, box.minY, box.minX, 1 - box.minZ, box.maxY, box.maxX);
            }
            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateShapeAroundX(Direction from, Direction to, VoxelShape shape) {
        return rotateShapeUnoptimizedAroundX(from, to, shape).optimize();
    }

    public static VoxelShape rotateShapeUnoptimizedAroundX(Direction from, Direction to, VoxelShape shape) {
        if (OtherUtils.isX(from) || OtherUtils.isX(to)) {
            throw new IllegalArgumentException("Invalid Direction!");
        }
        if (from == to) {
            return shape;
        }

        List<AABB> sourceBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        int times = (DIR_ROT_X_2D_DATA[to.ordinal()] - DIR_ROT_X_2D_DATA[from.ordinal()] + 4) % 4;
        for (AABB box : sourceBoxes) {
            for (int i = 0; i < times; i++) {
                box = new AABB(box.minX, 1 - box.maxZ, box.minY, box.maxX, 1 - box.minZ, box.maxY);
            }
            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static VoxelShape rotateShapeAroundZ(Direction from, Direction to, VoxelShape shape) {
        return rotateShapeUnoptimizedAroundZ(from, to, shape).optimize();
    }

    public static VoxelShape rotateShapeUnoptimizedAroundZ(Direction from, Direction to, VoxelShape shape) {
        if (OtherUtils.isZ(from) || OtherUtils.isZ(to)) {
            throw new IllegalArgumentException("Invalid Direction!");
        }
        if (from == to) {
            return shape;
        }

        List<AABB> sourceBoxes = shape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        int times = (DIR_ROT_Z_2D_DATA[to.ordinal()] - DIR_ROT_Z_2D_DATA[from.ordinal()] + 4) % 4;
        for (AABB box : sourceBoxes) {
            for (int i = 0; i < times; i++) {
                //noinspection SuspiciousNameCombination
                box = new AABB(box.minY, 1 - box.maxX, box.minZ, box.maxY, 1 - box.minX, box.maxZ);
            }
            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    private static class OtherUtils {

        public static boolean isZ(Direction dir) {
            return dir.getStepZ() != 0;
        }

        public static boolean isY(Direction dir) {
            return dir.getStepY() != 0;
        }

        public static boolean isX(Direction dir) {
            return dir.getStepX() != 0;
        }

        public static int[] make(int[] ints, Consumer<int[]> consumer) {
            consumer.accept(ints);
            return ints;
        }
    }
}
