package net.hootisman.bananas.util;

import net.hootisman.bananas.block.DoughCauldronBlock;
import net.hootisman.bananas.block.entity.DoughBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DoughData {
    private final String[] keys = {"water","yeast","salt"};
    /**
     * Map of all ingredients corresponding to {@link #keys}
     */
    private final Map<String, BreadIngredient> breadIngredients;
    /**
     * once a {@link BreadIngredient} bakers percentage >= its max bound, will give its max attribute;  corresponds to {@link #keys}; <br>
     * Example: if {@link BreadIngredient#bakersPercent} of water >= {@link #maxBounds}.get("water"), then bread will have max saturation
     */
    private static final Map<String, Float> maxBounds;
    static {
        maxBounds = new HashMap<>();
        maxBounds.put("water",0.65f);
        maxBounds.put("yeast",0.25f);
        maxBounds.put("salt",0.06f);
    }
    // TODO javadoc
    private int pointsToAllocate;   //points to divide between nutrition and saturation
    private DoughBlockEntity blockEntity;
    // TODO javadoc
    private short totalFlour;
    //TODO javadoc
    private int doughSize;
    // TODO javadoc
    private int nutrition;
    // TODO javadoc
    private float saturation;
    // TODO javadoc
    private float saturationMod;
    // TODO javadoc
    private CompoundTag doughTag;

    public DoughData(DoughBlockEntity entity) {
        pointsToAllocate = 11;
        blockEntity = entity;
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
            applyToIngredients(this::getTagData);
            updateDoughSize();
//            calculateBreadData();
        }
    }
    public int calculateBlockStateLayer(){
        return doughSize / 1000;
    }
    // TODO javadoc
    public void setIngredient(String key, short value){
        breadIngredients.get(key).set(value, totalFlour);
    }
    public short get(String key){
        return key.equals("flour") ? totalFlour : breadIngredients.get(key).getAmount();
    }
    public int getSize(){
        return doughSize;
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
        return key.equals("flour") ? 1.0f : breadIngredients.get(key).getBakersPercent();
    }
    public float getMaxBound(String key){
        return maxBounds.get(key);
    }
    private short getTagData(String key){
        return ((NumericTag)doughTag.get(key)).getAsShort();
    }
    public void updateDoughSize(){
        doughSize = totalFlour;
        for (String key : keys){
            doughSize += get(key);
        }
    }
    public void increaseWater(int addWater){
        setIngredient("water", (short) (get("water") + addWater));
        updateDoughSize();
        blockEntity.updateBlockState();
    }
    /**
     * No check for get("water") - subWater > 0, be wary
     * @param subWater
     */
    public void decreaseWater(int subWater){
        increaseWater(-subWater);
    }
    public void increaseFlour(int addFlour){
        totalFlour += addFlour;
        updateDoughSize();
        blockEntity.updateBlockState();
    }
    /**
     * No check for {@link #totalFlour} - subFlour > 0, be wary
     * @param subFlour
     */
    public void decreaseFlour(int subFlour){
        increaseFlour(-subFlour);
    }
    public void harvestDough(int subFlour){
        decreaseFlour(subFlour);
        applyToIngredients((key) -> (short) (Math.round(getBakersPercent(key) * totalFlour)));
    }

    /**
     * For every {@link BreadIngredient}, set value to func return value
     * @param func function to apply ingredient key to
     */
    private void applyToIngredients(Function<String, Short> func){
        for (String key : keys){
            setIngredient(key, func.apply(key));
        }
    }
    /**
     * Used to take one breads worth of ingredients from dough.
     * <p>
     *     <b>ratioForTaking</b> = max bread size / current dough size; ratio for how much flour to take
     *     <b>takenFlourF</b> = ratioForTaking * totalFlour;
     *     <b>takenFlour</b> = round(takenFlourF); int version of takenFlourF
     * </p>
     * @return tag containing dough data
     */
    public CompoundTag takeSomeDough(){
        updateDoughSize();
        if (DoughUtils.MAX_BREAD_SIZE <= doughSize){
            float ratioForTaking = (float) DoughUtils.MAX_BREAD_SIZE / doughSize;      //ratio for how much flour to take such that bread = 1000g and bakers percentage remains the same
            float takenFlourF = ratioForTaking * totalFlour;
            short takenFlour = (short) (Math.round(takenFlourF));
            /* bread tag for raw_bread */
            CompoundTag breadTag = DoughUtils.saveSpecificContent(0L,takenFlour,
                    Math.round(takenFlourF * getBakersPercent("water")),
                    Math.round(takenFlourF * getBakersPercent("yeast")),
                    Math.round(takenFlourF * getBakersPercent("salt")));
            /* update internal values */
            harvestDough(takenFlour);       //take flour >:)
            return breadTag;
        }
        return null;
    }

    /**
     *  Uses food values to calculate nutrition and saturation of a bread, then adds to an eat tag. An eat tag is an item tag with int nutrition and float saturationMod for setting food properties of bread in
     * {@link net.hootisman.bananas.item.BreadItem#getFoodProperties(ItemStack, LivingEntity)}.
     * <p>
     *     <b>ratio</b> = bakers percent of water / maxBound of water; a float value between 0 and 1 <br>
     *     <b>saturation</b> = points of bread * ratio; rounded to tenth decimal place <br>
     *     <b>nutrition</b> = points of bread - saturation; int value between 1 and 20 <br>
     *     <b>saturationMod</b> = saturation / (2 * nutrition); used in vanilla food data calculation
     * </p>
     * @return tag with int nutrition and float saturationMod values
     * @see net.hootisman.bananas.item.BreadItem#getFoodProperties(ItemStack, LivingEntity)
     * @see net.minecraft.world.food.FoodData#eat(int, float)
     */
    public CompoundTag calculateBreadData(){
        float ratio = Math.min(getBakersPercent("water") / getMaxBound("water"), 1.0f); //float from 0 - 1
        saturation = (float)Math.round(pointsToAllocate * ratio * 10.0f)/10.0f;
        nutrition = (int) Math.min(pointsToAllocate - saturation + 1,20);
        saturationMod = saturation / (2.0f * nutrition);

        CompoundTag eatTag = new CompoundTag();
        eatTag.putInt("nutrition",nutrition);
        eatTag.putFloat("saturationMod",saturationMod);
        return eatTag;
    }
    public void updateDoughData(){
        blockEntity.updateBlockState();
        blockEntity.setChanged();
    }

    // TODO javadoc
    public boolean doYeastFerment(){
        if (DoughUtils.canYeastGrow(get("yeast"))){
            setIngredient("yeast", (short) (get("yeast") + 1));
            updateDoughData();
            return true;
        }
        return false;
    }
    // TODO javadoc

    /**
     * ingredient in bread that has certain amount and bakers percentage (amount / {@link #totalFlour})
     */
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
