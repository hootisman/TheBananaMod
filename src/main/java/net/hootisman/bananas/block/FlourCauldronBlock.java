package net.hootisman.bananas.block;

import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Map;

public class FlourCauldronBlock extends AbstractCauldronBlock {
    public static final IntegerProperty LEVEL = BlockStateProperties.LAYERS;
    private static final CauldronInteraction FILL_USING_FLOUR = (blockState, level, blockPos, player, hand, itemStack) -> {
        if (!level.isClientSide() && blockState.getValue(LEVEL) != 8){
            itemStack.shrink(1);
            level.setBlockAndUpdate(blockPos, blockState.setValue(LEVEL, blockState.getValue(LEVEL) + 1));
            player.awardStat(Stats.FILL_CAULDRON);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    public static Map<Item, CauldronInteraction> FLOUR_INTERACT = CauldronInteraction.newInteractionMap();
    public FlourCauldronBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON), FLOUR_INTERACT);
    }

    @SubscribeEvent
    public static void addCauldronInteractions(FMLCommonSetupEvent event){
        event.enqueueWork(() -> FLOUR_INTERACT.put(BananaItems.FLOUR.get(), FILL_USING_FLOUR));
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
