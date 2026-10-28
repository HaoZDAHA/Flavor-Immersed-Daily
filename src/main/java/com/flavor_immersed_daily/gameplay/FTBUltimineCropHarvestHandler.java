package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import com.flavor_immersed_daily.block.block.fruit.GrapeBlock;
import dev.ftb.mods.ftbultimine.api.rightclick.RegisterRightClickHandlerEvent;
import dev.ftb.mods.ftbultimine.api.rightclick.RightClickHandler;
import dev.ftb.mods.ftbultimine.api.shape.ShapeContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

// FTB Ultimine 连锁收割适配。
// FTB 的 CropHarvesting 内置处理器只有在连锁 matcher 为 MATCH_BY_CROP_TYPE 时
// 才会工作，而默认配置下定形形状（方阵/隧道等）总是优先匹配 MATCH_BY_TAGS_SHAPED，
// 导致连锁右键收割对任何作物（包括原版小麦）都零掉落。这里注册一个自定义
// RightClickHandler，它排在内置 CropHarvesting 之后；当内置处理器返回 0 时，
// 由本处理器遍历 FTB 选中的全部方块，对成熟的 FIDCropBlock 执行与单株右键
// 完全一致的 FIDCropBlock#harvest（3 个果实 + 1 个种子）。
// 本类不强依赖 FTB：所有对 FTB 类型的使用都集中在这里，并且只有 FTB Ultimine
// 已加载时（见 #register() 的调用方）类才会被加载。
public enum FTBUltimineCropHarvestHandler implements RightClickHandler {
    INSTANCE;

// 向 FTB 注册本处理器。仅当 ModList.isLoaded("ftbultimine") 时才应调用，
// 缺失 FTB 时不会加载本类，从而避免 NoClassDefFoundError。
    public static void register() {
        RegisterRightClickHandlerEvent.REGISTER.register(
                dispatcher -> dispatcher.registerHandler(INSTANCE));
    }

    @Override
    public int handleRightClickBlock(ShapeContext shapeContext, InteractionHand hand,
                                     Collection<BlockPos> positions) {
        int count = 0;
        Level level = shapeContext.player().level();
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof FIDCropBlock crop
                    && state.getValue(crop.getAgeProperty()) >= crop.getMaxAge()) {
                crop.harvest(state, level, pos);
                count++;
            } else if (state.getBlock() instanceof GrapeBlock grape
                    && state.getValue(GrapeBlock.STAGE) >= GrapeBlock.MAX_STAGE) {
// 爬架作物（葡萄/番茄/黄瓜等）：成熟果实同样支持连锁收获
                grape.harvest(state, level, pos);
                count++;
            }
        }
        return count;
    }
}
