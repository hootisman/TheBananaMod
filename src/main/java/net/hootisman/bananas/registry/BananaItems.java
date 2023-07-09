package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.item.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BananaItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BananaCore.MODID);

    public static final RegistryObject<Item> BANANA = ITEMS.register("banana",BananaItem::new);
    public static final RegistryObject<Item> BANANA_PICKAXE = ITEMS.register("banana_pickaxe", BananaPickaxeItem::new);
    public static final RegistryObject<Item> BANANA_BUNDLE = ITEMS.register("banana_bundle",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BURNT_BANANA_PICKAXE = ITEMS.register("burnt_banana_pickaxe", BurntBananaPickaxeItem::new);
    public static final RegistryObject<Item> BANANA_BOOTS = ITEMS.register("banana_boots", BananaBootsItem::new);
    public static final RegistryObject<Item> FLOUR = ITEMS.register("flour", FlourItem::new);
    public static final RegistryObject<Item> DOUGH_BOWL = ITEMS.register("dough_bowl",DoughBowlItem::new);


    public static void register(IEventBus b){
        ITEMS.register(b);
    }

}
