package me.starzebra.features.feature;

import me.starzebra.features.feature.settings.StringSetting;

public class CustomName extends Feature{

    public StringSetting name;

    public CustomName() {
        super("Custom Name", Category.OTHER);
        this.name = new StringSetting("Name");
        this.addSetting(this.name);
    }
}
