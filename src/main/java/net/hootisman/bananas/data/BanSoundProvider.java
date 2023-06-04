package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class BanSoundProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    protected BanSoundProvider(PackOutput output,  ExistingFileHelper helper) {
        super(output, BananaCore.MODID, helper);
    }

    @Override
    public void registerSounds() {

        add(BananaSounds.BANANA_MUSH.get().getLocation().getPath(),definition()
                .subtitle("sound." + BananaCore.MODID + "." +  BananaSounds.BANANA_MUSH.get().getLocation().getPath())
                .with(
                        sound(BananaSounds.BANANA_MUSH.get().getLocation())
                ));
    }
}
