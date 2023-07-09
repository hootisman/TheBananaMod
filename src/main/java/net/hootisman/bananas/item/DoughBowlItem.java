package net.hootisman.bananas.item;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class DoughBowlItem extends Item {

    public DoughBowlItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level level, Player player) {
        itemStack.setTag((CompoundTag) new CompoundTag().put("jaja", ShortTag.valueOf((short) 2)));
    }


    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        LogUtils.getLogger().info("DROPPED SHITT!");
        if(item.hasTag()){
            LogUtils.getLogger().info("DOUGH HAS TAGG!!");

        }
        CompoundTag tag = new CompoundTag();
        tag.putInt("jaja",1);
        item.setTag(tag);

        return true;
    }

    @SubscribeEvent
    public static void onBowlUse(PlayerInteractEvent.RightClickBlock event){
        Optional<DoughBlockEntity> dough;
        if(!event.getLevel().isClientSide()
                && (dough = event.getLevel().getBlockEntity(event.getPos(), BananaBlockEntities.DOUGH_BLOCK_ENTITY.get())).isPresent()
                    && event.getItemStack().is(Items.BOWL)){


            ItemStack dough_bowl = new ItemStack(BananaItems.DOUGH_BOWL.get());
            CompoundTag tag = new CompoundTag();
            tag.putByte("yeast",dough.get().getYeastContent());
            dough_bowl.setTag(tag);

            event.getEntity().setItemInHand(event.getHand(),dough_bowl);
            event.getLevel().setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
            event.setCanceled(true);

        }
    }
}
