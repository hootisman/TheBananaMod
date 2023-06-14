package net.hootisman.bananas.item;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.slf4j.Logger;

public class FlourItem extends Item {

    private static Logger LOGGER = LogUtils.getLogger();

    public FlourItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level,player, ClipContext.Fluid.NONE);
        if (level.isClientSide){
            return InteractionResultHolder.success(itemStack);
        }else if (hitResult.getType() == HitResult.Type.BLOCK){
            LOGGER.info("HIT A BLOCK!" );
            BlockState hitBlock = level.getBlockState(hitResult.getBlockPos());
            BlockState hitNeighborBlock = level.getBlockState(hitResult.getBlockPos().relative(hitResult.getDirection()));

            BlockPos posToPlace;
            BlockState blockToPlace;

            if (hitBlock.is(BananaBlocks.FLOUR_BLOCK.get()) && hitBlock.getValue(FlourBlock.LAYERS) < 8){
//                LOGGER.info("BLOCK IS FLOUR, ADDING LAYER...");
                posToPlace = hitResult.getBlockPos();
                blockToPlace = hitBlock.setValue(FlourBlock.LAYERS ,Math.min(8,hitBlock.getValue(FlourBlock.LAYERS) + 1));

            } else if (hitNeighborBlock.is(BananaBlocks.FLOUR_BLOCK.get()) && hitNeighborBlock.getValue(FlourBlock.LAYERS) < 8) {
//                LOGGER.info("NEIGHBORING BLOCK IS FLOUR, ADDING LAYER...");
                posToPlace = hitResult.getBlockPos().relative(hitResult.getDirection());
                blockToPlace = hitNeighborBlock.setValue(FlourBlock.LAYERS ,Math.min(8,hitNeighborBlock.getValue(FlourBlock.LAYERS) + 1) );

            } else {
//                LOGGER.info("NO FLOUR FOUND, ADDING TO WORLD...");
                posToPlace = hitResult.getBlockPos().relative(hitResult.getDirection());
                blockToPlace = BananaBlocks.FLOUR_BLOCK.get().defaultBlockState();

            }


            level.setBlockAndUpdate(posToPlace,blockToPlace);
            itemStack.shrink(1);

            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }
}
