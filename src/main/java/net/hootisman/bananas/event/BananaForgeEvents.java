package net.hootisman.bananas.event;

import com.mojang.logging.LogUtils;
import net.hootisman.bananas.BananaCore;
import net.hootisman.bananas.util.DoughData;
import net.hootisman.bananas.util.DoughUtils;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = BananaCore.MODID)
public class BananaForgeEvents {
    public static Logger LOGGER = LogUtils.getLogger();
//    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        //for debugging
        if (event.getSide() == LogicalSide.SERVER && event.getItemStack().isEdible() ){
            FoodProperties food = event.getItemStack().getItem().getFoodProperties(event.getItemStack(),event.getEntity());
//            LOGGER.info("RIGHT CLICKED EDIBLE!! nutrition: " + food.getNutrition() + " saturation: " + food.getSaturationModifier());
//            float res = (20.0F * (0.55F/0.65F));
//            LOGGER.info("before: " + res + " after: " + (int)res);
//            DoughData data = new DoughData(DoughUtils.saveSpecificContent(0,100,50,0,0));
//            LOGGER.info("DOUGH DATA! nutrition: " + data.getNutrition() + " saturation: " + data.getSaturation() + " satModifier: " + data.getSaturationMod());
//            DoughData data2 = new DoughData(DoughUtils.saveSpecificContent(0,100,65,0,0));
//            LOGGER.info("DOUGH DATA! nutrition: " + data2.getNutrition() + " saturation: " + data2.getSaturation() + " satModifier: " + data2.getSaturationMod());
//            DoughData data3 = new DoughData(DoughUtils.saveSpecificContent(0,100,0,0,0));
//            LOGGER.info("DOUGH DATA! nutrition: " + data3.getNutrition() + " saturation: " + data3.getSaturation() + " satModifier: " + data3.getSaturationMod());
//            DoughData data4 = new DoughData(DoughUtils.saveSpecificContent(0,100,100,0,0));
//            LOGGER.info("DOUGH DATA! nutrition: " + data4.getNutrition() + " saturation: " + data4.getSaturation() + " satModifier: " + data4.getSaturationMod());
        }
    }
}
