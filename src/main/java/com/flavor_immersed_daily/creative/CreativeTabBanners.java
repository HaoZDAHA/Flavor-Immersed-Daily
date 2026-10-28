package com.flavor_immersed_daily.creative;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModCreateTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

// 创造模式物品栏里的分区横幅：绘制（含鼠标悬停高亮），以及点击折叠／展开。
// 横幅的占位格由 SectionedCreativeModeTab 排在「横幅行」的最左格，
// 这里遍历界面上可见的 45 个格子，认出那一格就把整行铺上一条横幅。
// 画在 ContainerScreenEvent.Render.Foreground：此时槽位底图、物品与
// 悬停高亮都画完了，而拖拽中的物品与提示框还没画，横幅刚好压住空槽又不会挡住提示框。
// 另外在 ContainerScreenEvent.Render.Background 里把面板上下边缘各 4 像素
// 用本标签页的背景图重铺一遍，抹掉原版「选中的标签页按钮」压进面板的那一截。
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class CreativeTabBanners {

// 物品格区域相对界面左上角的偏移与尺寸，与 ItemPickerMenu 的槽位坐标一致
    private static final int GRID_X = 9;
    private static final int GRID_Y = 18;
    private static final int SLOTS_PER_ROW = 9;
    private static final int SLOT_SIZE = 18;
    private static final int VISIBLE_ROWS = 5;
    private static final int ROW_WIDTH = SLOTS_PER_ROW * SLOT_SIZE;
    private static final int ROW_HEIGHT = SLOT_SIZE;

// 视觉微调：横幅（纹理连同标题）整体相对槽位左上角左上各挪 1 像素，看着才不歪
    private static final int BANNER_NUDGE_X = -1;
    private static final int BANNER_NUDGE_Y = -1;

// 鼠标压在横幅上时铺的那层淡白，跟原版槽位高亮一个路子，只是整行铺满
    private static final int HOVER_HIGHLIGHT = 0x66FFFFFF;

// 标签页按钮压进面板的高度。原版 renderTabButton 把按钮画在
// topPos - 28（上排）或 topPos + imageHeight - 4（下排），
// 而按钮本身高 32，于是上下排各侵入面板 4 像素。
    private static final int TAB_OVERLAP = 4;

// 原版把「当前选中的标签页」藏成 CreativeModeInventoryScreen 的
// 私有静态字段 selectedTab，且没有任何公开访问器，只能反射读；读一次缓存下来。
    private static final Field SELECTED_TAB = selectedTabField();

    private CreativeTabBanners() {
    }

    @Nullable
    private static Field selectedTabField() {
        try {
            Field field = CreativeModeInventoryScreen.class.getDeclaredField("selectedTab");
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

// 当前创造栏选中的是不是本模组的标签页；反射不可用时安全地当作「不是」
    private static boolean isOurTab() {
        if (SELECTED_TAB == null) {
            return false;
        }
        try {
            return SELECTED_TAB.get(null) == ModCreateTab.DTOOLS_TAB.get();
        } catch (IllegalAccessException e) {
            return false;
        }
    }

// 原版在背景层之后又单独画了一遍「选中的标签页按钮」，它的下沿会压进面板 4 像素、盖住我们的背景图。
// 这个事件刚好夹在背景层与物品之间，把上下各 4 像素用我们自己的图重铺一遍即可抹平。
    @SubscribeEvent
    public static void onRenderBackground(ContainerScreenEvent.Render.Background event) {
        if (!(event.getContainerScreen() instanceof CreativeModeInventoryScreen screen) || !isOurTab()) {
            return;
        }
        GuiGraphics graphics = event.getGuiGraphics();
        int x = screen.getGuiLeft();
        int y = screen.getGuiTop();
// 上下两条各重铺 4 像素，两条都不会碰到搜索框（y≥6）与滚动条滑块（y≥18）
        graphics.blit(ModCreateTab.TAB_BACKGROUND, x, y,
                0, 0, ModCreateTab.TAB_WIDTH, TAB_OVERLAP);
        graphics.blit(ModCreateTab.TAB_BACKGROUND, x, y + ModCreateTab.TAB_HEIGHT - TAB_OVERLAP,
                0, ModCreateTab.TAB_HEIGHT - TAB_OVERLAP, ModCreateTab.TAB_WIDTH, TAB_OVERLAP);
    }

    @SubscribeEvent
    public static void onRenderForeground(ContainerScreenEvent.Render.Foreground event) {
        if (!(event.getContainerScreen() instanceof CreativeModeInventoryScreen screen)) {
            return;
        }
        List<Slot> slots = screen.getMenu().slots;
        GuiGraphics graphics = event.getGuiGraphics();
// 鼠标坐标必须用事件自带的这份：它和 getGuiLeft() 同属已缩放的界面坐标系。
// MouseHandler.xpos()/ypos() 是 GLFW 的原始窗口像素，没乘 GUI 缩放，界面缩放不是 1 时永远对不上。
        SectionedCreativeModeTab.Section hovered = bannerAt(screen, event.getMouseX(), event.getMouseY());
        int visible = Math.min(SLOTS_PER_ROW * VISIBLE_ROWS, slots.size());
        for (int index = 0; index < visible; index += SLOTS_PER_ROW) {
            SectionedCreativeModeTab.Section banner =
                    SectionedCreativeModeTab.bannerOf(slots.get(index).getItem());
            if (banner != null) {
                int x = GRID_X + BANNER_NUDGE_X;
                int y = GRID_Y + (index / SLOTS_PER_ROW) * SLOT_SIZE + BANNER_NUDGE_Y;
                drawBanner(graphics, x, y, banner, banner == hovered);
            }
        }
    }

    @SubscribeEvent
    public static void onMousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != 0 || !(event.getScreen() instanceof CreativeModeInventoryScreen screen)) {
            return;
        }
        SectionedCreativeModeTab.Section banner =
                bannerAt(screen, event.getMouseX(), event.getMouseY());
        if (banner == null) {
            return;
        }
        SectionedCreativeModeTab.toggleCollapsed(banner);
// resize 会重新选中当前标签页：重建物品列表并按原行号恢复滚动位置，正好拿来当刷新用
        screen.resize(Minecraft.getInstance(), screen.width, screen.height);
// 吃掉这次点击，免得横幅的空白格再被原版当成拾取/放置处理
        event.setCanceled(true);
    }

// 查鼠标底下的横幅：把界面坐标换算成「第几行」，再看那一行最左格是不是横幅占位
    @Nullable
    private static SectionedCreativeModeTab.Section bannerAt(
            CreativeModeInventoryScreen screen, double mouseX, double mouseY) {
        int relX = (int) mouseX - screen.getGuiLeft() - GRID_X - BANNER_NUDGE_X;
        int relY = (int) mouseY - screen.getGuiTop() - GRID_Y - BANNER_NUDGE_Y;
        if (relX < 0 || relX >= ROW_WIDTH || relY < 0) {
            return null;
        }
        int row = relY / SLOT_SIZE;
        if (row >= VISIBLE_ROWS) {
            return null;
        }
        List<Slot> slots = screen.getMenu().slots;
        int index = row * SLOTS_PER_ROW;
        return index < slots.size() ? SectionedCreativeModeTab.bannerOf(slots.get(index).getItem()) : null;
    }

// 画一条横跨整行的横幅（此事件的坐标已相对界面左上角），纹理与标题都来自分区定义
    private static void drawBanner(GuiGraphics graphics, int x, int y,
                                   SectionedCreativeModeTab.Section banner, boolean hovered) {
// 纹理整张铺在横幅行上，宽高与行尺寸一致，不需要裁切
        graphics.blit(banner.texture(), x, y, ROW_WIDTH, ROW_HEIGHT,
                0.0F, 0.0F, ROW_WIDTH, ROW_HEIGHT, ROW_WIDTH, ROW_HEIGHT);
        if (hovered) {
// 高亮压在纹理上、标题下面：整行亮起来，字还是清清楚楚的
            graphics.fill(x, y, x + ROW_WIDTH, y + ROW_HEIGHT, HOVER_HIGHLIGHT);
        }
// 标题文字叠在纹理上，起点与纹理里的小色块留出间距
        graphics.drawString(Minecraft.getInstance().font, banner.title(), x + 10, y + 5, 0xFFF3E4BE, true);
    }
}
