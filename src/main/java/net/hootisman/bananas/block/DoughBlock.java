package net.hootisman.bananas.block;

import net.hootisman.bananas.block.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DoughBlock extends BaseEntityBlock {
    private static VoxelShape[] SHAPE = {
            Block.box(0.0D,0.0D,0.0D,16.0D,4.0D,16.0D),
            Block.box(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D),
            Block.box(0.0D,0.0D,0.0D,16.0D,10.0D,16.0D),
            Block.box(0.0D,0.0D,0.0D,16.0D,13.0D,16.0D),
            Block.box(0.0D,0.0D,0.0D,16.0D,16.0D,16.0D)
    };
    public DoughBlock() {
        super(Properties.of().noLootTable().strength(0.2F));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DoughCauldronBlock.LAYERS);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DoughBlockEntity(blockPos,blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE[blockState.getValue(DoughCauldronBlock.LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return SHAPE[blockState.getValue(DoughCauldronBlock.LAYERS)];
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BananaBlockEntities.DOUGH_BLOCK_ENTITY.get(), DoughBlockEntity::tick);
    }
}
