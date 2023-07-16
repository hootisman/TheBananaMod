package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.entity.DoughBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BananaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BananaCore.MODID);
    public static final RegistryObject<BlockEntityType<DoughBlockEntity>> DOUGH_BLOCK_ENTITY = BLOCK_ENTITIES.register("dough_block",() -> BlockEntityType.Builder.of(DoughBlockEntity::new,BananaBlocks.DOUGH_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<DoughBlockEntity>> DOUGH_CAULDRON_ENTITY = BLOCK_ENTITIES.register("dough_cauldron_block",() -> BlockEntityType.Builder.of(DoughBlockEntity::new,BananaBlocks.DOUGH_CAULDRON.get()).build(null));
    public static void register(IEventBus b){
        BLOCK_ENTITIES.register(b);
    }
}
