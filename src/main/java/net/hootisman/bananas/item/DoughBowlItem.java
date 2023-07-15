package net.hootisman.bananas.item;

import net.hootisman.bananas.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.util.DoughUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
        if (!stack.hasTag() || !stack.getTag().contains("time") || !stack.getTag().contains("yeast")) return;

        CompoundTag tag = stack.getTag();
        long lastTickTime = tag.getLong("time");
        short yeast;
        if(DoughUtils.hasYeastFermented(level.getGameTime(), lastTickTime) && DoughUtils.canYeastGrow(yeast = tag.getShort("yeast"))){
            tag.putShort("yeast", (short) (yeast + 1));
            tag.putLong("time",level.getGameTime());
            stack.setTag(tag);
            if (selected) DoughUtils.playYeastSound(level,entity.blockPosition());
        }

    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        //filled dough bowl used on ground
        boolean result = tryPlaceDough(context.getPlayer(),context.getLevel(),context.getClickedPos().relative(context.getClickedFace()),context.getHand(),context.getItemInHand());
        return result ? InteractionResult.sidedSuccess(context.getLevel().isClientSide()) : InteractionResult.FAIL;
    }
    private boolean tryPlaceDough(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack stack){
        //used in 'useOn'
        if (level.isClientSide()) return true;

        boolean result = false;
        if (player != null && player.isShiftKeyDown()){
            DoughBlockEntity dough = BananaBlockEntities.DOUGH_BLOCK_ENTITY.get().create(pos,BananaBlocks.DOUGH_BLOCK.get().defaultBlockState());
            CompoundTag tag = stack.getTag();
            if (dough != null && tag != null) dough.loadIngredientsContent(tag);

            result = swapItemAndBlock(player,level,pos,hand,new ItemStack(Items.BOWL), dough.getBlockState());
            level.setBlockEntity(dough);
        }
        return result;
    }
    @SubscribeEvent
    public static void onBowlUse(PlayerInteractEvent.RightClickBlock event){
        //bowl used on dough block
        boolean result = tryPickupDough(event.getEntity(), event.getLevel(), event.getPos(), event.getHand(), event.getItemStack());
        event.setCanceled(result);
    }

    private static boolean tryPickupDough(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack stack){
        //used in 'onBowlUse"
        if (level.isClientSide()) return false;

        Optional<DoughBlockEntity> dough;
        if(stack.is(Items.BOWL) && (dough = level.getBlockEntity(pos, BananaBlockEntities.DOUGH_BLOCK_ENTITY.get())).isPresent()){
            ItemStack doughBowl = new ItemStack(BananaItems.DOUGH_BOWL.get());
            doughBowl.setTag(dough.get().saveIngredientsContent(new CompoundTag()));

            return swapItemAndBlock(player,level,pos,hand,doughBowl,Blocks.AIR.defaultBlockState());
        }
        return false;
    }

    private static boolean swapItemAndBlock(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack stack, BlockState state){
        player.setItemInHand(hand,stack);
        return level.setBlockAndUpdate(pos,state);
    }
}
