package net.hootisman.bananas.entity;

import net.minecraft.world.level.block.state.BlockState;

public interface SettableBlockState {
    //for 'MixinFallingBlockEntity"
    void setBlockState(BlockState state);
}
