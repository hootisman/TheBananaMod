package net.hootisman.bananas.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;

public class BurntBananaPickaxeItem extends PickaxeItem {
    public BurntBananaPickaxeItem(){
        super(BananaToolTiers.BURNT_BANANA,1,-2.8F,new Item.Properties().setNoRepair().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2F).build()));
    }
    @Override
    public ItemStack finishUsingItem(ItemStack item, Level level, LivingEntity livingentity) {
        //basically BowlFoodItem code
        ItemStack itemstack = super.finishUsingItem(item,level,livingentity);
        return (livingentity instanceof Player && ((Player)livingentity).getAbilities().instabuild) ? itemstack : new ItemStack(Items.STICK);
    }
}
