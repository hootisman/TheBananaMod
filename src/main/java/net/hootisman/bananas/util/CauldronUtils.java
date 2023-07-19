package net.hootisman.bananas.util;

import net.hootisman.bananas.block.FlourCauldronBlock;
import net.hootisman.bananas.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.swing.text.html.Option;
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
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    public static final CauldronInteraction EMPTY_FLOUR = (blockState, level, blockPos, player, hand, stack) -> {
        if (!level.isClientSide() && stack.isEmpty()){
            ItemStack flour = new ItemStack(BananaItems.FLOUR.get());
            flour.setCount(blockState.getValue(FlourCauldronBlock.LEVEL));
            player.getInventory().add(flour);
            level.setBlockAndUpdate(blockPos,Blocks.CAULDRON.defaultBlockState());
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.ITEM_PICKUP);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    public static final CauldronInteraction FILL_USING_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {
        if (!level.isClientSide() && DoughUtils.hasDoughTag(stack)){
            DoughBlockEntity dough = DoughUtils.placeDough(stack.getTag(),
                    () -> BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get().create(blockPos,BananaBlocks.DOUGH_CAULDRON.get().defaultBlockState()),
                    (BlockState doughState) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand,new ItemStack(Items.BOWL),doughState));

            doughPlaceHelper(level,dough,blockPos,SoundEvents.SHOVEL_FLATTEN);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    public static final CauldronInteraction EMPTY_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {
        Optional<DoughBlockEntity> dough = level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get());
        if (!level.isClientSide() && dough.isPresent()){
            DoughUtils.pickupDough(stack,player,dough.get(),
                    (ItemStack doughBowl) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand,doughBowl,Blocks.CAULDRON.defaultBlockState()));
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.ITEM_PICKUP);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };
    /**
     * Flour is used on water cauldron or dough cauldron, will create or update {@link DoughBlockEntity} respectively
     */
    public static final CauldronInteraction MIX_FLOUR = (blockState, level, blockPos, player, hand, stack) -> {
        if (!level.isClientSide() && blockState.is(Blocks.WATER_CAULDRON)){
            CompoundTag tag = DoughUtils.saveSpecificContent(level.getGameTime(), DoughUtils.FLOUR_PER_DUST, DoughUtils.GRAMS_IN_BOTTLE * blockState.getValue(LayeredCauldronBlock.LEVEL), 0, 0);
            DoughBlockEntity dough = DoughUtils.placeDough(tag,
                    () -> BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get().create(blockPos,BananaBlocks.DOUGH_CAULDRON.get().defaultBlockState()),
                    (BlockState doughState) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand, ItemUtils.createFilledResult(stack,player,ItemStack.EMPTY),doughState));

            doughPlaceHelper(level,dough,blockPos,SoundEvents.BREWING_STAND_BREW);
        } else if (!level.isClientSide() && blockState.is(BananaBlocks.DOUGH_CAULDRON.get())) {
            Optional<DoughBlockEntity> dough = level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get());
            if (dough.isPresent()){
                stack.shrink(1);
                dough.get().getDoughData().increaseFlour(DoughUtils.FLOUR_PER_DUST);
                DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SAND_PLACE);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };

    public static final CauldronInteraction MIX_WATER = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        Optional<DoughBlockEntity> doughCauldron = level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get());
        if (PotionUtils.getPotion(stack) == Potions.WATER && doughCauldron.isEmpty()){
            //right clicked on flour cauldron
            CompoundTag tag = DoughUtils.saveSpecificContent(level.getGameTime(), DoughUtils.FLOUR_PER_DUST * blockState.getValue(FlourCauldronBlock.LEVEL), DoughUtils.GRAMS_IN_BOTTLE, 0, 0);
            DoughBlockEntity dough = DoughUtils.placeDough(tag,
                    () -> BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get().create(blockPos,BananaBlocks.DOUGH_CAULDRON.get().defaultBlockState()),
                    (BlockState doughState) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand, ItemUtils.createFilledResult(stack,player,new ItemStack(Items.GLASS_BOTTLE)),doughState));

            doughPlaceHelper(level,dough,blockPos,SoundEvents.BREWING_STAND_BREW);
        }else if (PotionUtils.getPotion(stack) == Potions.WATER && doughCauldron.isPresent()){
            //right clicked on dough cauldron
            doughCauldron.get().getDoughData().increaseWater(DoughUtils.GRAMS_IN_BOTTLE);
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.BOTTLE_EMPTY);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };

    public static final CauldronInteraction HARVEST_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {

        Optional<DoughBlockEntity> dough = level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get());
        if (!level.isClientSide() && stack.getItem() instanceof AxeItem && dough.isPresent()){
            ItemEntity breadEntity = DoughUtils.harvestDough(dough.get().getDoughData(),
                    (bread) -> new ItemEntity(level,
                    blockPos.getX() + 0.5d,blockPos.getY() + 1d,blockPos.getZ() + 0.5d,
                    bread,
                    level.getRandom().nextGaussian() * 0.07d,0.265d,level.getRandom().nextGaussian() * 0.07d)
            );
            if (breadEntity != null){
                level.addFreshEntity(breadEntity);
                DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SHOVEL_FLATTEN);
                DoughUtils.spawnTopParticlesHelper(ParticleTypes.WAX_OFF,(ServerLevel) level,
                        blockPos.getX(),blockPos.getY() + 1.05f,blockPos.getZ(),5, 0.0f,0.01f,0.0f, 1.0f);
            }else return InteractionResult.FAIL;
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    };

    /**
     * run if cauldron interaction does a successful mix
     * @see #MIX_FLOUR
     * @see #MIX_WATER
     */
    public static void doughPlaceHelper(Level level, DoughBlockEntity dough, BlockPos blockPos, SoundEvent soundToPlay){
        level.setBlockEntity(dough);
        DoughUtils.playSoundHelper(level,blockPos,soundToPlay);
    }
}
