package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.creative.SectionedCreativeModeTab;
import com.flavor_immersed_daily.item.WugangAxeItem;

import static com.flavor_immersed_daily.FlavorImmersedDaily.*;
import static com.flavor_immersed_daily.all.ModItems.*;
import static com.flavor_immersed_daily.all.ModBlocks.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModCreateTab {
    private ModCreateTab() {
    }

    public static final DeferredRegister<CreativeModeTab> REGISTRY =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FlavorImmersedDaily.MODID);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

// 横幅纹理：textures/gui/creative_banner/<名字>.png，固定 162×18
    private static ResourceLocation bannerTexture(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/creative_banner/" + name + ".png");
    }

// 本标签页的整幅背景图。
// 画布必须 256×256，可见内容画在左上角 195×136 里：GuiGraphics#blit(location, x, y, u, v, w, h)
// 内部固定按 256×256 换算 UV，画布小于 256 只会把左上角一小块拉伸铺满整块面板
// （原版 tab_items.png 同样是一张 256×256）。走 Forge 的
// CreativeModeTab.Builder#withBackgroundLocation，要带 textures/ 前缀的完整路径。
// 图里请自带右侧滚动条凹槽（滑块由原版另画在上面）。
    public static final ResourceLocation TAB_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/container/creative_inventory/fi_dtools.png");

// 面板可见区域尺寸，与 vanilla 的 imageWidth / imageHeight 一致
    public static final int TAB_WIDTH = 195;
    public static final int TAB_HEIGHT = 136;

// 单标签页里的分区横幅，顺序与下面 displayItems 一一对应。
// 注意：原版创造栏会把同一个 ItemStack 去重，重复物品只在「首次出现」的分区里显示，
// 所以这里的数量是该分区**实际显示**的件数，而不是源码里 accept 的行数：
// 田间耕种 260（源码 261，青提重复）、食材杂项 251（源码 255，糯米/白菜/挂糊裹粉的鸡腿/肉卷重复）。
// 厨房工具 32 件（本模组 19 件，另预留 13 件给 fidworkblock 在标签页最前注入的厨具/教程卷轴）、
// 田间耕种 260 件、畜牧产品 79 件、食物佳肴 475 件、食材杂项 251 件，
// 建筑装饰用负数表示「从这里起余下的全部」。
// id 同时用作折叠状态的键与横幅纹理名。
    private static final List<SectionedCreativeModeTab.Section> SECTIONS = List.of(
            new SectionedCreativeModeTab.Section("kitchen_tools",
                    Component.translatable("gui.flavor_immersed_daily.creative_section.kitchen_tools"),
                    bannerTexture("kitchen_tools"), 32),
            new SectionedCreativeModeTab.Section("farming",
                    Component.translatable("gui.flavor_immersed_daily.creative_section.farming"),
                    bannerTexture("farming"), 260),
            new SectionedCreativeModeTab.Section("livestock",
                    Component.translatable("gui.flavor_immersed_daily.creative_section.livestock"),
                    bannerTexture("livestock"), 79),
            new SectionedCreativeModeTab.Section("food",
                    Component.translatable("gui.flavor_immersed_daily.creative_section.food"),
                    bannerTexture("food"), 475),
            new SectionedCreativeModeTab.Section("ingredient_misc",
                    Component.translatable("gui.flavor_immersed_daily.creative_section.ingredient_misc"),
                    bannerTexture("ingredient_misc"), 251),
            new SectionedCreativeModeTab.Section("building",
                    Component.translatable("gui.flavor_immersed_daily.creative_section.building"),
                    bannerTexture("building"), -1));

// 创造模式标签页（全模组唯一，物品按横幅分区）
    public static final RegistryObject<CreativeModeTab> DTOOLS_TAB = REGISTRY.register("fi_dtools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.fi_dtools"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> ALL.get().getDefaultInstance())
// Forge 1.20.1 自带背景图支持，renderBg 会原样绘制这张贴图
            .withBackgroundLocation(TAB_BACKGROUND)
