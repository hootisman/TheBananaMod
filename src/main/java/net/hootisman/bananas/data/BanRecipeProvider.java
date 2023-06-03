package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.block.BananaBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BanRecipeProvider extends RecipeProvider {

    //todo clean up this and literally every single other provider
    public BanRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipes ) {
        //blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BananaBlocks.BANANA_BLOCK.get(),1)
                .define('b',BananaItems.BANANA_BUNDLE.get())
                .pattern("bb")
                .pattern("bb")
                .unlockedBy("has_banana_bundle",has(BananaItems.BANANA_BUNDLE.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_block"));

        //items
        itemRecipes(recipes);
    }

    private void itemRecipes(@NotNull Consumer<FinishedRecipe> recipes){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BananaItems.BANANA_BUNDLE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STRING)
                .pattern("sb")
                .pattern("bb")
                .unlockedBy("has_banana",has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_bundle/banana_bundle0"));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BananaItems.BANANA_BUNDLE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STRING)
                .pattern("bs")
                .pattern("bb")
                .unlockedBy("has_banana",has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_bundle/banana_bundle1"));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BananaItems.BANANA_BUNDLE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STRING)
                .pattern("bb")
                .pattern("bs")
                .unlockedBy("has_banana",has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_bundle/banana_bundle2"));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BananaItems.BANANA_BUNDLE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STRING)
                .pattern("bb")
                .pattern("sb")
                .unlockedBy("has_banana",has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_bundle/banana_bundle3"));


        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BananaItems.BANANA_PICKAXE.get(),1)
                .define('b',BananaItems.BANANA.get())
                .define('s', Items.STICK)
                .pattern("b")
                .pattern("s")
                .unlockedBy("has_banana",has(BananaItems.BANANA.get()))
                .save(recipes, new ResourceLocation(BananaCore.MODID,"banana_pickaxe"));
    }


}
