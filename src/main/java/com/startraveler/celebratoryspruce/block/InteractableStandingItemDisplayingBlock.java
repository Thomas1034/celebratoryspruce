package com.startraveler.celebratoryspruce.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class InteractableStandingItemDisplayingBlock extends StandingItemDisplayingBlock {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public InteractableStandingItemDisplayingBlock(Optional<SimpleParticleType> gleamParticle, Properties properties) {
        super(gleamParticle, properties);
    }

    @Override
    public boolean canChangeItem(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }

}
