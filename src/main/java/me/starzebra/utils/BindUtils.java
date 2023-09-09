package me.starzebra.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.HashMap;

public class BindUtils {
    private static final HashMap<String, KeyBinding> bindMap = new HashMap<>();

    public static void registerBinds(Bind... binds) {
        for (Bind bind : binds) {
            String bindName = bind.getBindName();
            bindMap.put(bindName, new KeyBinding(bindName, bind.getBindKey(), "Insanity"));
            ClientRegistry.registerKeyBinding(bindMap.get(bindName));
        }
    }

    public static boolean isBindPressed(String bindName) {
        return bindMap.get(bindName).isPressed();
    }

    public static boolean isBindDown(String bindName) {
        return bindMap.get(bindName).isKeyDown();
    }

    public static class Bind {
        private final int bindKey;
        private final String bindName;

        public Bind(int bindKey, String bindName) {
            this.bindKey = bindKey;
            this.bindName = bindName;
        }

        public String getBindName(){
            return this.bindName;
        }

        public int getBindKey(){
            return this.bindKey;
        }
    }
}
