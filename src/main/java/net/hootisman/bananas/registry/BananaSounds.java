package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BananaSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BananaCore.MODID);
    public static final RegistryObject<SoundEvent> BANANA_MUSH = SOUNDS.register("banana_mush",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BananaCore.MODID,"banana_mush")));

    public static void register(IEventBus b){
        SOUNDS.register(b);
    }
}
