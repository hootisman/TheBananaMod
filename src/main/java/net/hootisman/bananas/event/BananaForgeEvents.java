package net.hootisman.bananas.event;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.util.BakersPercent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = BananaCore.MODID)
public class BananaForgeEvents {
    public static Logger LOGGER = LogUtils.getLogger();
    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        if (event.getSide() == LogicalSide.SERVER && event.getItemStack().isEdible() ){
            FoodProperties food = event.getItemStack().getItem().getFoodProperties(event.getItemStack(),event.getEntity());
            LOGGER.info("RIGHT CLICKED EDIBLE!! nutrition: " + food.getNutrition() + " saturation: " + food.getSaturationModifier());
            float res = (20.0F * (0.55F/0.65F));
            LOGGER.info("before: " + res + " after: " + (int)res);
            BakersPercent perc = new BakersPercent((short) 55, (short) 100);
            LOGGER.info("bakers percent: " + perc.get());
        }
    }
}
