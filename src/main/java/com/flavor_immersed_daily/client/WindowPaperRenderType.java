package com.flavor_immersed_daily.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

// 窗纸自定义 RenderType — 逐像素渲染，无需纹理
// 参照 MCPaint 的 CANVAS RenderType 实现
public class WindowPaperRenderType extends RenderStateShard {

    public static final RenderType WINDOWPAPER = RenderType.create(
            "windowpaper",
            DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            65536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(ShaderStateShard.POSITION_COLOR_LIGHTMAP_SHADER)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(false)
    );

    private WindowPaperRenderType(String name, Runnable setupTask, Runnable clearTask) {
        super(name, setupTask, clearTask);
        throw new UnsupportedOperationException("No instances");
    }
}