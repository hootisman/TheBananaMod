package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.data.loot_tables.BanLootTableProvider;
import net.hootisman.bananas.data.recipes.BanRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BananaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeClient(), new BanBlockStateProvider(output,helper));
        gen.addProvider(event.includeClient(), new BanItemModelProvider(output,helper));
        gen.addProvider(event.includeClient(), new BanLangProviderENUS(output,"en_us"));
        gen.addProvider(event.includeClient(), new BanSoundProvider(output,helper));

        gen.addProvider(event.includeServer(), new BanBlockTagProvider(output,lookupProvider,helper));
        gen.addProvider(event.includeServer(), BanLootTableProvider.create(output));
        gen.addProvider(event.includeServer(), new BanLootModifierProvider(output));
        gen.addProvider(event.includeServer(), new BanRecipeProvider(output));
    }
}
