package me.starzebra.gui;

import me.starzebra.Insanity;
import me.starzebra.config.ConfigManager;
import me.starzebra.features.feature.Feature;
import me.starzebra.features.feature.Keybind;
import me.starzebra.features.feature.settings.*;
import me.starzebra.mixin.MixinMinecraftAccessor;
import me.starzebra.utils.MilliTimer;
import me.starzebra.utils.RenderUtils;
import me.starzebra.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClickGUI extends GuiScreen {

    public static ArrayList<Panel> panels;
    private Feature binding;
    private Panel draggingPanel;
    private StringSetting settingString;
    private NumberSetting draggingSlider;
    private float startX;
    private float startY;
    public int guiScale;
    private final MilliTimer animationTimer;
    private static final int background;

    public ClickGUI() {
        this.binding = null;
        this.animationTimer = new MilliTimer();
        panels = new ArrayList<>();
        int pwidth = 100;
        int pheight = 20;
        int px = 100;
        int py = 10;
        for (Feature.Category c : Feature.Category.values()) {
            panels.add(new Panel(c, px, py, pwidth, pheight));
            px += pwidth + 10;
        }
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height){
        this.animationTimer.reset();
        super.setWorldAndResolution(mc,width,height);
    }

    public void handleInput() throws IOException{
        for(Panel p : panels){
            p.prevScrolling = p.scrolling;
        }
        super.handleInput();
    }

    public void handleMouseInput() throws IOException{
        super.handleMouseInput();
        int scroll = Mouse.getEventDWheel();
        if(scroll != 0){
            if(scroll > 1){
                scroll = 1;
            }
            if(scroll < 1){
                scroll = -1;
            }
            int mouseX = Mouse.getX() * this.width/this.mc.displayWidth;
            int mouseY = this.height - Mouse.getY() * this.height/this.mc.displayHeight- 1;
            this.handle(mouseX, mouseY, scroll, 0.0f, Handle.SCROLL);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        this.draggingPanel = null;
        this.draggingSlider = null;
        this.settingString = null;
        this.binding = null;
        this.handle(mouseX,mouseY, mouseButton, 0.0f, Handle.CLICK);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state){
        Keybind.saveKeybinds();
        ConfigManager.saveConfig();
        this.handle(mouseX, mouseY, state, 0.0f, Handle.RELEASE);
        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        Keybind.saveKeybinds();
        ConfigManager.saveConfig();
        if(keyCode == 1 || keyCode == Insanity.clickGui.getKeycode()){
            if(this.binding != null){
                this.binding.setKeycode(0);
                this.binding = null;
            }else if(this.settingString != null){
                this.settingString = null;
            }else{
                this.draggingPanel = null;
                this.draggingSlider = null;
                super.keyTyped(typedChar, keyCode);
            }
        }else if (this.binding != null){
            this.binding.setKeycode(keyCode);
            this.binding = null;
        }else if (this.settingString != null){
            if(keyCode == 28){
                this.settingString = null;
            }else if (keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
                this.settingString.setValue(ChatAllowedCharacters.filterAllowedCharacters((this.settingString.getValue() + ClickGUI.getClipboardString())));
            } else if (keyCode != 14) {
                this.settingString.setValue(ChatAllowedCharacters.filterAllowedCharacters((this.settingString.getValue() + typedChar)));
            } else {
                this.settingString.setValue(this.settingString.getValue().substring(0, Math.max(0, this.settingString.getValue().length() - 1)));
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.handle(mouseX,mouseY,-1, partialTicks, Handle.DRAW);
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    private void handle(int mouseX, int mouseY, int key, float partialTicks, Handle handle){
        int toggled = Insanity.clickGui.getColor().getRGB();
        float scale = 2.0f / (float) this.mc.gameSettings.guiScale;
        int prevScale = this.mc.gameSettings.guiScale;
        GL11.glPushMatrix();
        if(this.mc.gameSettings.guiScale > 2 && !Insanity.clickGui.scaleGui.isEnabled()){
            this.mc.gameSettings.guiScale = 2;
            mouseX /= (int) scale;
            mouseY /= (int) scale;
            GL11.glScaled(scale, scale, scale);
        }
        for (Panel p : panels){
            switch (handle){
                case DRAW:
                    if(this.draggingPanel == p){
                        p.x = this.startX + mouseX;
                        p.y = this.startY + mouseY;
                    }
                case CLICK:
                    if(!this.isHovered(mouseX, mouseY, p.x, p.y, p.height, p.width)) break;
                    if(key == 1){
                        this.startX = p.x - mouseX;
                        this.startY = p.y - mouseY;
                        this.draggingPanel = p;
                        this.draggingSlider = null;
                        break;
                    }
                    if(key == 0) {
                        if(p.extended){
                            p.scrolling = -this.getTotalSettingsCount(p);
                        }else{
                            p.scrolling = 0;
                        }
                        p.extended = !p.extended;
                        break;
                    }
                    break;

                case RELEASE:
                    this.draggingPanel = null;
                    this.draggingSlider = null;
                    break;
            }
            float y = p.y + p.height + 3.0f;
            int featureHeight = 15;
            y += featureHeight * (p.prevScrolling + (p.scrolling - p.prevScrolling) * ((MixinMinecraftAccessor)this.mc).getTimer().renderPartialTicks);
            List<Feature> list = Feature.getFeaturesByCategory(p.category).stream().sorted(Comparator.comparing(feature ->
                    Fonts.robotoMediumBold.getStringWidth(feature.getName()))).collect(Collectors.toList());
            Collections.reverse(list);
            for(Feature feature2 : list){
                if(feature2.isDevOnly() && !Insanity.devMode) continue;
                switch (handle){
                    case DRAW:
                        if(y < p.y) break;
                        RenderUtils.drawRect(p.x, y, p.x+p.width, y+featureHeight, feature2.isToggled() ? new Color(toggled).getRGB() : background);
                        Fonts.robotoMediumBold.drawSmoothCenteredString(
                                feature2.getName(), p.x +p.width /2.0f, y+featureHeight/2.0f-Fonts.robotoMediumBold.getHeight() /2.0f, Color.white.getRGB());
                        break;
                    case CLICK:
                        if(!this.isHovered(mouseX, mouseY, p.x, y, featureHeight, p.width)) break;
                        if(y < p.y + p.height + 3.0f) break;
                        switch (key){
                            case 1:
                                feature2.extended = !feature2.extended;
                                break;
                            case 0:
                                feature2.toggle();
                                break;
                        }
                        break;
                }

                y += featureHeight;
                if(!feature2.extended) continue;
                for(Setting setting : feature2.settings){
                    if(setting.isHidden()) continue;
                    if((handle == Handle.DRAW && y >= p.y) || (handle == Handle.CLICK && y >= p.y + p.height + 3.0f)) {
                        if(setting instanceof BooleanSetting){
                            switch (handle){
                                case DRAW:
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + (float)featureHeight, new Color(background).brighter().getRGB());
                                    RenderUtils.drawBorderedRoundedRect(p.x + p.width - 12.0f, y + (float)featureHeight / 2.0f - 4.0f, 8.0f, 8.0f, 3.0f, 3.0f, ((BooleanSetting)setting).isEnabled() ? new Color(toggled).brighter().getRGB() : new Color(background).brighter().getRGB(), new Color(toggled).brighter().getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(setting.name, p.x + 2.0f, y + (float)featureHeight / 2.0f - (float)Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                    break;
                                case CLICK:
                                    if(!this.isHovered(mouseX,mouseY, p.x+p.width-12.0f, y+featureHeight/2.0f-4.0f,8.0,8.0)) break;
                                    ((BooleanSetting) setting).toggle();
                                    break;
                            }
                        }else if (setting instanceof StringSetting) {
                            String value = this.settingString == setting ? ((StringSetting)setting).getValue() + "_" : (((StringSetting)setting).getValue() == null || ((StringSetting)setting).getValue().equals("") ? setting.name + "..." : Fonts.robotoMediumBold.trimStringToWidth(((StringSetting)setting).getValue(), (int)p.width));
                            switch (handle) {
                                case DRAW: {
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + (float)featureHeight, new Color(background).brighter().getRGB());
                                    Fonts.robotoMediumBold.drawCenteredString(value, p.x + p.width / 2.0f, y + (float)featureHeight / 2.0f - (float)Fonts.robotoMediumBold.getHeight() / 2.0f, ((StringSetting)setting).getValue() == null || ((StringSetting)setting).getValue().equals("") && this.settingString != setting ? new Color(143, 143, 143, 255).getRGB() : Color.white.getRGB());
                                    break;
                                }
                                case CLICK: {
                                    if (!this.isHovered(mouseX, mouseY, p.x, y, featureHeight, p.width)) break;
                                    switch (key) {
                                        case 0: {
                                            this.settingString = (StringSetting)setting;
                                            break;
                                        }
                                        case 1: {
                                            ((StringSetting)setting).setValue("");
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }else if (setting instanceof ModeSetting){
                            switch (handle) {
                                case DRAW: {
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + (float)featureHeight, new Color(background).brighter().getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(setting.name, p.x + 2.0f, y + (float)featureHeight / 2.0f - (float)Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(((ModeSetting)setting).getSelected(), (double)(p.x + p.width) - Fonts.robotoMediumBold.getStringWidth(((ModeSetting)setting).getSelected()) - 2.0, y + (float)featureHeight / 2.0f - (float)Fonts.robotoMediumBold.getHeight() / 2.0f, new Color(143, 143, 143, 255).getRGB());
                                    break;
                                }
                                case CLICK: {
                                    if (!this.isHovered(mouseX, mouseY, (double)(p.x + p.width) - Fonts.robotoMediumBold.getStringWidth(((ModeSetting)setting).getSelected()) - 2.0, y, featureHeight, Fonts.robotoMediumBold.getStringWidth(((ModeSetting)setting).getSelected()))) break;
                                    ((ModeSetting)setting).cycle(key);
                                    break;
                                }
                            }
                        }else if (setting instanceof NumberSetting) {
                            float percent = (float)((((NumberSetting)setting).getValue() - ((NumberSetting)setting).getMin()) / (((NumberSetting)setting).getMax() - ((NumberSetting)setting).getMin()));
                            float numberWidth = percent * p.width;
                            if (this.draggingSlider != null && this.draggingSlider == setting) {
                                double mousePercent = Math.min(1.0f, Math.max(0.0f, ((float)mouseX - p.x) / p.width));
                                double newValue = mousePercent * (((NumberSetting)setting).getMax() - ((NumberSetting)setting).getMin()) + ((NumberSetting)setting).getMin();
                                ((NumberSetting)setting).setValue(newValue);
                            }
                            switch (handle) {
                                case DRAW: {
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + (float)featureHeight, new Color(background).brighter().brighter().getRGB());
                                    RenderUtils.drawRect(p.x, y, p.x + numberWidth, y + (float)featureHeight, new Color(toggled).brighter().getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(setting.name, p.x + 2.0f, y + (float)featureHeight / 2.0f - (float)Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                    Fonts.robotoMediumBold.drawSmoothString(String.valueOf(((NumberSetting)setting).getValue()), (double)(p.x + p.width - 2.0f) - Fonts.robotoMediumBold.getStringWidth(String.valueOf(((NumberSetting)setting).getValue())), y + (float)featureHeight / 2.0f - (float)Fonts.robotoBig.getHeight() / 2.0f, Color.white.getRGB());
                                    break;
                                }
                                case CLICK: {
                                    if (!this.isHovered(mouseX, mouseY, p.x, y, featureHeight, p.width)) break;
                                    this.draggingSlider = (NumberSetting)setting;
                                    this.draggingPanel = null;
                                    break;
                                }
                            }
                        }else if(setting instanceof RunnableSetting){
                            switch (handle) {
                                case CLICK: {
                                    if (!this.isHovered(mouseX, mouseY, p.x, y, featureHeight, p.width)) break;
                                    ((RunnableSetting)setting).execute();
                                    break;
                                }
                                case DRAW: {
                                    RenderUtils.drawRect(p.x, y, p.x + p.width, y + (float)featureHeight, new Color(background).brighter().getRGB());
                                    Fonts.robotoMediumBold.drawCenteredString(setting.name, p.x + p.width / 2.0f, y + (float)featureHeight / 2.0f - (float)Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                                    break;
                                }
                            }
                        }
                    }

                    y+=featureHeight;
                }
                if ((handle == Handle.DRAW && y >= p.y) || (handle == Handle.CLICK && y >= p.y + p.height + 3.0f)){
                    String keyText = this.binding == feature2 ? "[...]" :
                            "[" + (feature2.getKeycode() >= 256 ? "  " :
                                    Keyboard.getKeyName(feature2.getKeycode()).replaceAll("NONE", "  ")) + "]";
                    switch (handle) {
                        case DRAW: {
                            RenderUtils.drawRect(p.x, y, p.x + p.width, y + (float)featureHeight, new Color(background).brighter().getRGB());
                            Fonts.robotoMediumBold.drawSmoothString("Key", p.x + 2.0f, y + (float)featureHeight / 2.0f - (float)Fonts.robotoMediumBold.getHeight() / 2.0f, Color.white.getRGB());
                            Fonts.robotoMediumBold.drawSmoothString(keyText, (double)(p.x + p.width) - Fonts.robotoMediumBold.getStringWidth(keyText) - 2.0, y + (float)featureHeight / 2.0f - (float)Fonts.robotoMediumBold.getHeight() / 2.0f, new Color(143, 143, 143, 255).getRGB());
                            break;
                        }
                        case CLICK: {
                            if (!this.isHovered(mouseX, mouseY, (double)(p.x + p.width) - Fonts.robotoMediumBold.getStringWidth(keyText) - 2.0, y, featureHeight, Fonts.robotoMediumBold.getStringWidth(keyText))) break;
                            switch (key) {
                                case 0: {
                                    this.binding = feature2;
                                    break;
                                }
                                case 1: {
                                    feature2.setKeycode(0);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                y+=featureHeight;
            }



            if (p.category == Feature.Category.KEYBINDS) {
                switch (handle) {
                    case CLICK: {
                        if (!this.isHovered(mouseX, mouseY, p.x, y, 15.0, p.width)) break;
                        Keybind keybind = new Keybind("Keybind " + (Feature.getFeaturesByCategory(Feature.Category.KEYBINDS).size() + 1));
                        Insanity.features.add(keybind);
                        MinecraftForge.EVENT_BUS.register(keybind);
                        keybind.setToggled(true);
                        break;
                    }
                    case DRAW: {
                        RenderUtils.drawRect(p.x, y, p.x + p.width, y + 15.0f, toggled);
                        Fonts.robotoMediumBold.drawSmoothCenteredString("Add new keybind", p.x + p.width / 2.0f, y + 7.0f - (float)(Fonts.robotoMediumBold.getHeight() / 2), Color.white.getRGB());
                        break;
                    }
                }
                y += 15.0f;
            }
            if(handle == Handle.DRAW) {
                RenderUtils.drawRect(p.x,p.y +3.0f, p.x +p.width, p.y + p.height+3.0f, new Color(toggled).getRGB());
                for (int i = 0; i < 4; i++) {
                    RenderUtils.drawRect(p.x, p.y + (float)i, p.x + p.width, p.y + p.height + (float)i, new Color(0, 0, 0, 40).getRGB());
                }
                RenderUtils.drawRect(p.x, p.y, p.x + p.width, p.y + p.height, new Color(toggled).darker().getRGB());
                Fonts.robotoBig.drawSmoothCenteredString(p.category.name, p.x + p.width / 2.0f, p.y + p.height / 2.0f - (float)Fonts.robotoBig.getHeight() / 2.0f, Color.white.getRGB());
                RenderUtils.drawRect(p.x, y, p.x + p.width, y + 5.0f, new Color(toggled).darker().getRGB());
            }else{
                if(handle != Handle.SCROLL || !this.isHovered(mouseX, mouseY, p.x, p.y, y-p.y, p.width)) continue;
                if(key == -1){
                    --p.scrolling;
                }else if (key == 1){
                    ++p.scrolling;
                }
                p.scrolling = Math.min(0, Math.max(p.scrolling, -this.getTotalSettingsCount(p)));
            }
        }
        GL11.glPopMatrix();
        this.guiScale = prevScale;

    }

    private int getTotalSettingsCount(final Panel panel) {
        int count = 0;
        for (final Feature module : Feature.getFeaturesByCategory(panel.category)) {
            if (module.isDevOnly() && !Insanity.devMode) {
                continue;
            }
            ++count;
            if (!module.extended) {
                continue;
            }
            for (final Setting setting : module.settings) {
                if (!setting.isHidden()) {
                    ++count;
                }
            }
            ++count;
        }
        return count;
    }

    public void onGuiClosed(){
        this.draggingPanel = null;
        this.draggingSlider = null;
        this.binding = null;
        this.settingString = null;
        super.onGuiClosed();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public boolean isHovered(int mouseX, int mouseY, double x, double y, double height, double width) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    static {
        background = Color.getHSBColor(0.0f, 0.0f, 0.1f).getRGB();
    }

    public static class Panel {
        public Feature.Category category;
        public float x;
        public float y;
        public float width;
        public float height;
        public boolean dragging;
        public boolean extended;
        public int scrolling;
        public int prevScrolling;

        public Panel(Feature.Category category, float x, float y, float width, float height) {
            this.category = category;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.extended = true;
            this.dragging = false;
        }
    }

    enum Handle {
        DRAW,
        CLICK,
        SCROLL,
        RELEASE
    }
}
