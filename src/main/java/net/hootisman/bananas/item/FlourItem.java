package net.hootisman.bananas.item;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.block.FlourBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

import java.util.Optional;

public class FlourItem extends Item {
    public FlourItem() {
        super(new Item.Properties());
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        boolean flag = tryPlaceFlour(context.getPlayer(),
                context.getLevel(),
                context.getLevel().getBlockState(context.getClickedPos()),
                context.getClickedPos(),
                context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace())),
                context.getClickedPos().relative(context.getClickedFace()),
                context.getItemInHand());

        return flag ? InteractionResult.sidedSuccess(context.getLevel().isClientSide()) : InteractionResult.FAIL;
    }
    private boolean tryPlaceFlour(Player player, Level level, BlockState hitBlock, BlockPos hitPos, BlockState neighborBlock, BlockPos neighborPos, ItemStack stack){
       if (player == null) return false;

       BlockPos posToPlace = null;
       BlockState stateToPlace = null;
       if (canBlockAddFlour(hitBlock) && !player.isShiftKeyDown()){
            posToPlace = hitPos;
            stateToPlace = incrementLayer(hitBlock);
       } else if (canBlockAddFlour(neighborBlock)) {
            posToPlace = neighborPos;
            stateToPlace = incrementLayer(neighborBlock);
       } else if (neighborBlock.isAir()) {
           posToPlace = neighborPos;
           stateToPlace = BananaBlocks.FLOUR_BLOCK.get().defaultBlockState();
       }

       return posToPlace != null && changeBlockState(level, posToPlace, stateToPlace, stack);
    }
    private BlockState incrementLayer(BlockState state){
        return state.setValue(FlourBlock.LAYERS,Math.min(8,state.getValue(FlourBlock.LAYERS) + 1));
    }
    private boolean changeBlockState(Level level, BlockPos pos, BlockState state, ItemStack item){
        item.shrink(1);
        level.playSound(null,pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS);
        return level.setBlockAndUpdate(pos,state);
    }
    public static boolean canBlockAddFlour(BlockState block){
        return block.is(BananaBlocks.FLOUR_BLOCK.get()) && block.getValue(FlourBlock.LAYERS) < 8;
    }
}
