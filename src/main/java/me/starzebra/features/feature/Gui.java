package me.starzebra.features.feature;

import me.starzebra.Insanity;
import me.starzebra.features.feature.settings.BooleanSetting;
import me.starzebra.features.feature.settings.ModeSetting;
import me.starzebra.features.feature.settings.NumberSetting;
import me.starzebra.features.feature.settings.StringSetting;
import me.starzebra.gui.ClickGUI;
import me.starzebra.utils.ColorUtils;
import me.starzebra.utils.font.Fonts;
import me.starzebra.utils.font.MinecraftFontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Gui extends Feature{

    public ClickGUI clickGUI;
    public ModeSetting colorMode;
    public NumberSetting redCustom;
    public NumberSetting greenCustom;
    public NumberSetting blueCustom;
    public NumberSetting redShift1;
    public NumberSetting greenShift1;
    public NumberSetting blueShift1;
    public NumberSetting redShift2;
    public NumberSetting greenShift2;
    public NumberSetting blueShift2;
    public NumberSetting shiftSpeed;
    public NumberSetting rgbSpeed;
    public ModeSetting blur;
    public BooleanSetting scaleGui;
    public BooleanSetting arrayList;
    public BooleanSetting disableNotifs;
    public BooleanSetting arrayBlur;
    public BooleanSetting arrayOutline;
    public BooleanSetting waterMark;
    public BooleanSetting hsb;
    public static final StringSetting commandPrefix;
    public static final StringSetting clientName;


    public Gui() {
        super("Click Gui", 54, Category.RENDER);
        this.clickGUI = new ClickGUI();
        this.colorMode = new ModeSetting("Mode", "Color shift", "Rainbow", "Color shift", "Astolfo", "Pulse", "Custom");
        this.redCustom = new NumberSetting("Red", 0.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Custom") && !this.colorMode.is("Pulse"));
        this.greenCustom = new NumberSetting("Green", 80.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Custom") && !this.colorMode.is("Pulse"));
        this.blueCustom = new NumberSetting("Blue", 255.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Custom") && !this.colorMode.is("Pulse"));
        this.redShift1 = new NumberSetting("Red 1 ", 0.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.greenShift1 = new NumberSetting("Green 1 ", 255.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.blueShift1 = new NumberSetting("Blue 1 ", 110.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.redShift2 = new NumberSetting("Red 2 ", 255.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.greenShift2 = new NumberSetting("Green 2 ", 175.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.blueShift2 = new NumberSetting("Blue 2 ", 255.0, 0.0, 255.0, 1.0, aBoolean -> !this.colorMode.is("Color shift"));
        this.shiftSpeed = new NumberSetting("Shift Speed ", 1.0, 0.1, 5.0, 0.1, aBoolean -> !this.colorMode.is("Color shift") && !this.colorMode.is("Pulse") && !this.colorMode.is("Astolfo"));
        this.rgbSpeed = new NumberSetting("Rainbow Speed", 2.5, 0.1, 5.0, 0.1, aBoolean -> !this.colorMode.is("Rainbow"));
        this.blur = new ModeSetting("Blur strength", "Low", "None", "Low", "High");
        this.scaleGui = new BooleanSetting("Scale gui", false);
        this.arrayList = new BooleanSetting("ArrayList", true);
        this.disableNotifs = new BooleanSetting("Disable notifications", false);
        this.arrayBlur = new BooleanSetting("Array background", true);
        this.arrayOutline = new BooleanSetting("Array line", true);
        this.waterMark = new BooleanSetting("Watermark", true);
        this.hsb = new BooleanSetting("HSB ", true, aBoolean -> !this.colorMode.is("Color shift"));
        this.addSettings(this.colorMode, this.hsb, this.rgbSpeed, this.shiftSpeed, this.redCustom, this.greenCustom, this.blueCustom, this.redShift1, this.greenShift1, this.blueShift1, this.redShift2, this.greenShift2, this.blueShift2, Gui.commandPrefix, this.blur, this.waterMark, Gui.clientName, this.arrayList, this.arrayOutline, this.arrayBlur, this.disableNotifs, this.scaleGui);
    }

    public Color getColor(){
        return this.getColor(0);
    }

    public Color getColor(final int index) {
        final String selected = this.colorMode.getSelected();
        switch (selected) {
            case "Color shift": {
                final float location = (float)((Math.cos((index * 450.0 + System.currentTimeMillis() * this.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5);
                if (!this.hsb.isEnabled()) {
                    return new Color((int)(this.redShift1.getValue() + (this.redShift2.getValue() - this.redShift1.getValue()) * location), (int)(this.greenShift1.getValue() + (this.greenShift2.getValue() - this.greenShift1.getValue()) * location), (int)(this.blueShift1.getValue() + (this.blueShift2.getValue() - this.blueShift1.getValue()) * location));
                }
                final float[] c1 = Color.RGBtoHSB((int)this.redShift1.getValue(), (int)this.greenShift1.getValue(), (int)this.blueShift1.getValue(), null);
                final float[] c2 = Color.RGBtoHSB((int)this.redShift2.getValue(), (int)this.greenShift2.getValue(), (int)this.blueShift2.getValue(), null);
                return Color.getHSBColor(c1[0] + (c2[0] - c1[0]) * location, c1[1] + (c2[1] - c1[1]) * location, c1[2] + (c2[2] - c1[2]) * location);
            }
            case "Rainbow": {
                return Color.getHSBColor((float)((index * 100.0 + System.currentTimeMillis() * this.rgbSpeed.getValue()) / 5000.0 % 1.0), 0.8f, 1.0f);
            }
            case "Pulse": {
                final Color baseColor = new Color((int)this.redCustom.getValue(), (int)this.greenCustom.getValue(), (int)this.blueCustom.getValue(), 255);
                return ColorUtils.interpolateColor(baseColor, baseColor.darker().darker(), (float)((Math.sin((index * 450.0 + System.currentTimeMillis() * this.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5));
            }
            case "Astolfo": {
                final float pos = (float)((Math.cos((index * 450.0 + System.currentTimeMillis() * this.shiftSpeed.getValue()) / 1000.0) + 1.0) * 0.5);
                return Color.getHSBColor(0.5f + 0.4f * pos, 0.6f, 1.0f);
            }
            default: {
                return new Color((int)this.redCustom.getValue(), (int)this.greenCustom.getValue(), (int)this.blueCustom.getValue(), 255);
            }
        }
    }

    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            if (this.waterMark.isEnabled()) {
                if (Gui.clientName.getValue().length() == 0) {
                    Fonts.tahomaBold.drawSmoothString("nsanity", Fonts.tahomaBold.drawSmoothString("I", 5.0, 5.0f, this.getColor().getRGB()) + 1.0f, 5.0f, Color.white.getRGB());
                }
                else {
                    final float f = Fonts.tahomaBold.drawSmoothString(String.valueOf(Gui.clientName.getValue().charAt(0)), 5.0, 5.0f, this.getColor().getRGB()) + 1.0f;
                    if (Gui.clientName.getValue().length() > 1) {
                        Fonts.tahomaBold.drawSmoothString(Gui.clientName.getValue().substring(1), f, 5.0f, Color.white.getRGB());
                    }
                }
            }
            if (this.arrayList.isEnabled()) {
                final MinecraftFontRenderer font = Fonts.tahomaSmall;
                GL11.glPushMatrix();
                final ScaledResolution resolution = new ScaledResolution(Insanity.mc);

                List<Feature> list = Insanity.features.stream()
                        .filter(feature -> feature.getCategory() != Category.RENDER && feature.getCategory() != Category.KEYBINDS)
                        .filter(feature -> (feature.isToggled() || feature.toggledTime.getTimePassed() <= 250L))
                        .sorted(Comparator.comparing(feature -> font.getStringWidth(feature.getName()))).collect(Collectors.toList());

                Collections.reverse(list);
                float y = 2.0f;
                int x = list.size();
                for (final Feature module2 : list) {
                    --x;
                    GL11.glPushMatrix();
                    final float width = (float)(font.getStringWidth(module2.getName()) + 5.0);
                    final float translatedWidth = width * Math.max(Math.min(module2.isToggled() ? ((250.0f - module2.toggledTime.getTimePassed()) / 250.0f) : (module2.toggledTime.getTimePassed() / 250.0f), 1.0f), 0.0f);
                    GL11.glTranslated(translatedWidth, 0.0, 0.0);
                    final float height = (float)(font.getHeight() + 5);

                    font.drawSmoothCenteredString(module2.getName(), resolution.getScaledWidth() - 1 - width / 2.0f + 0.4f, y + height / 2.0f - font.getHeight() / 2.0f + 0.5f, new Color(20, 20, 20).getRGB());
                    font.drawSmoothCenteredString(module2.getName(), resolution.getScaledWidth() - 1 - width / 2.0f, y + height / 2.0f - font.getHeight() / 2.0f, this.getColor(x).getRGB(), this.getColor(x - 1).getRGB());
                    y += (font.getHeight() + 5) * Math.max(Math.min(module2.isToggled() ? (module2.toggledTime.getTimePassed() / 250.0f) : ((250.0f - module2.toggledTime.getTimePassed()) / 250.0f), 1.0f), 0.0f);
                    GL11.glPopMatrix();
                }
                x = list.size();
                y = 2.0f;
                if (this.arrayOutline.isEnabled()) {
                    final Tessellator tessellator = Tessellator.getInstance();
                    final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    GlStateManager.enableBlend();
                    GlStateManager.disableTexture2D();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    GlStateManager.shadeModel(7425);
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                    for (final Feature module3 : list) {
                        --x;
                        final float height = (font.getHeight() + 5.0f) * Math.max(Math.min(module3.isToggled() ? (module3.toggledTime.getTimePassed() / 250.0f) : ((250.0f - module3.toggledTime.getTimePassed()) / 250.0f), 1.0f), 0.0f);
                        addVertex((float)(resolution.getScaledWidth() - 1), y, (float)resolution.getScaledWidth(), y + height, this.getColor(x - 1).getRGB(), this.getColor(x).getRGB());
                        y += height;
                    }
                    tessellator.draw();
                    GlStateManager.shadeModel(7424);
                }
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }

    public static void addVertex(float left, float top, float right, float bottom, final int color, final int color2) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final float ff3 = (color2 >> 24 & 0xFF) / 255.0f;
        final float ff4 = (color2 >> 16 & 0xFF) / 255.0f;
        final float ff5 = (color2 >> 8 & 0xFF) / 255.0f;
        final float ff6 = (color2 & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.pos(left, bottom, 0.0).color(ff4, ff5, ff6, ff3).endVertex();
        worldrenderer.pos(right, bottom, 0.0).color(ff4, ff5, ff6, ff3).endVertex();
        worldrenderer.pos(right, top, 0.0).color(f4, f5, f6, f3).endVertex();
        worldrenderer.pos(left, top, 0.0).color(f4, f5, f6, f3).endVertex();
    }

    public float getHeight() {
        if (!this.arrayList.isEnabled()) {
            return 0.0f;
        }

        List<Feature> list = Insanity.features.stream()
                .filter(feature -> feature.getCategory() != Category.RENDER && feature.getCategory() != Category.KEYBINDS)
                .filter(feature -> (feature.isToggled() || feature.toggledTime.getTimePassed() <= 250L))
                .sorted(Comparator.comparing(feature -> Fonts.tahomaSmall.getStringWidth(feature.getName()))).collect(Collectors.toList());
        float y = 3.0f;
        for (final Feature module2 : list) {
            y += (Fonts.tahomaSmall.getHeight() + 5.0f) * Math.max(Math.min(module2.isToggled() ? (module2.toggledTime.getTimePassed() / 250.0f) : ((250.0f - module2.toggledTime.getTimePassed()) / 250.0f), 1.0f), 0.0f);
        }
        return y;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.isToggled()) {
            Insanity.mc.displayGuiScreen(this.clickGUI);
            this.toggle();
        }
        if(Insanity.mc.currentScreen instanceof ClickGUI){
            KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindForward.getKeyCode(), GameSettings.isKeyDown(Insanity.mc.gameSettings.keyBindForward));
            KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindBack.getKeyCode(), GameSettings.isKeyDown(Insanity.mc.gameSettings.keyBindBack));
            KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindLeft.getKeyCode(), GameSettings.isKeyDown(Insanity.mc.gameSettings.keyBindLeft));
            KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindRight.getKeyCode(), GameSettings.isKeyDown(Insanity.mc.gameSettings.keyBindRight));
            KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindJump.getKeyCode(), GameSettings.isKeyDown(Insanity.mc.gameSettings.keyBindJump));
            KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindSprint.getKeyCode(), GameSettings.isKeyDown(Insanity.mc.gameSettings.keyBindSprint));
            KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindSneak.getKeyCode(), GameSettings.isKeyDown(Insanity.mc.gameSettings.keyBindSneak));

        }
    }

    static {
        commandPrefix = new StringSetting("Prefix", ".", 1);
        clientName = new StringSetting("Client Name", "", 20);
    }
}
