package net.hootisman.bananas.item.armor;

import net.hootisman.bananas.registry.BananaSounds;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;

public class BananaBlockArmorMaterial {}
//        implements ArmorMaterial {
    //todo replace with mixin
//    private final String name;
//    private final int durabilityMultiplier;
//    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
//    private final int enchantmentValue;
//    private final SoundEvent sound;
//    private final float toughness;
//    private final float knockbackResistance;
//    private final LazyLoadedValue<Ingredient> repairIngredient;
//
//    public BananaBlockArmorMaterial() {
//
//        name = "banana";
//        durabilityMultiplier = 1;
//
//        protectionFunctionForType = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
//            map.put(ArmorItem.Type.BOOTS, 1);
//        });
//        enchantmentValue = 15;
//
//        sound = BananaSounds.BANANA_MUSH.get();
//        toughness = 0;
//        knockbackResistance = 0;
//
//        repairIngredient = new LazyLoadedValue<>(Ingredient::of);
//    }
//
//    @Override
//    public int getDurabilityForType(ArmorItem.Type p_266807_) {
//        return 0;
//    }
//
//    @Override
//    public int getDefenseForType(ArmorItem.Type p_267168_) {
//        return 0;
//    }
//
//    @Override
//    public int getEnchantmentValue() {
//        return 0;
//    }
//
//    @Override
//    public SoundEvent getEquipSound() {
//        return null;
//    }
//
//    @Override
//    public Ingredient getRepairIngredient() {
//        return null;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public float getToughness() {
//        return 0;
//    }
//
//    @Override
//    public float getKnockbackResistance() {
//        return 0;
//    }
//}