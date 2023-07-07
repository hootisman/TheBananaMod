package net.hootisman.bananas.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BanTagProvider extends TagsProvider {
    protected BanTagProvider(PackOutput p_256596_, ResourceKey p_255886_, CompletableFuture p_256513_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256596_, p_255886_, p_256513_, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {

    }
}
