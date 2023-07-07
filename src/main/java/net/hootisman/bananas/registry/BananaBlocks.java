package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.block.BananaBlock;
import net.hootisman.bananas.block.BananaMushBlock;
import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.block.FlourCauldronBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BananaBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BananaCore.MODID);
    public static final RegistryObject<Block> BANANA_BLOCK = registerBlockAndItem("banana_block", BananaBlock::new);
    public static final RegistryObject<Block> BANANA_MUSH_BLOCK = BLOCKS.register("banana_mush_block", BananaMushBlock::new);
    public static final RegistryObject<Block> FLOUR_BLOCK = BLOCKS.register("flour_block", FlourBlock::new);
    public static final RegistryObject<Block> FLOUR_CAULDRON = BLOCKS.register("flour_cauldron_block", () -> new FlourCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON), FlourCauldronBlock.FLOUR));
    public static void register (IEventBus b){
        BLOCKS.register(b);
    }
    private static <I extends Block> RegistryObject<Block> registerBlockAndItem(final String name, final Supplier<? extends I> supp){
        RegistryObject<Block> reg_block = BLOCKS.register(name, supp);
        BananaItems.ITEMS.register(name,() -> new BlockItem(reg_block.get(),new Item.Properties()));
        return reg_block;
    }
}
