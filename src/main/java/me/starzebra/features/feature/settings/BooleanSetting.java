package me.starzebra.features.feature.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.function.Predicate;

public class BooleanSetting extends Setting{

    @Expose
    @SerializedName("value")
    private boolean enabled;

    public BooleanSetting(String name, boolean enabled){
        super(name);
        this.enabled = enabled;
    }

    public BooleanSetting(String name, boolean enabled, Predicate<Boolean> isHidden) {
        super(name, isHidden);
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle(){
        this.setEnabled(!this.enabled);
    }
}
