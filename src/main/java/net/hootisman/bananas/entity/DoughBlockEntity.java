package net.hootisman.bananas.entity;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DoughBlockEntity extends BlockEntity {
    private static final short YEAST_TICK = 200;      //after x amount of ticks when to increment yeast
    private short flourContent = 0;
    private short waterContent = 0;
    private byte yeastContent = 0;
    private byte saltContent = 0;

    private short ticks = 0;
    public DoughBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BananaBlockEntities.DOUGH_BLOCK_ENTITY.get(), blockPos, blockState);
    }
    public void loadIngredientsContent(CompoundTag tag){
        flourContent = tag.getShort("flour");
        waterContent = tag.getShort("water");
        yeastContent = tag.getByte("yeast");
        saltContent = tag.getByte("salt");
    }
    public CompoundTag saveIngredientsContent(CompoundTag tag){
        tag.putShort("flour",flourContent);
        tag.putShort("water",waterContent);
        tag.putByte("yeast",yeastContent);
        tag.putByte("salt",saltContent);

        return tag;
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(saveIngredientsContent(nbt));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        loadIngredientsContent(nbt);
    }


    public static void tick(Level level, BlockPos blockPos, BlockState blockState, DoughBlockEntity entity) {
        if (level.isClientSide()) return;

        entity.ticks++;
        if (entity.ticks >= YEAST_TICK) {
            entity.ticks = 0;
            if(yeastTick(entity)){
                level.playSound(null,blockPos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS);
            }
            LogUtils.getLogger().info("yeast: " + entity.yeastContent);
        }

    }

    private static boolean yeastTick(DoughBlockEntity entity){
        //what to change entity data every x ticks
        if (entity.yeastContent < Byte.MAX_VALUE){
            entity.yeastContent++;
            entity.setChanged();
            return true;
        }
        return false;
    }
}
