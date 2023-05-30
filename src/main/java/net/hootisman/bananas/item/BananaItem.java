package net.hootisman.bananas.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class BananaItem extends Item {
    public BananaItem(){
        super(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build()));
    }
}