// 左上角 (8,6) 那行标签页名字不画，免得压在自定义背景上
            .hideTitle()
            .withTabFactory(builder -> new SectionedCreativeModeTab(builder, SECTIONS))
            .displayItems((parameters, output) -> {
// 厨房工具

// 大挂钩
                output.accept(BIGHOOK.asItem());
// 木盆
                output.accept(WOODBASIN.asItem());
// 切割刀
                output.accept(SHARPKNIFE.get());
// 宽刃刀
                output.accept(WIDEEDGEDKNIFE.get());
// 斩骨刀
                output.accept(BONECUTTERKNIFE.get());
// 菜刀
                output.accept(KITCHENKNIFE.get());
// 除草剪刀
                output.accept(KITCHENSCISSOR.get());
// 锅铲
                output.accept(SPATULA.get());
// 木槌
                output.accept(MALLET.get());
// 滤布
                output.accept(COARSECLOTH.get());
// 月饼模具
                output.accept(MOONCAKEMOLD.get());
// 擀面杖
                output.accept(ROLLINGPIN.get());
// 净化水桶
                output.accept(FILTERVAT.get());
// 鸡蛋仔模具
                output.accept(EEGGPUFFSMOULD.get());
// 农产鉴定机
                output.accept(AGRICULTURALAPPRAISALMACHINE.asItem());
// 冰箱
                output.accept(FRIDGE.asItem());
// 打蛋机
                output.accept(EGGBREAKINGMACHINE.asItem());
// 绞肉机盖
                output.accept(MINCER_COVER.get());
// 蜂窝煤
                output.accept(HONEYCOMBBRIQUET.get());
// 田间耕种
// 温带野果作物堆
                output.accept(TEMPERATEWILDFRUIT.get());
// 热带野果作物堆
                output.accept(TROPICALWILD_FRUIT.get());
// 野生花叶作物堆
                output.accept(WILDFLOWERANDLEAF.get());
// 寒带野果作物堆
                output.accept(WILDFRUITINCOLDZONE.get());
// 野生谷物作物堆
                output.accept(WILDGRAINPLANT.get());
// 野生菌菇作物堆
                output.accept(WILDMUSHROOMPLANT.get());
// 野生籽叶作物堆
                output.accept(WILDSEEDPLANT.get());
// 野生块茎作物堆
                output.accept(WILDTUBERPLANTS.get());
// 一根香蕉
                output.accept(APIECEOFBANANA.get());
// 香蕉
                output.accept(BANANA.get());
// 香蕉果肉
                output.accept(PULLEDBANANA_2.get());
// 橙子
                output.accept(ORANGE.get());
// 血橙
                output.accept(BLOODORANGE.get());
// 橘子
                output.accept(TANGERINE.get());
// 枳
                output.accept(TANGERINE_1.get());
// 丑橘
                output.accept(UGLYORANGE.get());
// 柠檬
                output.accept(LEMON.get());
// 哈密瓜
                output.accept(HAMIMELON.get());
// 甜瓜
                output.accept(SWEETMELON.get());
// 羊角蜜甜瓜
                output.accept(SWEETMELON_1.get());
// 椰子
                output.accept(COCONUT.get());
// 椰子肉
                output.accept(COCONUTMEAT.get());
// 椰子壳
                output.accept(COCONUT_SHELL.get());
// 榴莲
                output.accept(DURIAN.get());
// 榴莲果肉
                output.accept(DURIANMEAT.get());
// 榴莲壳帽子
                output.accept(DURIANSHELLHAT.get());
// 蓝莓
                output.accept(BLUEBERRY.get());
// 葡萄
                output.accept(GRAPE.get());
// 青提
                output.accept(GREENGRAPE.get());
// 桑葚
                output.accept(MULBERRY.get());
// 草莓
                output.accept(STRAWBERRY.get());
// 杏子
                output.accept(APRICOT.get());
// 樱桃
                output.accept(CHERRY.get());
// 青李子
                output.accept(GREENPLUM.get());
// 水蜜桃
                output.accept(HONEYPEACH.get());
// §6§l福寿蟠桃
                output.accept(LIFEPEACH.get());
// 油桃
                output.accept(NECTARINE.get());
// 李子
                output.accept(PLUM.get());
// 冬枣
                output.accept(WINTERJUJUBE.get());
// 杨桃
                output.accept(CARAMBOLA.get());
// 火龙果
                output.accept(DRAGONFRUIT.get());
// 猕猴桃
                output.accept(KIWIFRUIT.get());
// 荔枝
                output.accept(LYCHEE.get());
// 芒果
                output.accept(MANGO.get());
// 山竹
                output.accept(MANGOSTEEN.get());
// 木瓜
                output.accept(PAWPAW.get());
// 菠萝
                output.accept(PINEAPPLE.get());
// §6§l青苹果
                output.accept(GREENAPPLE.get());
// 山楂
                output.accept(HAWTHORN.get());
// 枇杷
                output.accept(LOQUAT.get());
// 梨
                output.accept(PEAR.get());
// 石榴
                output.accept(POMEGRANATE.get());
// 开心果
                output.accept(PISTACHIONUT.get());
// 红枣
                output.accept(REDDATE.get());
// 核桃
                output.accept(WALNUT.get());
// 干烤开心果
                output.accept(DRY_PISTACHIONUT.get());
// 开心果
                output.accept(PISTACHIONUT_0.get());
// 开口开心果
                output.accept(PISTACHIONUTSWITHOPENSHELLS.get());
// 核桃仁
                output.accept(WALNUTKINNEL.get());
// 白菜
                output.accept(CHINESE_LEAVES.get());
// 白菜种子
                output.accept(CHINESE_LEAVES_SEEDS.get());
// 白萝卜
                output.accept(RADISH.get());
// 白萝卜种子
                output.accept(RADISHSEED.get());
// 白蘑菇
                output.accept(WHITEMUSHROOM.get());
// 白蘑菇种子
                output.accept(WHITE_MUSHROOM_SEED.get());
// 木耳
                output.accept(BLACKFUNGUS.get());
// 木耳作物
                output.accept(BLACKFUNGSSEED.get());
// 干木耳
                output.accept(DRYBLACKFUNGUS.get());
// 杏鲍菇
                output.accept(PLEUROTUSERYNGII.get());
// 杏鲍菇作物
                output.accept(PLEUROTUSSEED.get());
// 金针菇
                output.accept(ENOKIMUSHROOM.get());
// 金针菇作物
                output.accept(ENOKIMUSHROOMSEED.get());
// 银耳
                output.accept(TREMELLA.get());
// 银耳作物
                output.accept(TREMELLASEED.get());
// 香菇
                output.accept(FRAGRANTMUSHROOM.get());
// 香菇作物
                output.accept(FRAGRANTSEED.get());
// 干香菇
                output.accept(DRYFRAGRANTMUSHROOM.get());
// 蓝莓种子
                output.accept(BLUEBERRYSEED.get());
// 火龙果种子
                output.accept(DRAGONFRUITSEED.get());
// 绿茶种子
                output.accept(GREENTEALEAVESSEED.get());
// 绿茶叶
                output.accept(GREENTEALEAVES.get());
// 干绿茶叶
                output.accept(DRYGREENTEA.get());
// 哈密瓜种子
                output.accept(HAMIMELONSEED.get());
// 菠萝种子
                output.accept(PINEAPPLESEED.get());
// 红茶种子
                output.accept(RED_TEA_SEED.get());
// 红茶叶
                output.accept(REDTEALEAVES.get());
// 干红茶叶
                output.accept(DRYREDTEA.get());
// 草莓种子
                output.accept(STRAWBERRYSEED.get());
// 莲藕种子
                output.accept(LOTUSROOTSEED.get());
// 莲藕
                output.accept(LOTUSROOT.get());
// 糯米秧苗
                output.accept(GLUTINOUSSEEDS.get());
// 糯米
                output.accept(POLISHEDGLUTINOUSRICE_2.get());
// 水稻秧苗
                output.accept(PADDYSEEDS.get());
// 大米
                output.accept(PADDYGRAIN.get());
// 八角
                output.accept(ANISEED_0.get());
// 高粱
                output.accept(KAOLIANGGRAIN.get());
// 高粱秧苗
                output.accept(KAOLIANG_SEED.get());
// 西蓝花
                output.accept(BROCCOIL.get());
// 西蓝花种子
                output.accept(BROCCOILSEED.get());
// 荞麦
                output.accept(BUCKWHEAT.get());
// 荞麦种子
                output.accept(BUCKWHEATSEED.get());
// 卷心菜
                output.accept(CABBAGE.get());
// 卷心菜种子
                output.accept(CABBAGESEED.get());
// 木薯
                output.accept(CASSAVA.get());
// 木薯种子
                output.accept(CASSAVASEEDS.get());
// 芹菜
                output.accept(CELERY.get());
// 芹菜种子
                output.accept(CELERYSEED.get());
// 韭菜
                output.accept(CHINESECHIVES.get());
// 韭菜种子
                output.accept(CHINESECHIVESSEED.get());
// 山药
                output.accept(CHINESEYAM.get());
// 山药种子
                output.accept(CHINESEYAMSEED.get());
// 咖啡豆种子
                output.accept(COFFEEBEANSEED.get());
// 玉米
                output.accept(CORN.get());
// 玉米粒
                output.accept(CORNSEED.get());
// 孜然
                output.accept(CUMIN.get());
// 孜然种子
                output.accept(CUMINSEED.get());
// 茴香
                output.accept(FENNEL.get());
// 茴香种子
                output.accept(FENNELSEEDSTATES.get());
// 大蒜
                output.accept(GARLIC.get());
// 大蒜种子
                output.accept(GARLICSEED.get());
// 生姜
                output.accept(GINGER.get());
// 生姜种子
                output.accept(GINGER_SEED.get());
// 青尖椒
                output.accept(GREENPEPPER.get());
// 青尖椒种子
                output.accept(GREENPEPPERSEEDS.get());
// 秋葵
                output.accept(GUMBO.get());
// 秋葵种子
                output.accept(GUMBOSEED.get());
// 小米
                output.accept(MILLETGRAIN_GRAIN.get());
// 小米秧苗
                output.accept(MILLET.get());
// 绿豆荚
                output.accept(MUNGBEAN.get());
// 绿豆
                output.accept(MUNGBEANPLANT.get());
// 芥末
                output.accept(MUSTARD.get());
// 芥末种子
                output.accept(MUSTRAD_SEED.get());
// 肉豆蔻种子
                output.accept(NUTMEGSEED.get());
// 燕麦
                output.accept(OAT.get());
// 燕麦种子
                output.accept(OATSEED.get());
// 油菜
                output.accept(OILSEEDRAPE.get());
// 油菜种子
                output.accept(OILRAPESEED.get());
// 洋葱
                output.accept(ONION.get());
// 洋葱种子
                output.accept(ONIONSEED.get());
// 豌豆
                output.accept(PEA.get());
// 豌豆种子
                output.accept(PEASEED.get());
// 紫薯
                output.accept(PURPLESWEETPOTATO.get());
// 紫薯种子
                output.accept(PUPLESWEETPOTATOSEED.get());
// 芝麻
                output.accept(SESAME.get());
// 芝麻种子
                output.accept(SESAMESEED.get());
// 黄豆荚
                output.accept(SOYBEAN.get());
// 黄豆
                output.accept(SOY_BEAN_SEED.get());
// 青椒
                output.accept(SWEETGREENPEPPER.get());
// 青椒种子
                output.accept(SWEETGREENPEPPERSEED.get());
// 西葫芦
                output.accept(ZUCCHINI.get());
// 西葫芦种子
                output.accept(ZUCCHINISEED.get());
// 菠菜种子
                output.accept(SPINACH_SEED.get());
// 菜花种子
                output.accept(CAULIFLOWER_SEED.get());
// 葱种子
                output.accept(SCALLION_SEED.get());
// 丁香种子
                output.accept(LILAC_SEED.get());
// 红豆
                output.accept(RED_BEAN_BLOCK.get());
// 红尖椒种子
                output.accept(RED_PEPPER_SEED.get());
// 红薯种子
                output.accept(SWEET_POTATO_SEED.get());
// 花椒种子
                output.accept(SI_CHUAN_PEPPER_SEED.get());
// 花生种子
                output.accept(PEA_NUT_SEED.get());
// 原木
// 灿阳木原木
                output.accept(SOLARWOOD_LOG.asItem());
// 蜜缘木原木
                output.accept(ORCHARD_HEARTWOOD_LOG.asItem());
// 核毅木原木
                output.accept(STONEBARK_LOG.asItem());
// 虬藤木原木
                output.accept(VINEHEART_TIMBER_LOG.asItem());
// 清韵原木
                output.accept(VERDANT_GRACE_LOG.asItem());
// 树叶
// 灿阳木树叶
                output.accept(SOLARWOOD_LEAVES.asItem());
// 蜜缘木树叶
                output.accept(ORCHARD_HEARTWOOD_LEAVES.asItem());
// 核毅木树叶
                output.accept(STONEBARK_LEAVES.asItem());
// 虬藤木树叶
                output.accept(VINEHEART_TIMBER_LEAVES.asItem());
// 清韵树叶
                output.accept(VERDANT_GRACE_LEAVES.asItem());
// 树苗
// 李子树苗
                output.accept(PLUM_SAPLING.asItem());
// 杏子树苗
                output.accept(APRICOT_SAPLING.asItem());
// 樱桃树苗
                output.accept(CHERRY_SAPLING.asItem());
// 青梅树苗
                output.accept(GREENPLUM_SAPLING.asItem());
// 山楂树苗
                output.accept(HAWTHORN_SAPLING.asItem());
// 枇杷树苗
                output.accept(LOQUAT_SAPLING.asItem());
// 石榴树苗
                output.accept(POMEGRANATE_SAPLING.asItem());
// 杨桃树苗
                output.accept(CARAMBOLA_SAPLING.asItem());
// 榴莲树苗
                output.accept(DURIAN_SAPLING.asItem());
// 柠檬树苗
                output.accept(LEMON_SAPLING.asItem());
// 荔枝树苗
                output.accept(LYCHEE_SAPLING.asItem());
// 芒果树苗
                output.accept(MANGO_SAPLING.asItem());
// 木瓜树苗
                output.accept(PAWPAW_SAPLING.asItem());
// 橘子树苗
                output.accept(TANGERINE_SAPLING.asItem());
// 橙子树苗
                output.accept(ORANGE_SAPLING.asItem());
// 苹果树苗
                output.accept(APPLE_SAPLING.asItem());
// 蜜桃树苗
                output.accept(HONEYPEACH_SAPLING.asItem());
// 油桃树苗
                output.accept(NECTARINE_SAPLING.asItem());
// 梨树苗
                output.accept(PEAR_SAPLING.asItem());
// 甜瓜树苗
                output.accept(SWEETMELON_SAPLING.asItem());
// 开心果树苗
                output.accept(PISTACHIONUT_SAPLING.asItem());
// 红枣树苗
                output.accept(REDDATE_SAPLING.asItem());
// 核桃树苗
                output.accept(WALNUT_SAPLING.asItem());
// 冬枣树苗
                output.accept(WINTERJUJUBE_SAPLING.asItem());
// 猕猴桃树苗
                output.accept(KIWIFRUIT_SAPLING.asItem());
// 山竹树苗
                output.accept(MANGOSTEEN_SAPLING.asItem());
// 桑葚树苗
                output.accept(MULBERRY_SAPLING.asItem());
// 椰子树苗
                output.accept(COCONUT_SAPLING.asItem());
// 香蕉叶
                output.accept(BANANA_SAPLING.asItem());
// 香蕉根茎
                output.accept(BANANAWOOD.asItem());
// 香蕉串
                output.accept(RAWBANANA.asItem());
// 桂皮
                output.accept(CINNAMON.get());
// 桂皮树苗
                output.accept(CINNAMON_SAPLING.asItem());
// 桂皮树干
                output.accept(CINNAMONWOOD.asItem());
// 树叶
// 李子树叶
                output.accept(VERDANT_GRACE_FRUITING_LEAVES.asItem());
// 杏子树叶
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_APRICOT.asItem());
// 樱桃树叶
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_CHERRY.asItem());
// 青梅树叶
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_GREENPLUM.asItem());
// 山楂树叶
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_HAWTHORN.asItem());
// 枇杷树叶
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_LOQUAT.asItem());
// 石榴树叶
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_POMEGRANATE.asItem());
// 杨桃树叶
                output.accept(CARAMBOLEAVE_FRUITING_LEAVES.asItem());
// 榴莲树叶
                output.accept(DURIANLEAVE_FRUITING_LEAVES.asItem());
// 柠檬树叶
                output.accept(LEMONLEAVE_FRUITING_LEAVES.asItem());
// 荔枝树叶
                output.accept(LYCHEELEAVE_FRUITING_LEAVES.asItem());
// 芒果树叶
                output.accept(MANGOLEAVE_FRUITING_LEAVES.asItem());
// 木瓜树叶
                output.accept(PAWPAWLEAVE_FRUITING_LEAVES.asItem());
// 橘子树叶
                output.accept(TANGERINELEAVE_FRUITING_LEAVES.asItem());
// 橙子树叶
                output.accept(ORANGELEAVE_FRUITING_LEAVES.asItem());
// 苹果树叶
                output.accept(APPLELEAVE_FRUITING_LEAVES.asItem());
// 蜜桃树叶
                output.accept(HONEYPEACHLEAVE_FRUITING_LEAVES.asItem());
// 油桃树叶
                output.accept(NECTARINELEAVE_FRUITING_LEAVES.asItem());
// 梨树叶
                output.accept(PEARLEAVE_FRUITING_LEAVES.asItem());
// 甜瓜树叶
                output.accept(SWEETMELONLEAVE_FRUITING_LEAVES.asItem());
// 开心果树叶
                output.accept(PISTACHIONUTLEAVE_FRUITING_LEAVES.asItem());
// 红枣树叶
                output.accept(REDDATELEAVE_FRUITING_LEAVES.asItem());
// 核桃树叶
                output.accept(WALNUTLEAVE_FRUITING_LEAVES.asItem());
// 冬枣树叶
                output.accept(WINTERJUJUBELEAVE_FRUITING_LEAVES.asItem());
// 猕猴桃树叶
                output.accept(KIWIFRUITSSLEAVE_FRUITING_LEAVES.asItem());
// 山竹树叶
                output.accept(MANGOSTEENLEAVE_FRUITING_LEAVES.asItem());
// 桑葚树叶
                output.accept(MULBERRYLEAVE_FRUITING_LEAVES.asItem());
// 爬架作物
// 作物架
                output.accept(TRELLIS.asItem());
// 葡萄籽
                output.accept(GRAPESEED.get());
// 黄瓜籽
                output.accept(CUCUMBERSEEDS.get());
// 黄瓜
                output.accept(CUCUMBER.get());
// 冬瓜籽
                output.accept(WAX_GOURD_SEED_BLOCK.get());
// 冬瓜
                output.accept(WAXGOURD.get());
// 四季豆籽
                output.accept(KIDNEYBEANSEED.get());
// 四季豆
                output.accept(KIDNEYBEAN.get());
// 茄子籽
                output.accept(AUBERGINESEEDBLOCK.get());
// 茄子
                output.accept(AUBERGINE.get());
// 番茄籽
                output.accept(TOMATOSEED.get());
// 番茄
                output.accept(TOMATO.get());
// 豇豆籽
                output.accept(COWPEABEANSEED.get());
