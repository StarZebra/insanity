package me.starzebra.features;

import me.starzebra.events.ClickEvent;
import me.starzebra.utils.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoClicker {

    public static boolean enabled = false;

    private static long nextleftClick = System.currentTimeMillis();

    @SubscribeEvent
    public void onLeftClick(ClickEvent.LeftClickEvent event){
        if(enabled){
            long nowMillis = System.currentTimeMillis();
            double maxCps = (double) 1000 /18;
            double minCps = (double) 1000 /16;
            long randomCps = (long) Math.floor(Math.random() *(maxCps - minCps + 1) + minCps);
            nextleftClick = nowMillis + randomCps;
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.thePlayer == null) return;
        if(mc.currentScreen != null && mc.thePlayer.isUsingItem()) return;
        long nowMillis = System.currentTimeMillis();

        if(enabled && mc.gameSettings.keyBindAttack.isKeyDown() && nowMillis >= nextleftClick){
            ReflectionUtils.click(ReflectionUtils.ClickType.LEFT);

        }

    }
}
