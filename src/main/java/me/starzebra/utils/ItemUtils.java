package me.starzebra.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUtils {

    public static String getSkyblockItemID(ItemStack itemStack){
        if(itemStack == null) return null;
        if(getExtraAttributes(itemStack) == null) return null;

        String itemId = getExtraAttributes(itemStack).getString("id");
        if(itemId.equals("")) return null;

        return itemId;
    }

    public static NBTTagCompound getExtraAttributes(ItemStack item) {
        if (item == null) return null;
        if (!item.hasTagCompound()) return null;
        return item.getSubCompound("ExtraAttributes", false);
    }

//    public static String stripFormatting(String string) {
//        return string.replaceAll("§.", "");
//    }

}
