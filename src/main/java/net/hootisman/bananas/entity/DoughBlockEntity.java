package net.hootisman.bananas.entity;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.util.DoughUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DoughBlockEntity extends BlockEntity {
    private long lastTickTime = 0;
    private short flourContent = 0;
    private short waterContent = 0;
    private short yeastContent = 0;
    private byte saltContent = 0;
    public DoughBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BananaBlockEntities.DOUGH_BLOCK_ENTITY.get(), blockPos, blockState);
    }
    public void loadIngredientsContent(CompoundTag tag){
        lastTickTime = tag.getLong("time");
        flourContent = tag.getShort("flour");
        waterContent = tag.getShort("water");
        yeastContent = tag.getShort("yeast");
        saltContent = tag.getByte("salt");
    }
    public CompoundTag saveIngredientsContent(CompoundTag tag){
        tag.putLong("time",lastTickTime);
        tag.putShort("flour",flourContent);
        tag.putShort("water",waterContent);
        tag.putShort("yeast",yeastContent);
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

        if (DoughUtils.hasYeastFermented(level.getGameTime(), entity.lastTickTime)) {
            LogUtils.getLogger().info("test of randomness! new tick till ferment is: " + level.random.nextIntBetweenInclusive(0,DoughUtils.YEAST_TICK));
            doYeastFerment(entity, level, blockPos);
        }

    }

    private static void doYeastFerment(DoughBlockEntity entity, Level level, BlockPos blockPos){
        //what to change entity data every x ticks
        entity.lastTickTime = level.getGameTime();
        if (DoughUtils.canYeastGrow(entity.yeastContent)){
            entity.yeastContent++;
            entity.setChanged();
            ((ServerLevel)level).sendParticles(ParticleTypes.BUBBLE,blockPos.getX() + level.random.nextDouble(),blockPos.getY() + 1.05f,blockPos.getZ() + level.random.nextDouble(),1, 0.0f,0.01f,0.0f,0.0f);
            DoughUtils.playYeastSound(level, blockPos);
        }
    }
}
