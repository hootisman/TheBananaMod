package net.hootisman.bananas.data;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.loot.BananaLeavesLootModifier;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class BanLootModifierProvider extends GlobalLootModifierProvider {
    public BanLootModifierProvider(PackOutput output) {
        super(output, BananaCore.MODID);
    }

    @Override
    protected void start() {
        //todo for the love of god please fix this shit
        add("banana_leaves", new BananaLeavesLootModifier(
                new LootItemCondition[]{
                    new LootItemBlockStatePropertyCondition.Builder(Blocks.JUNGLE_LEAVES).build(),

                      invertCondition(orConditions(
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(
                                new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))
                        )))).build()
                }
        ));

    }

    private LootItemCondition.Builder invertCondition(AlternativeLootItemCondition.Builder builder) {
        return builder.invert();
    }

    private AlternativeLootItemCondition.Builder orConditions(LootItemCondition.Builder condition1, LootItemCondition.Builder condition2) {
        return condition1.or(condition2);
    }

}
