package net.hootisman.bananas.util;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.block.FlourCauldronBlock;
import net.hootisman.bananas.block.entity.DoughBlockEntity;
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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.function.Consumer;

public class CauldronUtils {
    //FlourCauldronBlock interactions
    public static Map<Item, CauldronInteraction> FLOUR_INTERACT = CauldronInteraction.newInteractionMap();
    //DoughCauldronBlock interactions
    public static Map<Item, CauldronInteraction> DOUGH_INTERACT = CauldronInteraction.newInteractionMap();

    /**
     * flour -> cauldron = flour cauldron
     */
    public static final CauldronInteraction FILL_FLOUR_ON_EMPTY = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (!player.getAbilities().instabuild) stack.shrink(1);
        level.setBlockAndUpdate(blockPos, BananaBlocks.FLOUR_CAULDRON.get().defaultBlockState());
        DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SAND_PLACE);

        return InteractionResult.CONSUME;
    };
    /**
     * flour -> flour cauldron = flour cauldron (updated)
     */
    public static final CauldronInteraction FILL_FLOUR_ON_FLOUR = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (blockState.getValue(FlourCauldronBlock.LEVEL) != 8){
            if (!player.getAbilities().instabuild) stack.shrink(1);
            level.setBlockAndUpdate(blockPos, blockState.setValue(FlourCauldronBlock.LEVEL, blockState.getValue(FlourCauldronBlock.LEVEL) + 1));
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SAND_PLACE);
        }

        return InteractionResult.CONSUME;
    };
    /**
     * dough bowl -> cauldron = dough cauldron
     */
    public static final CauldronInteraction FILL_DOUGH_ON_EMPTY = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (DoughUtils.hasDoughTag(stack)){
            placeDoughHelper(level, blockPos, SoundEvents.SHOVEL_FLATTEN, stack.getTag(),
                    BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get(),
                    BananaBlocks.DOUGH_CAULDRON.get().defaultBlockState(),
                    (BlockState doughState) -> DoughUtils.swapItemAndBlock(player, level, blockPos, hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BOWL)), doughState));
        }
        return InteractionResult.CONSUME;
    };
    /**
     * empty -> flour cauldron = cauldron
     */
    public static final CauldronInteraction EMPTY_FLOUR = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide() || !player.isShiftKeyDown()) return InteractionResult.SUCCESS;

        ItemStack flour = new ItemStack(BananaItems.FLOUR.get());
        flour.setCount(blockState.getValue(FlourCauldronBlock.LEVEL));

        ItemEntity flourEntity = new ItemEntity(level,
                blockPos.getX() + 0.5d,blockPos.getY() + 1d,blockPos.getZ() + 0.5d,
                flour,
                level.getRandom().nextGaussian() * 0.07d,0.265d,level.getRandom().nextGaussian() * 0.07d);

        level.addFreshEntity(flourEntity);
        level.setBlockAndUpdate(blockPos,Blocks.CAULDRON.defaultBlockState());
        DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SAND_HIT);

        return InteractionResult.CONSUME;
    };
    /**
     * bowl -> dough cauldron = cauldron
     */
    public static final CauldronInteraction EMPTY_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get()).ifPresent((DoughBlockEntity doughCauldron) -> {
            DoughUtils.pickupDough(stack,player,doughCauldron,
                    (ItemStack doughBowl) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand,doughBowl,Blocks.CAULDRON.defaultBlockState()));
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SHOVEL_FLATTEN);
        });

        return InteractionResult.CONSUME;
    };
    /**
     * water bottle -> flour cauldron = dough cauldron
     */
    public static final CauldronInteraction MIX_WATER_ON_FLOUR = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (PotionUtils.getPotion(stack) == Potions.WATER ) {
            CompoundTag tag = DoughUtils.saveSpecificContent(level.getGameTime(), DoughUtils.FLOUR_PER_DUST * blockState.getValue(FlourCauldronBlock.LEVEL), DoughUtils.WATER_PER_BOTTLE, 0, 0);
            placeDoughHelper(level, blockPos, SoundEvents.BREWING_STAND_BREW, tag,
                    BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get(),
                    BananaBlocks.DOUGH_CAULDRON.get().defaultBlockState(),
                    (BlockState doughState) -> DoughUtils.swapItemAndBlock(player, level, blockPos, hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)), doughState));
        }

        return InteractionResult.CONSUME;
    };
    /**
     * flour -> water cauldron = dough cauldron
     */
    public static final CauldronInteraction MIX_FLOUR_ON_WATER = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        CompoundTag tag = DoughUtils.saveSpecificContent(level.getGameTime(), DoughUtils.FLOUR_PER_DUST, DoughUtils.WATER_PER_BOTTLE * blockState.getValue(LayeredCauldronBlock.LEVEL), 0, 0);
        placeDoughHelper(level,blockPos, SoundEvents.BREWING_STAND_BREW, tag,
                BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get(),
                BananaBlocks.DOUGH_CAULDRON.get().defaultBlockState(),
                (BlockState doughState) -> DoughUtils.swapItemAndBlock(player, level, blockPos, hand, ItemUtils.createFilledResult(stack, player, ItemStack.EMPTY), doughState));

        return InteractionResult.CONSUME;
    };
    /**
     * flour -> dough cauldron = dough cauldron (updated)
     */
    public static final CauldronInteraction MIX_FLOUR_ON_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get()).ifPresent((DoughBlockEntity doughCauldron) -> {
            DoughData data = doughCauldron.getDoughData();
            data.increaseFlour(DoughUtils.FLOUR_PER_DUST);
            LogUtils.getLogger().info("FLOUR; SIZE AFTER INCREASE " + data.getSize());
            if (data.getSize() <= DoughUtils.MAX_DOUGH_SIZE){
                //dough size is less than max
                if (!player.getAbilities().instabuild) stack.shrink(1);
                DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SAND_PLACE);
            } else if (data.getSize() - DoughUtils.MAX_DOUGH_SIZE < DoughUtils.FLOUR_PER_DUST) {
                //dough size is greater than max, difference in size and max is less than grams of flour
                data.decreaseFlour(data.getSize() - DoughUtils.MAX_DOUGH_SIZE);

                if (!player.getAbilities().instabuild) stack.shrink(1);
                DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SAND_PLACE);
                DoughUtils.playSoundHelper(level,blockPos,SoundEvents.ITEM_FRAME_ADD_ITEM);
            } else data.decreaseFlour(DoughUtils.FLOUR_PER_DUST);
        });

        return InteractionResult.CONSUME;
    };
    /**
     * water bottle -> dough cauldron = dough cauldron (updated)
     */
    public static final CauldronInteraction MIX_WATER_ON_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get()).ifPresent((DoughBlockEntity doughCauldron) -> {
            DoughData data = doughCauldron.getDoughData();
            data.increaseWater(DoughUtils.WATER_PER_BOTTLE); //try increase

            LogUtils.getLogger().info("WATER; SIZE AFTER INCREASE " + data.getSize());
            if (data.getSize() <= DoughUtils.MAX_DOUGH_SIZE){
                //dough size is less than max
                if (!player.getAbilities().instabuild) player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                DoughUtils.playSoundHelper(level,blockPos,SoundEvents.BOTTLE_EMPTY);
            } else if (data.getSize() - DoughUtils.MAX_DOUGH_SIZE < DoughUtils.WATER_PER_BOTTLE) {
                //dough size is greater than max, difference in size and max is less than grams of bottle
                data.decreaseWater(data.getSize() - DoughUtils.MAX_DOUGH_SIZE); //round to max dough size

                if (!player.getAbilities().instabuild) player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                DoughUtils.playSoundHelper(level,blockPos,SoundEvents.BOTTLE_EMPTY);
                DoughUtils.playSoundHelper(level,blockPos,SoundEvents.ITEM_FRAME_ADD_ITEM);
            } else data.decreaseWater(DoughUtils.WATER_PER_BOTTLE);
        });

        return InteractionResult.CONSUME;
    };
    /**
     * axe -> dough cauldron = raw bread
     */
    public static final CauldronInteraction HARVEST_DOUGH = (blockState, level, blockPos, player, hand, stack) -> {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        level.getBlockEntity(blockPos,BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get()).ifPresent((DoughBlockEntity doughCauldron) -> {
            ItemEntity breadEntity = DoughUtils.harvestDough(doughCauldron.getDoughData(),
                    (bread) -> new ItemEntity(level,
                            blockPos.getX() + 0.5d,blockPos.getY() + 1d,blockPos.getZ() + 0.5d,
                            bread,
                            level.getRandom().nextGaussian() * 0.07d,0.265d,level.getRandom().nextGaussian() * 0.07d));
            level.addFreshEntity(breadEntity);
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SHOVEL_FLATTEN);
            DoughUtils.spawnTopParticlesHelper(ParticleTypes.WAX_OFF,(ServerLevel) level,
                    blockPos.getX(),blockPos.getY() + 1.05f,blockPos.getZ(),5, 0.0f,0.01f,0.0f, 1.0f);
        });

        return InteractionResult.CONSUME;
    };

    public static void placeDoughHelper(Level level, BlockPos blockPos, SoundEvent soundToPlay, CompoundTag tag, BlockEntityType<DoughBlockEntity> entityToCreate, BlockState blockStateToCreate, Consumer<BlockState> swapItemBlockFunc){
        DoughBlockEntity dough = DoughUtils.placeDough(tag,
                () -> entityToCreate.create(blockPos, blockStateToCreate),
                swapItemBlockFunc);

        level.setBlockEntity(dough);
        DoughUtils.playSoundHelper(level,blockPos,soundToPlay);
    }
}
