package net.hootisman.bananas.loot;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.util.GeneralUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class BananaLeavesLootModifier extends LootModifier {
    private static final float dropChance = 0.03F;

    public BananaLeavesLootModifier(LootItemCondition[] conditionsIn){
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ItemStack toolUsed = context.getParamOrNull(LootContextParams.TOOL);
        int fortuneLevel = toolUsed != null ? toolUsed.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) : 0;
        float actualChance = fortuneLevel > 0 ? dropChance * GeneralUtils.fortuneEquation(fortuneLevel) : dropChance;
        int intToRand = GeneralUtils.convertChanceToInt(actualChance);
        if(intToRand == 0 || context.getRandom().nextInt(intToRand) == 0){
            generatedLoot.add(new ItemStack(BananaItems.BANANA.get()));
        }

        return generatedLoot;
    }

    public static final Codec<BananaLeavesLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst,BananaLeavesLootModifier::new));

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    public static class Builder {

    }
}
