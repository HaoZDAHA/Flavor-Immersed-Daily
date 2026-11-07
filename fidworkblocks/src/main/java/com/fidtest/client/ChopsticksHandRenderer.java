package com.fidtest.client;

import com.fidtest.ExampleMod;
import com.fidtest.item.ChopsticksItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import org.joml.Vector3f;

public final class ChopsticksHandRenderer {
    public static final ResourceLocation CHOPSTICK_A = ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "item/chopstick_firstperson_a");
    public static final ResourceLocation CHOPSTICK_B = ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "item/chopstick_firstperson_b");
    private static final float RAISE_TICKS = 8.0F;
    private static final float EAT_TICKS = 20.0F;
    private static final PlayerModel<LocalPlayer> NORMAL_ARMS = createArms(false);
    private static final PlayerModel<LocalPlayer> SLIM_ARMS = createArms(true);
    private static LocalPlayer trackedPlayer;
    private static ClientLevel trackedLevel;
    private static int trackedSlot = -1;
    private static boolean holdingChopsticks;
    private static float previousRaiseTicks;
    private static float raiseTicks;
    private static float previousEatTicks;
    private static float eatTicks;
    private static boolean eating;
    private static boolean queuedEating;
    private static ListTag eatingFragments = new ListTag();
    private static ListTag queuedFragments = new ListTag();
    private static ItemStack eatingFood = ItemStack.EMPTY;
    private static ItemStack queuedFood = ItemStack.EMPTY;

    private ChopsticksHandRenderer() {
    }

    private static PlayerModel<LocalPlayer> createArms(boolean slim) {
        return new PlayerModel<>(LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, slim), 64, 64).bakeRoot(), slim);
    }

    private static void updateContext(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        int slot = player == null ? -1 : player.getInventory().selected;
        boolean holding = player != null && player.isAlive() && minecraft.level != null
                && player.getMainHandItem().getItem() instanceof ChopsticksItem;
        if (player != trackedPlayer || minecraft.level != trackedLevel
                || slot != trackedSlot || holding != holdingChopsticks) {
            trackedPlayer = player;
            trackedLevel = minecraft.level;
            trackedSlot = slot;
            holdingChopsticks = holding;
            previousRaiseTicks = 0.0F;
            raiseTicks = 0.0F;
            previousEatTicks = 0.0F;
            eatTicks = 0.0F;
            eating = false;
            queuedEating = false;
            eatingFragments = new ListTag();
            queuedFragments = new ListTag();
            eatingFood = ItemStack.EMPTY;
            queuedFood = ItemStack.EMPTY;
        }
    }

    private static ListTag normalizeFragments(CompoundTag bite) {
        ListTag source = bite.getList("Fragments", 10);
        ListTag fragments = new ListTag();
        int count = Math.min(3, source.size());
        for (int i = 0; i < count; i++) {
            CompoundTag fragment = source.getCompound(i).copy();
            fragment.putFloat("X", (i - (count - 1) * 0.5F) * 0.025F);
            fragment.putFloat("Y", 0.08F);
            fragment.putFloat("Z", i == 1 ? 0.015F : -0.01F);
            fragment.putFloat("Scale", 0.018F);
            fragment.putFloat("Angle", 0.0F);
            fragments.add(fragment);
        }
        return fragments;
    }

    public static void startEating(int slot, CompoundTag bite, ItemStack food) {
        Minecraft minecraft = Minecraft.getInstance();
        updateContext(minecraft);
        if (!holdingChopsticks || slot < 0 || slot > 8 || slot != trackedSlot) {
            return;
        }
        if (eating) {
            queuedEating = true;
            queuedFragments = normalizeFragments(bite);
            queuedFood = food.copy();
        } else {
            eatingFragments = normalizeFragments(bite);
            eatingFood = food.copy();
            previousEatTicks = 0.0F;
            eatTicks = 0.0F;
            eating = true;
        }
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        updateContext(minecraft);
        if (minecraft.isPaused()) {
            return;
        }
        previousRaiseTicks = raiseTicks;
        if (holdingChopsticks) {
            raiseTicks = Math.min(RAISE_TICKS, raiseTicks + 1.0F);
        }
        previousEatTicks = eatTicks;
        if (eating) {
            // Keep the final interval available for partial-tick interpolation.
            if (eatTicks >= EAT_TICKS) {
                eating = queuedEating;
                eatingFragments = queuedEating ? queuedFragments : new ListTag();
                eatingFood = queuedEating ? queuedFood : ItemStack.EMPTY;
                queuedFragments = new ListTag();
                queuedFood = ItemStack.EMPTY;
                queuedEating = false;
                previousEatTicks = 0.0F;
                eatTicks = 0.0F;
            }
            if (eating) {
                eatTicks += 1.0F;
            }
        }
    }

    private static float smoothstep(float value) {
        float t = Mth.clamp(value, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }

    private static float easeOutCubic(float value) {
        float t = 1.0F - Mth.clamp(value, 0.0F, 1.0F);
        return 1.0F - t * t * t;
    }

    private static float raiseProgress(float time) {
        if (time < 3.0F) return 1.08F * easeOutCubic(time / 3.0F);
        if (time < 5.0F) return Mth.lerp(smoothstep((time - 3.0F) / 2.0F), 1.08F, 0.98F);
        return Mth.lerp(smoothstep((time - 5.0F) / 3.0F), 0.98F, 1.0F);
    }

    public static void onRenderHand(RenderHandEvent event) {
        if (event.getHand() != InteractionHand.MAIN_HAND
                || !(event.getItemStack().getItem() instanceof ChopsticksItem)) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        updateContext(minecraft);
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null || !player.isAlive()) {
            return;
        }
        event.setCanceled(true);
        boolean rightHand = player.getMainArm() == HumanoidArm.RIGHT;
        float side = rightHand ? 1.0F : -1.0F;
        float raised = raiseProgress(Mth.lerp(event.getPartialTick(), previousRaiseTicks, raiseTicks));
        // An outgoing cached stack follows vanilla equip lowering, not a new entrance.
        if (!holdingChopsticks) {
            raised = 1.0F;
        }
        float lowered = 1.0F - raised * (1.0F - Mth.clamp(event.getEquipProgress(), 0.0F, 1.0F));
        float time = eating ? Mth.lerp(event.getPartialTick(), previousEatTicks, eatTicks) : 0.0F;
        float reset = smoothstep((time - 15.0F) / 3.0F);
        float feed = smoothstep((time - 5.0F) / 8.0F);
        float thrust = time < 2.0F ? 1.10F * easeOutCubic(time / 2.0F)
                : Mth.lerp(smoothstep(time - 2.0F), 1.10F, 1.0F);
        float reach = thrust * (1.0F - feed);
        float mouth = feed * (1.0F - reset);
        float pinch = smoothstep(time - 2.0F) * (1.0F - reset);
        float settle = Mth.clamp((time - 18.0F) / 2.0F, 0.0F, 1.0F);
        float rebound = -0.035F * 16.0F * settle * settle * (1.0F - settle) * (1.0F - settle);
        float swing = eating ? 0.0F : Mth.sin(Mth.clamp(event.getSwingProgress(), 0.0F, 1.0F) * (float) Math.PI);
        ItemDisplayContext context = rightHand ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        BakedModel original = minecraft.getItemRenderer().getModel(event.getItemStack(), minecraft.level, player, 0);
        ItemTransform transform = original.getTransforms().getTransform(context);
        Vector3f compensation = mouthCompensation(rightHand, side, transform);
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        try {
            poseStack.translate(compensation.x() * mouth, compensation.y() * mouth, compensation.z() * mouth);
            poseStack.translate(side * (0.56F - 0.04F * swing - 0.10F * reach - 0.34F * mouth),
                    -0.40F - 0.8F * lowered - 0.08F * reach + 0.27F * mouth,
                    -0.86F - 0.03F * swing - 0.22F * reach + 0.38F * mouth + rebound);
            poseStack.mulPose(Axis.ZP.rotationDegrees(side * (8.0F - 2.0F * swing)));
            poseStack.mulPose(Axis.XP.rotationDegrees(-3.0F * swing + 8.0F * reach - 12.0F * mouth));
            if (!player.isInvisible()) {
                renderArm(player, event, rightHand, side, mouth);
            }
            applyGripDisplay(poseStack, rightHand, side, mouth, transform);
            renderChopsticks(minecraft, event, pinch);
            if (time >= 3.0F && time < 14.0F) {
                poseStack.pushPose();
                try {
                    if (!eatingFood.isEmpty()) {
                        poseStack.translate(0.0F, tipY(pinch), 0.0F);
                        poseStack.scale(1.05F, 1.05F, 1.05F);
                        minecraft.getItemRenderer().renderStatic(eatingFood, ItemDisplayContext.FIXED,
                                event.getPackedLight(), OverlayTexture.NO_OVERLAY, poseStack,
                                event.getMultiBufferSource(), minecraft.level, 0);
                    } else if (!eatingFragments.isEmpty()) {
                        poseStack.translate(0.0F, tipY(pinch), 0.0F);
                        poseStack.scale(3.0F, 3.0F, 3.0F);
                        poseStack.translate(0.0F, -0.08F, 0.0F);
                        DishRenderCache.getFragments(eatingFragments).render(poseStack,
                                event.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS)),
                                event.getPackedLight(), OverlayTexture.NO_OVERLAY);
                    }
                } finally {
                    poseStack.popPose();
                }
            }
        } finally {
            poseStack.popPose();
        }
    }

    private static float tipY(float pinch) {
        return -22.0F * Mth.cos(2.4F * pinch * Mth.DEG_TO_RAD) / 16.0F;
    }

    private static Vector3f mouthCompensation(boolean rightHand, float side, ItemTransform transform) {
        PoseStack poseStack = new PoseStack();
        poseStack.translate(side * 0.22F, -0.13F, -0.48F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(side * 8.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-12.0F));
        applyGripDisplay(poseStack, rightHand, side, 1.0F, transform);
        Vector3f tip = poseStack.last().pose().transformPosition(new Vector3f(0.0F, tipY(1.0F), 0.0F));
        return new Vector3f(0.0F, -0.20F, -0.32F).sub(tip);
    }

    private static void applyGripDisplay(PoseStack poseStack, boolean rightHand, float side, float mouth,
                                         ItemTransform transform) {
        // Move and scale only the grip, independently of the extended forearm.
        poseStack.translate(-side * 0.025F, 0.14F, -0.08F);
        poseStack.mulPose(Axis.YP.rotationDegrees(side * 125.0F * mouth));
        poseStack.mulPose(Axis.ZP.rotationDegrees(side * 12.0F * mouth));
        poseStack.scale(0.9F, 0.9F, 0.9F);
        // Cancel display translation before applying it: the shared grip remains at (8,8,8).
        poseStack.translate((rightHand ? -1.0F : 1.0F) * transform.translation.x(),
                -transform.translation.y(), -transform.translation.z());
        transform.apply(!rightHand, poseStack);
    }

    private static void renderChopsticks(Minecraft minecraft, RenderHandEvent event, float pinch) {
        // At y=-14 the inner gap is 4 - 44*sin(a) - 2*cos(a).
        // 2.4 degrees leaves ~0.159 pixels; neither 2px-thick stick crosses the other.
        float angle = 2.4F * pinch;
        renderStick(minecraft, event, CHOPSTICK_A, 6.0F, angle);
        renderStick(minecraft, event, CHOPSTICK_B, 10.0F, -angle);
    }

    private static void renderStick(Minecraft minecraft, RenderHandEvent event, ResourceLocation modelId,
                                    float gripX, float angle) {
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        try {
            poseStack.translate((gripX - 8.0F) / 16.0F, 0.0F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
            poseStack.translate(-gripX / 16.0F, -8.0F / 16.0F, -8.0F / 16.0F);
            BakedModel model = minecraft.getModelManager().getModel(ModelResourceLocation.standalone(modelId));
            for (BakedModel pass : model.getRenderPasses(event.getItemStack(), true)) {
                for (RenderType type : pass.getRenderTypes(event.getItemStack(), true)) {
                    minecraft.getItemRenderer().renderModelLists(pass, event.getItemStack(), event.getPackedLight(),
                            OverlayTexture.NO_OVERLAY, poseStack, ItemRenderer.getFoilBufferDirect(
                                    event.getMultiBufferSource(), type, true, event.getItemStack().hasFoil()));
                }
            }
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderArm(LocalPlayer player, RenderHandEvent event, boolean rightHand, float side, float mouth) {
        PoseStack poseStack = event.getPoseStack();
        boolean slim = player.getSkin().model() == PlayerSkin.Model.SLIM;
        PlayerModel<LocalPlayer> model = slim ? SLIM_ARMS : NORMAL_ARMS;
        ModelPart arm = rightHand ? model.rightArm : model.leftArm;
        ModelPart sleeve = rightHand ? model.rightSleeve : model.leftSleeve;
        poseStack.pushPose();
        try {
            // Negative local Y points down/outward and towards the camera, hiding the shoulder cap.
            poseStack.mulPose(Axis.ZP.rotationDegrees(side * 20.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-55.0F));
            // Roll around the forearm rather than turning its shoulder end into view.
            poseStack.mulPose(Axis.YP.rotationDegrees(side * 25.0F * mouth));
            // Stretch only along the arm, anchored at the wrist end (local cube y=10).
            poseStack.translate(0.0F, 1.0F / 16.0F, 0.0F);
            poseStack.scale(1.0F, 4.0F, 1.0F);
            poseStack.translate(side * (slim ? 5.5F : 6.0F) / 16.0F,
                    -(slim ? 12.5F : 12.0F) / 16.0F, 0.0F);
            ResourceLocation skin = player.getSkin().texture();
            arm.render(poseStack, event.getMultiBufferSource().getBuffer(RenderType.entitySolid(skin)),
                    event.getPackedLight(), OverlayTexture.NO_OVERLAY);
            if (player.isModelPartShown(rightHand ? PlayerModelPart.RIGHT_SLEEVE : PlayerModelPart.LEFT_SLEEVE)) {
                sleeve.render(poseStack, event.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(skin)),
                        event.getPackedLight(), OverlayTexture.NO_OVERLAY);
            }
        } finally {
            poseStack.popPose();
        }
    }
}
