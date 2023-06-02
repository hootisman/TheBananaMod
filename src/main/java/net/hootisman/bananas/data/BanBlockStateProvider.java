package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BanBlockStateProvider extends BlockStateProvider {
    public BanBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BananaCore.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItemHelper(BananaBlocks.BANANA_BLOCK);
    }

    private void blockWithItemHelper(RegistryObject<Block> blockRegObj){
        simpleBlockWithItem(blockRegObj.get(),cubeAll(blockRegObj.get()));
    }
}
