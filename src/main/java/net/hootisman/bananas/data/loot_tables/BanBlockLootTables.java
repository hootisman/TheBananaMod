package net.hootisman.bananas.data.loot_tables;

import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class BanBlockLootTables extends BlockLootSubProvider {
    protected BanBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(BananaBlocks.BANANA_BLOCK.get());

        add(BananaBlocks.BANANA_MUSH_BLOCK.get(),
                LootTable.lootTable().withPool(LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F,3.0F)))
                                        .add(LootItem.lootTableItem(BananaItems.BANANA.get()))
                ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BananaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
