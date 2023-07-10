package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BanItemModelProvider extends ItemModelProvider {
    public BanItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BananaCore.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(BananaItems.BANANA);
        simpleItem(BananaItems.BANANA_BUNDLE);
        simpleItem(BananaItems.BANANA_BOOTS);
        simpleItem(BananaItems.FLOUR);
        simpleItem(BananaItems.DOUGH_BOWL);
        handheldItem(BananaItems.BANANA_PICKAXE);
        handheldItem(BananaItems.BURNT_BANANA_PICKAXE);

    }

    private void simpleItem(RegistryObject<Item> item){
        withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(BananaCore.MODID, "item/" + item.getId().getPath()));
    }
    private void handheldItem(RegistryObject<Item> item){
        withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld"))
                .texture("layer0", new ResourceLocation(BananaCore.MODID, "item/" + item.getId().getPath()));
    }
}
