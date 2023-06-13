package net.hootisman.bananas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BananaMushBlock extends Block {
    public static final float BLOCK_HEIGHT = 6;
    private static VoxelShape SHAPE = Block.box(0.0D,0.0D,0.0D,16.0D,BLOCK_HEIGHT,16.0D);
    public BananaMushBlock() {
        super(BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.COLOR_YELLOW)
                .strength(0.1F)
                .sound(SoundType.SLIME_BLOCK));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
}
