package net.hootisman.bananas.util;

import net.hootisman.bananas.entity.DoughBlockEntity;
import net.hootisman.bananas.registry.BananaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoughUtils {
    /**
     * How many ticks it takes for the yeast amount in {@link DoughData} to increase by 1
     */
    public static final int YEAST_TICK = 200;
    /**
     * Maximum dough size in {@link DoughData} (in grams)
     */
    public static final int MAX_DOUGH_SIZE = 4000;
    /**
     * Size of one {@link net.hootisman.bananas.item.RawBreadItem} and {@link net.hootisman.bananas.item.BreadItem} (in grams)
     */
    public static final int MAX_BREAD_SIZE = 1000;

    /**
     * Tries to create and place a {@link DoughBlockEntity}
     *
     * @param tag tag that has passed {@link #isDoughTag(CompoundTag)}
     * @param createDoughEntity supplier for creating a dough block entity
     * @param swapItemBlockFunc consumer for running {@link #swapItemAndBlock(Player, Level, BlockPos, InteractionHand, ItemStack, BlockState)}
     * @return a newly created dough block entity
     */
    public static DoughBlockEntity placeDough(@Nullable CompoundTag tag, Supplier<DoughBlockEntity> createDoughEntity, Consumer<BlockState> swapItemBlockFunc){
        //places DoughBlockEntity
        DoughBlockEntity dough = createDoughEntity.get();
        if (dough != null){
            if (tag != null) dough.loadDoughContent(tag);
            swapItemBlockFunc.accept(dough.getBlockState());
        }
        return dough;
    }

    /**
     * Tries to pickup a {@link DoughBlockEntity} using a container
     *
     * @param container ItemStack player has right-clicked with
     * @param player who is performing action
     * @param dough block entity player has right-clicked
     * @param swapItemBlockFunc consumer for running {@link #swapItemAndBlock(Player, Level, BlockPos, InteractionHand, ItemStack, BlockState)}
     */
    public static void pickupDough(ItemStack container, Player player, DoughBlockEntity dough, Consumer<ItemStack> swapItemBlockFunc){
        //picks up DoughBlockEntity
        ItemStack doughBowl = new ItemStack(BananaItems.DOUGH_BOWL.get());
        doughBowl.setTag(dough.saveDoughContent(new CompoundTag()));
        swapItemBlockFunc.accept(ItemUtils.createFilledResult(container,player,doughBowl));
    }

    /**
     * Changes item in player's hand with newStack and changes block at position to newState
     * @param player who is performing action
     * @param level level to set block in
     * @param pos block position in level
     * @param hand hand the player used
     * @param newStack the new ItemStack
     * @param newState the new BlockState
     */
    public static void swapItemAndBlock(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack newStack, BlockState newState){
        player.setItemInHand(hand,newStack);
        level.setBlockAndUpdate(pos,newState);
    }
    public static void playSoundHelper(Level level, BlockPos blockPos, SoundEvent soundEvent){
        level.playSound(null,blockPos, soundEvent, SoundSource.BLOCKS);
    }
    public static <T extends ParticleOptions> void spawnParticlesHelper(T type, ServerLevel level, Vec3 pos, int count, Vec3 delta, float speed){
        level.sendParticles(type,pos.x(),pos.y(),pos.z(),count, delta.x(),delta.y(),delta.z(),speed);
    }
    public static CompoundTag saveSpecificContent(long time, int flour, int water, int yeast, int salt){
        CompoundTag tag = new CompoundTag();
        tag.putLong("time",time);
        tag.putShort("flour", (short) flour);
        tag.putShort("water", (short) water);
        tag.putShort("yeast", (short) yeast);
        tag.putByte("salt", (byte) salt);

        return tag;
    }
    public static boolean canYeastGrow(short yeast){
        return yeast < Short.MAX_VALUE;
    }

    /**
     * Checks if yeast amount in {@link DoughData} can increase by 1
     * @param currentTickTime current game tick time
     * @param lastTickTime last game tick time the yeast incremented
     * @return true if their difference is more than {@link #YEAST_TICK}
     */
    public static boolean hasYeastFermented(long currentTickTime, long lastTickTime){
        return currentTickTime - lastTickTime >= YEAST_TICK;
    }

    /**
     * Checks if the {@link CompoundTag} is a dough tag
     * @param tag
     * @return true if tag has data it needs to make dough
     * @see #hasDoughTag(ItemStack)
     */
    public static boolean isDoughTag(CompoundTag tag){
        return tag.contains("time") &&
                tag.contains("flour") &&
                tag.contains("water") &&
                tag.contains("yeast") &&
                tag.contains("salt");
    }

    /**
     * Checks if {@link ItemStack} has dough tag
     * @param stack stack to check
     * @return true if tag has data it needs to make dough
     * @see #isDoughTag(CompoundTag)
     */
    public static boolean hasDoughTag(ItemStack stack){
        return stack.hasTag() && isDoughTag(stack.getTag());
    }
}
