package net.hootisman.bananas.event;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.util.CauldronUtils;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = BananaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BananaEvents {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){
        //enqueue cauldron interactions
        event.enqueueWork(() -> CauldronUtils.FLOUR_INTERACT.put(BananaItems.FLOUR.get(), CauldronUtils.FILL_USING_FLOUR));
        event.enqueueWork(() -> CauldronInteraction.EMPTY.put(BananaItems.FLOUR.get(), CauldronUtils.FILL_USING_FLOUR));
    }
}