// 豇豆
                output.accept(COWPEA.get());
// 青葡萄籽
                output.accept(GREENGRAESEED.get());
// 青提
                output.accept(GREENGRAPE.get());
// 丝瓜籽
                output.accept(LOOFAHSEED.get());
// 丝瓜
                output.accept(LOOFAH.get());
// 青芒果
                output.accept(GREENMANGO.get());
// 葡萄
                output.accept(AGRAPE.get());
// 青葡萄
                output.accept(AGREENGRAPE.get());
// 菜花
                output.accept(CAULIFLOWER.get());
// 蒜薹
                output.accept(GARLICPEDICEL.get());
// 花生
                output.accept(PEANUT.get());
// 石榴籽
                output.accept(POMEGRANATE_SEED.get());
// 红尖椒
                output.accept(REDREPPER.get());
// 葱
                output.accept(SACLLION.get());
// 菠菜
                output.accept(SPINACH.get());
// 红薯
                output.accept(SWEETPOTATO.get());
// 黄桃
                output.accept(YELLOWPEACH.get());
// 畜牧产品
// 死牛
                output.accept(DEADCATTLE.get());
// 死羊
                output.accept(DEADSHEEP.get());
// 死猪
                output.accept(DEADPIG.get());
// 死鸡
                output.accept(DEADCHICKEN.get());
// 生牛血
                output.accept(RAWCATTLEBLOOD.get());
// 生牛脸
                output.accept(RAWCATTLEFACE.get());
// 生牛筋
                output.accept(RAWCATTLEJOINT.get());
// 生牛肚
                output.accept(RAWCATTLESTOMACH.get());
// 生牛肺
                output.accept(RAWCATTLELUNG.get());
// 生牛肝
                output.accept(RAWCATTLELIVER.get());
// 生牛肠
                output.accept(RAWCATTLEINTESTINE.get());
// 生牛心
                output.accept(RAWCATTLEHEART.get());
// 牛油
                output.accept(RAWCATTLEFAT.get());
// 生牛肉片
                output.accept(RAWDICEDCATTLEMEAT.get());
// 生牛雪花
                output.accept(RAWSNOWFLAKEBEEF.get());
// 生牛肘
                output.accept(RAWCATTLELEG.get());
// 生牛蹄
                output.accept(RAWCATTLEFEET.get());
// 生牛腱子肉
                output.accept(RAWCATTLETENDON.get());
// 牛角
                output.accept(BULLHORN.get());
// 牛大骨
                output.accept(BOVINEBONE.get());
// 动物头骨
                output.accept(ANIMALSKULL.get());
// 生猪血
                output.accept(RAWPIGBLOOD.get());
// 生猪肉片
                output.accept(RAWDICEDPIGMEAT.get());
// 生猪肉丝
                output.accept(RAWDICEDPIGMEAT_2.get());
// 生猪头肉
                output.accept(RAWPIGHEAD.get());
// 生猪耳朵
                output.accept(RAWPIGEAR.get());
// 生猪拱嘴
                output.accept(RAWPIGNOSE.get());
// 生猪尾巴
                output.accept(RAWPIGTAIL.get());
// 生猪皮
                output.accept(RAWPIGSKIN.get());
// 生猪五花肉
                output.accept(RAWPIGSTREAKYPORK.get());
// 生猪里脊肉
                output.accept(RAWPIGTENDERLOIN.get());
// 生猪排骨
                output.accept(RAWPIGSPARERIB.get());
// 生猪肘子
                output.accept(RAWPIGLEG.get());
// 生猪蹄
                output.accept(RAWPIGFEET.get());
// 生猪板油
                output.accept(RAWPIGFAT.get());
// 生猪肚
                output.accept(RAWPIGSTOMACH.get());
// 生猪肺
                output.accept(RAWPIGLUNG.get());
// 生猪肝
                output.accept(RAWPIGLIVER.get());
// 生猪肾
                output.accept(RAWPIGKIDNEY.get());
// 生猪心
                output.accept(RAWPIGHEART.get());
// 生猪肥肠
                output.accept(RAWPIGINTESTINE.get());
// 生猪脑花
                output.accept(RAWPIGCEREBRUM.get());
// 生羊血
                output.accept(RAWSHEEPBLOOD.get());
// 生羊肉片
                output.accept(RAWDICEDSHEEPMEAT.get());
// 生羊脸
                output.accept(RAWSHEEPFACE.get());
// 生羊尾油
                output.accept(RAWSHEEPTAILFAT.get());
// 生羊油
                output.accept(RAWSHEEPFAT.get());
// 生羊眼
                output.accept(RAWSHEEPEYE.get());
// 生羊肚
                output.accept(RAWSHEEPSTOMACH.get());
// 生羊肝
                output.accept(RAWSHEEPLIVER.get());
// 生羊肠
                output.accept(RAWSHEEPINTESTINE.get());
// 生羊肾
                output.accept(RAWSHEEPKIDNEY.get());
// 生羊心
                output.accept(RAWSHEEPHEART.get());
// 生羊排
                output.accept(RAWSHEEPSPARERIB.get());
// 生羊脊骨
                output.accept(RAWSHEEPSPINE.get());
// 生羊蹄
                output.accept(RAWSHEEPFEET.get());
// 生羊腿
                output.accept(RAWSHEEPLEG.get());
// 羊胡须
                output.accept(SHEEPBREAD.get());
// 生鸡血
                output.accept(RAWCHICKENBLOOD.get());
// 生鸡头
                output.accept(RAWCHICKENHEAD.get());
// 生鸡脖
                output.accept(RAWCHICKENNECK.get());
// 生鸡肉丁
                output.accept(RAWCHICKENPIECEPIECE.get());
// 生鸡胸肉
                output.accept(RAWCHICKENBREAST.get());
// 生鸡胸块
                output.accept(RAWCHICKENPIECE.get());
// 生鸡翅
                output.accept(RAWCHICKENWING.get());
// 生鸡翅尖
                output.accept(RAWCHICKENWINGTIP.get());
// 生鸡爪
                output.accept(RAWCHICKENFEET.get());
// 挂糊裹粉的鸡腿
                output.accept(RAWCHICKENLEGWITHLEG.get());
// 生鸡腿
                output.accept(RAWCHICKENLEG.get());
// 生鸡瘦肉
                output.accept(RAWCHICKENLEAN.get());
// 生鸡叉骨
                output.accept(RAWCHICKENFORK.get());
// 生鸡尾尖
                output.accept(RAWCHICKENASS.get());
// 生鸡心
                output.accept(RAWCHICKENHEART.get());
// 生鸡肝
                output.accept(RAWCHICKENLIVER.get());
// 生鸡胗
                output.accept(RAWCHICKENGIZZARD.get());
// 拔好毛的鸡
                output.accept(CHICKENWITHOUTFEATHER.get());
// 放血后的鸡
                output.accept(CHICKENWITHOUTBLOOD.get());
// 生鸡大腿
                output.accept(RAWHALFOFCHICKENLEG.get());
// 生羊尾
                output.accept(RAWSHEEPTAIL.get());
// 食物佳肴
// 糖酥茄盒
                output.accept(DRAWNEGGPLANT.get());
// 临沂炒鸡
                output.accept(LINYIFRIEDCHICKEN.get());
// 丸子汤
                output.accept(MEATBALLSOUP.get());
// 凉拌皮蛋
                output.accept(PRESERVEDEGGSALAD.get());
// 凉拌西红柿
                output.accept(TOMATOSALAD.get());
// 口水鸡
                output.accept(STEAMED_CHICKENWITH_CHILI_SAUCE.get());
// 可乐鸡翅
                output.accept(COLACHICKENWINGS.get());
// 四喜丸子
                output.accept(FOURJOYMEATBALLS.get());
// 回锅肉
                output.accept(STIRFRIEDBOILEDPORKSLICESINHOTSAUCE.get());
// 地三鲜
                output.accept(SAUTEED_POTATO_GREEN_PEPPER_EGGPLANT.get());
// 孜然洋葱炒肉片
                output.accept(FRIEDMEATWITHCUMINONION.get());
// 宫保鸡丁
                output.accept(KUNGPAOCHICKEN.get());
// 干煸四季豆
                output.accept(STIRFRIEDSTRINGBEANS.get());
// 拌丝小凉菜
                output.accept(MIXEDCOLDDISHES.get());
// 日本红烧豆腐
                output.accept(JAPANESEBRAISEDTOFU.get());
// 木耳黄瓜鸡蛋
                output.accept(SCRAMBLEDEGGSWITHFUNGUSANDCUCUMBER.get());
// 椒盐炒菇
                output.accept(PLEUROTUSERYNGIIWITHSALTANDPEPPER.get());
// 水煮肉片
                output.accept(POACHED_SPICY_SLICESOF_PORK.get());
// 水煮鱼
                output.accept(SLICED_FISHIN_HOT_CHILI_OIL.get());
// 油菜炒香菇
                output.accept(SAUTEEDMUSHROOMSWITHRAPESEED.get());
// 清蒸鱼
                output.accept(STEAMEDFISH.get());
// 炒豆橛子
                output.accept(FRIEDCOWPEA.get());
// 炒辣子鸡
                output.accept(FRIEDSPICYCHICKEN.get());
// 白切鸡
                output.accept(BOILED_CHICKENWITH_SAUCE.get());
// 红烧肉
                output.accept(STEWEDPORKWITHBROWNSAUCE.get());
// 菠菜炒肝尖
                output.accept(FRIEDLIVERTIPWITHSPINACH.get());
// 菠萝咕噜肉
                output.accept(PINEAPPLE_SWEETAND_SOUR_PORK.get());
// 葱油鸡
                output.accept(CHICKENWITH_SCALLION_OIL.get());
// 蛋黄炒菜花
                output.accept(SALTEDEGGYOLKFRIEDCAULIFLOWER.get());
// 西葫芦小炒肉
                output.accept(ZUCCHINISNACKMEAT.get());
// 酸菜鱼
                output.accept(BOILED_FISHWITH_PICKLED_CABBAGEAND_CHILI.get());
// 鱼香肉丝
                output.accept(FRIEDSHREDDEDPORKWITHSWEETANDSOURSAUCE.get());
// 麻婆豆腐
                output.accept(SPICYTOFU.get());
// 麻汁豆角
                output.accept(BEANWITHSESAMESAUCE.get());
// 麻辣卷心菜
                output.accept(SPICYCABBAGE.get());
// 奶油焗白蘑菇
                output.accept(BAKEDWHITEMUSHROOMSWITHCREAM.get());
// 豆芽焖粉丝
                output.accept(BRAISEDBEANSPROUTSWITHVERMICELLI.get());
// 粤式肠粉
                output.accept(CANTONESERICEROLLS.get());
// 咖喱鸡
                output.accept(CURRYSTEWEDCHICKEN.get());
// 秋葵浓汤
                output.accept(GUMBOSOUP.get());
// 红枣银耳粥
                output.accept(REDDATESANDTREMELLAPORRIDGE.get());
// 临沂牛肉糁
                output.accept(SA.get());
// 番茄炒蛋
                output.accept(SCRAMBLEDEGGWITHTOMATO.get());
// 大集羊杂汤
                output.accept(SHEEPGIBLETSSOUP.get());
// 蒸蛋羹
                output.accept(STEAMEDEGGCUSTARD.get());
// 香菇炖鸡汤
                output.accept(STEWEDCHICKENSOUPWITHMUSHROOMS.get());
// 冬瓜炖鸡
                output.accept(STEWEDCHICKENWITHWAXGOURD.get());
// 丝瓜炒蛋
                output.accept(STEWEDEGGSWITHLOOFAH.get());
// 炖猪杂
                output.accept(STEWEDPORKOFFAL.get());
