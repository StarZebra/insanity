package me.starzebra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import me.starzebra.Insanity;
import me.starzebra.features.feature.Feature;
import me.starzebra.features.feature.Keybind;
import me.starzebra.features.feature.settings.*;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    public static String configPath;

    public static boolean loadConfig(final String configPath) {
        try {
            final String configString = new String(Files.readAllBytes(new File(configPath).toPath()));
            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            final Feature[] modules = (Feature[])gson.fromJson(configString, (Class)Feature[].class);
            for (final Feature module : Insanity.features) {
                for (final Feature configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            try {
                                module.setToggled(configModule.isToggled());
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Toggled issue");
                            }
                            module.setKeycode(configModule.getKeycode());
                            for (final Setting setting : module.settings) {
                                for (final ConfigSetting cfgSetting : configModule.cfgSettings) {
                                    if (setting != null) {
                                        if (setting.name.equals(cfgSetting.name)) {
                                            if (setting instanceof BooleanSetting) {
                                                ((BooleanSetting)setting).setEnabled((boolean)cfgSetting.value);
                                            }
                                            else if (setting instanceof ModeSetting) {
                                                ((ModeSetting)setting).setSelected((String)cfgSetting.value);
                                            }
                                            else if (setting instanceof NumberSetting) {
                                                ((NumberSetting)setting).setValue((double)cfgSetting.value);
                                            }
                                            else if (setting instanceof StringSetting) {
                                                ((StringSetting)setting).setValue((String)cfgSetting.value);
                                            }
                                        }
                                    }
                                    else {
                                        System.out.println("[I] Setting in " + module.getName() + " is null!");
                                    }
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Config Issue");
                        }
                    }
                }
            }
            for (final Feature module2 : modules) {
                if (module2.getName().startsWith("Keybind ") && Feature.getModule(module2.getName()) == null) {
                    final Keybind keybind = new Keybind(module2.getName());
                    keybind.setKeycode(module2.getKeycode());
                    keybind.setToggled(module2.isToggled());
                    for (final Setting setting2 : keybind.settings) {
                        for (final ConfigSetting cfgSetting2 : module2.cfgSettings) {
                            if (setting2.name.equals(cfgSetting2.name)) {
                                if (setting2 instanceof BooleanSetting) {
                                    ((BooleanSetting)setting2).setEnabled((boolean)cfgSetting2.value);
                                }
                                else if (setting2 instanceof ModeSetting) {
                                    ((ModeSetting)setting2).setSelected((String)cfgSetting2.value);
                                }
                                else if (setting2 instanceof NumberSetting) {
                                    ((NumberSetting)setting2).setValue((double)cfgSetting2.value);
                                }
                                else if (setting2 instanceof StringSetting) {
                                    ((StringSetting)setting2).setValue((String)cfgSetting2.value);
                                }
                            }
                        }
                    }
                    MinecraftForge.EVENT_BUS.register(keybind);
                    Insanity.features.add(keybind);
                    System.out.println("Loaded Keybind: " + keybind.getName());
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }

    public static void loadConfig() {
        loadConfig(Insanity.mc.mcDataDir + "/config/Insanity/Insanity.json");
    }

    public static void saveConfig() {
        saveConfig(Insanity.mc.mcDataDir + "/config/Insanity/Insanity.json", false);
    }

    public static boolean saveConfig(final String configPath, final boolean openExplorer) {
        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        for (final Feature feature : Insanity.features) {
            feature.onSave();
            final List<ConfigSetting> settings = new ArrayList<>();
            for (final Setting setting : feature.settings) {
                final ConfigSetting cfgSetting = new ConfigSetting(null, null);
                cfgSetting.name = setting.name;
                if (setting instanceof BooleanSetting) {
                    cfgSetting.value = ((BooleanSetting)setting).isEnabled();
                }
                else if (setting instanceof ModeSetting) {
                    cfgSetting.value = ((ModeSetting)setting).getSelected();
                }
                else if (setting instanceof NumberSetting) {
                    cfgSetting.value = ((NumberSetting)setting).getValue();
                }
                else if (setting instanceof StringSetting) {
                    cfgSetting.value = ((StringSetting)setting).getValue();
                }
                settings.add(cfgSetting);
            }
            feature.cfgSettings = settings.toArray(new ConfigSetting[0]);
        }
        try {
            final File file = new File(configPath);
            Files.write(file.toPath(), gson.toJson(Insanity.features).getBytes(StandardCharsets.UTF_8));
            if (openExplorer) {
                try {
                    openExplorer();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }

    public static void openExplorer() throws IOException {
        Desktop.getDesktop().open(new File(ConfigManager.configPath));
    }

    static {
        ConfigManager.configPath = Insanity.mc.mcDataDir.getPath() + "/config/Insanity/configs";
    }

    public static class ConfigSetting
    {
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("value")
        public Object value;

        public ConfigSetting(final String name, final Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
