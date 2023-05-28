package me.starzebra.utils;

public class MilliTimer {
    private long time = System.currentTimeMillis();

    public long getTime() {
        return this.time;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - this.time;
    }

    public boolean hasTimePassed(long milliseconds) {
        return System.currentTimeMillis() - this.time >= milliseconds;
    }

    public void updateTime() {
        this.time = System.currentTimeMillis();
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }
    public void reset(final long time) {
        this.time = System.currentTimeMillis() - time;
    }
}
