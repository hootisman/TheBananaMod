package net.hootisman.bananas.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class BananaBlock extends Block {
    public BananaBlock(){
        super(BlockBehaviour.Properties.of(Material.CLAY));
    }
}
