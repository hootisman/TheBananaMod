package net.hootisman.bananas.block;

import net.hootisman.bananas.entity.FlourFallingEntity;
import net.hootisman.bananas.item.FlourItem;
import net.hootisman.bananas.registry.BananaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FlourBlock extends FallingBlock {
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    private static VoxelShape[] SHAPE = {
        Block.box(0.0D,0.0D,0.0D,16.0D,2.0D,16.0D),
        Block.box(0.0D,0.0D,0.0D,16.0D,4.0D,16.0D),
        Block.box(0.0D,0.0D,0.0D,16.0D,6.0D,16.0D),
        Block.box(0.0D,0.0D,0.0D,16.0D,8.0D,16.0D),
        Block.box(0.0D,0.0D,0.0D,16.0D,10.0D,16.0D),
        Block.box(0.0D,0.0D,0.0D,16.0D,12.0D,16.0D),
        Block.box(0.0D,0.0D,0.0D,16.0D,14.0D,16.0D),
        Block.box(0.0D,0.0D,0.0D,16.0D,16.0D,16.0D)
    };

    public FlourBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).sound(SoundType.SAND).strength(0.3F).noLootTable());
        registerDefaultState(this.getStateDefinition().any().setValue(LAYERS, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos blockPos, RandomSource source) {
        BlockState blockBelow = level.getBlockState(blockPos.below());
        if ((isFree(blockBelow)) && blockPos.getY() >= level.getMinBuildHeight()) {
            FlourFallingEntity flourFallingEntity = FlourFallingEntity.fall(level, blockPos, state);
            this.falling(flourFallingEntity);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        if (blockState.is(this)){
            return blockState.setValue(LAYERS,Math.min(8,blockState.getValue(LAYERS) + 1));
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return SHAPE[blockState.getValue(LAYERS) - 1];
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE[blockState.getValue(LAYERS) - 1];
    }

}