// 冬枣冬瓜汤
                output.accept(WINTERJUJUBEANDWAXGOURDSOUP.get());

// 主食
// 馒头
                output.accept(STEAMEDBREAD.get());
// 馒头片
                output.accept(DICEDSTEAMEDBREAD.get());
// 面条
                output.accept(NOODLES.get());
// 干面条
                output.accept(DRYCOOKEDNOODLES.get());
// 挂面
                output.accept(COOKEDVERMICELLI.get());
// 鸡蛋挂面
                output.accept(EGGVERMICELLI.get());
// 花卷
                output.accept(STEAMEDTWISTEDROLL.get());
// 沂蒙煎饼
                output.accept(YIMENGPANCAKES.get());
// 杂粮煎饼
                output.accept(GRAINSPANCAKE.get());
// 面糊饼
                output.accept(PANADAPANCAKE.get());
// 葱油烙饼
                output.accept(SCALLIONOILPANCAKE.get());
// 手抓饼
                output.accept(HANDGRABBEDPANCAKE.get());
// 包子
                output.accept(COOKED_BAOZI.get());
// 汤圆
                output.accept(TANGYUAN.get());
// 饺子
                output.accept(COOKEDDUMPLING.get());
// 馄饨
                output.accept(WONTON.get());
// 肉卷
                output.accept(MEATFLOURROOL.get());
// 繁花卷
                output.accept(FANHUAROLL.get());
// 馅饼
                output.accept(PIE.get());
// 烧麦
                output.accept(SHUMAI.get());
// 月饼
                output.accept(HARD_MOONCAKE.get());
// 广式月饼
                output.accept(CANTONESE_MOONCAKE.get());
// 苏式月饼
                output.accept(SUZHOU_MOONCAKE.get());
// 京式月饼
                output.accept(BEIJING_MOONCAKE.get());
// 冰皮月饼
                output.accept(SNOW_SKIN_MOONCAKE.get());
// 热狗
                output.accept(HOTDOG.get());
// 汉堡包
                output.accept(HAMBURGERBREAD.get());
// 切面包片
                output.accept(DICEDBREAD.get());
// 意大利面
                output.accept(SPAGHETTI.get());
// 糍粑
                output.accept(COOKEDGLUTINOUSRICEPOUNDEDINTOPASTE.get());
// 艾糍
                output.accept(AIKUI.get());
// 糯米饼
                output.accept(STEAMEDGLUTINOUSRICECAKE.get());
// 熟糯米团
                output.accept(COOKEDGLUTINOUSRICE.get());
// 熟粽子
                output.accept(COOKEDZONGZI.get());
// 煮粉丝
                output.accept(COOKEDVERMICELLI_0.get());
// 米饭
                output.accept(RICE.get());
// 小米饭
                output.accept(MILLIETRICE.get());
// 高粱饭
                output.accept(KAOLIANGRICE.get());
// 荞麦饭
                output.accept(BUCKWHEATRICE.get());
// 燕麦饭
                output.accept(OAKRICE.get());
// 大米粥
                output.accept(RICE_PORRIDGE.get());
// 小米粥
                output.accept(MILLIETPORRIDGE.get());
// 燕麦粥
                output.accept(OAKPORRIDGE.get());
// 米浆汤
                output.accept(RICEPASTESOUP.get());
// 高粱粥
                output.accept(KAOLIANGPORRIDGE.get());
// 熟玉米糊
                output.accept(COOKEDCORNBATTER.get());
// 腊八粥
                output.accept(LABAPORRIDGE.get());
// 八宝粥
                output.accept(EIGHTTREASURECONGEE.get());
// 皮蛋瘦肉粥
                output.accept(CONGEE_WITH_MINCE_PORK_AND_PRESERVED_EGG.get());
// 绿豆粥
                output.accept(GREENBEANPORRIDGE.get());
// 煮米粉
                output.accept(BOILEDRICEFLOURNOODLES.get());
// 拌米粉
                output.accept(NOILEDRICENOODLE.get());
// 蒸肠粉
                output.accept(STEAMEDVERMICELLIROLL_0.get());
// 酸辣粉
                output.accept(HOTANDSOURRICENOODLES.get());

// 豆制品
// 豆腐
                output.accept(TOUFU.get());
// 白切豆腐片
                output.accept(BAISED_TOFU_SLICES.get());
// 豆腐块
                output.accept(PICEDTOUFU.get());
// 豆腐干
                output.accept(DRIEDTOUFU.get());
// 豆腐泡
                output.accept(TOFU_PUFFS.get());
// 豆腐肠
                output.accept(TOFU_SAUSAGE.get());
// 豆腐棒
                output.accept(TOFU_STICKS.get());
// 腐竹
                output.accept(YUBA.get());
// 豆腐脑
                output.accept(TOFUCURD.get());
// 甜豆腐脑
                output.accept(SWEETTOUFUCURD.get());
// 咸豆腐脑
                output.accept(SALTTOFUCURD.get());
// 响铃卷
                output.accept(FRIED_TOFU_SKIN_ROLLS.get());
// 干豆腐皮
                output.accept(DRIED_TOFU_SKIN.get());
// 千层豆腐皮
                output.accept(THOUSAND_LAYER_TOFU_SKIN.get());


// 饮品
// 可口可乐
                output.accept(COLA.get());
// 百事可乐
                output.accept(BLUE_COLA.get());
// 雪碧
                output.accept(SPRITE.get());
// 冰红茶
                output.accept(ICEDBLACKTEA.get());
// 劲凉冰红茶
                output.accept(ICEDBLACKTEABLUE.get());
// 无糖冰红茶
                output.accept(SUGARFREICEDTEA.get());
// 电解质饮料
                output.accept(ELECTROLYTEBEVERAGE.get());
// 枇杷膏
                output.accept(HEALTHLOQUATCREAM.get());
// 花生牛奶
                output.accept(HEALTHPEANUTMILK.get());
// 甜牛奶
                output.accept(SWEETMILK.get());
// 小麦奶
                output.accept(WHEATMILK.get());
// 核桃露
                output.accept(HEALTHWALNUTDEW.get());
// 原味酸奶
                output.accept(YOGURT.get());
// 热水
                output.accept(HOTWATER.get());
// 凉白开水
                output.accept(DRINKINGWATER.get());
// 美式咖啡
                output.accept(CAFE_AMERICANO.get());
// 拿铁咖啡
                output.accept(CAFELATTE.get());
// 摩卡咖啡
                output.accept(MOCHACAFE.get());
// 卡布奇诺咖啡
                output.accept(CAPPUCCINO.get());
// 豆浆
                output.accept(SOYAMILK.get());
// 红茶
                output.accept(REDTEA.get());
// 绿茶
                output.accept(GREENTEA.get());
// 柠檬红茶
                output.accept(LEMONREDTEA.get());
// 波霸奶绿
                output.accept(GRENTEAMILKWITHPEARL.get());
// 清新奶绿
                output.accept(GREEN_TEA_LATTES.get());
// 糯香柠檬茶
                output.accept(LEMONTEAWITHGLUTINOUSRICEFLAVOR.get());
// 苦荞茶
                output.accept(BUCKWHEATTEA.get());
// 阿萨姆奶茶
                output.accept(ASSAMMILK_TEA.get());
// 青梅绿茶
                output.accept(GREENPLUMTEA.get());
// 黑霸红奶
                output.accept(REDTEAMILKWITHPEARL.get());
// 贵州茅台
                output.accept(KWEICHOW_MOUTAI.get());
// 青岛啤酒
                output.accept(TSINGTAO_BEER.get());
// 葡萄酒
                output.accept(GRAPEWINE.get());
// 三十里红
                output.accept(KAOLIANGWINE.get());

// 点心
// 鸡蛋饼干
                output.accept(EGGBISCUIT.get());
// 鸡蛋饼
                output.accept(EGGPANCAKE.get());
// 煎蛋卷
                output.accept(OMELETTE.get());
// 蛋挞
                output.accept(EGGTART.get());
// 红豆蛋挞
                output.accept(REDBEANEGGTART.get());
// 草莓蛋糕卷
                output.accept(STRAWBERRYCAKEROLL.get());
// 核桃糕
                output.accept(WALNUTCAKE.get());
// 核桃酥
                output.accept(WALNUTSHORTBREAD.get());
// 鸡蛋糕
                output.accept(EGGCAKE.get());
// 红豆铜锣烧
                output.accept(REDBEANSTUFFINGDORAYAKI.get());
// 肉松小贝
                output.accept(SMALLMICEDMEATCAKE.get());
// 沙琪玛
                output.accept(SACHIMA.get());
// 脆皮鸡蛋小蛋糕
                output.accept(CRISPYEGGCAKE.get());
// 鸡蛋仔
                output.accept(EGGPUFFS.get());

// 炸货
// 年糕
                output.accept(NEWYEARCAKE.get());
// 上校鸡块
                output.accept(COLONELCHICKENNUGGETS.get());
// 油炸糯米条
                output.accept(FRIEDGULTINOUSRICESTRIPS.get());
// 油炸花生米
                output.accept(FRIEDPEANUTS.get());
// 炸干蘑
                output.accept(FRIEDMUSHROOM.get());
// 炸年糕
                output.accept(DRIEDRICECAKE.get());
// 炸春卷
                output.accept(FIREDSPRINGROLL.get());
// 炸油条
                output.accept(FRIEDDOUGHSTICK.get());
// 炸牛奶
                output.accept(DRIEDMILK.get());
// 炸肉丸
                output.accept(DRIEDMEATBALL.get());
// 炸茄子片
                output.accept(DRIEDDICEDAUBERGINE.get());
// 炸薄脆
                output.accept(FRIEDGIZZARD.get());
// 薯片
                output.accept(POTATOCHIPS.get());
// 炸薯条
                output.accept(FRENCHFRIES.get());
// 炸鸡排
                output.accept(FRIEDCHICKENCHOP.get());
// 炸鸡米花
                output.accept(FRIEDCHICKENCORN.get());
// 炸鸡腿
                output.accept(FRIEDCHICKENLEG.get());
// 炸鸡翅
                output.accept(FRIEDCHICKENWING.get());
// 炸香肠
                output.accept(FRIEDSAUSAGE.get());
// 炸藕合
                output.accept(DRIEDLOTUSROOT.get());
// 炸豆干
                output.accept(FRIEDDRIEDTOUFU.get());
// 炸豆腐
                output.accept(FRIEDTOUFU.get());
// 炸面包
                output.accept(DRIEDBREAD.get());
// 炸面包片
                output.accept(DRIEDDICEDBREAD.get());
// 炸馒头片
                output.accept(DRIEDDICEDSTEAMEDBREAD.get());
// 炸馓子
                output.accept(FRIEDSANZI.get());
// 炸麻团
                output.accept(FRIEDHEMPBALL.get());
// 炸麻花
                output.accept(FRIEDDOUGHTWIST.get());
// 脆皮手枪腿
                output.accept(CRISPYPISTOLLEG.get());

// 腌制
// 咸蛋
                output.accept(SALTYEGG.get());
// 咸蛋黄
                output.accept(SALTEGGYOLK.get());
// 皮蛋
                output.accept(COOKEDPRESERVEDEGG.get());
// 咸萝卜
                output.accept(SALTYRADDISH.get());
// 酸菜
                output.accept(PICKLEDVEGETABLE.get());
// 切酸菜
                output.accept(DICEDPICKLEDVEGETABLE.get());
// 腊肉
                output.accept(CUREDMEAT.get());
// 腊肠
                output.accept(CUREDSAUSAGE.get());
// 腌黄瓜片
                output.accept(SALTYDICEDCURUMBER.get());
// 黄瓜丝咸菜
                output.accept(SLICEDSALTYCUCUMBER.get());

