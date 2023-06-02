package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class BanLangProviderENUS extends LanguageProvider {
    public BanLangProviderENUS(PackOutput output, String locale) {
        super(output, BananaCore.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        //items
        add(BananaItems.BANANA.get(),"Banana");
        add(BananaItems.BANANA_BUNDLE.get(),"Banana Bundle");
        add(BananaItems.BANANA_PICKAXE.get(),"Banana Pickaxe");
        add(BananaItems.BURNT_BANANA_PICKAXE.get(),"Burnt Banana Pickaxe");

        //blocks
        add(BananaBlocks.BANANA_BLOCK.get(),"Banana Block");
    }
}
