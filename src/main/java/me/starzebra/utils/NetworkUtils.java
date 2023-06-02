package me.starzebra.utils;

import me.starzebra.Insanity;
import net.minecraft.network.Packet;

public class NetworkUtils {

    public static void sendPacket(Packet<?> packet){
        Insanity.mc.getNetHandler().addToSendQueue(packet);
    }
}
