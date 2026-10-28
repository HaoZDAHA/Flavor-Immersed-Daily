package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.item.FillingFoodItem;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.HashMap;
import java.util.Map;

// 熟月饼"露馅"染色。
//
// 四个熟月饼（广式/苏式/京式/冰皮）的纹理在露馅区（u∈[4,7]、v∈[9,13]）本来就画了
// 灰色占位像素；物品模型用原版 item/generated 的两层：
// <ul>
// <li>layer0 = 月饼本体纹理，tintIndex=0，不染色；</li>
// <li>layer1 = 露馅遮罩纹理 item/fillingrender，tintIndex=1，按馅料染色。</li>
// </ul>
// 遮罩的白色形状与本体纹理的灰像素位置完全重合，所以染上色后正好盖住灰色区域，
// 达到"只在该位置渲染馅料色块"的效果。
//
// 染色方式与 list 模组月饼一致（原版 item/generated 双层 + ItemColor），
// 区别是颜色不写死，而是读取 NBT "filling" 指向的馅料物品，取该物品纹理的平均色。
//
// 之所以能"每个月饼不同颜色"，是因为 ItemColor 的回调带 ItemStack，
// 可以按堆叠各自的 NBT 计算颜色。
@OnlyIn(Dist.CLIENT)
public final class MooncakeFillingColors {

// layer1（露馅遮罩层）对应的 tintIndex
    private static final int FILLING_TINT_INDEX = 1;

// 不染色（纯白乘算）
    private static final int NO_TINT = 0xFFFFFFFF;

// 馅料物品 → 其纹理平均色；纹理重载后像素数据会变，需清空
    private static final Map<Item, Integer> FILLING_COLORS = new HashMap<>();

    private MooncakeFillingColors() {
    }

// 注册四个熟月饼的染色处理器
    public static void register(RegisterColorHandlersEvent.Item event) {
        event.register(MooncakeFillingColors::getColor,
                ModItems.CANTONESE_MOONCAKE.get(),
                ModItems.SUZHOU_MOONCAKE.get(),
                ModItems.BEIJING_MOONCAKE.get(),
                ModItems.SNOW_SKIN_MOONCAKE.get());
    }

// 资源重载后清空颜色缓存
    public static void onReloaded() {
        FILLING_COLORS.clear();
    }

    private static int getColor(ItemStack stack, int tintIndex) {
// layer0（饼皮）不染色
        if (tintIndex != FILLING_TINT_INDEX) {
            return NO_TINT;
        }
        Item filling = FillingFoodItem.getFillingItem(stack);
// 没有馅料：保持遮罩纹理自身的灰色占位外观
        if (filling == null) {
            return NO_TINT;
        }
        return FILLING_COLORS.computeIfAbsent(filling, MooncakeFillingColors::averageTextureColor);
    }

// 取馅料物品主纹理（layer0，即粒子图标）中所有不透明像素的平均色
    private static int averageTextureColor(Item filling) {
        BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(filling);
        TextureAtlasSprite sprite = model == null ? null : model.getParticleIcon();
        NativeImage image = sprite == null ? null : sprite.contents().getOriginalImage();
        if (image == null) {
            FlavorImmersedDaily.LOGGER.warn("[FID] mooncake filling texture unavailable: {}",
                    filling);
            return NO_TINT;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        long red = 0;
        long green = 0;
        long blue = 0;
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
// NativeImage 像素为 ABGR 打包：A<<24 | B<<16 | G<<8 | R
                int pixel = image.getPixelRGBA(x, y);
                if (((pixel >>> 24) & 0xFF) <= 128) {
// 跳过透明像素
                    continue;
                }
                red += pixel & 0xFF;
                green += (pixel >>> 8) & 0xFF;
                blue += (pixel >>> 16) & 0xFF;
                count++;
            }
        }
        if (count == 0) {
            return NO_TINT;
        }

        int color = 0xFF000000
                | ((int) (red / count) << 16)
                | ((int) (green / count) << 8)
                | (int) (blue / count);
        FlavorImmersedDaily.LOGGER.info("[FID] mooncake filling {} -> color #{}",
                filling, String.format("%06X", color & 0xFFFFFF));
        return color;
    }
}
