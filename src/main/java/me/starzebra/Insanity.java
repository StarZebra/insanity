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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

@Mod(modid = "insanity", version = "0.1", clientSideOnly = true)
public class Insanity {

    public static int fungiCooldown = 40;

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
        registerEvent(this);
    }

    private void registerEvent(Object clazz){
        MinecraftForge.EVENT_BUS.register(clazz);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START) return;
        if(Insanity.fungiCooldown > 0) Insanity.fungiCooldown--;
    }


}
