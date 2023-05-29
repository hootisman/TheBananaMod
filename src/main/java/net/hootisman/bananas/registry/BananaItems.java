package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
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

    public static final RegistryObject<Item> BANANA_BLOCK_ITEM = ITEMS.register("banana_block", () -> new BlockItem(BananaBlocks.BANANA_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus b){
        ITEMS.register(b);
        b.register(BananaItems.class);  //for creativetab event handler
    }

    @SubscribeEvent
    public static void addToCreative(CreativeModeTabEvent.BuildContents event){
        if (event.getTab() == CreativeModeTabs.BUILDING_BLOCKS){
            /* ADDING ITEMS TO CREATIVE TAB */
            event.accept(BANANA_BLOCK_ITEM);
        }
    }
}
