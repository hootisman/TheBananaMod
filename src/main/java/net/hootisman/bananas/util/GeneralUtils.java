package net.hootisman.bananas.util;

public class GeneralUtils {
    public static float fortuneEquation(float fortuneLevel){
        return (1 / (fortuneLevel + 2)) + ((fortuneLevel + 1) / 2);
    }

    /**
     * use with {@link net.minecraft.util.RandomSource#nextInt(int)}
     * @param dropChance drop chance in percent decimal form ex: 0.5% = 0.005
     * @return int
     */
    public static int convertChanceToInt(float dropChance){
        return (int) (1 / dropChance);
    }
}
