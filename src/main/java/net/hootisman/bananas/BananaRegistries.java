package net.hootisman.bananas;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.blocks.BananaBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

public class BananaRegistries {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BananaCore.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BananaCore.MODID);

    public static final RegistryObject<Block> BANANA_BLOCK = BLOCKS.register("banana_block", BananaBlock::new);
    public static final RegistryObject<Item> BANANA_BLOCK_ITEM = ITEMS.register("banana_block", () -> new BlockItem(BANANA_BLOCK.get(), new Item.Properties()));
    public static void init(IEventBus b){
        b.register(BananaRegistries.class);
        BLOCKS.register(b);
        ITEMS.register(b);

    }

    @SubscribeEvent
    public static void addToCreative(CreativeModeTabEvent.BuildContents event){
        if (event.getTab() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(BANANA_BLOCK_ITEM);
        }
    }
}
