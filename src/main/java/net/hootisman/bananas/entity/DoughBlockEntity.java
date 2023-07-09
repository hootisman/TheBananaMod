package net.hootisman.bananas.entity;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DoughBlockEntity extends BlockEntity {
    private byte yeastContent = 0;
    private short ticks = 0;
    public DoughBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BananaBlockEntities.DOUGH_BLOCK_ENTITY.get(), blockPos, blockState);
    }
    public byte getYeastContent(){
        return yeastContent;
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putByte("yeast",yeastContent);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        yeastContent = nbt.getByte("yeast");
    }


    public static void tick(Level level, BlockPos blockPos, BlockState blockState, DoughBlockEntity entity) {
        if (level.isClientSide()) return;

        entity.ticks++;
        if (entity.ticks >= 200) {
            entity.ticks = 0;
            yeastTick(entity);
//            LogUtils.getLogger().info("yeast: " + entity.yeastContent);
        }

    }

    private static void yeastTick(DoughBlockEntity entity){
        //what to change entity data every x ticks
        entity.yeastContent += entity.yeastContent < Byte.MAX_VALUE ? 1 : 0;
        entity.setChanged();
    }
}
