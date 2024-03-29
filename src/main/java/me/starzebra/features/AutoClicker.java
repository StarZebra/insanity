package me.starzebra.features;

import me.starzebra.events.ClickEvent;
import me.starzebra.features.feature.Feature;
import me.starzebra.utils.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.event.InputEvent;

public class AutoClicker extends Feature {

    private static long nextleftClick = System.currentTimeMillis();

    public AutoClicker() {
        super("Auto Clicker", 0, Category.PLAYER);
    }

    @SubscribeEvent
    public void onLeftClick(ClickEvent.LeftClickEvent event){
        if(this.isToggled()){
            long nowMillis = System.currentTimeMillis();
            double maxCps = (double) 1000 /18;
            double minCps = (double) 1000 /16;
            long randomCps = (long) Math.floor(Math.random() *(maxCps - minCps + 1) + minCps);
            nextleftClick = nowMillis + randomCps;
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.thePlayer == null) return;
        if(mc.currentScreen != null && mc.thePlayer.isUsingItem()) return;
        long nowMillis = System.currentTimeMillis();
        if(this.isToggled() && mc.gameSettings.keyBindAttack.isKeyDown() && nowMillis >= nextleftClick){
            ReflectionUtils.click(ReflectionUtils.ClickType.LEFT);
        }

    }

//    @SubscribeEvent
//    public void onWorldLoad(WorldEvent.Load event){
//        this.setToggled(false);
//    }
}
