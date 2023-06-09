package net.hootisman.bananas.event.armor;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.registry.BananaItems;
import net.hootisman.bananas.registry.BananaSounds;
import net.minecraft.nbt.ByteTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;


@Mod.EventBusSubscriber(modid = BananaCore.MODID)
public class BootsFallHandler {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean isItemAtLastDurability;

    private static final String tagName = "item_last_durability";

    @SubscribeEvent
    public static void onAfterArmorCalc(LivingDamageEvent event){

        DamageSource source = event.getSource();
        ItemStack feetArmor = event.getEntity().getItemBySlot(EquipmentSlot.FEET);

        if (isEntityWearingBoots(event.getEntity(), source,false)
                && !(feetArmor.getOrCreateTag().getBoolean(tagName))
                     ){
//                debugEvent(event.getEntity(),source,event.toString());
                feetArmor.setDamageValue(feetArmor.getDamageValue() - 1);
                feetArmor.addTagElement(tagName,ByteTag.valueOf(false));
        }
    }

    @SubscribeEvent
    public static void onBeforeArmorCalc(LivingHurtEvent event){

        DamageSource source = event.getSource();
        ItemStack feetArmor = event.getEntity().getItemBySlot(EquipmentSlot.FEET);

        if(isEntityWearingBoots(event.getEntity(),source,false)
                && feetArmor.getDamageValue() + 1 >= feetArmor.getMaxDamage()
                    ){
                debugEvent(event.getEntity(), source,event.toString());
                feetArmor.setDamageValue(feetArmor.getDamageValue() - 1);
//                if (feetArmor.getTag() != null) feetArmor.getTag().putBoolean(tagName,true);
                feetArmor.addTagElement(tagName,ByteTag.valueOf(true));
        }
    }


    @SubscribeEvent
    public static void onDamageWithBoots(LivingAttackEvent event){
        DamageSource source = event.getSource();
        ItemStack feetArmor = event.getEntity().getItemBySlot(EquipmentSlot.FEET);

        if (isEntityWearingBoots(event.getEntity(), source, true)){
//            debugEvent(event.getEntity(), source, event.toString());

            event.getEntity().getLevel().playSound(null,event.getEntity().blockPosition(),BananaSounds.BANANA_MUSH.get(), SoundSource.BLOCKS);
            feetArmor.hurtAndBreak(1, (ServerPlayer)event.getEntity(), (callback) -> callback.broadcastBreakEvent(EquipmentSlot.FEET));
            event.setCanceled(true);
        }
    }

    private static boolean isEntityWearingBoots(LivingEntity entity, DamageSource source, boolean checkIfOnlyFalling){

        boolean fallingDamage = source.is(DamageTypes.FALL) ;

        return entity.getItemBySlot(EquipmentSlot.FEET).is(BananaItems.BANANA_BOOTS.get())
                && entity instanceof ServerPlayer
                    && (checkIfOnlyFalling == fallingDamage);
    }

    private static void debugEvent(LivingEntity entity, DamageSource source, String event){
            LOGGER.info(event + source.toString() + "from" + entity.toString());
    }

}
