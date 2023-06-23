package net.hootisman.bananas.entity;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class FlourFallingEntity extends FallingBlockEntity {
    private final Logger LOGGER = LogUtils.getLogger();
    private BlockState blockState = BananaBlocks.FLOUR_BLOCK.get().defaultBlockState();
    public FlourFallingEntity(EntityType<? extends FallingBlockEntity> entityType, Level level, BlockState state) {
        super(entityType, level);
        ((SettableBlockState)this).setBlockState(state);
    }
    private FlourFallingEntity(Level level, double x, double y, double z, BlockState blockState){
        this(EntityType.FALLING_BLOCK, level, blockState);
        this.blockState = blockState;
        this.blocksBuilding = true;
        setPos(x, y, z);
        setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        setStartPos(blockPosition());
    }

    public static FlourFallingEntity fall(Level level, BlockPos blockPos, BlockState blockState) {
        FlourFallingEntity fallingblockentity = new FlourFallingEntity(level, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : blockState);
        level.setBlock(blockPos, blockState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(fallingblockentity);
        return fallingblockentity;
    }


    @Override
    public void tick() {
        moveFlourBlockTick();

        if (!level().isClientSide && onGround()) {       //on server tick and entity is on ground
            BlockState stateAtPos = level().getBlockState(blockPosition());   //air if block is solid, blockstate if not solid
            setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
            if (!stateAtPos.is(Blocks.MOVING_PISTON) && !stateAtPos.is(BananaBlocks.FLOUR_BLOCK.get())) {     //if not moving piston
                if(canPlaceBlockAt(stateAtPos)) trySetBlockAt(stateAtPos); else dropFlourItem();
            } else if (stateAtPos.is(BananaBlocks.FLOUR_BLOCK.get()) && Math.abs(getDeltaMovement().get(Direction.Axis.Y)) <= 0) {
                //TODO clean
                int layersToAdd = stateAtPos.getValue(FlourBlock.LAYERS) + blockState.getValue(FlourBlock.LAYERS);
                if (layersToAdd > 8) {
                    level().setBlockAndUpdate(blockPosition().above(), BananaBlocks.FLOUR_BLOCK.get().defaultBlockState().setValue(FlourBlock.LAYERS, layersToAdd - 8));
                    layersToAdd = 8;
                }
                if (level().setBlockAndUpdate(blockPosition(), stateAtPos.setValue(FlourBlock.LAYERS, layersToAdd))) {
                    discard();
                }
            }
        }else discardOutOfBoundsEntity();

        setDeltaMovement(getDeltaMovement().scale(0.98D));
    }
    private boolean canPlaceBlockAt(BlockState stateAtPos) {
        return stateAtPos.canBeReplaced(new DirectionalPlaceContext(level(), blockPosition(), Direction.DOWN, ItemStack.EMPTY, Direction.UP)) &&
                blockState.canSurvive(level(), blockPosition()) &&
                !FallingBlock.isFree(level().getBlockState(blockPosition().below()));
    }
    private void trySetBlockAt(BlockState stateAtPos){
//                            if (blockState.hasProperty(BlockStateProperties.WATERLOGGED) && level().getFluidState(blockPosition()).getType() == Fluids.WATER) {
//                                //can be waterlogged
//                                blockState = blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
//                            }

        if (level().setBlock(blockPosition(), blockState, 3)) {  //if set block
            ((ServerLevel)level()).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockPosition(), level().getBlockState(blockPosition())));
            discard();
            if (blockState.getBlock() instanceof Fallable) {
                ((Fallable)blockState.getBlock()).onLand(level(), blockPosition(), blockState, stateAtPos, this);
            }
        }
    }
    private void moveFlourBlockTick(){
        ++time;
        if (!isNoGravity()) {      //set movement to go down if does not have nogravity property
            setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        move(MoverType.SELF, getDeltaMovement());     //move entity
    }
    private void discardOutOfBoundsEntity(){
        if (!level().isClientSide() &&
                !onGround() &&
                (time > 100 && (blockPosition().getY() <= level().getMinBuildHeight() || blockPosition().getY() > level().getMaxBuildHeight()) || time > 600)) {
            //if server, atleast 100 ticks have passed and block position is below the min build height OR block position is above the max build height OR the time has exceeded 600 ticks
            discard();                         //remove entity
        }
    }
    private void dropFlourItem(){
        if (!level().isClientSide() && dropItem && level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            discard();
            spawnAtLocation(new ItemStack(BananaItems.FLOUR.get(), blockState.getValue(FlourBlock.LAYERS)));
        }
    }
}



