package net.hootisman.bananas.item;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
//        event.getItemStack()
    }
}
