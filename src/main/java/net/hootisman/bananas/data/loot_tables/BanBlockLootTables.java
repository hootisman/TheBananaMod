package net.hootisman.bananas.data.loot_tables;

import net.hootisman.bananas.registry.BananaBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class BanBlockLootTables extends BlockLootSubProvider {
    protected BanBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(BananaBlocks.BANANA_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BananaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
