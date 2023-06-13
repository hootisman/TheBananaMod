package net.hootisman.bananas;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.registry.BananaCreativeModeTab;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.registry.BananaLootModifiers;
import net.hootisman.bananas.registry.BananaSounds;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Arrays;

@Mod(BananaCore.MODID)
public class BananaCore
{
    public static final String MODID = "bananas";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BananaCore()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BananaBlocks.register(modEventBus);
        BananaItems.register(modEventBus);
        BananaLootModifiers.register(modEventBus);
        BananaSounds.register(modEventBus);
        BananaCreativeModeTab.register(modEventBus);


        MinecraftForge.EVENT_BUS.register(this);
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info(Arrays.toString(ArmorMaterials.values()));
    }

}
