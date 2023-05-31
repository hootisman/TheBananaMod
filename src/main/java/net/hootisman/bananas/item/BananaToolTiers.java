package net.hootisman.bananas.item;

import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum BananaToolTiers implements Tier {
    BANANA(0,48,5.0F,0.0F,18,() -> {
        return Ingredient.of(BananaItems.BANANA_ITEM.get());
    });

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    @NotNull
    private final Supplier<Ingredient> repairIngredient;
//    private final TagKey<Block> tag;
    private BananaToolTiers(int level, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient){
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getLevel(){
        return this.level;
    }

    @Override
    public int getUses(){
        return this.uses;
    }

    @Override
    public float getSpeed(){
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus(){
        return this.damage;
    }

    @Override
    public int getEnchantmentValue(){
        return this.enchantmentValue;
    }

    @NotNull
    @Override
    public Ingredient getRepairIngredient(){
        return this.repairIngredient.get();
    }

//    public TagKey<Block> getTag(){
//        return this.tag;
//    }
}
