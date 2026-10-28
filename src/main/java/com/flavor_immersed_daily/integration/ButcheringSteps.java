package com.flavor_immersed_daily.integration;

import com.flavor_immersed_daily.all.ModItems;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

// 屠宰配方步骤元数据（共享给 JEI / EMI 展示使用）。
// 掉落物本身来自 com.flavor_immersed_daily.config.Config#getDrops(int, int)
// （即 JSON 默认掉落表 + config 额外产物），这里只描述"输入动物、所需刀具、阶段名"。
public final class ButcheringSteps {

    private ButcheringSteps() {
    }

// 一次屠宰动作：输入动物、动物类型、阶段、所需刀具、阶段名称。
    public record Step(Item input, int animalType, int stage, Item tool, String stageName) {
    }

// 全部屠宰步骤（牛/羊/猪：1→5 连续；鸡：1→2、5→6 特殊流程）。
    public static List<Step> all() {
        List<Step> steps = new ArrayList<>();
// 牛/羊/猪：放血→剥皮→剔骨→掏空→切肉
        Item[] tools = {ModItems.WIDEEDGEDKNIFE.get(), ModItems.SHARPKNIFE.get(),
                ModItems.BONECUTTERKNIFE.get(), ModItems.SHARPKNIFE.get(), ModItems.SHARPKNIFE.get()};
        String[] names = {"放血", "剥皮", "剔骨", "掏空", "切肉"};
        addAnimal(steps, ModItems.DEADCATTLE.get(), 1, tools, names);
        addAnimal(steps, ModItems.DEADSHEEP.get(), 2, tools, names);
        addAnimal(steps, ModItems.DEADPIG.get(), 3, tools, names);

// 鸡（特殊流程）
        steps.add(new Step(ModItems.DEADCHICKEN.get(), 4, 1, ModItems.WIDEEDGEDKNIFE.get(), "放血"));
        steps.add(new Step(ModItems.CHICKENWITHOUTFEATHER.get(), 4, 5, ModItems.SHARPKNIFE.get(), "掏空"));
        steps.add(new Step(ModItems.DEADCHICKEN.get(), 4, 6, ModItems.SHARPKNIFE.get(), "切割"));
        return steps;
    }

    private static void addAnimal(List<Step> steps, Item input, int animalType, Item[] tools, String[] names) {
        for (int stage = 1; stage <= tools.length; stage++) {
            steps.add(new Step(input, animalType, stage, tools[stage - 1], names[stage - 1]));
        }
    }
}