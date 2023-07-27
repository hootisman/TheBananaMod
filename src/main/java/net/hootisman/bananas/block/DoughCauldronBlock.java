package net.hootisman.bananas.block;

import net.hootisman.bananas.block.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.hootisman.bananas.util.CauldronUtils;
import net.hootisman.bananas.util.EntityBlockTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class DoughCauldronBlock extends AbstractCauldronBlock implements EntityBlock {
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers",0,4);
    public DoughCauldronBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON), CauldronUtils.DOUGH_INTERACT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    public boolean isFull(BlockState blockState) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DoughBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return EntityBlockTicker.createTickerHelper(blockEntityType, BananaBlockEntities.DOUGH_CAULDRON_ENTITY.get(), DoughBlockEntity::tick);
    }
}
