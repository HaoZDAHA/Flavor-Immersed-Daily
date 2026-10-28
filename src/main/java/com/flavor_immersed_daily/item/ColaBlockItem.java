package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

// 可乐罐方块物品 — 右键可乐方块本体时不放置新方块（返回 PASS，交给方块自身的升级/降级逻辑），
// 右键其他位置时正常放置。
public class ColaBlockItem extends BlockItem {
    private final String blockId;

    public ColaBlockItem(Block block, String blockId, Properties properties) {
        super(block, properties);
        this.blockId = blockId;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Block colaBlock = BuiltInRegistries.BLOCK.get(
                ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, blockId));
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.is(colaBlock)) {
            return InteractionResult.PASS;
        }
        return super.useOn(context);
    }

// 可乐是饮品：食用动画改为「喝」而不是「吃」
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}
