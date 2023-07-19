package net.hootisman.bananas.util;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.entity.DoughBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DoughData {
    private final String[] keys = {"water","yeast","salt"};
    // TODO javadoc
    private final Map<String, BreadIngredient> breadIngredients;
    // TODO javadoc
    private final Map<String, Float> maxBounds;
    // TODO javadoc
    private int pointsToAllocate;   //points to divide between nutrition and saturation
    // TODO javadoc
    private short totalFlour;
    // TODO javadoc
    private int nutrition;
    // TODO javadoc
    private float saturation;
    // TODO javadoc
    private float saturationMod;
    // TODO javadoc
    private CompoundTag doughTag;

    public DoughData() {
        pointsToAllocate = 11;
        maxBounds = new HashMap<>();
        maxBounds.put("water",0.65f);
        maxBounds.put("yeast",0.25f);
        maxBounds.put("salt",0.06f);

        breadIngredients = new HashMap<>();
        for (String attribute : keys){
            breadIngredients.put(attribute, new BreadIngredient((short) 0,(short)1));
        }
    }

    // TODO javadoc
    public void loadIngredients(CompoundTag tag){
        if (DoughUtils.isDoughTag(tag)){
            doughTag = tag;
            totalFlour = getTagData("flour");
            updateFlour(this::getTagData);
//            calculateBreadData();
        }
    }
    // TODO javadoc
    public void setIngredient(String key, short value){
        breadIngredients.get(key).set(value, totalFlour);
    }
    // TODO javadoc
    public short getUsingPercent(String key){
        return (short) (totalFlour * getBakersPercent(key));
    }
    public short get(String key){
        return key.equals("flour") ? totalFlour : breadIngredients.get(key).getAmount();
    }
    public float getSize(){
        return getBakersSum() * totalFlour;
    }
    public int getNutrition() {
        return nutrition;
    }
    public float getSaturation() {
        return saturation;
    }
    public float getSaturationMod() {
        return saturationMod;
    }
    public float getBakersSum(){
        return (float) breadIngredients.values().stream().mapToDouble(BreadIngredient::getBakersPercent).sum() + 1.0f;
    }
    public float getBakersPercent(String key){
        return key.equals("flour") ? 1.0f : breadIngredients.get(key).getBakersPercent();
    }
    public float getMaxBound(String key){
        return maxBounds.get(key);
    }
    private short getTagData(String key){
        return ((NumericTag)doughTag.get(key)).getAsShort();
    }
    // TODO javadoc
    private void updateFlour(Function<String, Short> func){
        for (String key : keys){
            setIngredient(key, func.apply(key));
        }
    }
    // TODO javadoc
    public CompoundTag takeSomeDough(){
        float doughSize = getSize();
        if (DoughUtils.MAX_BREAD_SIZE <= doughSize){
            float ratioForTaking = DoughUtils.MAX_BREAD_SIZE / doughSize;      //ratio for how much flour to take such that bread = 1000g and bakers percentage remains the same
            float takenFlourF = ratioForTaking * totalFlour;
            short takenFlour = (short) (Math.round(takenFlourF));
            /* bread tag for raw_bread */
            CompoundTag breadTag = DoughUtils.saveSpecificContent(0L,takenFlour,
                    Math.round(takenFlourF * getBakersPercent("water")),
                    Math.round(takenFlourF * getBakersPercent("yeast")),
                    Math.round(takenFlourF * getBakersPercent("salt")));
            /* update internal values */
            totalFlour -= takenFlour;       //take flour >:)
            updateFlour((key) -> (short) (Math.round(getBakersPercent(key) * totalFlour)));
            return breadTag;
        }
        return null;
    }
    // TODO javadoc
    public CompoundTag calculateBreadData(){
        float ratio = Math.min(getBakersPercent("water") / getMaxBound("water"), 1.0f); //float from 0 - 1
        saturation = (float)Math.round(pointsToAllocate * ratio * 10.0f)/10.0f;
        nutrition = (int) Math.min(pointsToAllocate - saturation + 1,20.0f);
        saturationMod = saturation / (2.0f * nutrition);

        CompoundTag eatTag = new CompoundTag();
        eatTag.putInt("nutrition",nutrition);
        eatTag.putFloat("saturationMod",saturationMod);
        return eatTag;
    }

    // TODO javadoc
    public boolean doYeastFerment(DoughBlockEntity entity){
        if (DoughUtils.canYeastGrow(get("yeast"))){
            setIngredient("yeast", (short) (get("yeast") + 1));
            entity.setChanged();
            return true;
        }
        return false;
    }
    // TODO javadoc
    public static class BreadIngredient {
        private short amount;
        private short flourAmount;
        private float bakersPercent;
        private BreadIngredient(short amount, short flourAmount){
            set(amount, flourAmount);
        }
        public static float bakersPercent(short amount, short flourAmount){
            return (float) amount /flourAmount;
        }
        public void set(short amount, short flourAmount){
            this.amount = amount;
            this.flourAmount = flourAmount;
            this.bakersPercent = BreadIngredient.bakersPercent(amount, flourAmount);
        }
        public short getAmount(){
            return amount;
        }
        public float getBakersPercent(){
            return bakersPercent;
        }
    }
}
