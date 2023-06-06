package net.hootisman.bananas.mixin;

import net.hootisman.bananas.item.BananaBootsItem;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.Supplier;

@Mixin(ArmorMaterials.class)
public abstract class MixinArmorMaterials {
    @SuppressWarnings("ShadowTarget")
    @Shadow private @Final @Mutable static ArmorMaterials[] $VALUES;


    @Inject(method = "<clinit>",at = @At("RETURN"))
    private static void changeEnum(CallbackInfo ci){
        ArrayList<ArmorMaterials> tempValues = new ArrayList<>(Arrays.asList(MixinArmorMaterials.$VALUES));

        BananaBootsItem.BANANA = invokeInit("BANANA",tempValues.get(tempValues.size() - 1).ordinal() + 1,"banana",1, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS,1);
            map.put(ArmorItem.Type.CHESTPLATE,1);
            map.put(ArmorItem.Type.LEGGINGS,1);
            map.put(ArmorItem.Type.HELMET,1);
        }),10, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F,0.0F, Ingredient::of);

        tempValues.add(BananaBootsItem.BANANA);
        MixinArmorMaterials.$VALUES = tempValues.toArray(new ArmorMaterials[0]);
    }

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    static ArmorMaterials invokeInit(String internalName, int internalID, String p_268171_, int p_268303_, EnumMap<ArmorItem.Type, Integer> p_267941_, int p_268086_, SoundEvent p_268145_, float p_268058_, float p_268180_, Supplier<Ingredient> p_268256_){
        throw new AssertionError();
    }

}
