package net.hootisman.bananas.event;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.util.CauldronUtils;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BananaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BananaSetupEvents {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){
        //enqueue cauldron interactions
        event.enqueueWork(() -> CauldronUtils.FLOUR_INTERACT.put(Items.AIR, CauldronUtils.EMPTY_FLOUR));
        event.enqueueWork(() -> CauldronUtils.FLOUR_INTERACT.put(BananaItems.FLOUR.get(), CauldronUtils.FILL_USING_FLOUR));
        event.enqueueWork(() -> CauldronUtils.FLOUR_INTERACT.put(Items.POTION, CauldronUtils.MIX_WATER));
        event.enqueueWork(() -> CauldronInteraction.EMPTY.put(BananaItems.FLOUR.get(), CauldronUtils.FILL_USING_FLOUR));
        event.enqueueWork(() -> CauldronInteraction.EMPTY.put(BananaItems.DOUGH_BOWL.get(), CauldronUtils.FILL_USING_DOUGH));
        event.enqueueWork(() -> CauldronUtils.DOUGH_INTERACT.put(Items.BOWL, CauldronUtils.EMPTY_DOUGH));
        event.enqueueWork(() -> CauldronUtils.DOUGH_INTERACT.put(Items.NETHERITE_AXE, CauldronUtils.HARVEST_DOUGH));
        event.enqueueWork(() -> CauldronUtils.DOUGH_INTERACT.put(Items.DIAMOND_AXE, CauldronUtils.HARVEST_DOUGH));
        event.enqueueWork(() -> CauldronUtils.DOUGH_INTERACT.put(Items.GOLDEN_AXE, CauldronUtils.HARVEST_DOUGH));
        event.enqueueWork(() -> CauldronUtils.DOUGH_INTERACT.put(Items.IRON_AXE, CauldronUtils.HARVEST_DOUGH));
        event.enqueueWork(() -> CauldronUtils.DOUGH_INTERACT.put(Items.STONE_AXE, CauldronUtils.HARVEST_DOUGH));
        event.enqueueWork(() -> CauldronUtils.DOUGH_INTERACT.put(Items.WOODEN_AXE, CauldronUtils.HARVEST_DOUGH));

    }
}