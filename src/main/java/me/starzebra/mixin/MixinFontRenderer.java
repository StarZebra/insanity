package me.starzebra.mixin;

import me.starzebra.Insanity;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FontRenderer.class})
public abstract class MixinFontRenderer {

    @Shadow protected abstract void renderStringAtPos(String text, boolean shadow);

    @Shadow public abstract int getStringWidth(String text);

    @Inject(method = { "renderStringAtPos"}, at = {@At("HEAD")}, cancellable = true)
    private void renderString(String text, boolean shadow, CallbackInfo ci){
        if(Insanity.customRank.isToggled() && text.contains(Insanity.mc.getSession().getUsername()) && !Insanity.mc.getSession().getUsername().equals(Insanity.customRank.name.getValue())){
            ci.cancel();
            this.renderStringAtPos(text.replaceAll(Insanity.mc.getSession().getUsername(), Insanity.customRank.name.getValue()), shadow);
        }
    }

    @Inject(method = { "getStringWidth" }, at = { @At("RETURN") }, cancellable = true)
    private void getStringWidth(final String text, final CallbackInfoReturnable<Integer> cir) {
        if (text != null && Insanity.customRank.isToggled() && text.contains(Insanity.mc.getSession().getUsername()) && !Insanity.mc.getSession().getUsername().equals(Insanity.customRank.name.getValue())) {
            cir.setReturnValue(this.getStringWidth(text.replaceAll(Insanity.mc.getSession().getUsername(), Insanity.customRank.name.getValue())));
        }
    }
}
