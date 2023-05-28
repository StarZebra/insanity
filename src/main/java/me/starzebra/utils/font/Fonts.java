package me.starzebra.utils.font;

import me.starzebra.Insanity;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Fonts {

    private static Map<String, Font> fontCache;
    public static MinecraftFontRenderer robotoMedium;
    public static MinecraftFontRenderer robotoMediumBold;
    public static MinecraftFontRenderer robotoBig;
    public static MinecraftFontRenderer robotoSmall;
    public static MinecraftFontRenderer tahoma;
    public static MinecraftFontRenderer tahomaBold;
    public static MinecraftFontRenderer tahomaSmall;

    private Fonts() {
    }

    private static Font getFont(String location, int size) {
        Font font;
        try {
            if (Fonts.fontCache.containsKey(location)) {
                font = Fonts.fontCache.get(location).deriveFont(0, size);
            } else {
                InputStream is = Insanity.mc.getResourceManager().getResource(new ResourceLocation("insanity", "fonts/" + location)).getInputStream();
                font = Font.createFont(0, is);
                Fonts.fontCache.put(location, font);
                font = font.deriveFont(0, size);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static void bootstrap() {
        Fonts.robotoMediumBold = new MinecraftFontRenderer(Fonts.getFont("robotoMedium.ttf", 19), true, false);
        Fonts.robotoBig = new MinecraftFontRenderer(Fonts.getFont("robotoMedium.ttf", 20), true, false);
        Fonts.tahomaBold = new MinecraftFontRenderer(Fonts.getFont( "TAHOMAB0.ttf", 22), true, false);
        Fonts.tahoma = new MinecraftFontRenderer(Fonts.getFont( "TAHOMA_0.ttf", 22), true, false);
        Fonts.tahomaSmall = Fonts.robotoMediumBold;
        Fonts.robotoSmall = new MinecraftFontRenderer(Fonts.getFont( "openSans.ttf", 22), true, false);
    }

    static {
        Fonts.fontCache = new HashMap<>();
    }
}
