package com.fidtest.client.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ColorMixer {

    public static int getDominantColor(TextureAtlasSprite sprite) {
        int width = sprite.contents().width();
        int height = sprite.contents().height();
        int frameCount = (int) sprite.contents().getUniqueFrames().count();

        if (width <= 0 || height <= 0 || frameCount <= 0) {
            return 0xFFFFFF;
        }

        TreeMap<Integer, Integer> counts = new TreeMap<>();
        for (int f = 0; f < frameCount; f++) {
            for (int v = 0; v < height; v++) {
                for (int u = 0; u < width; u++) {
                    int rgba = sprite.getPixelRGBA(f, u, v);
                    int alpha = (rgba >> 24) & 0xFF;
                    if (alpha > 0) {
                        counts.merge(rgba & 0x00FFFFFF, 1, Integer::sum);
                    }
                }
            }
        }

        int dominantColor = 0xFFFFFF;
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                dominantColor = entry.getKey();
            }
        }

        int r = (dominantColor >> 16) & 0xFF;
        int g = (dominantColor >> 8) & 0xFF;
        int b = dominantColor & 0xFF;
        r = Math.min(255, (int) (r * 1.15F));
        g = Math.min(255, (int) (g * 1.15F));
        b = Math.min(255, (int) (b * 1.15F));
        return (r << 16) | (g << 8) | b;
    }

    public static int getMixedColor(List<Integer> colors) {
        if (colors.isEmpty()) return 0xFFFFFF;
        int r = 0, g = 0, b = 0;
        for (int c : colors) {
            r += (c >> 16) & 0xFF;
            g += (c >> 8) & 0xFF;
            b += c & 0xFF;
        }
        r /= colors.size();
        g /= colors.size();
        b /= colors.size();
        return (r << 16) | (g << 8) | b;
    }
}
