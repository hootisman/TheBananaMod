package net.hootisman.bananas.util;

import net.hootisman.bananas.block.FlourCauldronBlock;
import net.hootisman.bananas.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Optional;

public class CauldronUtils {
    //FlourCauldronBlock interactions
    public static Map<Item, CauldronInteraction> FLOUR_INTERACT = CauldronInteraction.newInteractionMap();
    //DoughCauldronBlock interactions
    public static Map<Item, CauldronInteraction> DOUGH_INTERACT = CauldronInteraction.newInteractionMap();

    public static final CauldronInteraction FILL_USING_FLOUR = (blockState, level, blockPos, player, hand, stack) -> {
        if (!level.isClientSide() && canFlourBePlaced(blockState)){
            stack.shrink(1);
            BlockState newFlourCauldron = BananaBlocks.FLOUR_CAULDRON.get().defaultBlockState();
            level.setBlockAndUpdate(blockPos, newFlourCauldron.setValue(FlourCauldronBlock.LEVEL, blockState.is(Blocks.CAULDRON) ? 1 : blockState.getValue(FlourCauldronBlock.LEVEL) + 1));
            level.playSound(null,blockPos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS);
            player.awardStat(Stats.FILL_CAULDRON);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };

    public static final CauldronInteraction FILL_USING_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {

        if (!level.isClientSide() && DoughUtils.hasDoughTag(stack)){
            CompoundTag tag = stack.getTag();

//            DoughBlockEntity dough = BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get().create(blockPos,blockState);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };



    private static boolean canFlourBePlaced(BlockState state){
        return state.is(Blocks.CAULDRON) || (state.is(BananaBlocks.FLOUR_CAULDRON.get()) && state.getValue(FlourCauldronBlock.LEVEL) != 8);
    }
}
