package net.hootisman.bananas.data;

import com.google.common.collect.ImmutableList;
import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.registry.BananaRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BanRecipeProvider extends RecipeProvider {

    //todo clean up this and literally every single other provider
    public BanRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipes ) {
        /** blocks **/
        //Banana Block
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BananaBlocks.BANANA_BLOCK.get(),1)
                .define('b',BananaItems.BANANA_BUNDLE.get())
                .pattern("bb")
                .pattern("bb")
                .unlockedBy(getHasName(BananaItems.BANANA_BUNDLE.get()),has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_block"));

        /** Items **/
        new SimpleCookingRecipeBuilder(RecipeCategory.FOOD, CookingBookCategory.FOOD,
                BananaItems.BREAD.get(),Ingredient.of(BananaItems.RAW_BREAD.get()),
                0.35f,200,BananaRecipes.BREAD_BAKING_SERIALIZER.get())
                .unlockedBy("has_raw_bread",has(BananaItems.RAW_BREAD.get()))
                .save(recipes);
        bananaBundleRecipes(recipes);

        //Banana Pickaxe
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BananaItems.BANANA_PICKAXE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STICK)
                .pattern("b")
                .pattern("s")
                .unlockedBy(getHasName(BananaItems.BANANA.get()),has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_pickaxe"));

        //Banana Boots
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BananaItems.BANANA_BOOTS.get(),1)
                .define('b',BananaBlocks.BANANA_BLOCK.get())
                .define('s', Items.STRING)
                .pattern("s s")
                .pattern("b b")
                .unlockedBy(getHasName(BananaBlocks.BANANA_BLOCK.get()),has(BananaBlocks.BANANA_BLOCK.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_boots"));
    }

    private void bananaBundleRecipes(@NotNull Consumer<FinishedRecipe> recipes){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BananaItems.BANANA_BUNDLE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STRING)
                .pattern("sb")
                .pattern("bb")
                .unlockedBy(getHasName(BananaItems.BANANA.get()),has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_bundle/banana_bundle0"));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BananaItems.BANANA_BUNDLE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STRING)
                .pattern("bs")
                .pattern("bb")
                .unlockedBy(getHasName(BananaItems.BANANA.get()),has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_bundle/banana_bundle1"));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BananaItems.BANANA_BUNDLE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STRING)
                .pattern("bb")
                .pattern("bs")
                .unlockedBy(getHasName(BananaItems.BANANA.get()),has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_bundle/banana_bundle2"));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BananaItems.BANANA_BUNDLE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STRING)
                .pattern("bb")
                .pattern("sb")
                .unlockedBy(getHasName(BananaItems.BANANA.get()),has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_bundle/banana_bundle3"));


    }


}