// 烧烤
// 奥尔良烤翅
                output.accept(ORLEANWING.get());
// 奥尔良烤鸡腿
                output.accept(ORLEANLEG.get());
// 烤蘑菇
                output.accept(ROASTEDMUSHROOM.get());
// 烤金针菇
                output.accept(ROASTED_FLAMMULINAVELUTIPES.get());
// 烤韭菜
                output.accept(ROASTEDCHINESECHIVES.get());
// 烤鱼片
                output.accept(ROASTEDDICEDFISH.get());
// 卤肉
// 卤猪肘子
                output.accept(STEWEDPIGLEG.get());
// 卤鸡大腿
                output.accept(STEWEDHALFCHICKENLEG.get());
// 卤鸡心
                output.accept(STEWEDCHICKENHEART.get());
// 卤鸡肝
                output.accept(STEWEDCHICKENLIVERS.get());
// 卤鸡腿
                output.accept(STEWEDCHICKENLEG.get());
// 酱牛肉
                output.accept(SAUCINGBEEF.get());
// 烤肉
// 烤牛心
                output.accept(COOKEDCATTLEHEART.get());
// 烤牛筋
                output.accept(COOKEDCATTLEJOINT.get());
// 烤牛肚
                output.accept(COOKEDCATTLESTOMACH.get());
// 烤牛肺
                output.accept(COOKEDCATTLELUNG.get());
// 烤牛肝
                output.accept(COOKEDCATTLELIVER.get());
// 烤牛肠
                output.accept(COOKEDCATTLEINTESTINE.get());
// 烤牛肉片
                output.accept(COOKEDDICEDCATTLE.get());
// 烟熏牛头
                output.accept(COOKEDCATTLEFACE.get());
// 烤牛雪花
                output.accept(COOKEDCATTLEMARBLEDBEEF.get());
// 烤牛肘
                output.accept(COOKEDCATTLELEG.get());
// 烤牛蹄
                output.accept(COOKEDCATTLEFEET.get());
// 香烤牛腱子
                output.accept(COOKEDCATTLETENDON.get());
// 烤猪肉片
                output.accept(COOKEDDICEDPIGMEAT.get());
// 烤猪肉丝
                output.accept(COOKEDPIGSHREDS.get());
// 烤猪耳朵
                output.accept(COOKEDPIGEAR.get());
// 烤猪鼻子
                output.accept(COOKEDPIGNOSE.get());
// 烤猪尾巴
                output.accept(COOKEDPIGTAIL.get());
// 烤猪皮
                output.accept(COOKEDPIGSKIN.get());
// 烤猪五花
                output.accept(COOKEDPIGSTREAKYPORK.get());
// 烤猪里脊
                output.accept(COOKEDPIGTENDERLOIN.get());
// 烤猪排骨
                output.accept(COOKEDPIGSPARERIB.get());
// 烤猪肘子
                output.accept(COOKEDPIGLEG.get());
// 烤猪蹄
                output.accept(COOKEDPIGFEET.get());
// 烤猪油渣
                output.accept(COOKEDPIGFAT.get());
// 烤猪肚
                output.accept(COOKEDPIGSTOMACH.get());
// 烤猪肺
                output.accept(COOKEDPIGLUNG.get());
// 烤猪肝
                output.accept(COOKEDPIGLIVER.get());
// 烤猪腰子
                output.accept(COOKEDPIGKIDNEY.get());
// 烤猪心
                output.accept(COOKEDPIGHEART.get());
// 烤猪脑花
                output.accept(COOKEDPIGCEREBRUM.get());
// 烤猪肥肠
                output.accept(COOKEDPIGINTESTINE.get());
// 烤猪脸
                output.accept(COOKEDPIGFACE.get());
// 烤羊肉片
                output.accept(COOKEDDICEDSHEEPMEAT.get());
// 烤羊脸
                output.accept(COOKEDSHEEPFACE.get());
// 烤羊眼
                output.accept(COOKEDSHEEPEYE.get());
// 烤羊尾
                output.accept(COOKEDSHEEPTAIL.get());
// 烤羊肚
                output.accept(COOKEDSHEEPSTOMACH.get());
// 烤羊肝
                output.accept(COOKEDSHEEPLIVER.get());
// 烤羊肠
                output.accept(COOKEDSHEEPINTESTINE.get());
// 烤羊肾
                output.accept(COOKEDSHEEPKIDNEY.get());
// 烤羊心
                output.accept(COOKEDSHEEPHEART.get());
// 煎烤羊排
                output.accept(COOKEDSHEEPSPARERLIP.get());
// 烤羊脊骨
                output.accept(COOKEDSHEEPSPINE.get());
// 烤羊蹄
                output.accept(COOKEDSHEEPFEET.get());
// 美味烤羊腿
                output.accept(COOKEDSHEEPLEG.get());
// 羊油渣
                output.accept(COOKEDSHEEPFAT.get());
// 烤鸡肉丁
                output.accept(COOKEDCHICKENDICED.get());
// 碎烤鸡肉
                output.accept(COOKEDCHICKENPIECEPIECE.get());
// 烤鸡头
                output.accept(COOKEDCHICKENHEAD.get());
// 烤鸡脖
                output.accept(COOKEDCHICKENNECK.get());
// 烤鸡翅
                output.accept(COOKEDCHICKENWING.get());
// 烤鸡翅尖
                output.accept(COOKEDCHICKENWINGTIP.get());
// 烤鸡爪
                output.accept(COOKEDCHICKENFEET.get());
// 烤鸡大腿
                output.accept(COOKEDCHICKENLEGWITHLEG.get());
// 烤鸡腿
                output.accept(COOKEDCHICKENLEG.get());
// 烤鸡块
                output.accept(COOKEDCHICKENPIECE.get());
// 烤鸡排
                output.accept(COOKEDCHICKENSTEAK.get());
// 烤鸡叉骨
                output.accept(COOKEDCHICKENFORK.get());
// 烤鸡尾尖
                output.accept(COOKEDCHICKENASS.get());
// 烤鸡心
                output.accept(COOKEDCHICKENHEART.get());
// 烤鸡肝
                output.accept(COOKEDCHICKENLIVER.get());
// 烤鸡叉骨
                output.accept(ROASTCHICKENFORK.get());
// 烤鸡胗
                output.accept(COOKEDCHICKENGIZZARD.get());
// 熟鸡胸肉
                output.accept(COOKEDCHICKENBREAST.get());
// 熟鸡瘦肉
                output.accept(COOKEDCHICKENLEAN.get());
// 脆皮烤肉
                output.accept(COOKEDCRISPYPORK.get());
// 熟肥牛片
                output.accept(COOKEDFLAKEBEEF.get());
// 熟鸡大腿
                output.accept(COOKEDHALFOFCHCIKENLEG.get());
// 熟猪五花肉
                output.accept(COOKEDPIGSTREAKMEAT.get());
// 棒冰
// 苹果棒冰
                output.accept(APPLEPOPSICLE.get());
// 杏子棒冰
                output.accept(APRICOTPOPSICLE.get());
// 香蕉棒冰
                output.accept(BANANAPOPSICLE.get());
// 蓝莓棒冰
                output.accept(BLUEBERRYPOPSICLE.get());
// 杨桃棒冰
                output.accept(CARAMBOLAPOPSICLE.get());
// 樱桃棒冰
                output.accept(CHERRYPOPSICLE.get());
// 巧克力棒冰
                output.accept(CHOCOLATEPOPSICLE.get());
// 椰子棒冰
                output.accept(COCONUTPOPSICLE.get());
// 咖啡棒冰
                output.accept(COFFEEPOPSICLE.get());
// 火龙果棒冰
                output.accept(DRAGONFRUITPOPSICLE.get());
// 榴莲棒冰
                output.accept(DURIANPOPSICLE.get());
// 葡萄棒冰
                output.accept(GRAPEPOPSICLE.get());
// 青葡萄棒冰
                output.accept(GREENGRAPEPOPSICLE.get());
// 青李子棒冰
                output.accept(GREENPLUMPOPSICLE.get());
// 哈密瓜棒冰
                output.accept(HAMIMELONPOPSICLE.get());
// 山楂棒冰
                output.accept(HAWTHORNPOPSICLE.get());
// 水蜜桃棒冰
                output.accept(HONEYPEACHPOPSICLE.get());
// 猕猴桃棒冰
                output.accept(KIWIFRUITPOPSICLE.get());
// 柠檬棒冰
                output.accept(LEMONPOPSICLE.get());
// 枇杷棒冰
                output.accept(LOQUATPOPSICLE.get());
// 荔枝棒冰
                output.accept(LYCHEEPOPSICLE.get());
// 芒果棒冰
                output.accept(MANGOPOPSICLE.get());
// 山竹棒冰
                output.accept(MANGOSTEEPOPSICLE.get());
// 桑葚棒冰
                output.accept(MULBERRYPOPSICLE.get());
// 油桃棒冰
                output.accept(NECTARINEPOPSICLE.get());
// 橙子棒冰
                output.accept(ORANGEPOPSICLE.get());
// 木瓜棒冰
                output.accept(PAWPAWPOPSICLE.get());
// 梨子棒冰
                output.accept(PEARPOPSICLE.get());
// 菠萝棒冰
                output.accept(PINEAPPLEPOPSICLE.get());
// 李子棒冰
                output.accept(PLUMPOPSICLE.get());
// 石榴棒冰
                output.accept(POMEGRANATEPOPSICLE.get());
// 棒冰
                output.accept(POPSICLE.get());
// 草莓棒冰
                output.accept(STRAWBERRYPOPSICLE.get());
// 甜浆果棒冰
                output.accept(SWEETBERRYPOPSICLE.get());
// 甜瓜棒冰
                output.accept(SWEETMELONPOPSICLE.get());
// 橘子棒冰
                output.accept(TANGERINEPOPSICLE.get());
// 西瓜棒冰
                output.accept(WATERMELONPOPSICLE.get());
// 冬枣棒冰
                output.accept(WINTERJUJUBEPOPSICLE.get());
// 冰激凌
// 苹果冰淇淋
                output.accept(APPLEICECREAM.get());
// 杏子冰淇淋
                output.accept(APRICOTICECREAM.get());
// 香蕉冰淇淋
                output.accept(BANANAICECREAM.get());
// 蓝莓冰淇淋
                output.accept(BLUEBERRYICECREAM.get());
// 杨桃冰淇淋
                output.accept(CARAMBOLAICECREAM.get());
// 樱桃冰淇淋
                output.accept(CHERRYICECREAM.get());
// 巧克力冰淇淋
                output.accept(CHOCOLATEICECREAM.get());
// 椰子冰淇淋
                output.accept(COCONUTICECREAM.get());
// 咖啡冰淇淋
                output.accept(COFFEEICECREAM.get());
// 火龙果冰淇淋
                output.accept(DRAGONFRUITICECREAM.get());
// 榴莲冰淇淋
                output.accept(DURIANICECREAM.get());
// 葡萄冰淇淋
                output.accept(GRAPEICECREAM.get());
// 青葡萄冰淇淋
                output.accept(GREENGRAPEICECREAM.get());
// 青李子冰淇淋
                output.accept(GREENPLUMICECREAM.get());
// 哈密瓜冰淇淋
                output.accept(HAMIMELONICECREAM.get());
// 山楂冰淇淋
                output.accept(HAWTHORNICECREAM.get());
// 水蜜桃冰淇淋
                output.accept(HONEYPEACHICECREAM.get());
// 猕猴桃冰淇淋
                output.accept(KIWIFRUITICECREAM.get());
// 柠檬冰淇淋
                output.accept(LEMONICECREAM.get());
// 枇杷冰淇淋
                output.accept(LOQUATICECREAM.get());
// 荔枝冰淇淋
                output.accept(LYCHEEICECREAM.get());
