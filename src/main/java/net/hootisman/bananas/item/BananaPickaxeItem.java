package net.hootisman.bananas.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraftforge.common.ForgeTier;

public class BananaPickaxeItem extends PickaxeItem {
    public BananaPickaxeItem(){
        super(BananaToolTiers.BANANA,1,-2.8F,new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build()));
    }
}
