package net.hootisman.bananas.item;

import net.hootisman.bananas.util.DoughUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RawBreadItem extends Item {
    //TODO add yeast ticking clock for here, same as DoughBowlItem
    public RawBreadItem() {
        super(new Properties().stacksTo(1));
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        DoughUtils.tickInventoryDough(stack, level, entity, slot, selected);
    }
}
