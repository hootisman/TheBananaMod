package net.hootisman.bananas.item;

import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.block.BananaBlock;
import net.hootisman.bananas.registry.BananaBlocks;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.registry.BananaSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = BananaCore.MODID)
public class BananaBootsItem extends ArmorItem {
    static {
        ArmorMaterials.values();
    }
    public static ArmorMaterials BANANA;
    protected final ArmorItem.Type type;
    public static final int uniqueDamage = -81329340;   //this feels like a terrible way to do it but idgaf


    public BananaBootsItem() {
        super(BANANA, Type.BOOTS, new Item.Properties().setNoRepair().defaultDurability(4));
        this.type = ArmorItem.Type.BOOTS;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return amount == uniqueDamage ? 1 : 0;
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event){
        if (isEntityWearingBoots(event.getEntity())
                && BananaBlock.isEntityTakingDamage(event.getEntity(),event.getDistance())
                    && !event.getEntity().getBlockStateOn().is(BananaBlocks.BANANA_BLOCK.get())){
            ItemStack feetarmor = event.getEntity().getItemBySlot(EquipmentSlot.FEET);
            feetarmor.hurtAndBreak(uniqueDamage, event.getEntity(), (cb) -> cb.broadcastBreakEvent(EquipmentSlot.FEET));

            event.setDamageMultiplier(0.0F);
            event.getEntity().level().playSound(null,event.getEntity().blockPosition(), BananaSounds.BANANA_MUSH.get(), SoundSource.BLOCKS);

        }
    }

    private static boolean isEntityWearingBoots(LivingEntity entity){
        return entity.getItemBySlot(EquipmentSlot.FEET).is(BananaItems.BANANA_BOOTS.get())
                && entity instanceof ServerPlayer;
    }
}
