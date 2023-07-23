package net.hootisman.bananas.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class FlourRecipe extends ShapelessRecipe {

    private String group;
    private CraftingBookCategory category;
    private NonNullList<Ingredient> ingredients;
    private ItemStack result;

    public FlourRecipe(ResourceLocation location, String group, CraftingBookCategory category, ItemStack stack, NonNullList<Ingredient> ingredients) {
        super(location, group, category, stack, ingredients);
        this.group = group;
        this.category = category;
        this.result = stack;
        this.ingredients = ingredients;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer craftingContainer) {
        NonNullList<ItemStack> list = NonNullList.withSize(craftingContainer.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); ++i){
            ItemStack item = craftingContainer.getItem(i);
            if (item.hasCraftingRemainingItem()){
                LogUtils.getLogger().info("REMAINING ITEM IDK " + item.getCraftingRemainingItem().getDisplayName());
                list.set(i, item.getCraftingRemainingItem());
            }
        }

        return list;
    }

    public static class Serializer implements RecipeSerializer<FlourRecipe>{
        private static final ResourceLocation NAME = new ResourceLocation("minecraft", "crafting_shapeless");
        @Override
        public FlourRecipe fromJson(ResourceLocation p_44290_, JsonObject p_44291_) {
            String s = GsonHelper.getAsString(p_44291_, "group", "");
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(p_44291_, "category", (String)null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(p_44291_, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44291_, "result"));
                return new FlourRecipe(p_44290_, s, craftingbookcategory, itemstack, nonnulllist);
            }
        }
        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_44276_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < p_44276_.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(p_44276_.get(i), false);
                if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        public FlourRecipe fromNetwork(ResourceLocation p_44293_, FriendlyByteBuf p_44294_) {
            String s = p_44294_.readUtf();
            CraftingBookCategory craftingbookcategory = p_44294_.readEnum(CraftingBookCategory.class);
            int i = p_44294_.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(p_44294_));
            }

            ItemStack itemstack = p_44294_.readItem();
            return new FlourRecipe(p_44293_, s, craftingbookcategory, itemstack, nonnulllist);
        }


        public void toNetwork(FriendlyByteBuf p_44281_, FlourRecipe p_44282_) {
            p_44281_.writeUtf(p_44282_.group);
            p_44281_.writeEnum(p_44282_.category);
            p_44281_.writeVarInt(p_44282_.ingredients.size());

            for(Ingredient ingredient : p_44282_.ingredients) {
                ingredient.toNetwork(p_44281_);
            }

            p_44281_.writeItem(p_44282_.result);
        }
    }
}
