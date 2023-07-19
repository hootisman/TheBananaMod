package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.recipe.BreadRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BananaRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BananaCore.MODID);
    public static final RegistryObject<RecipeSerializer<BreadRecipe>> BREAD_BAKING_SERIALIZER = RECIPE_SERIALIZERS.register("bread_baking_serializer", () -> new SimpleCookingSerializer<BreadRecipe>(BreadRecipe::new, 200));
    public static void register(IEventBus b){
        RECIPE_SERIALIZERS.register(b);
    }
}
