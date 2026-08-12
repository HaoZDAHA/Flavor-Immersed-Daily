package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.client.ClientGuiHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import org.joml.Vector3f;

public class FairySparklerItem extends Item {

    // 颜色常量
    public static final int COLOR_RED = 0xFF0000;
    public static final int COLOR_BLUE = 0x0000FF;
    public static final int COLOR_GREEN = 0x00FF00;
    public static final int COLOR_YELLOW = 0xFFFF00;
    public static final int COLOR_PURPLE = 0xFF00FF;
    public static final int COLOR_ORANGE = 0xFF8800;
    public static final int COLOR_WHITE = 0xFFFFFF;
    public static final int COLOR_PINK = 0xFF69B4;

    public static final int[] COLORS = {COLOR_RED, COLOR_BLUE, COLOR_GREEN, COLOR_YELLOW, COLOR_PURPLE, COLOR_ORANGE, COLOR_WHITE, COLOR_PINK};

    // 形状常量
    public static final int SHAPE_CIRCLE = 0;
    public static final int SHAPE_SPIRAL = 1;
    public static final int SHAPE_HEART = 2;
    public static final String[] SHAPE_NAMES = {"circle", "spiral", "heart"};

    // 第一圈（内圈）
    private static final String TAG_COLOR = "fairy_color";
    private static final String TAG_SHAPE = "fairy_shape";

    // 第二圈（外圈）
    private static final String TAG_COLOR2 = "fairy_color2";
    private static final String TAG_SHAPE2 = "fairy_shape2";

    private static final int PARTICLE_INTERVAL = 2;

    // 内圈参数
    private static final int INNER_PARTICLE_COUNT = 12;
    private static final float INNER_PARTICLE_SIZE = 1.0f;
    private static final double INNER_RADIUS = 1.5;

    // 外圈参数（更大更浓厚）
    private static final int OUTER_PARTICLE_COUNT = 20;
    private static final float OUTER_PARTICLE_SIZE = 1.5f;
    private static final double OUTER_RADIUS = 2.8;

