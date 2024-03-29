package net.hootisman.bananas.item;

import net.hootisman.bananas.block.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.util.DoughUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class DoughBowlItem extends Item {

    public DoughBowlItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        DoughUtils.tickInventoryDough(stack,level,entity,slot,selected);
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        //filled dough bowl used on ground
        return tryPlaceDough(context.getPlayer(),context.getLevel(),context.getClickedPos().relative(context.getClickedFace()),context.getHand(),context.getItemInHand());
    }
    private InteractionResult tryPlaceDough(Player player, Level level, BlockPos blockPos, InteractionHand hand, ItemStack stack){
        //used in 'useOn'
        if (!level.isClientSide() && DoughUtils.hasDoughTag(stack) && player.isShiftKeyDown()){
            DoughBlockEntity dough = DoughUtils.placeDough(stack.getTag(),
                    () -> BananaBlockEntities.DOUGH_BLOCK_ENTITY.get().create(blockPos, BananaBlocks.DOUGH_BLOCK.get().defaultBlockState()),
                    (BlockState doughState) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand,new ItemStack(Items.BOWL), doughState));

            level.setBlockEntity(dough);
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SHOVEL_FLATTEN);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    @SubscribeEvent
    public static void onBowlUse(PlayerInteractEvent.RightClickBlock event){
        //bowl used on dough block
        boolean result = tryPickupDough(event.getEntity(), event.getLevel(), event.getPos(), event.getHand(), event.getItemStack());
        event.setCanceled(result);
    }

    private static boolean tryPickupDough(Player player, Level level, BlockPos blockPos, InteractionHand hand, ItemStack stack){
        //used in 'onBowlUse"
        boolean result = false;
        Optional<DoughBlockEntity> dough;
        if(!level.isClientSide() && stack.is(Items.BOWL) && (dough = level.getBlockEntity(blockPos, BananaBlockEntities.DOUGH_BLOCK_ENTITY.get())).isPresent()){
            DoughUtils.pickupDough(stack,player,dough.get(),
                    (ItemStack doughBowl) -> DoughUtils.swapItemAndBlock(player,level,blockPos,hand,doughBowl,Blocks.AIR.defaultBlockState()));
            DoughUtils.playSoundHelper(level,blockPos,SoundEvents.SHOVEL_FLATTEN);
            result = true;
        }
        return result;
    }

}
