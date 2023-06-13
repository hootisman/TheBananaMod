package net.hootisman.bananas.misc;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BananaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BananaCreativeModeTab {

    private static final String tabSuffix = "creativetab";
    public static final String tabName = "itemGroup." + BananaCore.MODID + "." + tabSuffix;

    @SubscribeEvent
    public static void onCreativeTabCreation(CreativeModeTabEvent.Register event){
        event.registerCreativeModeTab(new ResourceLocation(BananaCore.MODID, tabSuffix), builder -> {
            builder.title(Component.translatable(tabName))
                    .icon(() -> new ItemStack(BananaItems.BANANA.get()))
                    .displayItems((params,output) -> {
                        output.accept(new ItemStack(BananaItems.BANANA.get()));
                        output.accept(new ItemStack(BananaItems.BANANA_BUNDLE.get()));
                        output.accept(new ItemStack(BananaItems.BANANA_BLOCK_ITEM.get()));
                        output.accept(new ItemStack(BananaItems.BANANA_PICKAXE.get()));
                        output.accept(new ItemStack(BananaItems.BURNT_BANANA_PICKAXE.get()));
                        output.accept(new ItemStack(BananaItems.BANANA_BOOTS.get()));
                    });
        });
    }
}
