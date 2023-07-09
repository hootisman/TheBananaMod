package net.hootisman.bananas.item;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.block.DoughBlock;
import net.hootisman.bananas.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jline.utils.Log;

import java.util.Optional;

public class DoughBowlItem extends Item {

    public DoughBowlItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        boolean result = tryPlaceDough(context.getPlayer(),context.getLevel(),context.getClickedPos().relative(context.getClickedFace()),context.getHand(),context.getItemInHand());
        return result ? InteractionResult.sidedSuccess(context.getLevel().isClientSide()) : InteractionResult.FAIL;
    }
    private boolean tryPlaceDough(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack stack){
        boolean result = false;
        if (!level.isClientSide() && player != null && player.isShiftKeyDown()){
            DoughBlockEntity dough = BananaBlockEntities.DOUGH_BLOCK_ENTITY.get().create(pos,BananaBlocks.DOUGH_BLOCK.get().defaultBlockState());
            if (dough != null && stack.getTag() != null) dough.setYeastContent(stack.getTag().getByte("yeast"));

            player.setItemInHand(hand,new ItemStack(Items.BOWL));

            result = swapItemAndBlock(player,level,pos,hand,new ItemStack(Items.BOWL), dough.getBlockState());
            level.setBlockEntity(dough);
        }
        return result;
    }
    @SubscribeEvent
    public static void onBowlUse(PlayerInteractEvent.RightClickBlock event){
        boolean result = tryPickupDough(event.getEntity(), event.getLevel(), event.getPos(), event.getHand(), event.getItemStack());
        event.setCanceled(result);
    }

    private static boolean tryPickupDough(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack stack){
        Optional<DoughBlockEntity> dough;
        if(!level.isClientSide()
                && (dough = level.getBlockEntity(pos, BananaBlockEntities.DOUGH_BLOCK_ENTITY.get())).isPresent()
                && stack.is(Items.BOWL)){
            ItemStack doughBowl = new ItemStack(BananaItems.DOUGH_BOWL.get());
            CompoundTag tag = new CompoundTag();
            tag.putByte("yeast",dough.get().getYeastContent());
            doughBowl.setTag(tag);

            return swapItemAndBlock(player,level,pos,hand,doughBowl,Blocks.AIR.defaultBlockState());
        }
        return false;
    }

    private static boolean swapItemAndBlock(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack stack, BlockState state){
        player.setItemInHand(hand,stack);
        return level.setBlockAndUpdate(pos,state);
    }
}
