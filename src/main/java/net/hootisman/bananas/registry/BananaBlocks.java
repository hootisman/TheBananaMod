package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.block.BananaBlock;
import net.hootisman.bananas.block.BananaMushBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BananaBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BananaCore.MODID);
    public static final RegistryObject<Block> BANANA_BLOCK = BLOCKS.register("banana_block", BananaBlock::new);
    public static final RegistryObject<Block> BANANA_MUSH_BLOCK = BLOCKS.register("banana_mush_block", BananaMushBlock::new);
    public static void register (IEventBus b){
        BLOCKS.register(b);
    }
}
