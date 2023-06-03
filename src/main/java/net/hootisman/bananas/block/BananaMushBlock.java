package net.hootisman.bananas.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class BananaMushBlock extends Block {
    //todo add drops and fix hitbox (voxelshape?); also add provider for this guys blockstates n models :)
    public BananaMushBlock() {
        super(BlockBehaviour.Properties.of(Material.CLAY).strength(0.1F));
    }
}
