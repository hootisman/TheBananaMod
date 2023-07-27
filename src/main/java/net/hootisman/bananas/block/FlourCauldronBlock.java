package net.hootisman.bananas.block;

import net.hootisman.bananas.util.CauldronUtils;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FlourCauldronBlock extends AbstractCauldronBlock {
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    public FlourCauldronBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON), CauldronUtils.FLOUR_INTERACT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LAYERS) == 8;
    }

}
