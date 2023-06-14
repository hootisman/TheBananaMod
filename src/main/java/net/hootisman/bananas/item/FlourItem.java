package net.hootisman.bananas.item;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState hitBlock = level.getBlockState(context.getClickedPos());
        BlockState hitBlockNeighbor = level.getBlockState(context.getClickedPos().relative(context.getClickedFace()));


        boolean flag = false;
        if (canBlockAddFlour(hitBlock)){
            level.setBlockAndUpdate(context.getClickedPos(),hitBlock.setValue(FlourBlock.LAYERS,Math.min(8,hitBlock.getValue(FlourBlock.LAYERS) + 1) ));
            flag = true;
        } else if (canBlockAddFlour(hitBlockNeighbor)) {
            level.setBlockAndUpdate(context.getClickedPos().relative(context.getClickedFace()),hitBlockNeighbor.setValue(FlourBlock.LAYERS,Math.min(8,hitBlockNeighbor.getValue(FlourBlock.LAYERS) + 1) ));
            flag = true;
        } else if (hitBlockNeighbor.is(Blocks.AIR)) {
            level.setBlockAndUpdate(context.getClickedPos().relative(context.getClickedFace()), BananaBlocks.FLOUR_BLOCK.get().defaultBlockState());
            flag = true;
        }

        if (flag){
            context.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }else{
            return InteractionResult.FAIL;
        }
    }

    private boolean canBlockAddFlour(BlockState block){
        return block.is(BananaBlocks.FLOUR_BLOCK.get()) && block.getValue(FlourBlock.LAYERS) < 8;
    }
}