// 芒果冰淇淋
                output.accept(MANGOICECREAM.get());
// 山竹冰淇淋
                output.accept(MANGOSTEENICECREAM.get());
// 牛奶冰淇淋
                output.accept(MILKICECREAM.get());
// 桑葚冰淇淋
                output.accept(MULBERRYICECREAM.get());
// 油桃冰淇淋
                output.accept(NECTARINEICECREAM.get());
// 橙子冰淇淋
                output.accept(ORANGEICECREAM.get());
// 木瓜冰淇淋
                output.accept(PAWPAWICECREAM.get());
// 梨子冰淇淋
                output.accept(PEARICECREAM.get());
// 菠萝冰淇淋
                output.accept(PINEAPPLEICECREAM.get());
// 李子冰淇淋
                output.accept(PLUMICECREAM.get());
// 石榴冰淇淋
                output.accept(POMEGRANATEICECREAM.get());
// 草莓冰淇淋
                output.accept(STRAWBERRYICECREAM.get());
// 甜浆果冰淇淋
                output.accept(SWEETBERRYICECREAM.get());
// 甜瓜冰淇淋
                output.accept(SWEETMELONICECREAM.get());
// 橘子冰淇淋
                output.accept(TANGERINEICECREAM.get());
// 西瓜冰淇淋
                output.accept(WATERMELONICECREAM.get());
// 冬枣冰淇淋
                output.accept(WINTERJUJUBEICECREAM.get());
// 果酱
// 菠萝果酱
                output.accept(PINEAPPLEJAM.get());
// 草莓果酱
                output.accept(STRAWBERRYJAM.get());
// 橙子果酱
                output.accept(ORANGEJAM.get());
// 冬枣果酱
                output.accept(WINTERJUJUBEJAM.get());
// 哈密瓜果酱
                output.accept(HAMIMELONJAM.get());
// 火龙果果酱
                output.accept(DRAGONFRUITJAM.get());
// 橘子果酱
                output.accept(TANGERINEJAM.get());
// 蓝莓果酱
                output.accept(BLUEBERRYJAM.get());
// 梨果酱
                output.accept(PEARJAM.get());
// 荔枝果酱
                output.accept(LYCHEEJAM.get());
// 李子果酱
                output.accept(PLUMJAM.get());
// 榴莲果酱
                output.accept(DURIANJAM.get());
// 芒果果酱
                output.accept(MANGOJAM.get());
// 猕猴桃果酱
                output.accept(KIWIFRUITJAM.get());
// 木瓜果酱
                output.accept(PAWPAWJAM.get());
// 柠檬果酱
                output.accept(LEMONJAM.get());
// 枇杷果酱
                output.accept(LOQUATJAM.get());
// 苹果果酱
                output.accept(APPLEJAM.get());
// 葡萄果酱
                output.accept(GRAPEJAM.get());
// 青梅果酱
                output.accept(GREEMPLUMJAM.get());
// 青提果酱
                output.accept(GREENGRAPEJAM.get());
// 桑葚果酱
                output.accept(MULBERRYJAM.get());
// 山楂果酱
                output.accept(HAWTHORNJAM.get());
// 山竹果酱
                output.accept(MANGOSTEENJAM.get());
// 石榴果酱
                output.accept(POMEGRANATEJAM.get());
// 水蜜桃果酱
                output.accept(HONEYPEACHJAM.get());
// 香瓜果酱
                output.accept(SWEETMELONJAM.get());
// 甜浆果果酱
                output.accept(SWEETBERRYJAM.get());
// 西瓜果酱
                output.accept(WATERMELONJAM.get());
// 香蕉果酱
                output.accept(BANANAJAM.get());
// 杏果酱
                output.accept(APRICOTJAM.get());
// 杨桃果酱
                output.accept(CARAMBOLAJAM.get());
// 椰子果酱
                output.accept(COCONUTJAM.get());
// 樱桃果酱
                output.accept(CHERRYJAM.get());
// 油桃果酱
                output.accept(NECTARINEJAM.get());
// 果汁
// 哈密瓜果汁
                output.accept(HAMIMELONJUICE.get());
// 山楂果汁
                output.accept(HAWTHORNJUIVE.get());
// 火龙果果汁
                output.accept(DRAGONFRUIEJUICE.get());
// 杨桃果汁
                output.accept(CARAMBOLAJUICE.get());
// 冬枣果汁
                output.accept(WINTERJUJUBEJUICE.get());
// 石榴果汁
                output.accept(POMEGRANATEJUICE.get());
// 甜浆果果汁
                output.accept(SWEETBERRYJUICE.get());
// 榴莲果汁
                output.accept(DURIANJUICE.get());
// 柠檬果汁
                output.accept(LEMONJUICE.get());
// 蓝莓果汁
                output.accept(BLUEBERRYJUICE.get());
// 香瓜果汁
                output.accept(SWEETMELONJUICE.get());
// 青提果汁
                output.accept(GREENGRAPEJUICE.get());
// 橘子果汁
                output.accept(TANGERINEJUICE.get());
// 山竹果汁
                output.accept(MANGOSTEENJUICE.get());
// 樱桃果汁
                output.accept(CHERRYJUICE.get());
// 草莓果汁
                output.accept(STRAWBERRYJUICE.get());
// 橙子果汁
                output.accept(ORANGEJUICE.get());
// 青梅果汁
                output.accept(GREENPLUMJUICE.get());
// 苹果果汁
                output.accept(APPLEJUICE.get());
// 水蜜桃果汁
                output.accept(HONEYPEACHJUIVE.get());
// 香蕉果汁
                output.accept(BANANAJUICE.get());
// 荔枝果汁
                output.accept(LYCHEEJUICE.get());
// 杏果汁
                output.accept(APRICOTJUICE.get());
// 葡萄果汁
                output.accept(GRAPEJUICE.get());
// 桑葚果汁
                output.accept(MULBERRYJUIVE.get());
// 李子果汁
                output.accept(PKUMJUICE.get());
// 芒果果汁
                output.accept(MANGOJUICE.get());
// 西瓜果汁
                output.accept(WATERMELONJUICE.get());
// 油桃果汁
                output.accept(NECTARINEJUICE.get());
// 菠萝果汁
                output.accept(PINEAPPLEJUICE.get());
// 枇杷果汁
                output.accept(LOQUATJUICE.get());
// 猕猴桃果汁
                output.accept(KIWIFRUITJUICE.get());
// 梨果汁
                output.accept(PEARJUICE.get());
// 木瓜果汁
                output.accept(PAWPAWJUICE.get());
// 椰子果汁
                output.accept(COCONUTJUICE.get());

// 小吃
// 哈密瓜片
                output.accept(DICEDHAMIMELON.get());
// 泡椒凤爪
                output.accept(CHICKENFEETWITHPEPPERS.get());
// 熟山楂
                output.accept(COOKEDHAWTHORN.get());
// 水煮玉米
                output.accept(BOILEDCORN.get());
// 虎皮青椒
                output.accept(TIGERGREENPEPPER.get());
// 辣条
                output.accept(CHINESESPICYSNACKFOOD.get());
// 酒鬼麻辣花生
                output.accept(SPICYPEANUT.get());
// 香肠
                output.accept(COOKEDSAUSAGE.get());
// 冰糖熟梨
                output.accept(RIPEPEARWITHROCKSUGAR.get());
// 冰糖葫芦
                output.accept(SUGARCOATEDHAWS.get());
// 煮山药
                output.accept(COOKEDCHINESEYAM.get());
// 山药蘸白糖
                output.accept(CHINESEYAMANDSUGAR.get());
// 巧克力
                output.accept(CHOCOLATE.get());
// 巧克力豆
                output.accept(CHOCOLATEBEAN.get());
// 爆米花
                output.accept(POPCORN.get());
// 焦糖爆米花
                output.accept(CARAMELPOPCORN.get());
// 奶油爆米花
                output.accept(CREAMPOPCORN.get());
// 巧克力爆米花
                output.accept(CHOCOLATEPOPCORN.get());
// 白糖果冻
                output.accept(JELLY.get());
// 糖渍小枣
                output.accept(TANG_ZI_XIAO_ZAO.get());
// 糖霜核桃
                output.accept(SUGARCOATEDWALNUT.get());
// 无骨柠檬凤爪
                output.accept(BONELESSLEMONCHICKENFEET.get());
// 烟熏鸡胸肉
                output.accept(SOMKEDCHICKENBREAST.get());
// 肉松
                output.accept(MEATFLOSS.get());
// 鱼片刺身
                output.accept(FISHSASHIMI.get());
// 烤紫薯
                output.accept(ROASTEDPURPLEPOTATO.get());
// 烤花生
                output.accept(ROASTEDPEANUT.get());
// 蒸血糕
                output.accept(STEAMEDBLOOD.get());
// 骨头汤
                output.accept(BONESOUP.get());
// 盐水菠萝片
                output.accept(EATENPINEAPPLE.get());
// 豌豆粉糕
                output.accept(PEAFLOURCAKE.get());
// 烤红薯
                output.accept(ROASTEDSWEETPOTATO.get());
// 剥壳煮蛋
                output.accept(SHELLEDBOILEDEGG.get());
// 手开心果
                output.accept(SHOU_KAI_XIN_GUO.get());
// 蒸盐橙
                output.accept(STEAMEDSALTORANGE.get());
// 滚烫的糯米
                output.accept(HOT_GLUTINOUS.get());
// 金米糕
                output.accept(GOLDRICECAKE.get());
// 金米糕
                output.accept(GOLDRICECAKEMAX.get());
// 金葡萄
                output.accept(GOLDENGRAPE.get());
// 金葡萄
                output.accept(GOLDENGRAPEMAX.get());
// 五角杨桃的乐趣
                output.accept(FIVEPOINTEDCARAMBOLADELIGHT.get());
// 马丞相的乐趣
                output.accept(M_SECRETDELIGHT.get());
// 烤全羊
                output.accept(COOKED_WHOLE_SHEEP.asItem());
// 食材杂项
// 切好的白菜
                output.accept(CUT_CHINESE_CABBAGE.get());
// 纯净水
                output.accept(TIDYWATER.get());
// 食用盐
                output.accept(SALT.get());
// 盐矿渣
                output.accept(SALTPIECE.get());
// 食用油
                output.accept(COOKINGOIL.get());
// 酿造酱油
                output.accept(SOY.get());
// 食醋
                output.accept(VINEGAR.get());
// 香油
                output.accept(SESAMEOIL.get());
// 冰糖块
                output.accept(CRYSTALSUGAR.get());
// 红糖
                output.accept(BROWNSUGAR.get());
// 清糖浆
                output.accept(WHITESUGARSYRUP.get());
// 糖浆
                output.accept(BROWNSUGARSYRUP.get());

// 麻汁
                output.accept(MAJUICE.get());
// 黄油
                output.accept(BUTTER.get());
// 咖喱酱汁
                output.accept(CURRY.get());
// 骨汤精华
                output.accept(BONESOUPESSENCE.get());
// 甜面酱
                output.accept(SWEETFLOURASUVE.get());
// 豆瓣酱
                output.accept(THICKBROADBEANSAUCE.get());
// 卤料包
                output.accept(HALOGENBAG.get());
// 丁香粉
                output.accept(LILACPOWDER.get());
// 五香粉
                output.accept(FIVESPICEPOWDER.get());
// 八角粉
                output.accept(ANISEEDPOWDER.get());
// 奥尔良粉
                output.accept(ORLEANSPOWDER.get());
// 姜粉
                output.accept(GROUNDPOWDER.get());
// 孜然粉
                output.accept(CUMINPOWDER.get());
// 桂皮粉
                output.accept(CINNAMONPOWDER.get());
// 椒盐粉
                output.accept(PEPPEREDSALT.get());
