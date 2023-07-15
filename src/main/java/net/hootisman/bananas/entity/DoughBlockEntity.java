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
    public static final short YEAST_TICK = 200;      //after x amount of ticks when to increment yeast
    private long lastTickTime = 0;
    private short flourContent = 0;
    private short waterContent = 0;
    private byte yeastContent = 0;
    private byte saltContent = 0;
    public DoughBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BananaBlockEntities.DOUGH_BLOCK_ENTITY.get(), blockPos, blockState);
    }
    public void loadIngredientsContent(CompoundTag tag){
        lastTickTime = tag.getLong("time");
        flourContent = tag.getShort("flour");
        waterContent = tag.getShort("water");
        yeastContent = tag.getByte("yeast");
        saltContent = tag.getByte("salt");
    }
    public CompoundTag saveIngredientsContent(CompoundTag tag){
        tag.putLong("time",lastTickTime);
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

        if (hasYeastFermented(level.getGameTime(), entity.lastTickTime)) {
            doYeastFerment(entity, level, blockPos);
        }

    }

    public static boolean hasYeastFermented(long currentTickTime, long lastTickTime){
        return currentTickTime - lastTickTime >= YEAST_TICK;
    }


    private static void doYeastFerment(DoughBlockEntity entity, Level level, BlockPos blockPos){
        //what to change entity data every x ticks
        entity.lastTickTime = level.getGameTime();
        if (entity.yeastContent < Byte.MAX_VALUE){
            entity.yeastContent++;
            entity.setChanged();
            level.playSound(null,blockPos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS);
        }
    }
}
