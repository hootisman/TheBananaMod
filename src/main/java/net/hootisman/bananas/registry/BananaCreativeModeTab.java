package net.hootisman.bananas.registry;

import net.hootisman.bananas.BananaCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BananaCreativeModeTab {

    public static final String tabName = "itemGroup." + BananaCore.MODID + ".creativetab" ;

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,BananaCore.MODID);


    public static final RegistryObject<CreativeModeTab> BANANA_CREATIVE_TAB = CREATIVE_TABS.register("bananas_tab",() -> {
        return CreativeModeTab.builder().title(Component.translatable(tabName))
                .icon(() -> new ItemStack(BananaItems.BANANA.get()))
                .displayItems((flags,output) -> {
                    /* blocks */
                    output.accept(new ItemStack(BananaBlocks.BANANA_BLOCK.get()));
                    /* items */
                    output.accept(new ItemStack(BananaItems.BANANA.get()));
                    output.accept(new ItemStack(BananaItems.BANANA_BUNDLE.get()));
                    output.accept(new ItemStack(BananaItems.BANANA_PICKAXE.get()));
                    output.accept(new ItemStack(BananaItems.BURNT_BANANA_PICKAXE.get()));
                    output.accept(new ItemStack(BananaItems.BANANA_BOOTS.get()));
                    output.accept(new ItemStack(BananaItems.FLOUR.get()));
                    output.accept(new ItemStack(BananaItems.DOUGH_BOWL.get()));
                }).build();
    });


    public static void register(IEventBus b){
        CREATIVE_TABS.register(b);
    }
}
