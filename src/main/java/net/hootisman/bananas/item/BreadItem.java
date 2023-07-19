package net.hootisman.bananas.item;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BreadItem extends Item {
    public BreadItem() {
        super(new Properties().food(Foods.BREAD));  //default if no tag was smelted
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        //TODO override here to set custom food values based on flour/water/yeast
        if (stack.hasTag() && stack.getTag().contains("nutrition") && stack.getTag().contains("saturationMod")){
            int nutri = stack.getTag().getInt("nutrition");
            float satmod = stack.getTag().getInt("saturationMod");
            LogUtils.getLogger().info("EATING BREAD@!! nutri: " + nutri + " satmod: " + satmod);
            return new FoodProperties.Builder().nutrition(nutri).saturationMod(satmod).build();
        }
        return super.getFoodProperties(stack, entity);
    }
}
