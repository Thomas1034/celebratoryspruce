package com.startraveler.celebratoryspruce.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class InteractableWallItemDisplayingBlock extends WallItemDisplayingBlock {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public InteractableWallItemDisplayingBlock(Optional<SimpleParticleType> gleamParticle, Properties properties) {
        super(gleamParticle, properties);
    }

    @Override
    public boolean canChangeItem(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }


}
