package net.hootisman.bananas.util;

import net.hootisman.bananas.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DoughUtils {
    public static final int YEAST_TICK = 200;      //after x amount of ticks, ferment yeast once

    public static DoughBlockEntity placeDough(BlockPos blockPos, CompoundTag tag, BlockEntityType<DoughBlockEntity> entityToCreate, BlockState stateToCreate, Consumer<BlockState> swapItemBlockFunc){
        //places DoughBlockEntity
        DoughBlockEntity dough = entityToCreate.create(blockPos, stateToCreate);
        if (dough != null){
            dough.loadIngredientsContent(tag);
            swapItemBlockFunc.accept(dough.getBlockState());
        }
        return dough;
    }
    public static void pickupDough(DoughBlockEntity dough, Consumer<ItemStack> swapItemBlockFunc){
        //picks up DoughBlockEntity
        ItemStack doughBowl = new ItemStack(BananaItems.DOUGH_BOWL.get());
        doughBowl.setTag(dough.saveIngredientsContent(new CompoundTag()));
        swapItemBlockFunc.accept(doughBowl);
    }
    public static void swapItemAndBlock(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack newStack, BlockState newState){
        player.setItemInHand(hand,newStack);
        level.setBlockAndUpdate(pos,newState);
    }

    public static boolean canYeastGrow(short yeast){
        return yeast < Short.MAX_VALUE;
    }
    public static boolean hasYeastFermented(long currentTickTime, long lastTickTime){
        return currentTickTime - lastTickTime >= YEAST_TICK;
    }

    public static void playYeastSound(Level level, BlockPos blockPos){
        level.playSound(null,blockPos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS);
    }
    public static BlockEntityType<DoughBlockEntity> getDoughEntityType(BlockState doughState){
        BlockEntityType<DoughBlockEntity> doughEntityType = BananaBlockEntities.DOUGH_BLOCK_ENTITY.get();   //default
        if (doughState.is(BananaBlocks.DOUGH_CAULDRON.get())){
            doughEntityType = BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get();
        }
        return doughEntityType;
    }
    public static boolean hasDoughTag(ItemStack stack){
        CompoundTag tag;
        return stack.hasTag()
                && (tag = stack.getTag()).contains("time")
                && tag.contains("flour")
                && tag.contains("water")
                && tag.contains("yeast")
                && tag.contains("salt");
    }

}