// 洋葱粉
                output.accept(ONIONPOWDER_2.get());
// 红茶粉
                output.accept(REDTEAPOWDER.get());
// 绿茶粉
                output.accept(GREENTEAPOWDER.get());
// 花椒粉
                output.accept(CHINESEPICKLYASHPOWDER.get());
// 茴香粉
                output.accept(FENNELPOWDER.get());
// 葱粉
                output.accept(ONIONPOWDER.get());
// 蒜粉
                output.accept(GARLICPOWDER.get());
// 辣椒粉
                output.accept(CHILLIPOWDER.get());

// 酱料
// 可可酱
                output.accept(COCOASAUCE.get());
// 奶油
                output.accept(CREAM.get());
// 新鲜牛奶
                output.accept(MILKBOTTLE.get());
// 杂粮面糊
                output.accept(MULTIGRAINBATTER.get());
// 浓缩糖浆
                output.accept(CONCENTRATEDSYRUP.get());
// 甜蛋液
                output.accept(SWEETEGGLIQUID.get());
// 生豆浆
                output.accept(RAWSOYBEANMILK.get());
// 糯米面糊
                output.accept(GLUTINOUSRICEBATTER.get());
// 花生酱
                output.accept(PEANUTJAM.get());
// 蛋液
                output.accept(EGGLIQUID.get());
// 面糊
                output.accept(PANADA.get());
// 食用明胶
                output.accept(GELATIN.get());
// 鸡蛋面糊
                output.accept(EGGBATTER.get());
// 盐水
                output.accept(SALTYWATER.get());
// 可可浆
                output.accept(COCOAMASS.get());

// 粉或者粮食类
// 面粉
                output.accept(WHEATFLOUR.get());
// 可可粉
                output.accept(COCOAPOWDER.get());
// 咖啡粉
                output.accept(COFFEEPOWDER.get());
// 木薯粉
                output.accept(TAPIOCAFLOUR.get());
// 核桃粉
                output.accept(WALNUTPOWDER.get());
// 糯米粉
                output.accept(GULTINOUSRICEPOWDER.get());
// 红薯淀粉
                output.accept(SWEETPOTATSTARCH.get());
// 芝麻粉
                output.accept(SESAMEPOWDER.get());
// 芝麻糯米粉
                output.accept(GULTINOUSRICESASAMEPOWDER.get());
// 花生粉
                output.accept(PEANUTPOWDER.get());
// 花生芝麻粉
                output.accept(PEANUTSESAMEPOWDER.get());
// 调制小麦粉
                output.accept(MODULATEDWHEATFLOUR.get());
// 豌豆粉
                output.accept(PEAMEAL.get());
// 豌豆粉
                output.accept(PEAFLOUR.get());
// 燕麦粒
                output.accept(OATGRAIN.get());
// 糯米
                output.accept(POLISHEDGLUTINOUSRICE_2.get());

// 干的
// 干八角
                output.accept(DRYANISEED.get());
// 干桂皮
                output.accept(DRYCINNAMON.get());
// 干咖啡豆
                output.accept(DRYCOFFEEBEAN.get());
// 干丁香
                output.accept(DRYLILAC.get());
// 干肉豆蔻
                output.accept(DRYNUTMEG.get());
// 干葱
                output.accept(DRYSCALION.get());
// 干花椒
                output.accept(DRYSICHUANPEPPER.get());


// 馅料
// 冬瓜馅
                output.accept(WAXGROUDPASTE.get());
// 枣泥馅
                output.accept(JUJUBEPASTE.get());
// 核桃碎
                output.accept(BROKENWALNUT.get());
// 椒盐馅
                output.accept(PEPPERANDSALTMASS.get());
// 水果馅
                output.accept(FRUITFLAVOREDMOONCAKESTUFFING.get());
// 肉馅
                output.accept(MEATPASTE.get());
// 蔬菜馅
                output.accept(VEGETABLEPASTE.get());
// 红豆馅
                output.accept(REDBEANPASTE.get());
// 蔬菜肉馅
                output.accept(MEATANDVEGETABLESTUFFING.get());
// 鸡蛋肉馅
                output.accept(MEATANDEGGPASTE.get());
// 芝麻花生丸
                output.accept(SESAMEANDPEANUTBALLS.get());
// 豌豆馅
                output.accept(PEASTUFFING.get());
// 鸡蛋馅
                output.accept(EGGSTUFFING.get());
// 豌豆泥
                output.accept(PEAPASTE.get());
// 芝麻糯米丸
                output.accept(SESAMEGLUTINOUSRICEBALLS.get());
// 肉蛋馅
                output.accept(MEATANDEGGFILLING.get());
// 肉菜馅
                output.accept(MEATANDVEGETABLEFILLING.get());
// 绿豆泥
                output.accept(MUNGBEANPASTE.get());
// 冬瓜泥
                output.accept(WAXGOURDPASTE.get());
// 不太常见的馅料
                output.accept(UNCOMMONSTUFFING.get());
// 菜蛋馅
                output.accept(VEGETABLEANDEGGSTUFFING.get());
// 面类
// 包子皮
                output.accept(BAOZI_SKIN.get());
// 压实的米浆
                output.accept(COMPACTEDRICEBRICK.get());
// 干粉丝
                output.accept(DRIEDVERMICELLI.get());
// 干粉条
                output.accept(DRYPOWDERSTRIP.get());
// 干粉皮
                output.accept(DRYPOWDERSKIN.get());
// 意大利通心粉
                output.accept(MACARONI.get());
// 捶打后的糯米面剂子
                output.accept(BEATGLUTINOUSRICEFLOURPASTE.get());
// 核桃仁面剂子
                output.accept(WALNUTFLOURPASTE.get());
// 涂了油的小面皮
                output.accept(OILEDFLOURSKIN.get());
// 烧麦皮
                output.accept(SHUMAISKIN.get());
// 干米糕
                output.accept(DRYRICECAKE.get());
// 生月饼
                output.accept(RAWMOONCAKE.get());
// 生的广式月饼
                output.accept(RAWCANTONESE_MOONCAKE.get());
// 生的苏式月饼
                output.accept(RAWSUZHOU_MOONCAKE.get());
// 生的京式月饼
                output.accept(RAWBEIJING_MOONCAKE.get());
// 生的冰皮月饼
                output.accept(RAWSNOW_SKIN_MOONCAKE.get());
// 生烧麦
                output.accept(RAWSHUMAI.get());
// 生粗面条
                output.accept(RAWCOARSENOODLES.get());
// 生糯米面剂子
                output.accept(RAWGLUTINOUSPASTE.get());
// 生的撒芝麻糯米面剂子
                output.accept(RAWSESAMEGLUTINOUSPASTE.get());
// 生糯米面团
                output.accept(RAWGLUTINOUSDOUGH.get());
// 生糯米面条
                output.accept(RAWGLUTINOUSNOODLES.get());
// 生糯米面皮
                output.accept(RAWGLUTINOUSSKIN.get());
// 生糯米面饼
                output.accept(RAWGLUTINOUSRICEFLOURPANCAKE.get());
// 生面卷
                output.accept(RAWFLOURROLL.get());
// 生面条
                output.accept(RAWNOODLES.get());
// 生面饼
                output.accept(RAWFLOURPANCAKE.get());
// 生馅饼
                output.accept(RAWPIE.get());
// 生鸡蛋面剂子
                output.accept(RAWEGGPASTE.get());
// 生鸡蛋面皮
                output.accept(RAWEGGSKIN.get());
// 生鸡蛋饼
                output.accept(RAWEGGPANCAKE.get());
// 米粉
                output.accept(RAWRICENOODLES.get());
// 米线
                output.accept(RICENOODLES_2.get());
// 红薯淀粉面团
                output.accept(SWEETPOTATODOUGH.get());
// 生肠粉
                output.accept(VERMICELLIROLL.get());
// 蒸米糕
                output.accept(STEAMEDRICEBRICK.get());
// 裹上糖浆的面条
                output.accept(NOODLESWRAPPEDINSYRUP.get());
// 裹上糖浆的鸡蛋面条
                output.accept(EGGNOODLESWRAPPEDINSYRUP.get());
// 面剂子
                output.accept(FLOURPASTE.get());
// 面团
                output.accept(DOUGH.get());
// 饺子皮
                output.accept(DUMPLING_SKIN.get());
// 馄饨皮
                output.accept(FLOURSKIN.get());
// 生鸡蛋面条
                output.accept(RAWEGGNOODLES.get());
// 鸡蛋面团
                output.accept(EGGDOUGH.get());
// 糖油面团
                output.accept(SWEET_OIL_DOUGH.get());
// 油酥面团
                output.accept(WATER_SHORT_DOUGH.get());
// 提浆面团
                output.accept(SYRUP_DOUGH.get());
// 糯奶面团
                output.accept(GLUTINOUS_MILK_DOUGH.get());
// 切开的面饼
                output.accept(CUTPANCAKE.get());
// 发面剂子
                output.accept(FERMENTEDFLOURPASTE.get());
// 切开的鸡蛋面饼
                output.accept(CUTEGGPANCAKE.get());
// 滤布裹起来的米浆
                output.accept(RICESLURRYWRAPPEDINFILTERCLOTH.get());
// 捶打后的糯米剂子
                output.accept(POUNDEDGLUTINOUSPASTE.get());
// 长剂子
                output.accept(LONGDOUGH.get());
// 长鸡蛋面饼
                output.accept(LONGEGGPANCAKE.get());
// 切菜
// 芦苇叶
                output.accept(REEDLEAF.get());
// 洗净的芦苇叶
                output.accept(TIDYREEDLEAF.get());
// 丝瓜段
                output.accept(DICEDLOOFACH.get());
// 切开的冬瓜
                output.accept(DICEDWAXGOURD.get());
// 切开的橙子
                output.accept(DICEDORANGE.get());
// 切片的杏鲍菇
                output.accept(DICEDPLEUROTUSERYNGII.get());
// 切过的洋葱
                output.accept(DICEDONION.get());
// 四季豆段
                output.accept(DICEDSAUTEEDGREENBEANS.get());
// 土豆丝
                output.accept(SHREDDEDPOTATO_2.get());
// 土豆块
                output.accept(DICEDPOTATO.get());
// 土豆条
                output.accept(CHOPPEDPOTATO_1.get());
// 土豆片
                output.accept(SLICEDPOTATO.get());
// 山药段
                output.accept(DICEDCHINESEYAM.get());
// 木薯段
                output.accept(DICEDCASSAVA.get());
// 柠檬片
                output.accept(SLICEDLEMON.get());
// 生鱼片
                output.accept(DICEDFISH.get());
// 紫薯段
                output.accept(DICEDPURPLESWEETPOTATO.get());
// 胡萝卜丝
                output.accept(SHREDDEDCARROTS.get());
// 切块的胡萝卜
                output.accept(DICEDCARROTS.get());
// 芹菜段
                output.accept(DICEDCELERY.get());
// 茄子条
                output.accept(SHREDDERABUERGINE.get());
// 茄子片
                output.accept(SLICEDABUERGINE.get());
// 萝卜丝
                output.accept(SHREDDERRADISH.get());
// 萝卜块
                output.accept(DICEDRADISH.get());
// 萝卜段
                output.accept(DIECEDRADISH.get());
// 萝卜片
                output.accept(SLICEDRADISH.get());
// 葱段
                output.accept(SCALLION_2.get());
// 藕段
                output.accept(DICEDLOTUSROOT.get());
// 藕片
                output.accept(SLICEDLOTUSROOT.get());
// 西葫芦片
                output.accept(DICEDZUCCHINI.get());
// 豇豆段
                output.accept(CHOPPEDCOWPEA.get());
// 青椒圈
                output.accept(DICEDSWEETPEPPER.get());
