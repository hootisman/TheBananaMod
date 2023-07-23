package net.hootisman.bananas.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.hootisman.bananas.registry.BananaRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class FlourRecipe extends CustomRecipe{

    private String group;
    private ItemStack result;
    private NonNullList<Ingredient> ingredients;
    private CraftingBookCategory category;
    public FlourRecipe(ResourceLocation location,String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(location, category);
        this.group = group;
        this.category = category;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        int numOfItems = 0;
        for (int i = 0; i < craftingContainer.getContainerSize(); ++i){
            ItemStack item = craftingContainer.getItem(i);
            if (!item.isEmpty()){
                ++numOfItems;
            }
        }
        return numOfItems == ingredients.size();
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= 2;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer craftingContainer) {
        NonNullList<ItemStack> list = NonNullList.withSize(craftingContainer.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); ++i){
            ItemStack item = craftingContainer.getItem(i);
            if (item.hasCraftingRemainingItem()){
                list.set(i, item.getCraftingRemainingItem());
            } else if (item.getItem() instanceof AxeItem) {
                list.set(i,item.copyWithCount(1));
                break;
            }
        }

        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BananaRecipes.FLOUR_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<FlourRecipe>{
        private static final ResourceLocation NAME = new ResourceLocation("bananas", "flour_serializer");
        @Override
        public FlourRecipe fromJson(ResourceLocation location, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", (String)null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                return new FlourRecipe(location, s, craftingbookcategory, itemstack, nonnulllist);
            }
        }
        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i), false);
                if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        public FlourRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf buffer) {
            String s = buffer.readUtf();
            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new FlourRecipe(location, s, craftingbookcategory, itemstack, nonnulllist);
        }


        public void toNetwork(FriendlyByteBuf buffer, FlourRecipe flourRecipe) {
            buffer.writeUtf(flourRecipe.group);
            buffer.writeEnum(flourRecipe.category);
            buffer.writeVarInt(flourRecipe.ingredients.size());

            for(Ingredient ingredient : flourRecipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(flourRecipe.result);
        }
    }
}
