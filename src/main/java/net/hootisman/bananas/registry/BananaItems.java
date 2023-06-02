package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.item.BananaItem;
import net.hootisman.bananas.item.BananaPickaxeItem;
import net.hootisman.bananas.item.BurntBananaPickaxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BananaItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BananaCore.MODID);

    public static final RegistryObject<Item> BANANA = ITEMS.register("banana",BananaItem::new);
    public static final RegistryObject<Item> BANANA_BLOCK_ITEM = ITEMS.register("banana_block", () -> new BlockItem(BananaBlocks.BANANA_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BANANA_PICKAXE = ITEMS.register("banana_pickaxe", BananaPickaxeItem::new);
    public static final RegistryObject<Item> BANANA_BUNDLE = ITEMS.register("banana_bundle",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BURNT_BANANA_PICKAXE = ITEMS.register("burnt_banana_pickaxe", BurntBananaPickaxeItem::new);


    public static void register(IEventBus b){
        ITEMS.register(b);
        b.register(BananaItems.class);  //for creativetab event handler
    }

    @SubscribeEvent
    public static void addToCreative(CreativeModeTabEvent.BuildContents event){
        if (event.getTab() == CreativeModeTabs.BUILDING_BLOCKS){
            /* Building Blocks tab */
            event.accept(BANANA_BLOCK_ITEM);
        } else if (event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) {
            /* Food n Drinks Tab */
            event.accept(BANANA);
            event.accept(BANANA_BUNDLE);
        } else if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(BANANA_PICKAXE);
            event.accept(BURNT_BANANA_PICKAXE);
        }
    }
}
