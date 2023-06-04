package me.starzebra;

import me.starzebra.commands.SetRotationCommand;
import me.starzebra.commands.SettingsCommand;
import me.starzebra.config.ConfigManager;
import me.starzebra.features.AutoClicker;
import me.starzebra.features.ItemUpdateFix;
import me.starzebra.features.feature.*;
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

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod(modid = Insanity.MODID, name = Insanity.NAME, version = "0.2", clientSideOnly = true)
public class Insanity {
    public static final String MODID = "insanity";
    public static final String NAME = "insanity";
    public static Minecraft mc;
    public static int fungiCooldown = 40;

    public static ItemUpdateFix itemUpdateFix = new ItemUpdateFix();
    public static CopyOnWriteArrayList<Feature> features;
    public static Gui clickGui;
    public static ModHider modHider= new ModHider();
    public static CustomName customRank = new CustomName();
    public static boolean devMode;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ClientCommandHandler.instance.registerCommand(new SetRotationCommand());

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new File(Insanity.mc.mcDataDir.getPath() + "/config/Insanity").mkdir();
        Display.setTitle("Insanity - 1.8.9");
        Insanity.features.add(Insanity.clickGui);
        Insanity.features.add(itemUpdateFix);
        Insanity.features.add(new AutoClicker());
        Insanity.features.add(modHider);
        Insanity.features.add(new EasyAOTV());
        Insanity.features.add(customRank);
//        Insanity.loadKeybinds();
        for(Feature f: Insanity.features){
            MinecraftForge.EVENT_BUS.register(f);
            //System.out.println("LOADED: " +f.getName());
        }

        registerEvent(new RotationUtils());
        registerEvent(this);
        ClientCommandHandler.instance.registerCommand(new SettingsCommand());

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

//    private static void loadKeybinds() {
//        try {
//            File oringoKeybinds = new File(Insanity.mc.mcDataDir.getPath() + "/config/Insanity/InsanityKeybinds.cfg");
//            if (!oringoKeybinds.exists()) {
//                oringoKeybinds.createNewFile();
//            } else {
//                DataInputStream dataInputStream = new DataInputStream(Files.newInputStream(Paths.get(Insanity.mc.mcDataDir.getPath() + "/config/Insanity/InsanityKeybinds.cfg")));
//                int h = dataInputStream.readInt();
//                for (int i = 0; i < h; ++i) {
//                    String name = dataInputStream.readUTF();
//                    features.add(new Keybind(name));
//                }
//                dataInputStream.close();
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START) return;
        if(Insanity.fungiCooldown > 0) Insanity.fungiCooldown--;
    }

    static {
        mc = Minecraft.getMinecraft();
        clickGui = new Gui();
        features = new CopyOnWriteArrayList<>();
    }

}
