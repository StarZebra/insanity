package me.starzebra.utils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundRect(float left, float top, float right, float bottom, float radius, int color) {
        if ((left += radius) < (right -= radius)) {
            float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.color(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((right - radius), (top - radius), 0.0).endVertex();
        worldrenderer.pos(right, (top - radius), 0.0).endVertex();
        worldrenderer.pos(right, (bottom + radius), 0.0).endVertex();
        worldrenderer.pos((right - radius), (bottom + radius), 0.0).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, (top - radius), 0.0).endVertex();
        worldrenderer.pos((left + radius), (top - radius), 0.0).endVertex();
        worldrenderer.pos((left + radius), (bottom + radius), 0.0).endVertex();
        worldrenderer.pos(left, (bottom + radius), 0.0).endVertex();
        tessellator.draw();
        RenderUtils.drawArc(right, bottom + radius, radius, 180);
        RenderUtils.drawArc(left, bottom + radius, radius, 90);
        RenderUtils.drawArc(right, top - radius, radius, 270);
        RenderUtils.drawArc(left, top - radius, radius, 0);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void drawArc(float x, float y, float radius, int angleStart) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        GlStateManager.translate(x, y, 0.0);
        worldrenderer.pos(0.0, 0.0, 0.0).endVertex();
        int points = 21;
        for (double i = 0.0; i < (double)points; i += 1.0) {
            double radians = Math.toRadians(i / (double)points * 90.0 + (double)angleStart);
            worldrenderer.pos((double)radius * Math.sin(radians), (double)radius * Math.cos(radians), 0.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.translate((-x), (-y), 0.0);
    }


    public static void drawBorderedRoundedRect(float x, float y, float width, float height, float radius, float linewidth, int insideC, int borderC) {
        RenderUtils.drawRoundRect(x, y, x + width, y + height, radius, insideC);
        RenderUtils.drawOutlinedRoundedRect(x, y, width, height, radius, linewidth, borderC);
    }

    public static void drawOutlinedRoundedRect(float x, float y, float width, float height, float radius, float linewidth, int color) {
        int i;
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.color(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0f;
        y *= 2.0f;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glLineWidth(linewidth);
        GL11.glDisable(3553);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * -1.0f)), ((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * -1.0f)));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * -1.0f)), (y1 - (double)radius + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * -1.0f)));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((x1 - (double)radius + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (y1 - (double)radius + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((x1 - (double)radius + Math.sin((double)i * Math.PI / 180.0) * (double)radius), ((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
