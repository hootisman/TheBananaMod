package net.hootisman.bananas;

import net.hootisman.bananas.blocks.BananaBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BananaRegistries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BananaCore.MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BananaCore.MODID);

    public static void init(IEventBus b){

        BLOCKS.register(b);
        ITEMS.register(b);

        RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
        RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));
        RegistryObject<Block> BANANA_BLOCK = BLOCKS.register("banana_block", BananaBlock::new);
        RegistryObject<Item> BANANA_BLOCK_ITEM = ITEMS.register("banana_block", () -> new BlockItem(BANANA_BLOCK.get(), new Item.Properties()));
    }
}
