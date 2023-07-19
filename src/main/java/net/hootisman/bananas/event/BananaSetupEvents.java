package net.hootisman.bananas.event;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.util.CauldronUtils;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BananaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BananaSetupEvents {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){
        //enqueue cauldron interactions
        event.enqueueWork(BananaSetupEvents::doughFillInteractions);
        event.enqueueWork(BananaSetupEvents::doughEmptyInteractions);
        event.enqueueWork(BananaSetupEvents::doughMixInteractions);
    }
    private static void doughFillInteractions(){
        CauldronInteraction.EMPTY.put(BananaItems.FLOUR.get(), CauldronUtils.FILL_USING_FLOUR);
        CauldronInteraction.EMPTY.put(BananaItems.DOUGH_BOWL.get(), CauldronUtils.FILL_USING_DOUGH);
        CauldronUtils.FLOUR_INTERACT.put(BananaItems.FLOUR.get(), CauldronUtils.FILL_USING_FLOUR);
    }
    private static void doughEmptyInteractions(){
        CauldronUtils.FLOUR_INTERACT.put(Items.AIR, CauldronUtils.EMPTY_FLOUR);
        CauldronUtils.DOUGH_INTERACT.put(Items.BOWL, CauldronUtils.EMPTY_DOUGH);
    }
    private static void doughMixInteractions(){
        CauldronInteraction.WATER.put(BananaItems.FLOUR.get(), CauldronUtils.MIX_FLOUR);
        CauldronUtils.FLOUR_INTERACT.put(Items.POTION, CauldronUtils.MIX_WATER);
        CauldronUtils.DOUGH_INTERACT.put(BananaItems.FLOUR.get(), CauldronUtils.MIX_FLOUR);
        CauldronUtils.DOUGH_INTERACT.put(Items.POTION, CauldronUtils.MIX_WATER);
    }
}
