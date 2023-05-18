package me.starzebra.utils;

import net.minecraft.client.Minecraft;

import java.lang.reflect.Method;

public class ReflectionUtils {

    public enum ClickType {
        LEFT, MIDDLE, RIGHT
    }

    public static boolean invoke(Object object, String methodName) {
        try {
            final Method method = object.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(object);
            return true;
        } catch(Exception ignored) {}
        return false;
    }

    public static void click(ClickType click){
        switch (click) {
            case LEFT:
                tryInvoke("func_147116_af", "clickMouse");
                break;
            case MIDDLE:
                tryInvoke("func_147112_ai", "middleClickMouse");
                break;
            case RIGHT:
                tryInvoke("func_147121_ag", "rightClickMouse");
                break;
        }
    }

    private static void tryInvoke(String obfName, String normalName){
        if(!ReflectionUtils.invoke(Minecraft.getMinecraft(), obfName))
            ReflectionUtils.invoke(Minecraft.getMinecraft(), normalName);
    }

}
