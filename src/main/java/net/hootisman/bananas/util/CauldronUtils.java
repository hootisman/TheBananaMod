package net.hootisman.bananas.util;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.block.FlourCauldronBlock;
import net.hootisman.bananas.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
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
        if (!level.isClientSide() &&
                blockState.is(Blocks.CAULDRON) ||
                (blockState.is(BananaBlocks.FLOUR_CAULDRON.get()) && blockState.getValue(FlourCauldronBlock.LEVEL) != 8)){
            stack.shrink(1);

            level.setBlockAndUpdate(blockPos, BananaBlocks.FLOUR_CAULDRON.get().defaultBlockState().setValue(FlourCauldronBlock.LEVEL,
                    blockState.is(Blocks.CAULDRON) ? 1 : blockState.getValue(FlourCauldronBlock.LEVEL) + 1));

            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SAND_PLACE);
            player.awardStat(Stats.FILL_CAULDRON);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    public static final CauldronInteraction EMPTY_FLOUR = (blockState, level, blockPos, player, hand, stack) -> {
        if (!level.isClientSide() && stack.isEmpty()){
            ItemStack flour = new ItemStack(BananaItems.FLOUR.get());
            flour.setCount(blockState.getValue(FlourCauldronBlock.LEVEL));
            player.getInventory().add(flour);
            level.setBlockAndUpdate(blockPos,Blocks.CAULDRON.defaultBlockState());
            level.playSound(null, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    public static final CauldronInteraction FILL_USING_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {
        if (!level.isClientSide() && DoughUtils.hasDoughTag(stack)){
            DoughBlockEntity dough = DoughUtils.placeDough(stack.getTag(),
                    () -> BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get().create(blockPos,BananaBlocks.DOUGH_CAULDRON.get().defaultBlockState()),
                    (BlockState doughState) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand,new ItemStack(Items.BOWL),doughState));

            level.setBlockEntity(dough);
            player.awardStat(Stats.FILL_CAULDRON);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    public static final CauldronInteraction EMPTY_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {
        Optional<DoughBlockEntity> dough = level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get());
        if (!level.isClientSide() && dough.isPresent()){
            DoughUtils.pickupDough(stack,player,dough.get(),
                    (ItemStack doughBowl) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand,doughBowl,Blocks.CAULDRON.defaultBlockState()));
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };

    public static final CauldronInteraction MIX_FLOUR = (blockState, level, blockPos, player, hand, stack) -> {
        //right click flour in water cauldron
        return InteractionResult.sidedSuccess(level.isClientSide());
    };

    public static final CauldronInteraction MIX_WATER = (blockState, level, blockPos, player, hand, stack) -> {
        //right click water in flour cauldron
        if (!level.isClientSide() && PotionUtils.getPotion(stack) == Potions.WATER){
            CompoundTag tag = DoughUtils.saveSpecificContent(level.getGameTime(), 250 * blockState.getValue(FlourCauldronBlock.LEVEL), 250, 0, 0);
            DoughBlockEntity dough = DoughUtils.placeDough(tag,
                    () -> BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get().create(blockPos,BananaBlocks.DOUGH_CAULDRON.get().defaultBlockState()),
                    (BlockState doughState) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand, ItemUtils.createFilledResult(stack,player,new ItemStack(Items.GLASS_BOTTLE)),doughState));

            level.setBlockEntity(dough);
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.BREWING_STAND_BREW);
            player.awardStat(Stats.FILL_CAULDRON);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };

}
