package net.hootisman.bananas.data.loot_tables;

import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.block.FlourCauldronBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
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

        LootTable.Builder flourTable = LootTable.lootTable();
        LootTable.Builder flourCauldronTable = LootTable.lootTable()
                                                        .withPool(LootPool.lootPool()
                                                                            .setRolls(ConstantValue.exactly(1.0F))
                                                                            .add(LootItem.lootTableItem(Blocks.CAULDRON)));
        for (int i = 1; i <= 8; i++){
            flourTable.withPool(blockStatePool(BananaBlocks.FLOUR_BLOCK.get()
                    .defaultBlockState()
                    .setValue(FlourBlock.LAYERS,i), BananaItems.FLOUR.get(), FlourBlock.LAYERS));

            flourCauldronTable.withPool(blockStatePool(BananaBlocks.FLOUR_CAULDRON.get()
                    .defaultBlockState()
                    .setValue(FlourCauldronBlock.LEVEL,i), BananaItems.FLOUR.get(), FlourCauldronBlock.LEVEL));
        }
        add(BananaBlocks.FLOUR_BLOCK.get(), flourTable);
        add(BananaBlocks.FLOUR_CAULDRON.get(), flourCauldronTable);
    }

    private LootPool.Builder blockStatePool(BlockState state, Item item, Property<Integer> property){
        return LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(state.getBlock())
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(property,state.getValue(property))))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(state.getValue(property))))
                    .add(LootItem.lootTableItem(item));
    }
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BananaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
