package me.starzebra;

import me.starzebra.commands.ItemUpdateFixCommand;
import me.starzebra.commands.SetRotationCommand;
import me.starzebra.features.ItemUpdateFix;
import me.starzebra.utils.RotationUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;

@Mod(modid = "insanity", version = "0.1", clientSideOnly = true)
public class Insanity {


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ClientCommandHandler.instance.registerCommand(new SetRotationCommand());
        ClientCommandHandler.instance.registerCommand(new ItemUpdateFixCommand());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle("Insanity - 1.8.9");
        registerEvent(new RotationUtils());
        registerEvent(new ItemUpdateFix());
    }

    private void registerEvent(Object clazz){
        MinecraftForge.EVENT_BUS.register(clazz);
    }
}
