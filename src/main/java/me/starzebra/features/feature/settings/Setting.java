package me.starzebra.features.feature.settings;

import java.util.function.Predicate;

public class Setting {

    public String name;
    private boolean hidden;
    private final Predicate<Boolean> predicate;

    protected Setting(final String name, final Predicate<Boolean> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    protected Setting(final String name) {
        this(name, null);
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return this.predicate != null && this.predicate.test(true);
    }
}
