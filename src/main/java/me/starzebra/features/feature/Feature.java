package me.starzebra.features.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import me.starzebra.Insanity;
import me.starzebra.config.ConfigManager;
import me.starzebra.features.feature.settings.Setting;
import me.starzebra.utils.MilliTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Feature {

    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("toggled")
    private boolean toggled;
    @Expose
    @SerializedName("keycode")
    private int keycode;
    public boolean extended;
    private final Category category;
    @Expose
    @SerializedName("settings")
    public ConfigManager.ConfigSetting[] cfgSettings;
    private boolean devOnly;
    protected static final Minecraft mc;
    public MilliTimer toggledTime;
    public List<Setting> settings;

    public Feature(final String name, int keycode, Category category){
        this.name = name;
        this.keycode = keycode;
        this.category = category;
        this.toggledTime = new MilliTimer();
        this.settings = new ArrayList<>();
    }

    public Feature(final String name, Category category){
        this(name, 0, category);
    }

    public boolean isToggled(){
        return this.toggled;
    }

    public void toggle(){
        this.setToggled(!this.toggled);
    }

    public boolean isKeybind(){
        return false;
    }

    public void addSetting(Setting setting){
        this.getSettings().add(setting);
    }

    public void addSettings(Setting ... settings) {
        for (Setting setting : settings) {
            this.addSetting(setting);
        }
    }

    public Category getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public boolean isPressed(){
        return this.keycode != 0 && Keyboard.isKeyDown(this.keycode) && this.isKeybind();
    }

    public int getKeycode() {
        return keycode;
    }

    public void setKeycode(int keycode) {
        this.keycode = keycode;
    }

    public List<Setting> getSettings(){
        return this.settings;
    }

    public static List<Feature> getFeaturesByCategory(Category c){
        ArrayList<Feature> features = new ArrayList<>();
        for(Feature f : Insanity.features){
            if(f.getCategory() != c) continue;
            features.add(f);
        }
        return features;
    }

    public void onSave(){

    }

    public static <T> T getFeature(Class<T> module) {
        for (Feature f : Insanity.features) {
            if (f.getClass().equals(module)){
                return (T)f;
            }
        }
        return null;
    }

    public static Feature getFeature(Predicate<Feature> predicate) {
        for (Feature f : Insanity.features) {
            if (predicate.test(f)){
                return f;
            }
        }
        return null;
    }

    public static Feature getModule(final String string) {
        for (final Feature m : Insanity.features) {
            if (m.getName().equalsIgnoreCase(string)) {
                return m;
            }
        }
        return null;
    }

    public static Feature getFeatureByName(String string) {
        for (Feature f : Insanity.features) {
            if (!f.getName().equalsIgnoreCase(string)) continue;
            return f;
        }
        return null;
    }

    public void setToggled(boolean toggled) {
        if (this.toggled != toggled) {
            this.toggled = toggled;
            this.toggledTime.reset();
            if (toggled) {
                this.onEnable();
            }
            else {
                this.onDisable();
            }
        }
    }

    public void setDevOnly(boolean devOnly) {
        this.devOnly = devOnly;
    }

    protected static void sendMessage(String message){
        Insanity.mc.thePlayer.addChatMessage(new ChatComponentText("[§4I§r] "+message));
    }

    public boolean isDevOnly() {
        return devOnly;
    }

    public void onEnable(){
        if(Insanity.mc.thePlayer == null) return;
        Insanity.mc.thePlayer.addChatMessage(new ChatComponentText("[§4I§r] §d"+ name+ " §r[§aON§r]"));
    }

    public void onDisable(){
        if(Insanity.mc.thePlayer == null) return;
        Insanity.mc.thePlayer.addChatMessage(new ChatComponentText("[§4I§r] §d"+ name+ " §r[§cOFF§r]"));
    }

    static {
        mc = Minecraft.getMinecraft();
    }

    public enum Category {
        COMBAT("Combat"),
        RENDER("Render"),
        PLAYER("Player"),
        OTHER("Other"),
        KEYBINDS("Keybinds");

        public final String name;

        Category(String name){
            this.name = name;
        }
    }
}