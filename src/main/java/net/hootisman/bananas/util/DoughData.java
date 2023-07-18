package net.hootisman.bananas.util;

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
    private int totalFlour;
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
            breadIngredients.put(attribute, new BreadIngredient(0,1));
        }
    }

    // TODO javadoc
    public void loadIngredients(CompoundTag tag){
        if (DoughUtils.isDoughTag(tag)){
            doughTag = tag;
            totalFlour = getTagData("flour");
            updateFlour(this::getTagData);
            calculateBreadData();
        }
    }
    // TODO javadoc
    public void setIngredient(String key, int value){
        breadIngredients.get(key).set(value, totalFlour);
    }
    // TODO javadoc
    public int getUsingPercent(String key){
        return (int) (totalFlour * getBakersPercent(key));
    }
    public int get(String key){
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
    private int getTagData(String key){
        return ((NumericTag)doughTag.get(key)).getAsInt();
    }
    // TODO javadoc
    private void updateFlour(Function<String, Integer> func){
        for (String key : keys){
            setIngredient(key, func.apply(key));
        }
    }
    // TODO javadoc
    public CompoundTag takeSomeDough(){
        float doughSize = getSize();
        if (DoughUtils.MAX_BREAD_SIZE <= doughSize){
            float ratioForTaking = DoughUtils.MAX_BREAD_SIZE / doughSize;      //ratio for how much flour to take such that bread = 1000g and bakers percentage remains the same
            int takenFlour = (int) (ratioForTaking * totalFlour);
            /* update internal values */
            totalFlour -= takenFlour;       //take flour >:)
            updateFlour((key) -> (int) (getBakersPercent(key) * totalFlour));
            /* return new bread tag */
            return DoughUtils.saveSpecificContent(0L,takenFlour,
                    (int) (takenFlour * getBakersPercent("water")),
                    (int) (takenFlour * getBakersPercent("yeast")),
                    (int) (takenFlour * getBakersPercent("salt")));
        }
        return null;
    }
    // TODO javadoc
    private void calculateBreadData(){
        float ratio = Math.min(getBakersPercent("water") / getMaxBound("water"), 1.0f); //float from 0 - 1
        saturation = (float)Math.round(pointsToAllocate * ratio * 10.0f)/10.0f;
        nutrition = (int) Math.min(pointsToAllocate - saturation + 1,20.0f);
        saturationMod = saturation / (2.0f * nutrition);
    }

    // TODO javadoc
    public boolean doYeastFerment(DoughBlockEntity entity){
        if (DoughUtils.canYeastGrow((short) get("yeast"))){
            setIngredient("yeast",get("yeast") + 1);
            entity.setChanged();
            return true;
        }
        return false;
    }

    // TODO javadoc
    public static class BreadIngredient {
        private int amount;
        private int flourAmount;
        private float bakersPercent;
        private BreadIngredient(int amount, int flourAmount){
            set(amount, flourAmount);
        }
        public static float bakersPercent(int amount, int flourAmount){
            return (float) amount /flourAmount;
        }
        public void set(int amount, int flourAmount){
            this.amount = amount;
            this.flourAmount = flourAmount;
            this.bakersPercent = BreadIngredient.bakersPercent(amount, flourAmount);
        }
        public int getAmount(){
            return amount;
        }
        public float getBakersPercent(){
            return bakersPercent;
        }
    }
}
