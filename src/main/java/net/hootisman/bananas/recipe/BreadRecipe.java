package net.hootisman.bananas.recipe;

import net.hootisman.bananas.registry.BananaRecipes;
import net.hootisman.bananas.util.DoughData;
import net.hootisman.bananas.util.DoughUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BreadRecipe extends AbstractCookingRecipe {
    public BreadRecipe(ResourceLocation p_249379_, String p_249518_, CookingBookCategory p_250891_, Ingredient p_251354_, ItemStack p_252185_, float p_252165_, int p_250256_) {
        super(RecipeType.SMELTING, p_249379_, p_249518_, p_250891_, p_251354_, p_252185_, p_252165_, p_250256_);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return ingredient.test(container.getItem(0)) && DoughUtils.hasDoughTag(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        DoughData data = new DoughData();
        data.loadIngredients(container.getItem(0).getTag());
        ItemStack resultWithTag = result.copy();
        resultWithTag.setTag(data.calculateBreadData());
        return resultWithTag;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.FURNACE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BananaRecipes.BREAD_BAKING_SERIALIZER.get();
    }
}
