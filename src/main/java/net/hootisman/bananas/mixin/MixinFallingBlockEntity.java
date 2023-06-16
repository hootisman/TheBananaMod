package net.hootisman.bananas.mixin;

import net.hootisman.bananas.entity.SettableBlockState;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FallingBlockEntity.class)
public abstract class MixinFallingBlockEntity implements SettableBlockState {
    @Shadow private BlockState blockState;

    public void setBlockState(BlockState state){
        blockState = state;
    }
}
