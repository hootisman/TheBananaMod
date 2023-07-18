package net.hootisman.bananas.entity;

import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.util.DoughData;
import net.hootisman.bananas.util.DoughUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DoughBlockEntity extends BlockEntity {
    private DoughData data;
    private long lastTickTime = 0;
    public DoughBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(getDoughEntityType(blockState),blockPos,blockState);
        data = new DoughData();
    }
    public void loadDoughContent(CompoundTag tag){
        lastTickTime = tag.getLong("time");
        data.loadIngredients(tag);
    }
    public CompoundTag saveDoughContent(CompoundTag tag){
        tag.putLong("time",lastTickTime);
        tag.putShort("flour", (short) data.get("flour"));
        tag.putShort("water", (short) data.get("water"));
        tag.putShort("yeast", (short) data.get("yeast"));
        tag.putByte("salt", (byte) data.get("salt"));

        return tag;
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(saveDoughContent(nbt));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        loadDoughContent(nbt);
    }

    public static BlockEntityType<DoughBlockEntity> getDoughEntityType(BlockState doughState){
        BlockEntityType<DoughBlockEntity> doughEntityType = BananaBlockEntities.DOUGH_BLOCK_ENTITY.get();   //default
        if (doughState.is(BananaBlocks.DOUGH_CAULDRON.get())){
            doughEntityType = BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get();
        }
        return doughEntityType;
    }
    public static void tick(Level level, BlockPos blockPos, BlockState blockState, DoughBlockEntity entity) {
        if (level.isClientSide()) return;

        if (DoughUtils.hasYeastFermented(level.getGameTime(), entity.lastTickTime) && entity.data.doYeastFerment(entity)) {
//            LogUtils.getLogger().info("test of randomness! new tick till ferment is: " + level.random.nextIntBetweenInclusive(0,DoughUtils.YEAST_TICK));

            entity.lastTickTime = level.getGameTime();
            DoughUtils.playSoundHelper(level, blockPos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP);
            DoughUtils.spawnParticlesHelper(ParticleTypes.BUBBLE,(ServerLevel) level,
                    new Vec3(blockPos.getX() + level.random.nextDouble(),blockPos.getY() + 1.05f,blockPos.getZ() + level.random.nextDouble()),
                    1,
                    new Vec3(0.0f,0.01f,0.0f),
                    0.0f);
        }

    }

}
