package me.starzebra.utils;

import java.awt.*;

public class ColorUtils {

    public static Color rainbow(int delay){
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + delay) / 20);
        return Color.getHSBColor((float) ((rainbowState %= 360.0)/ 360.0), 1.0f, 1.0f );
    }

    public static Color interpolateColor(Color color1, Color color2, float value) {
        return new Color((int)((float)color1.getRed() + (float)(color2.getRed() - color1.getRed()) * value), (int)((float)color1.getGreen() + (float)(color2.getGreen() - color1.getGreen()) * value), (int)((float)color1.getBlue() + (float)(color2.getBlue() - color1.getBlue()) * value));
    }
}

