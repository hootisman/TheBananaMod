package net.hootisman.bananas.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BreadItem extends Item {
    public BreadItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        //TODO override here to set custom food values based on flour/water/yeast
        return super.getFoodProperties(stack, entity);
    }
}
