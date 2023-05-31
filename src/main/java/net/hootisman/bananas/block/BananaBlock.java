package net.hootisman.bananas.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class BananaBlock extends Block {
    public BananaBlock(){
        super(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_YELLOW).strength(0.6F).sound(SoundType.SLIME_BLOCK));
    }
}
