package net.hootisman.bananas.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class BananaLeavesLootModifier extends LootModifier {
    private static final float dropChance = 0.02F;   //TODO Remove and replace with random + fortune

    public BananaLeavesLootModifier(LootItemCondition[] conditionsIn){
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(context.getRandom().nextFloat() <= dropChance){
            generatedLoot.add(new ItemStack(BananaItems.BANANA_ITEM.get()));
        }

        return generatedLoot;
    }

    public static final Codec<BananaLeavesLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst,BananaLeavesLootModifier::new));

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
