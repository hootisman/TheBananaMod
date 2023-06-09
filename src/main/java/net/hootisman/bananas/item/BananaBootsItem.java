package net.hootisman.bananas.item;

import net.minecraft.world.item.*;

import java.util.function.Consumer;

//public class BananaBootsItem extends ArmorItem {
public class BananaBootsItem extends ArmorItem {
    static {
        ArmorMaterials.values();
    }
    public static ArmorMaterials BANANA;
    protected final ArmorItem.Type type;


    public BananaBootsItem() {
        super(BANANA, Type.BOOTS, new Item.Properties().setNoRepair().defaultDurability(4));
        this.type = ArmorItem.Type.BOOTS;
    }




}
