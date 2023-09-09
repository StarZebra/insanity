package me.starzebra.features.feature;

import me.starzebra.utils.BindUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ToggleBreak extends Feature{

    public ToggleBreak() {
        super("Toggle Break", Category.OTHER);
    }

    private static boolean isAutoBreaking;

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event){
        Minecraft mc = Minecraft.getMinecraft();
        KeyBinding breakKey = mc.gameSettings.keyBindAttack;
        if(this.isToggled() && BindUtils.isBindPressed("Break")){
            isAutoBreaking = !isAutoBreaking;
            KeyBinding.setKeyBindState(breakKey.getKeyCode(), isAutoBreaking);
            mc.thePlayer.addChatMessage(new ChatComponentText("§f[§4I§f] §dToggled auto break: "+isAutoBreaking));

        }
    }


}
