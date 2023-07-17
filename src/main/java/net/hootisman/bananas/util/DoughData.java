package net.hootisman.bananas.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;

import java.util.HashMap;
import java.util.Map;

public class DoughData {
    private int pointsToAllocate;   //points to divide between nutrition and saturation
    private int nutrition;
    private float saturation;
    private float saturationMod;
    private CompoundTag doughTag;
    String[] attributes = {"water", "yeast","salt"};
    Map<String, Float> bakersPercentages;
    Map<String, Float> maxBounds;

    public DoughData(CompoundTag tag) {
        if (DoughUtils.isDoughTag(tag)){
            this.pointsToAllocate = 11;
            this.doughTag = tag;
            initializeBakersPercent(getTagData("flour"));
            initializeMaxBounds();
            calculateBreadData();
        }
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
    public int getTagData(String key){
        return ((NumericTag)doughTag.get(key)).getAsInt();
    }
    public float getBakersPercent(String key){
        return bakersPercentages.get(key);
    }
    public float getMaxBound(String key){
        return maxBounds.get(key);
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
            this.percent = BakersPercent.of(amount, flourAmount);
        }
        public static float of(int amount, int flourAmount){
            return (float) amount /flourAmount;
        }
        public static BakersPercent ofNew(int amount, int flourAmount){
            return new BakersPercent(amount, flourAmount);
        }
        public float get(){
            return percent;
        }
    }
}
