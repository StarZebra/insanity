package me.starzebra.mixin;

import me.starzebra.events.ClickEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void onLeftClick(CallbackInfo ci){
        if(MinecraftForge.EVENT_BUS.post(new ClickEvent.LeftClickEvent())) ci.cancel();
    }
}