    public FairySparklerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<net.minecraft.network.chat.Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.flavor_immersed_daily.fairy_sparkler.desc1").withStyle(net.minecraft.ChatFormatting.RED, net.minecraft.ChatFormatting.BOLD));
        tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.flavor_immersed_daily.fairy_sparkler.desc2").withStyle(net.minecraft.ChatFormatting.GRAY));
        tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.flavor_immersed_daily.fairy_sparkler.desc3").withStyle(net.minecraft.ChatFormatting.GRAY));
    }

    private static CompoundTag getData(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    private static void putData(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    // ===== 内圈 =====
    public static int getColor(ItemStack stack) {
        CompoundTag tag = getData(stack);
        if (tag.contains(TAG_COLOR)) {
            return tag.getInt(TAG_COLOR);
        }
        return COLOR_RED;
    }

    public static void setColor(ItemStack stack, int color) {
        CompoundTag tag = getData(stack);
        tag.putInt(TAG_COLOR, color);
        putData(stack, tag);
    }

    public static int getShape(ItemStack stack) {
        CompoundTag tag = getData(stack);
        if (tag.contains(TAG_SHAPE)) {
            return tag.getInt(TAG_SHAPE);
        }
        return SHAPE_CIRCLE;
    }

    public static void setShape(ItemStack stack, int shape) {
        CompoundTag tag = getData(stack);
        tag.putInt(TAG_SHAPE, shape);
        putData(stack, tag);
    }

    // ===== 外圈 =====
    public static int getColor2(ItemStack stack) {
        CompoundTag tag = getData(stack);
        if (tag.contains(TAG_COLOR2)) {
            return tag.getInt(TAG_COLOR2);
        }
        return COLOR_BLUE;
    }

    public static void setColor2(ItemStack stack, int color) {
        CompoundTag tag = getData(stack);
        tag.putInt(TAG_COLOR2, color);
        putData(stack, tag);
    }

    public static int getShape2(ItemStack stack) {
        CompoundTag tag = getData(stack);
        if (tag.contains(TAG_SHAPE2)) {
            return tag.getInt(TAG_SHAPE2);
        }
        return SHAPE_SPIRAL;
    }

    public static void setShape2(ItemStack stack, int shape) {
        CompoundTag tag = getData(stack);
        tag.putInt(TAG_SHAPE2, shape);
        putData(stack, tag);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 潜行右键：打开配置界面（仅客户端）。
        // 关键：必须返回 PASS 而不是 sidedSuccess——PASS 不会向服务端发送使用数据包，
        // 服务端因此完全不参与这次右键。若返回 SUCCESS/CONSUME，服务端会因无法感知
        // 潜行状态（isShiftKeyDown 在服务端恒为 false）而误入非潜行分支，双持时执行
        // startUsingItem 把玩家置入"持续使用"状态，造成客户端/服务端状态错乱。
        if (player.isShiftKeyDown()) {
            if (level.isClientSide) {
                ClientGuiHelper.openFairySparklerConfig();
            }
            return InteractionResultHolder.pass(stack);
        }

        // 非潜行：主手 + 副手双持时进入持续使用状态（绽放烟花粒子）
        ItemStack offhand = player.getOffhandItem();
        if (offhand.is(this) && hand == InteractionHand.MAIN_HAND) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remainingUseDuration) {
        if (!(living instanceof Player player)) return;

        if (!player.getOffhandItem().is(this)) {
            player.stopUsingItem();
            return;
        }

        if (level.isClientSide) {
            int elapsed = this.getUseDuration(stack, living) - remainingUseDuration;
            if (elapsed % PARTICLE_INTERVAL != 0) return;

            double gameTime = level.getGameTime();
            double baseAngle = gameTime * 0.15;
            Vec3 playerPos = player.position();
            Vec3 look = player.getLookAngle();

            // 计算视线方向的垂直向量
            Vec3 up = new Vec3(0, 1, 0);
            Vec3 right = Math.abs(look.y) > 0.99
                    ? new Vec3(1, 0, 0).cross(look).normalize()
                    : look.cross(up).normalize();
            Vec3 forward = right.cross(look).normalize();

            // 内圈
            int innerColor = getColor(stack);
            int innerShape = getShape(stack);
            spawnParticleRing(level, playerPos, look, right, forward, baseAngle,
                    innerColor, innerShape, INNER_PARTICLE_COUNT, INNER_PARTICLE_SIZE, INNER_RADIUS);

            // 外圈（更大更浓厚）
            int outerColor = getColor2(stack);
            int outerShape = getShape2(stack);
            spawnParticleRing(level, playerPos, look, right, forward, baseAngle + Math.PI,
                    outerColor, outerShape, OUTER_PARTICLE_COUNT, OUTER_PARTICLE_SIZE, OUTER_RADIUS);
        }
    }

    private void spawnParticleRing(Level level, Vec3 playerPos, Vec3 look, Vec3 right, Vec3 up,
                                   double angle, int color, int shape, int count, float size, double radius) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        DustParticleOptions particle = new DustParticleOptions(new Vector3f(r, g, b), size);

        switch (shape) {
            case SHAPE_SPIRAL -> spawnSpiral(level, particle, playerPos, look, right, up, angle, radius, count);
            case SHAPE_HEART -> spawnHeart(level, particle, playerPos, look, right, up, angle, radius, count);
            default -> spawnCircle(level, particle, playerPos, look, right, up, angle, radius, count);
        }
    }

    private void spawnCircle(Level level, DustParticleOptions particle, Vec3 playerPos,
                             Vec3 look, Vec3 right, Vec3 up, double angle, double radius, int count) {
        Vec3 center = playerPos.add(0, 0.5, 0).add(look.scale(0.5));

        for (int i = 0; i < count; i++) {
            double theta = angle + (2 * Math.PI * i / count);
            Vec3 offset = right.scale(radius * Math.cos(theta))
                    .add(up.scale(radius * Math.sin(theta)));
            Vec3 pos = center.add(offset);
            level.addParticle(particle, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }

    private void spawnSpiral(Level level, DustParticleOptions particle, Vec3 playerPos,
                             Vec3 look, Vec3 right, Vec3 up, double angle, double radius, int count) {
        Vec3 center = playerPos.add(0, 0.5, 0).add(look.scale(0.5));

        for (int i = 0; i < count; i++) {
            double theta = angle + (2 * Math.PI * i / count);
            double yOffset = Math.sin(angle * 0.5 + i * 0.5) * 0.8;
            Vec3 offset = right.scale(radius * Math.cos(theta))
                    .add(up.scale(radius * Math.sin(theta)))
                    .add(look.scale(yOffset));
            Vec3 pos = center.add(offset);
            level.addParticle(particle, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }

    private void spawnHeart(Level level, DustParticleOptions particle, Vec3 playerPos,
                            Vec3 look, Vec3 right, Vec3 up, double angle, double radius, int count) {
        Vec3 center = playerPos.add(0, 0.5, 0).add(look.scale(0.3));
        double scale = radius / 16.0;

        for (int i = 0; i < count * 2; i++) {
            double t = (2 * Math.PI * i / (count * 2)) + angle * 0.1;
            double localX = 16 * Math.pow(Math.sin(t), 3) * scale;
            double localZ = (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)) * scale;
            Vec3 offset = right.scale(localX).add(up.scale(localZ));
            Vec3 pos = center.add(offset);
            level.addParticle(particle, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, livingEntity.blockPosition(),
                    SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.PLAYERS, 0.5f, 1.0f);
        }
    }
}