package net.hootisman.bananas.data.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.hootisman.bananas.registry.BananaRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FlourRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    @Nullable
    private String group;

    public FlourRecipeBuilder(RecipeCategory category, Item result, int count) {
        this.category = category;
        this.result = result;
        this.count = count;
    }

    public FlourRecipeBuilder requires(TagKey<Item> tagKey){
        return requires(Ingredient.of(tagKey));
    }
    public FlourRecipeBuilder requires(ItemLike item){
        return requires(Ingredient.of(item));
    }
    public FlourRecipeBuilder requires(Ingredient ingredient){
        return requires(ingredient, 1);
    }
    public FlourRecipeBuilder requires(Ingredient ingredient, int count){
        for (int i = 0; i < count; ++i){
            ingredients.add(ingredient);
        }
        return this;
    }
    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        advancement.addCriterion(name,criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String newGroup) {
        group = newGroup;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipes, ResourceLocation location) {
        if (advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }

        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);
        recipes.accept(new ShapelessRecipeBuilder.Result(location, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.ingredients, this.advancement, location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
    public static class Result extends CraftingRecipeBuilder.CraftingResult{
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        protected Result(CraftingBookCategory p_250313_, ResourceLocation id, Item result, int count, String group, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(p_250313_);
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.ingredients = ingredients;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            super.serializeRecipeData(jsonObject);
//            System.out.println("HERE FOR THE SHIT!!!" + jsonObject.get("type").getAsString());
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }

            jsonObject.add("ingredients", jsonarray);
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject.addProperty("count", this.count);
            }

            jsonObject.add("result", jsonobject);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BananaRecipes.FLOUR_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}
