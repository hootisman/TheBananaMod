package net.hootisman.bananas.block;

import net.hootisman.bananas.util.CauldronUtils;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class DoughCauldronBlock extends AbstractCauldronBlock {
    public DoughCauldronBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON), CauldronUtils.DOUGH_INTERACT);
    }

    @Override
    public boolean isFull(BlockState blockState) {
        return true;
    }
}