// 西蓝花块
                output.accept(DICEDBROCCOIL.get());
// 菜花块
                output.accept(DICEDCAULIFLOWER.get());
// 黄瓜丝
                output.accept(SHREDDERCUCUMBER.get());
// 黄瓜滚刀块
                output.accept(DICEDCUCUMBER.get());
// 黄瓜片
                output.accept(SLICEDCUCUMBER.get());
// 白菜
                output.accept(CHINESE_LEAVES.get());
// 皮蛋块
                output.accept(DIECEDPRESERVEDEGG.get());
// 皮蛋丁
                output.accept(CUTDIECEDPRESERVEEGG.get());
// 红薯块
                output.accept(DICEDSWEETPOTATO.get());
// 紫薯块
                output.accept(PURPLEDICEDSWEETPOTATO.get());
// 处理后的卷心菜
                output.accept(PROCESSEDCABBAGE.get());
// 切块西红柿
                output.accept(DICEDTOMATO.get());
// 切好的卷心菜
                output.accept(CUTCABBAGE.get());
                output.accept(CASSAVACHUNKS.get());
// 西葫芦切片
                output.accept(SLICEDZUCCHINI.get());
// 胡萝卜块
                output.accept(CARROTCHUNKS.get());
// 芹菜叶
                output.accept(CELERYLEAF.get());
// 半成品
// 刷了脆皮水的五花肉
                output.accept(CRISPYPORKBELLY.get());
// 填馅青椒
                output.accept(RAW_STUFFEDGREENPEPPER.get());
// 奶油玉米粒
                output.accept(CREAMCORNKERNELS.get());
// 巧克力玉米粒
                output.accept(CHOCOLATECORNKERNELS.get());
// 挂上面糊的蘑菇
                output.accept(MUSHROOMSWRAPPEDINBATTER.get());
// 挂糊裹粉的鸡大腿
                output.accept(RAWCHICKENHALFLEGWITHBATTER.get());
// 挂糊裹粉的鸡翅
                output.accept(RAWCHICKENWINGWITHBATTER.get());
// 挂糊裹粉的鸡胸肉
                output.accept(RAWCHICKENMEATWITHBATTER.get());
// 挂糊裹粉的鸡腿
                output.accept(RAWCHICKENLEGWITHLEG.get());
// 灌好的肠
                output.accept(RAWSAUSAGE.get());
// 焦糖玉米粒
                output.accept(CARAMELCORNKENNELS.get());
// 生春卷
                output.accept(RAWSPRINGROLL.get());
// 生汤圆
                output.accept(RAW_TANGYUAN.get());
// 生粽子
                output.accept(RAWAZONGZI.get());
// 生红豆蛋挞
                output.accept(SWEETREDBEANEGGTART.get());
// 生肉松鸡蛋面剂子
                output.accept(RAWFLOURPASTEWITHDRIEDMEATFLOSS.get());
// 生藕合
                output.accept(RAWCOUPLING.get());
// 生蛋挞
                output.accept(RAWEGGTART.get());
// 生辣条
                output.accept(RAWSPICYGLUTEN.get());
// 生铜锣烧
                output.accept(RAWDORAYAKI.get());
// 生青色面团
                output.accept(RAWAIKUI.get());
// 盐腌鸡蛋
                output.accept(RAWSALTYEGG.get());
// 裹上面糊的鸡肉丁
                output.accept(RAWDICEDCHICKENWITHBATTER.get());
// 裹了蛋液的生肉丸
                output.accept(RAWMEATBALLWITHEGGBALL.get());
// 裹了蛋液的肉丸
                output.accept(EGGCOATEDMEATBALLS.get());
// 裹上面糊的鸡胸块
                output.accept(BATTEREDCHICKENBREASTCHUNKS.get());
// 生肉松面剂子
                output.accept(RAWMEATFLOSSDOUGH.get());
// 挂糊的蘑菇
                output.accept(BATTEREDMUSHROOMS.get());
// 裹起来的牛奶条
                output.accept(RAWWRAPPEDMILK.get());
// 生包子
                output.accept(RAW_BAOZI.get());
// 生饺子
                output.accept(RAW_DUMPLING.get());
// 生馄饨
                output.accept(RAW_WONTON.get());
// 冰沙
                output.accept(SORBET.get());
// 冷冻牛奶
                output.accept(FROZENMILK.get());
// 食用小苏打
                output.accept(NAHCO_3.get());
// 木薯珍珠
                output.accept(CASSAVAPEARL.get());
// 泡发的黄豆
                output.accept(SOAKEDSOYBEANS.get());
// 生酱油肉丝
                output.accept(RAWSOYSHREDDEDMEAT.get());
// 益生菌落
                output.accept(PROBIOTICS.get());
// 石膏粉
                output.accept(LANDPLASTER.get());
// 砂砾膏
                output.accept(GRAVELPASTE.get());
// 碎羊杂
                output.accept(RAWSHEEPOFFAL.get());
// 肠衣
                output.accept(CASING.get());
// 麸
                output.accept(BRAN.get());
// 大豆蛋白
                output.accept(SOYBEANPROTEIN.get());
// 生猪杂
                output.accept(RAW_PIGOFFAL.get());
// 脱骨的鸡爪
                output.accept(DEBONEDCHICKENFEET.get());
// 砂砾裹起来的蛋
                output.accept(EGGWRAPPEDINGRAVEL.get());
// 豆芽
                output.accept(BEANSPROUT.get());
// 蛋壳
                output.accept(EGGSHELL.get());

// 肉卷
                output.accept(MEATFLOURROOL.get());
// 火锅肉卷
                output.accept(NORMALMEATROLL.get());
// 羔羊嫩肉卷
                output.accept(LAMBROLL.get());
// 雪花肥牛卷
                output.accept(FATBEEFROLL.get());
// 番茄火锅底料
                output.accept(TOMATO_HOT_POT_BASE.get());
// 藤椒火锅底料
                output.accept(PEPPER_HOT_POT_BASE.get());
// 麻辣火锅底料
                output.accept(SPICY_HOT_POT_BASE.get());
// 建筑装饰
// 建筑木料
// 灿阳木木板
                output.accept(SOLARWOOD_PLANKS.asItem());
// 灿阳木楼梯
                output.accept(SOLARWOOD_STAIRS.asItem());
// 灿阳木台阶
                output.accept(SOLARWOOD_SLAB.asItem());
// 灿阳木栅栏
                output.accept(SOLARWOOD_FENCE.asItem());
// 灿阳木栅栏门
                output.accept(SOLARWOOD_FENCE_GATE.asItem());
// 灿阳木按钮
                output.accept(SOLARWOOD_BUTTON.asItem());
// 灿阳木压力板
                output.accept(SOLARWOOD_PRESSURE_PLATE.asItem());
// 蜜缘木木板
                output.accept(ORCHARD_HEARTWOOD_PLANKS.asItem());
// 蜜缘木楼梯
                output.accept(ORCHARD_HEARTWOOD_STAIRS.asItem());
// 蜜缘木台阶
                output.accept(ORCHARD_HEARTWOOD_SLAB.asItem());
// 蜜缘木栅栏
                output.accept(ORCHARD_HEARTWOOD_FENCE.asItem());
// 蜜缘木栅栏门
                output.accept(ORCHARD_HEARTWOOD_FENCE_GATE.asItem());
// 蜜缘木按钮
                output.accept(ORCHARD_HEARTWOOD_BUTTON.asItem());
// 蜜缘木压力板
                output.accept(ORCHARD_HEARTWOOD_PRESSURE_PLATE.asItem());
// 核毅木木板
                output.accept(STONEBARK_PLANKS.asItem());
// 核毅木楼梯
                output.accept(STONEBARK_STAIRS.asItem());
// 核毅木台阶
                output.accept(STONEBARK_SLAB.asItem());
// 核毅木栅栏
                output.accept(STONEBARK_FENCE.asItem());
// 核毅木栅栏门
                output.accept(STONEBARK_FENCE_GATE.asItem());
// 核毅木按钮
                output.accept(STONEBARK_BUTTON.asItem());
// 核毅木压力板
                output.accept(STONEBARK_PRESSURE_PLATE.asItem());
// 虬藤木木板
                output.accept(VINEHEART_TIMBER_PLANKS.asItem());
// 虬藤木楼梯
                output.accept(VINEHEART_TIMBER_STAIRS.asItem());
// 虬藤木台阶
                output.accept(VINEHEART_TIMBER_SLAB.asItem());
// 虬藤木栅栏
                output.accept(VINEHEART_TIMBER_FENCE.asItem());
// 虬藤木栅栏门
                output.accept(VINEHEART_TIMBER_FENCE_GATE.asItem());
// 虬藤木按钮
                output.accept(VINEHEART_TIMBER_BUTTON.asItem());
// 虬藤木压力板
                output.accept(VINEHEART_TIMBER_PRESSURE_PLATE.asItem());
// 清韵木木板
                output.accept(VERDANT_GRACE_PLANKS.asItem());
// 清韵木楼梯
                output.accept(VERDANT_GRACE_STAIRS.asItem());
// 清韵木台阶
                output.accept(VERDANT_GRACE_SLAB.asItem());
// 清韵木栅栏
                output.accept(VERDANT_GRACE_FENCE.asItem());
// 清韵木栅栏门
                output.accept(VERDANT_GRACE_FENCE_GATE.asItem());
// 清韵木按钮
                output.accept(VERDANT_GRACE_BUTTON.asItem());
// 清韵木压力板
                output.accept(VERDANT_GRACE_PRESSURE_PLATE.asItem());
// 灿阳木马扎
                output.accept(SOLARWOODCHAIR.asItem());
// 蜜缘木椅子
                output.accept(ORCHARDCHAIR.asItem());
// 核毅木凳子
                output.accept(STONEBARKCHAIR.asItem());
// 虬藤木椅子
                output.accept(VINEHEARTCHAIR.asItem());
// 清韵木椅子
                output.accept(VERDANTGRACECHAIR.asItem());
// 无限砍伐（附魔书，3级）
                output.accept(EnchantedBookItem.createForEnchantment(
                        new EnchantmentInstance(ModEnchantments.INFINITE_LOGGING.get(), 3)));
// 吴刚之斧
                output.accept(WugangAxeItem.makeEnchanted(WUGANG_AXE.get()));
// 青玉灯笼
                output.accept(JADE_LANTERN.asItem());
// 灯谜牌
                output.accept(RADDLES.asItem());
// 仙女棒
                                output.accept(FAIRY_SPARKLER.get());
// 箱装烟花
                output.accept(COLORFUL_FIREWORKS_BOX.asItem());
// 摔炮
                output.accept(WRESTLING_GUN.get());
// 左门纸
                output.accept(LEFT_DOOR_PAPER.asItem());
// 右门纸
                output.accept(RIGHT_DOOR_PAPER.asItem());
// 中国结
                output.accept(CHINESE_KNOTTING.asItem());
// 格栅灯
                output.accept(LAMP_CABINET.asItem());
// 屏风
                output.accept(CANVAS_SCREEN_1.asItem());
// 屏风
                output.accept(CANVAS_SCREEN_2.asItem());
// 香炉
                output.accept(INCENSE_BURNER.asItem());
// 木板挂灯
                output.accept(PLANK_HANGING_LIGHT.asItem());
// 红灯笼
                output.accept(REDLANTERN.asItem());
// 金灯笼
                output.accept(GOLDLANTERN.asItem());
// 石狮子
                output.accept(STONE_LION.asItem());
// 窗纸
                output.accept(WINDOW_PAPER_ITEM.get());
// 横幅
                output.accept(ANTITHETICAL_COUPLET_1.asItem());
// 竖联
                output.accept(ANTITHETICAL_COUPLET_2.asItem());
            }).build());
}
