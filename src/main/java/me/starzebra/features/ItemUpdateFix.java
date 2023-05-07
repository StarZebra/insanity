package me.starzebra.features;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemUpdateFix {

    public static boolean toggled = false;
    public static boolean saveOnWorldSwap = false;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event){
        if(!saveOnWorldSwap){
            toggled = false;
        }
    }
}
