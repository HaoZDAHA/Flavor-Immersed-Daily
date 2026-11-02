package com.fidtest;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

// 开局是否发放教程卷轴
    public static final ModConfigSpec.BooleanValue GIVE_STARTBOOK_ON_JOIN = BUILDER
            .comment("玩家首次进入存档时是否发放教程卷轴，默认 true；关闭后不再发放")
            .define("giveStartbookOnJoin", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
