package me.starzebra.features.feature.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.function.Predicate;

public class Setting {

    @Expose
    @SerializedName("name")
    public String name;
    private final Predicate<Boolean> predicate;

    protected Setting(final String name, final Predicate<Boolean> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    protected Setting(final String name) {
        this(name, null);
    }

    public boolean isHidden() {
        return this.predicate != null && this.predicate.test(true);
    }
}
