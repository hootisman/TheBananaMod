package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.block.BananaMushBlock;
import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.block.FlourCauldronBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
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
        flourCauldronBlock(BananaBlocks.FLOUR_CAULDRON.get());
        simpleBlock(BananaBlocks.DOUGH_BLOCK.get());
    }
    private void flourCauldronBlock(Block block){
        int height = 3;
        for(int i = 1; i <= 8; i++){
            height += 1 + (i % 2);
            getVariantBuilder(block).partialState()
                    .with(FlourCauldronBlock.LEVEL,i)
                    .setModels(cauldronBlock(models()
                            .withExistingParent(String.format("%s%d","flour_cauldron_height",height),mcLoc("block/cauldron"))
                            .texture("content",modLoc("block/flour_block"))
                            .element().face(Direction.UP).tintindex(0).texture("#content").end().from(2,4,2).to(14,height,14).end() ));
        }
    }
    private void flourLayerBlock(Block block){
        for(int i = 1; i <= 8; i++){
            getVariantBuilder(block).partialState()
                    .with(FlourBlock.LAYERS,i)
                    .setModels(addLayerModel(new ResourceLocation("block/thin_block"),String.format("%s%d","flour_height",i*2), modLoc("block/flour_block"),i*2));
        }
    }
    private void simpleLayerBlock(Block block, String blockName ,float height){

        getVariantBuilder(block).partialState().setModels(
                addLayerModel(new ResourceLocation("block/thin_block"),blockName, modLoc("block/flour_block"),height)
        );

    }
    private ConfiguredModel cauldronBlock(BlockModelBuilder content){
        //i've commited a sin here
        String side = "#side", top = "#top", inside = "#inside", bottom = "#bottom";

        return new ConfiguredModel(content
                .texture("particle",mcLoc("block/cauldron_side"))
                .texture("side",mcLoc("block/cauldron_side"))
                .texture("top",mcLoc("block/cauldron_top"))
                .texture("bottom",mcLoc("block/cauldron_bottom"))
                .texture("inside",mcLoc("block/cauldron_inner"))
                .element().face(Direction.NORTH).texture(side).cullface(Direction.NORTH).end()
                .face(Direction.EAST).texture(side).end()
                .face(Direction.SOUTH).texture(side).cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture(side).cullface(Direction.WEST).end()
                .face(Direction.UP).texture(top).cullface(Direction.UP).end()
                .face(Direction.DOWN).texture(inside).end()
                .from(0,3,0).to(2,16,16).end()
                .element()
                .face(Direction.UP).texture(inside).end()
                .face(Direction.DOWN).texture(inside).end()
                .from(2,3,2).to(14,4,14).end()
                .element().face(Direction.NORTH).texture(side).cullface(Direction.NORTH).end()
                .face(Direction.EAST).texture(side).cullface(Direction.EAST).end()
                .face(Direction.SOUTH).texture(side).cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture(side).end()
                .face(Direction.UP).texture(top).cullface(Direction.UP).end()
                .face(Direction.DOWN).texture(inside).end()
                .from(14,3,0).to(16,16,16).end()
                .element().face(Direction.NORTH).texture(side).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture(side).end()
                .face(Direction.UP).texture(top).cullface(Direction.UP).end()
                .face(Direction.DOWN).texture(inside).end()
                .from(2,3,0).to(14,16,2).end()
                .element().face(Direction.NORTH).texture(side).end()
                .face(Direction.SOUTH).texture(side).cullface(Direction.SOUTH).end()
                .face(Direction.UP).texture(top).cullface(Direction.UP).end()
                .face(Direction.DOWN).texture(inside).end()
                .from(2,3,14).to(14,16,16).end()
                .element().face(Direction.NORTH).texture(side).cullface(Direction.NORTH).end()
                .face(Direction.EAST).texture(side).end()
                .face(Direction.SOUTH).texture(side).end()
                .face(Direction.WEST).texture(side).cullface(Direction.WEST).end()
                .face(Direction.DOWN).texture(bottom).cullface(Direction.DOWN).end()
                .from(0,0,0).to(4,3,2).end()
                .element().face(Direction.EAST).texture(side).end()
                .face(Direction.SOUTH).texture(side).end()
                .face(Direction.WEST).texture(side).cullface(Direction.WEST).end()
                .face(Direction.DOWN).texture(bottom).cullface(Direction.DOWN).end()
                .from(0,0,2).to(2,3,4).end()
                .element().face(Direction.NORTH).texture(side).cullface(Direction.NORTH).end()
                .face(Direction.EAST).texture(side).cullface(Direction.EAST).end()
                .face(Direction.SOUTH).texture(side).end()
                .face(Direction.WEST).texture(side).end()
                .face(Direction.DOWN).texture(bottom).cullface(Direction.DOWN).end()
                .from(12,0,0).to(16,3,2).end()
                .element().face(Direction.EAST).texture(side).cullface(Direction.EAST).end()
                .face(Direction.SOUTH).texture(side).end()
                .face(Direction.WEST).texture(side).end()
                .face(Direction.DOWN).texture(bottom).cullface(Direction.DOWN).end()
                .from(14,0,2).to(16,3,4).end()
                .element().face(Direction.NORTH).texture(side).end()
                .face(Direction.EAST).texture(side).end()
                .face(Direction.SOUTH).texture(side).cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture(side).cullface(Direction.WEST).end()
                .face(Direction.DOWN).texture(bottom).cullface(Direction.DOWN).end()
                .from(0,0,14).to(4,3,16).end()
                .element().face(Direction.NORTH).texture(side).end()
                .face(Direction.EAST).texture(side).end()
                .face(Direction.WEST).texture(side).cullface(Direction.WEST).end()
                .face(Direction.DOWN).texture(inside).cullface(Direction.DOWN).end()
                .from(0,0,12).to(2,3,14).end()
                .element().face(Direction.NORTH).texture(side).end()
                .face(Direction.EAST).texture(side).cullface(Direction.EAST).end()
                .face(Direction.SOUTH).texture(side).cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture(side).end()
                .face(Direction.DOWN).texture(bottom).cullface(Direction.DOWN).end()
                .from(12,0,14).to(16,3,16).end()
                .element().face(Direction.NORTH).texture(side).end()
                .face(Direction.EAST).texture(side).cullface(Direction.EAST).end()
                .face(Direction.WEST).texture(side).end()
                .face(Direction.DOWN).texture(bottom).cullface(Direction.DOWN).end()
                .from(14,0,12).to(16,3,14).end()
        );
    }
    private ConfiguredModel addLayerModel(ResourceLocation parent, String blockName, ResourceLocation textureLoc, float height){

        return new ConfiguredModel(models().singleTexture(blockName, parent, textureLoc)
                .texture("particle", textureLoc)
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
