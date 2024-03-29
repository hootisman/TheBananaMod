package net.hootisman.bananas.block;

import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class BananaBlock extends Block {
    public BananaBlock(){
//        super(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_YELLOW).strength(0.6F).sound(SoundType.SLIME_BLOCK));
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.6F).sound(SoundType.SLIME_BLOCK));
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float distance) {
        if (!level.isClientSide() && entity instanceof LivingEntity ){
            entity.causeFallDamage(distance,0.0F,level.damageSources().fall());

            if (isEntityTakingDamage((LivingEntity) entity, distance)){
                level.setBlock(blockPos, BananaBlocks.BANANA_MUSH_BLOCK.get().defaultBlockState(), 3);
                level.playSound(null,blockPos,BananaSounds.BANANA_MUSH.get(), SoundSource.BLOCKS);
            }
        }


    }

    public static boolean isEntityTakingDamage(LivingEntity entity, float distance){

        MobEffectInstance mobeffectinstance = ((LivingEntity)entity).getEffect(MobEffects.JUMP);
        float f = mobeffectinstance == null ? 0.0F : (float)(mobeffectinstance.getAmplifier() + 1);
        return Mth.ceil(distance - 3.0F - f) > 0;
    }
}
