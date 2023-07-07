package net.hootisman.bananas.block;

import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Map;

public class FlourCauldronBlock extends AbstractCauldronBlock {
    public static final IntegerProperty LEVEL = BlockStateProperties.LAYERS;
    public static Map<Item, CauldronInteraction> FLOUR = CauldronInteraction.newInteractionMap();
    private final CauldronInteraction FILL_USING_FLOUR = (blockState, level, blockPos, player, hand, itemStack) -> {
        if (!level.isClientSide() && blockState.getValue(LEVEL) != 8){
            itemStack.shrink(1);
            level.setBlockAndUpdate(blockPos, blockState.setValue(LEVEL, blockState.getValue(LEVEL) + 1));
            player.awardStat(Stats.FILL_CAULDRON);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    public FlourCauldronBlock(Properties properties, Map<Item, CauldronInteraction> map) {
        super(properties, map);
        addInteractions();
    }

    private void addInteractions(){
        FLOUR.put(BananaItems.FLOUR.get(), FILL_USING_FLOUR);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == 8;
    }
}
