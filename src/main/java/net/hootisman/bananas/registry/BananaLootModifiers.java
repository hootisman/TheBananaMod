package net.hootisman.bananas.registry;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.loot.BananaLeavesLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BananaLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BananaCore.MODID);
    public static final RegistryObject<Codec<BananaLeavesLootModifier>> BANANA_LEAVES_LOOT_MODIFIER = LOOT_MODIFIERS.register("banana_glm",() -> BananaLeavesLootModifier.CODEC);

    public static void register(IEventBus b){
        LOOT_MODIFIERS.register(b);
    }

}
