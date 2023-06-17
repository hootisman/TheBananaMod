package net.hootisman.bananas.entity;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.mixin.MixinFallingBlockEntity;
import net.hootisman.bananas.registry.BananaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public static FlourFallingEntity fall(Level level, BlockPos blockPos, BlockState blockState) {
        FlourFallingEntity fallingblockentity = new FlourFallingEntity(level, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : blockState);
        level.setBlock(blockPos, blockState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(fallingblockentity);
        return fallingblockentity;
    }


    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();     //remove entity (discard)
        } else {        //blockstate NOT air
            Block block = this.blockState.getBlock();
            ++this.time;
            if (!this.isNoGravity()) {      //set movement to go down if does not have nogravity property
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());     //move entity
            if (!this.level().isClientSide) {       //on server tick
                BlockPos blockpos = this.blockPosition();


                if (!this.onGround()) {   //if entity is not on ground
                    if (!this.level().isClientSide && (this.time > 100 && (blockpos.getY() <= this.level().getMinBuildHeight() || blockpos.getY() > this.level().getMaxBuildHeight()) || this.time > 600)  ) {
                        //if server, atleast 100 ticks have passed and block position is below the min build height OR block position is above the max build height OR the time has exceeded 600 ticks
//                        if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
//                            //if entity can drop item and doEntityDrops gamerule is true
//                            this.spawnAtLocation(block);        //spawn itemEntity of the block
//                        }
                        this.discard();                         //remove entity
                    }

                } else {        //if enetiy is on ground
                    BlockState blockstate = this.level().getBlockState(blockpos);   //air if block is solid, blockstate if not solid
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
                    if (!blockstate.is(Blocks.MOVING_PISTON) && !blockstate.is(BananaBlocks.FLOUR_BLOCK.get())) {     //if not moving piston

//                        LOGGER.info(blockstate.toString());
                        boolean flag2 = blockstate.canBeReplaced(new DirectionalPlaceContext(this.level(), blockpos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));   //if block can be replaced
                        boolean flag3 = FallingBlock.isFree(this.level().getBlockState(blockpos.below()));  //if free to drop
                        boolean flag4 = this.blockState.canSurvive(this.level(), blockpos) && !flag3;       //if block can survive and NOT free to drop
                        if (flag2 && flag4) {
                            //will return true if block that landed on is solid

//                            if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(blockpos).getType() == Fluids.WATER) {
//                                //can be waterlogged
//                                this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
//                            }

                            if (this.level().setBlock(blockpos, this.blockState, 3)) {  //if set block
//                                LOGGER.info("SET BLOCK!!!!");
                                ((ServerLevel)this.level()).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockpos, this.level().getBlockState(blockpos)));
                                this.discard();
                                if (block instanceof Fallable) {
                                    ((Fallable)block).onLand(this.level(), blockpos, this.blockState, blockstate, this);
                                }
                            } else{}
                        } else {    //block cannot be replaced or cant survive or IS free to drop

                            this.discard();
//                            if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
//                                this.callOnBrokenAfterFall(block, blockpos);
//                                this.spawnAtLocation(block);
//                            }
                        }
                    }
                    else if (blockstate.is(BananaBlocks.FLOUR_BLOCK.get()) && Math.abs(this.getDeltaMovement().get(Direction.Axis.Y)) <= 0) {
//                        LOGGER.info("IS A FLOUR BLOCK!");
//                        LOGGER.info(blockstate.toString());
//                        LOGGER.info("below flour block: " + level().getBlockState(blockpos.below()));
//                        LOGGER.info("is on ground? " + this.onGround());

                        int layersToAdd = blockstate.getValue(FlourBlock.LAYERS) + blockState.getValue(FlourBlock.LAYERS);
                        if (layersToAdd > 8){
                            level().setBlockAndUpdate(blockpos.above(), BananaBlocks.FLOUR_BLOCK.get().defaultBlockState().setValue(FlourBlock.LAYERS,layersToAdd - 8));
                            layersToAdd = 8;
                        }
                        if(level().setBlockAndUpdate(blockpos, blockstate.setValue(FlourBlock.LAYERS,layersToAdd))){
                            this.discard();
                        }


                    }
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }
    }
    private void dropFlour(){
        if (!this.level().isClientSide() && this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.discard();
//            this.spawnAtLocation(block);
//            level().getServer().getLootData().getElement(LootDataType.TABLE,new ResourceLocation(BananaCore.MODID,"flour_block")).getRandomItems();
        }
    }
}



