package net.hootisman.bananas.util;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;

public interface EntityBlockTicker {
    //ticker helper outside 'BaseEntityBlock'
    @Nullable
    static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<? super E> ticker) {
        return blockEntityType2 == blockEntityType ? (BlockEntityTicker<A>)ticker : null;
    }
}
