package net.hootisman.bananas.util;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;

import java.util.HashMap;
import java.util.Map;

public class DoughData {
    private final String[] attributes = {"water","yeast","salt"};
    private int pointsToAllocate;   //points to divide between nutrition and saturation
    private int totalFlour;
    private int nutrition;
    private float saturation;
    private float saturationMod;
    private CompoundTag doughTag;
    private Map<String, BakersPercent> bakersPercentages;
    private Map<String, Float> maxBounds;

    public DoughData(CompoundTag tag) {
        if (DoughUtils.isDoughTag(tag)){
            this.pointsToAllocate = 11;
            this.doughTag = tag;
            this.totalFlour = getTagData("flour");
            initializeBakersPercent(this.totalFlour);
            initializeMaxBounds();
            calculateBreadData();
        }
    }
    public int get(String key){
        return (int) (totalFlour * getBakersPercent(key));
    }
    public float getBakersSum(){
       return (float) bakersPercentages.values().stream().mapToDouble(BakersPercent::get).sum() + 1.0f;
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
    public float getBakersPercent(String key){
        return key.equals("flour") ? 1.0f : bakersPercentages.get(key).get();
    }
    public float getMaxBound(String key){
        return maxBounds.get(key);
    }
    private int getTagData(String key){
        return ((NumericTag)doughTag.get(key)).getAsInt();
    }
    private void initializeMaxBounds() {
        maxBounds = new HashMap<>();
        maxBounds.put("water",0.65f);
        maxBounds.put("yeast",0.25f);
        maxBounds.put("salt",0.06f);
    }
    private void initializeBakersPercent(int flourAmount){
        bakersPercentages = new HashMap<>();
        for (String attribute : attributes){
            bakersPercentages.put(attribute,BakersPercent.of(getTagData(attribute), flourAmount));
        }
    }

    public CompoundTag takeSomeDough(){
        float doughSize = getSize();
        if (DoughUtils.MAX_BREAD_SIZE <= doughSize){
            float ratioForTaking = DoughUtils.MAX_BREAD_SIZE / doughSize;      //ratio for how much flour to take such that bread = 1000g and bakers percentage remains the same
            int takenFlour = (int) (ratioForTaking * totalFlour);
            totalFlour -= takenFlour;       //take flour >:)
            return DoughUtils.saveSpecificContent(0L,takenFlour,
                    (int) (takenFlour * getBakersPercent("water")),
                    (int) (takenFlour * getBakersPercent("yeast")),
                    (int) (takenFlour * getBakersPercent("salt")));
        }
        return null;
    }
    private void calculateBreadData(){
        float ratio = Math.min(getBakersPercent("water") / getMaxBound("water"), 1.0f); //float from 0 - 1
        saturation = (float)Math.round(pointsToAllocate * ratio * 10.0f)/10.0f;
        nutrition = (int) Math.min(pointsToAllocate - saturation + 1,20.0f);
        saturationMod = saturation / (2.0f * nutrition);
    }


    public static class BakersPercent {
        private int amount;
        private int flourAmount;
        private float percent;
        private BakersPercent(int amount, int flourAmount){
            this.amount = amount;
            this.flourAmount = flourAmount;
            this.percent = BakersPercent.set(amount, flourAmount);
        }
        public static float set(int amount, int flourAmount){
            return (float) amount /flourAmount;
        }
        public static BakersPercent of(int amount, int flourAmount){
            return new BakersPercent(amount, flourAmount);
        }
        public float get(){
            return percent;
        }
    }
}
