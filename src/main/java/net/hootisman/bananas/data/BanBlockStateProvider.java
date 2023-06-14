package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.block.BananaMushBlock;
import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BanBlockStateProvider extends BlockStateProvider {
    public BanBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BananaCore.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItemHelper(BananaBlocks.BANANA_BLOCK);
        simpleLayerBlock(BananaBlocks.BANANA_MUSH_BLOCK.get(),"banana_mush_block", BananaMushBlock.BLOCK_HEIGHT);
        flourLayerBlock(BananaBlocks.FLOUR_BLOCK.get());
    }
    private void flourLayerBlock(Block block){
        for (int i = 1; i <= 8; i++){
            getVariantBuilder(block).partialState()
                                    .with(FlourBlock.LAYERS,i)
                                    .setModels(addLayerModel(block,String.format("%s%d","flour_height",i*2),"flour_block",i*2));
        }
    }
    private void simpleLayerBlock(Block block, String blockName ,float height){

        getVariantBuilder(block).partialState().setModels(
                addLayerModel(block,blockName,blockName,height)
        );

    }
    private ConfiguredModel addLayerModel(Block block, String blockName, String textureName, float height){

        return new ConfiguredModel(models().singleTexture(blockName, new ResourceLocation("block/thin_block"), modLoc("block/"+textureName))
                        .texture("particle",modLoc("block/" + textureName))
                        .element()
                        .allFaces((direction, faceBuilder) -> {
                            float midV = (direction == Direction.DOWN || direction == Direction.UP) ? 0.0F : 16.0F - height;
                            faceBuilder.texture("#texture")
                                    .uvs(0.0F,midV,16.0F,16.0F);
                        })
                        .to(16.0f,height,16.0F)
                        .end());
    }
    private void blockWithItemHelper(RegistryObject<Block> blockRegObj){
        simpleBlockWithItem(blockRegObj.get(),cubeAll(blockRegObj.get()));
    }
}
