package net.hootisman.bananas;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.block.FlourCauldronBlock;
import net.hootisman.bananas.item.DoughBowlItem;
import net.hootisman.bananas.registry.*;
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

        BananaItems.register(modEventBus);
        BananaBlocks.register(modEventBus);
        BananaLootModifiers.register(modEventBus);
        BananaSounds.register(modEventBus);
        BananaCreativeModeTab.register(modEventBus);
        BananaBlockEntities.register(modEventBus);
        modEventBus.addListener(FlourCauldronBlock::addCauldronInteractions);

        MinecraftForge.EVENT_BUS.addListener(DoughBowlItem::onBowlUse);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
