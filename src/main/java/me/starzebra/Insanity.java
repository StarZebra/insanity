package me.starzebra;

import me.starzebra.commands.SetRotationCommand;
import me.starzebra.commands.SettingsCommand;
import me.starzebra.config.ConfigManager;
import me.starzebra.features.AutoClicker;
import me.starzebra.features.ItemUpdateFix;
import me.starzebra.features.feature.Feature;
import me.starzebra.features.feature.Gui;
import me.starzebra.features.feature.Keybind;
import me.starzebra.features.feature.ModHider;
import me.starzebra.utils.RotationUtils;
import me.starzebra.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

import java.io.DataInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod(modid = Insanity.MODID, name = Insanity.NAME, version = "0.1", clientSideOnly = true)
public class Insanity {
    public static final String MODID = "insanity";
    public static final String NAME = "insanity";
    public static Minecraft mc = Minecraft.getMinecraft();
    public static int fungiCooldown = 40;

    public static ItemUpdateFix itemUpdateFix = new ItemUpdateFix();
    public static CopyOnWriteArrayList<Feature> features = new CopyOnWriteArrayList<>();
    public static Gui clickGui = new Gui();
    public static ModHider modHider = new ModHider();
    public static boolean devMode;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ClientCommandHandler.instance.registerCommand(new SetRotationCommand());
        ClientCommandHandler.instance.registerCommand(new SettingsCommand());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new File(Insanity.mc.mcDataDir.getPath() + "/config/Insanity").mkdir();
        Display.setTitle("Insanity - 1.8.9");
        features.add(clickGui);
        features.add(new ItemUpdateFix());
        features.add(new AutoClicker());
        features.add(modHider);
        Insanity.loadKeybinds();
        for(Feature f: features){
            MinecraftForge.EVENT_BUS.register(f);
        }

        registerEvent(new RotationUtils());
        registerEvent(this);
        ConfigManager.loadConfig();
        Fonts.bootstrap();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){

    }

    private void registerEvent(Object clazz){
        MinecraftForge.EVENT_BUS.register(clazz);
    }

    public static void handleKeyPress(int key){
        if(key == 0) return;
        for(Feature f: features){
            if(f.getKeycode() != key || f.isKeybind()) continue;
            f.toggle();

        }
    }

    private static void loadKeybinds() {
        try {
            File oringoKeybinds = new File(Insanity.mc.mcDataDir.getPath() + "/config/Insanity/InsanityKeybinds.cfg");
            if (!oringoKeybinds.exists()) {
                oringoKeybinds.createNewFile();
            } else {
                DataInputStream dataInputStream = new DataInputStream(Files.newInputStream(Paths.get(Insanity.mc.mcDataDir.getPath() + "/config/Insanity/InsanityKeybinds.cfg")));
                int h = dataInputStream.readInt();
                for (int i = 0; i < h; ++i) {
                    String name = dataInputStream.readUTF();
                    features.add(new Keybind(name));
                }
                dataInputStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START) return;
        if(Insanity.fungiCooldown > 0) Insanity.fungiCooldown--;
    }


}
