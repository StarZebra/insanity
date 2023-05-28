package me.starzebra.utils.font;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class MinecraftFontRenderer extends CFont{

    CFont.CharData[] boldChars = new CFont.CharData[256];
    CFont.CharData[] italicChars = new CFont.CharData[256];
    CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    int[] colorCode = new int[32];
    String colorcodeIdentifiers = "0123456789abcdefklmnor";
    DynamicTexture texBold;
    DynamicTexture texItalic;
    DynamicTexture texItalicBold;

    public MinecraftFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public int drawStringWithShadow(String text, double x2, double y2, int color) {
        float shadowWidth = this.drawString(text, x2 + (double)0.9f, y2 + 0.5, color, true, 8.3f);
        return (int)Math.max(shadowWidth, this.drawString(text, x2, y2, color, false, 8.3f));
    }

    public int drawSmoothStringWithShadow(String text, double x2, double y2, int color) {
        float shadowWidth = this.drawSmoothString(text, x2 + (double)0.9f, y2 + 0.5, color, true);
        return (int)Math.max(shadowWidth, this.drawSmoothString(text, x2, y2, color, false));
    }

    public int drawSmoothCenteredStringWithShadow(String text, double x2, double y2, int color) {
        float shadowWidth = this.drawSmoothString(text, x2 + (double)0.9f, y2 + 0.5, color, true);
        return (int)Math.max(shadowWidth, this.drawSmoothString(text, x2, y2, color, false));
    }

    public int drawString(String text, double x2, double y2, int color) {
        return (int)this.drawString(text, x2, y2, color, false, 8.3f);
    }

    public int drawNoBSString(String text, double x2, float y2, int color) {
        return (int)this.drawNoBSString(text, x2, y2, color, false);
    }

    public int drawSmoothString(String text, double x2, float y2, int color) {
        return (int)this.drawSmoothString(text, x2, y2, color, false);
    }

    public float drawSmoothCenteredString(String text, float x2, float y2, int color) {
        return this.drawSmoothString(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color);
    }

    public float drawSmoothCenteredString(final String text, final float x2, final float y2, final int topColor, final int bottomColor) {
        return this.drawSmoothMultiColoredStringTB(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, topColor, bottomColor, false);
    }

    public float drawSmoothMultiColoredStringTB(final String text, double x, double y, final int topColor, final int bottomColor, final boolean shadow) {
        --x;
        if (text == null) {
            return 0.0f;
        }
        final CharData[] currentData = this.charData;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glShadeModel(7425);
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character < currentData.length) {
                GL11.glShadeModel(7425);
                GL11.glBegin(4);
                this.drawMultiColoredCharTB(currentData, character, (float)x, (float)y, topColor, bottomColor);
                GL11.glEnd();
                GL11.glShadeModel(7424);
                x += currentData[character].width - 8.3f + this.charOffset;
            }
        }
        GL11.glHint(3155, 4352);
        GL11.glShadeModel(7424);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }

    public float drawCenteredString(String text, float x2, float y2, int color) {
        return this.drawString(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color);
    }

    public float drawNoBSCenteredString(String text, float x2, float y2, int color) {
        return this.drawNoBSString(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color);
    }

    public float drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
        return this.drawStringWithShadow(text, x2 - (float)(this.getStringWidth(text) / 2.0), y2, color);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow, float kerning) {
        x -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        if (color == 0x20FFFFFF) {
            color = 0xFFFFFF;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(((float)(color >> 16 & 0xFF) / 255.0f), ((float)(color >> 8 & 0xFF) / 255.0f), ((float)(color & 0xFF) / 255.0f), alpha);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
        for (int index = 0; index < text.length(); ++index) {
            char character = text.charAt(index);
            if (character == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color(((float)(colorcode >> 16 & 0xFF) / 255.0f), ((float)(colorcode >> 8 & 0xFF) / 255.0f), ((float)(colorcode & 0xFF) / 255.0f), alpha);
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texBold.getGlTextureId());
                        currentData = this.boldChars;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                        currentData = this.italicChars;
                    }
                } else {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color(((float)(color >> 16 & 0xFF) / 255.0f), ((float)(color >> 8 & 0xFF) / 255.0f), ((float)(color & 0xFF) / 255.0f), alpha);
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                }
                ++index;
                continue;
            }
            if (character >= currentData.length) continue;
            GL11.glBegin(4);
            this.drawChar(currentData, character, (float)x, (float)y);
            GL11.glEnd();
            if (strikethrough) {
                this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0, y + (double)(currentData[character].height / 2), 1.0f);
            }
            if (underline) {
                this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
            }
            x += ((float)currentData[character].width - kerning + (float)this.charOffset);
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }

    public float drawSmoothString(String text, double x, double y, int color, boolean shadow) {
        x -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(((float)(color >> 16 & 0xFF) / 255.0f), ((float)(color >> 8 & 0xFF) / 255.0f), ((float)(color & 0xFF) / 255.0f), alpha);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        for (int index = 0; index < text.length(); ++index) {
            char character = text.charAt(index);
            if (character == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    if (!shadow) {
                        GlStateManager.color(((float)(colorcode >> 16 & 0xFF) / 255.0f), ((float)(colorcode >> 8 & 0xFF) / 255.0f), ((float)(colorcode & 0xFF) / 255.0f), alpha);
                    }
                } else if (colorIndex != 16) {
                    if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            GL11.glBindTexture(3553, this.texItalicBold.getGlTextureId());
                            GL11.glTexParameteri(3553, 10241, 9729);
                            GL11.glTexParameteri(3553, 10240, 9729);
                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.bindTexture(this.texBold.getGlTextureId());
                            GL11.glBindTexture(3553, this.texBold.getGlTextureId());
                            GL11.glTexParameteri(3553, 10241, 9729);
                            GL11.glTexParameteri(3553, 10240, 9729);
                            currentData = this.boldChars;
                        }
                    } else if (colorIndex == 18) {
                        strikethrough = true;
                    } else if (colorIndex == 19) {
                        underline = true;
                    } else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            GL11.glBindTexture(3553, this.texItalicBold.getGlTextureId());
                            GL11.glTexParameteri(3553, 10241, 9729);
                            GL11.glTexParameteri(3553, 10240, 9729);
                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                            GL11.glBindTexture(3553, this.texItalic.getGlTextureId());
                            GL11.glTexParameteri(3553, 10241, 9729);
                            GL11.glTexParameteri(3553, 10240, 9729);
                            currentData = this.italicChars;
                        }
                    } else {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.color(((float)(color >> 16 & 0xFF) / 255.0f), ((float)(color >> 8 & 0xFF) / 255.0f), ((float)(color & 0xFF) / 255.0f), alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                    }
                }
                ++index;
                continue;
            }
            if (character >= currentData.length) continue;
            GL11.glBegin(4);
            this.drawChar(currentData, character, (float)x, (float)y);
            GL11.glEnd();
            if (strikethrough) {
                this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0, y + (double)(currentData[character].height / 2), 1.0f);
            }
            if (underline) {
                this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
            }
            x += ((float)currentData[character].width - 8.3f + (float)this.charOffset);
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }

    public float drawNoBSString(String text, double x, double y, int color, boolean shadow) {
        x -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(((float)(color >> 16 & 0xFF) / 255.0f), ((float)(color >> 8 & 0xFF) / 255.0f), ((float)(color & 0xFF) / 255.0f), alpha);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        for (int index = 0; index < text.length(); ++index) {
            char character = text.charAt(index);
            if (character == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color(((float)(colorcode >> 16 & 0xFF) / 255.0f), ((float)(colorcode >> 8 & 0xFF) / 255.0f), ((float)(colorcode & 0xFF) / 255.0f), alpha);
                } else if (colorIndex != 16) {
                    if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.bindTexture(this.texBold.getGlTextureId());
                            currentData = this.boldChars;
                        }
                    } else if (colorIndex == 18) {
                        strikethrough = true;
                    } else if (colorIndex == 19) {
                        underline = true;
                    } else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                            currentData = this.italicChars;
                        }
                    } else {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.color(((float)(color >> 16 & 0xFF) / 255.0f), ((float)(color >> 8 & 0xFF) / 255.0f), ((float)(color & 0xFF) / 255.0f), alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                    }
                }
                ++index;
                continue;
            }
            if (character >= currentData.length) continue;
            GL11.glBegin(4);
            this.drawChar(currentData, character, (float)x, (float)y);
            GL11.glEnd();
            if (strikethrough) {
                this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0, y + (double)(currentData[character].height / 2), 1.0f);
            }
            if (underline) {
                this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
            }
            x += ((float)currentData[character].width - 8.3f + (float)this.charOffset);
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (float)x / 2.0f;
    }

    public double getStringWidth(String text) {
        if (text == null) {
            return 0.0;
        }
        float width = 0.0f;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        for (int index = 0; index < text.length(); ++index) {
            char character = text.charAt(index);
            if (character == '§') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                ++index;
                continue;
            }
            if (character >= currentData.length) continue;
            width += (float)currentData[character].width - 8.3f + (float)this.charOffset;
        }
        return width / 2.0f;
    }

    public double getStringWidth(String text, float kerning) {
        if (text == null) {
            return 0.0;
        }
        float width = 0.0f;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        for (int index = 0; index < text.length(); ++index) {
            char c = text.charAt(index);
            if (c == '§') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(c);
                ++index;
                continue;
            }
            if (c >= currentData.length) continue;
            width += (float)currentData[c].width - kerning + (float)this.charOffset;
        }
        return width / 2.0f;
    }

    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void drawLine(double x2, double y2, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public ArrayList<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if (this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            StringBuilder currentWord = new StringBuilder();
            char lastColorCode = '￿';
            for (String word : words) {
                for (int innerIndex = 0; innerIndex < word.toCharArray().length; ++innerIndex) {
                    char c = word.toCharArray()[innerIndex];
                    if (c != '§' || innerIndex >= word.toCharArray().length - 1) continue;
                    lastColorCode = word.toCharArray()[innerIndex + 1];
                }
                StringBuilder stringBuilder = new StringBuilder();
                if (this.getStringWidth(stringBuilder.append((Object)currentWord).append(word).append(" ").toString()) < width) {
                    currentWord.append(word).append(" ");
                    continue;
                }
                finalWords.add(currentWord.toString());
                currentWord = new StringBuilder("§" + lastColorCode + word + " ");
            }
            if (currentWord.length() > 0) {
                if (this.getStringWidth(currentWord.toString()) < width) {
                    finalWords.add("§" + lastColorCode + currentWord + " ");
                    currentWord = new StringBuilder();
                } else {
                    finalWords.addAll(this.formatString(currentWord.toString(), width));
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public ArrayList<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        StringBuilder currentWord = new StringBuilder();
        char lastColorCode = '￿';
        char[] chars = string.toCharArray();
        for (int index = 0; index < chars.length; ++index) {
            char c = chars[index];
            if (c == '§' && index < chars.length - 1) {
                lastColorCode = chars[index + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (this.getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
                currentWord.append(c);
                continue;
            }
            finalWords.add(currentWord.toString());
            currentWord = new StringBuilder("§" + lastColorCode + c);
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord.toString());
        }
        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index & 1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    private float getCharWidthFloat(char c) {
        if (c == '§') {
            return -1.0f;
        }
        if (c == ' ') {
            return 2.0f;
        }
        if (this.charData[c] == null) {
            return 0.0f;
        }
        int var2 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c);
        if (c > '\u0000' && var2 != -1) {
            return (float)this.charData[var2].width / 2.0f - 4.0f;
        }
        if ((float)this.charData[c].width / 2.0f - 4.0f != 0.0f) {
            int var3 = (int)((float)this.charData[c].width / 2.0f - 4.0f) >>> 4;
            int var4 = (int)((float)this.charData[c].width / 2.0f - 4.0f) & 0xF;
            return (++var4 - (var3 &= 0xF)) / 2 + 1;
        }
        return 0.0f;
    }

    public String trimStringToWidth(String text, int width, boolean custom) {
        StringBuilder buffer = new StringBuilder();
        float lineWidth = 0.0f;
        int offset = custom ? text.length() - 1 : 0;
        int increment = custom ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;
        for (int index = offset; index >= 0 && index < text.length() && lineWidth < (float)width; index += increment) {
            char character = text.charAt(index);
            float charWidth = this.getCharWidthFloat(character);
            if (var8) {
                var8 = false;
                if (character != 'l' && character != 'L') {
                    if (character == 'r' || character == 'R') {
                        var9 = false;
                    }
                } else {
                    var9 = true;
                }
            } else if (charWidth < 0.0f) {
                var8 = true;
            } else {
                lineWidth += charWidth;
                if (var9) {
                    lineWidth += 1.0f;
                }
            }
            if (lineWidth > (float)width) break;
            if (custom) {
                buffer.insert(0, character);
                continue;
            }
            buffer.append(character);
        }
        return buffer.toString();
    }
}
