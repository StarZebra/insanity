package me.starzebra.features.feature;

import me.starzebra.Insanity;
import me.starzebra.features.feature.settings.*;
import me.starzebra.utils.MilliTimer;
import net.minecraftforge.common.MinecraftForge;

import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Keybind extends Feature{
    public ModeSetting button;
    public ModeSetting mode;
    public NumberSetting delay;
    public NumberSetting clickCount;
    public BooleanSetting fromInv;
    public RunnableSetting remove;
    public StringSetting item;
    private boolean wasPressed;
    private final MilliTimer delayTimer;
    private boolean isEnabled;


    public Keybind(String name) {
        super(name, Category.KEYBINDS);
        this.button = new ModeSetting("Button", "Right", "Right", "Left", "Swing");
        this.mode = new ModeSetting("Mode", "Normal", "Normal", "Rapid", "Toggle");
        this.delay = new NumberSetting("Delay", 50.0, 0.0, 5000.0, 1.0);
        this.clickCount = new NumberSetting("Click Count", 1.0, 1.0, 64.0, 1.0);
        this.item = new StringSetting("Item");
        this.fromInv = new BooleanSetting("From inventory", false);
        this.remove = new RunnableSetting("Remove keybinding", () -> {
            this.setToggled(false);
            Insanity.features.remove(this);
            MinecraftForge.EVENT_BUS.unregister(this);
        });
        this.delayTimer = new MilliTimer();
        this.addSettings(this.item, this.button, this.mode, this.delay, this.fromInv, this.remove);
    }

    @Override
    public String getName() {
        return this.item.getValue().equals("") ? "Keybind " + (Feature.getFeaturesByCategory(Feature.Category.KEYBINDS).indexOf(this) + 1) : this.item.getValue();
    }

    @Override
    public boolean isKeybind() {
        return true;
    }

    public static void saveKeybinds() {
        try {
            File insanityKeybinds = new File(Insanity.mc.mcDataDir.getPath() + "/config/Insanity/InsanityKeybinds.cfg");
            if (!insanityKeybinds.exists()) {
                insanityKeybinds.createNewFile();
            } else {
                DataOutputStream dataOutputStream = new DataOutputStream(Files.newOutputStream(Paths.get(Insanity.mc.mcDataDir.getPath() + "/config/Insanity/InsanityKeybinds.cfg")));
                List<Feature> keybinds = Feature.getFeaturesByCategory(Feature.Category.KEYBINDS);
                dataOutputStream.writeInt(keybinds.size());
                for (Feature keybind : keybinds) {
                    dataOutputStream.writeUTF(keybind.name);
                }
                dataOutputStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
