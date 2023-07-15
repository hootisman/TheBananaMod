package net.hootisman.bananas.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class DoughUtils {
    public static final int YEAST_TICK = 200;      //after x amount of ticks, ferment yeast once

    public static boolean canYeastGrow(short yeast){
        return yeast < Short.MAX_VALUE;
    }
    public static boolean hasYeastFermented(long currentTickTime, long lastTickTime){
        return currentTickTime - lastTickTime >= YEAST_TICK;
    }

    public static void playYeastSound(Level level, BlockPos blockPos){
        level.playSound(null,blockPos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS);
    }
}