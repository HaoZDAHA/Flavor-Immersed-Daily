package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.client.tooltip.FillingTooltip;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

// 带馅料口味的食物（生/熟包子、饺子、馄饨、汤圆、月饼、馅饼、粽子等）。
// NBT 标签 "filling"（存于物品 NBT）记录馅料的物品注册名，
// tooltip 中渲染为 "馅料口味：&lt;馅料图标&gt; &lt;馅料名称&gt;"。
public class FillingFoodItem extends Item {

// NBT 标签名：馅料物品注册名，如 flavor_immersed_daily:meatpaste
    public static final String FILLING_NBT_KEY = "filling";

// TNT 馅：食用时的爆炸威力（等同原版 TNT）
    private static final float TNT_EXPLOSION_POWER = 4.0F;
// TNT 馅：附加给食用者的向上冲量，保证在爆心处也能被炸飞
    private static final double TNT_LAUNCH_SPEED = 1.0D;

    public FillingFoodItem(Properties properties) {
        super(properties);
    }

// 读取物品记录的馅料物品；标签为空、格式错误或物品未安装时返回 null
    public static Item getFillingItem(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        String fillingId = tag.getString(FILLING_NBT_KEY);
        if (fillingId.isEmpty()) return null;

        ResourceLocation id = ResourceLocation.tryParse(fillingId);
        if (id == null) return null;

        Item item = ForgeRegistries.ITEMS.getValue(id);
        return item == Items.AIR ? null : item;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
// 馅料信息要在消耗物品前读取
        Item filling = getFillingItem(stack);
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

// 馅料是原版 TNT：吃下去把玩家炸飞（ExplosionInteraction.NONE = 不破坏方块）
        if (filling == Items.TNT && !level.isClientSide) {
            level.explode(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    TNT_EXPLOSION_POWER, Level.ExplosionInteraction.NONE);
// 食用者正处于爆心，原版爆炸对爆心实体没有击退，这里补一次向上冲量
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(0.0D, TNT_LAUNCH_SPEED, 0.0D));
            livingEntity.hurtMarked = true;
        }
        return result;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        Item filling = getFillingItem(stack);
        if (filling == null) return Optional.empty();
        return Optional.of(new FillingTooltip(new ItemStack(filling)));
    }
}