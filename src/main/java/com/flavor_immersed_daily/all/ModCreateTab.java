package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;

import static com.flavor_immersed_daily.FlavorImmersedDaily.*;
import static com.flavor_immersed_daily.all.ModItems.*;
import static com.flavor_immersed_daily.all.ModBlocks.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreateTab {
    private ModCreateTab() {
    }

    public static final DeferredRegister<CreativeModeTab> REGISTRY =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FlavorImmersedDaily.MODID);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

// ========== ����ģʽ��ǩҳ ==========
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AGRICULTURE_TAB = REGISTRY.register("agriculture", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.agriculture"))
            .withTabsBefore(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "food"))
            .icon(() -> GREENAPPLE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // ===== ��ʵ���� =====
                output.accept(TEMPERATEWILDFRUIT.get());
                output.accept(TROPICALWILD_FRUIT.get());
                output.accept(WILDFLOWERANDLEAF.get());
                output.accept(WILDFRUITINCOLDZONE.get());
                output.accept(WILDGRAINPLANT.get());
                output.accept(WILDMUSHROOMPLANT.get());
                output.accept(WILDSEEDPLANT.get());
                output.accept(WILDTUBERPLANTS.get());
                output.accept(APIECEOFBANANA.get()); output.accept(BANANA.get()); output.accept(PULLEDBANANA_2.get());
                output.accept(ORANGE.get()); output.accept(BLOODORANGE.get()); output.accept(TANGERINE.get()); output.accept(TANGERINE_1.get()); output.accept(UGLYORANGE.get()); output.accept(LEMON.get());
                output.accept(HAMIMELON.get()); output.accept(SWEETMELON.get()); output.accept(SWEETMELON_1.get());
                output.accept(COCONUT.get()); output.accept(COCONUTMEAT.get()); output.accept(COCONUT_SHELL.get());
                output.accept(DURIAN.get()); output.accept(DURIANMEAT.get()); output.accept(DURIANSHELLHAT.get());
                output.accept(BLUEBERRY.get()); output.accept(GRAPE.get()); output.accept(GREENGRAPE.get()); output.accept(MULBERRY.get()); output.accept(STRAWBERRY.get());
                output.accept(APRICOT.get()); output.accept(CHERRY.get()); output.accept(GREENPLUM.get()); output.accept(HONEYPEACH.get()); output.accept(LIFEPEACH.get()); output.accept(NECTARINE.get()); output.accept(PLUM.get()); output.accept(WINTERJUJUBE.get());
                output.accept(CARAMBOLA.get()); output.accept(DRAGONFRUIT.get()); output.accept(KIWIFRUIT.get()); output.accept(LYCHEE.get()); output.accept(MANGO.get()); output.accept(MANGOSTEEN.get()); output.accept(PAWPAW.get()); output.accept(PINEAPPLE.get());
                output.accept(GREENAPPLE.get()); output.accept(HAWTHORN.get()); output.accept(LOQUAT.get()); output.accept(PEAR.get()); output.accept(POMEGRANATE.get());
                output.accept(PISTACHIONUT.get()); output.accept(REDDATE.get()); output.accept(WALNUT.get());
                output.accept(DRY_PISTACHIONUT.get()); output.accept(PISTACHIONUT_0.get()); output.accept(PISTACHIONUTSWITHOPENSHELLS.get()); output.accept(WALNUTKINNEL.get());
                output.accept(CHINESE_LEAVES.get()); // ��Ӱײ�
                output.accept(CHINESE_LEAVES_SEEDS.get()); // ��Ӱײ�����
                output.accept(RADISH.get()); // ����ܲ�
                output.accept(RADISHSEED.get()); // ����ܲ�����
                output.accept(WHITEMUSHROOM.get()); // ��Ӱ�Ģ??
                output.accept(WHITE_MUSHROOM_SEED.get()); // ��Ӱ�Ģ����??
                output.accept(BLACKFUNGUS.get());
                output.accept(BLACKFUNGSSEED.get()); // ���ľ����??
                output.accept(DRYBLACKFUNGUS.get()); // ��Ӹ�ľ??
                output.accept(PLEUROTUSERYNGII.get()); // ����ӱ�??
                output.accept(PLEUROTUSSEED.get()); // ����ӱ�����??
                output.accept(ENOKIMUSHROOM.get()); // ��ӽ���??
                output.accept(ENOKIMUSHROOMSEED.get()); // ��ӽ��빽��??
                output.accept(TREMELLA.get());
                output.accept(TREMELLASEED.get()); // ���������??
                output.accept(FRAGRANTMUSHROOM.get()); // ����㹽
                output.accept(FRAGRANTSEED.get()); // ����㹽����
                output.accept(DRYFRAGRANTMUSHROOM.get()); // ��Ӹ���??
                output.accept(BLUEBERRYSEED.get()); // �����ݮ����
                output.accept(DRAGONFRUITSEED.get()); // ��ӻ�������??
                output.accept(GREENTEALEAVESSEED.get()); // ����̲�����
                output.accept(GREENTEALEAVES.get()); // ����̲�??
                output.accept(DRYGREENTEA.get()); // ��Ӹ���??
                output.accept(HAMIMELONSEED.get()); // ��ӹ��ܹ���??
                output.accept(PINEAPPLESEED.get()); // ��Ӳ�������
                output.accept(RED_TEA_SEED.get()); // ��Ӻ������
                output.accept(REDTEALEAVES.get()); // ��Ӻ��??
                output.accept(DRYREDTEA.get()); // ��Ӹɺ�??
                output.accept(STRAWBERRYSEED.get()); // ��Ӳ�ݮ����
                output.accept(LOTUSROOTSEED.get()); // �����ź����
                output.accept(LOTUSROOT.get()); // �����ź
                output.accept(GLUTINOUSSEEDS.get()); // ���Ŵ������
                // ===== Ұ���ɼ�??=====
                output.accept(POLISHEDGLUTINOUSRICE_2.get()); // ��Ӿ���Ŵ��
                output.accept(PADDYSEEDS.get()); // ���ˮ������
                output.accept(PADDYGRAIN.get()); // ��ӵ���
                output.accept(ANISEED_0.get()); // ��Ӱ˽�
                output.accept(KAOLIANGGRAIN.get()); // ��Ӹ���
                output.accept(KAOLIANG_SEED.get()); // ��Ӹ�������
                output.accept(BROCCOIL.get()); // �������??
                output.accept(BROCCOILSEED.get()); // �����������??
                output.accept(BUCKWHEAT.get()); // �������
                output.accept(BUCKWHEATSEED.get()); // �����������
                output.accept(CABBAGE.get()); // ��Ӿ���??
                output.accept(CABBAGESEED.get()); // ��Ӿ��Ĳ���??
                output.accept(CASSAVA.get()); // ���ľ��
                output.accept(CASSAVASEEDS.get()); // ���ľ������
                output.accept(CELERY.get()); // ����۲�
                output.accept(CELERYSEED.get()); // ����۲�����
                output.accept(CHINESECHIVES.get()); // ��Ӿ²�
                output.accept(CHINESECHIVESSEED.get()); // ��Ӿ²�����
                output.accept(CHINESEYAM.get()); // ���ɽҩ
                output.accept(CHINESEYAMSEED.get()); // ���ɽҩ����
                output.accept(COFFEEBEANSEED.get()); // ��ӿ��ȶ���??
                output.accept(CORN.get()); // �������
                output.accept(CORNSEED.get()); // �����������
                output.accept(CUMIN.get()); // �����Ȼ
                output.accept(CUMINSEED.get()); // �����Ȼ����
                output.accept(FENNEL.get()); // �������
                output.accept(FENNELSEEDSTATES.get()); // �����������
                output.accept(GARLIC.get()); // ��Ӵ���
                output.accept(GARLICSEED.get()); // ��Ӵ�������
                output.accept(GINGER.get()); // �������
                output.accept(GINGER_SEED.get()); // �����������
                output.accept(GREENPEPPER.get()); // ������??
                output.accept(GREENPEPPERSEEDS.get()); // �����⽷��??
                output.accept(GUMBO.get()); // ������
                output.accept(GUMBOSEED.get()); // ����������
                output.accept(MILLETGRAIN_GRAIN.get()); // ���С��
                output.accept(MILLET.get()); // ���С������
                output.accept(MUNGBEAN.get()); // ����̶�
                output.accept(MUNGBEANPLANT.get()); // ����̶�����
                output.accept(MUSTARD.get()); // ��ӽ�ĩ
                output.accept(MUSTRAD_SEED.get()); // ��ӽ�ĩ����
                output.accept(NUTMEGSEED.get()); // ����ⶹޢ��??
                output.accept(OAT.get()); // �������
                output.accept(OATSEED.get()); // �����������
                output.accept(OILSEEDRAPE.get()); // ����Ͳ�
                output.accept(OILRAPESEED.get()); // ����Ͳ�����
                output.accept(ONION.get()); // ������
                output.accept(ONIONSEED.get()); // ����������
                output.accept(PEA.get()); // ����㶹
                output.accept(PEASEED.get()); // ����㶹����
                output.accept(PURPLESWEETPOTATO.get()); // �������
                output.accept(PUPLESWEETPOTATOSEED.get()); // �����������
                output.accept(SESAME.get()); // ���֥��
                output.accept(SESAMESEED.get()); // ���֥������
                output.accept(SOYBEAN.get()); // ��ӻƶ�
                output.accept(SOY_BEAN_SEED.get()); // ��ӻƶ�����
                output.accept(SWEETGREENPEPPER.get()); // ����ཷ
                output.accept(SWEETGREENPEPPERSEED.get()); // ����ཷ����
                output.accept(ZUCCHINI.get()); // �������??
                output.accept(ZUCCHINISEED.get()); // �������«��??
                output.accept(SPINACH_SEED.get()); // ��Ӳ�������
                output.accept(CAULIFLOWER_SEED.get()); // ��Ӳ˻�����
                output.accept(SCALLION_SEED.get()); // ��Ӵ���??
                output.accept(LILAC_SEED.get()); // ��Ӷ��㣨�������Ӽ�����??
                output.accept(RED_BEAN_BLOCK.get()); // ��Ӻ춹���춹���Ӽ�����??
                output.accept(RED_PEPPER_SEED.get()); // ��Ӻ�⽷��??
                output.accept(SWEET_POTATO_SEED.get()); // ��Ӻ�������
                output.accept(SI_CHUAN_PEPPER_SEED.get()); // ��ӻ������������Ӽ�����??
                output.accept(PEA_NUT_SEED.get()); // ��ӻ�������
                // ===== ��������ԭ??=====
                output.accept(SOLARWOOD_LOG.asItem());
                output.accept(ORCHARD_HEARTWOOD_LOG.asItem());
                output.accept(STONEBARK_LOG.asItem());
                output.accept(VINEHEART_TIMBER_LOG.asItem());
                output.accept(VERDANT_GRACE_LOG.asItem());
                // ===== ������??=====
                output.accept(SOLARWOOD_LEAVES.asItem());
                output.accept(ORCHARD_HEARTWOOD_LEAVES.asItem());
                output.accept(STONEBARK_LEAVES.asItem());
                output.accept(VINEHEART_TIMBER_LEAVES.asItem());
                output.accept(VERDANT_GRACE_LEAVES.asItem());
                // ===== ���й�ʵ��??=====
                output.accept(PLUM_SAPLING.asItem());
                output.accept(APRICOT_SAPLING.asItem());
                output.accept(CHERRY_SAPLING.asItem());
                output.accept(GREENPLUM_SAPLING.asItem());
                output.accept(HAWTHORN_SAPLING.asItem());
                output.accept(LOQUAT_SAPLING.asItem());
                output.accept(POMEGRANATE_SAPLING.asItem());
                output.accept(CARAMBOLA_SAPLING.asItem());
                output.accept(DURIAN_SAPLING.asItem());
                output.accept(LEMON_SAPLING.asItem());
                output.accept(LYCHEE_SAPLING.asItem());
                output.accept(MANGO_SAPLING.asItem());
                output.accept(PAWPAW_SAPLING.asItem());
                output.accept(TANGERINE_SAPLING.asItem());
                output.accept(ORANGE_SAPLING.asItem());
                output.accept(APPLE_SAPLING.asItem());
                output.accept(HONEYPEACH_SAPLING.asItem());
                output.accept(NECTARINE_SAPLING.asItem());
                output.accept(PEAR_SAPLING.asItem());
                output.accept(SWEETMELON_SAPLING.asItem());
                output.accept(PISTACHIONUT_SAPLING.asItem());
                output.accept(REDDATE_SAPLING.asItem());
                output.accept(WALNUT_SAPLING.asItem());
                output.accept(WINTERJUJUBE_SAPLING.asItem());
                output.accept(KIWIFRUIT_SAPLING.asItem());
                output.accept(MANGOSTEEN_SAPLING.asItem());
                output.accept(MULBERRY_SAPLING.asItem());
                // ===== Ҭ��??=====
                output.accept(COCONUT_SAPLING.asItem());
                // ===== �㽶??=====
                output.accept(BANANA_SAPLING.asItem());
                output.accept(BANANAWOOD.asItem());
                output.accept(RAWBANANA.asItem());
                // ===== ��Ƥ??=====
                output.accept(CINNAMON.get());
                output.accept(CINNAMON_SAPLING.asItem());
                output.accept(CINNAMONWOOD.asItem());
                // ===== ���й�ʵ�����??=====
                output.accept(VERDANT_GRACE_FRUITING_LEAVES.asItem());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_APRICOT.asItem());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_CHERRY.asItem());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_GREENPLUM.asItem());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_HAWTHORN.asItem());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_LOQUAT.asItem());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_POMEGRANATE.asItem());
                output.accept(CARAMBOLEAVE_FRUITING_LEAVES.asItem());
                output.accept(DURIANLEAVE_FRUITING_LEAVES.asItem());
                output.accept(LEMONLEAVE_FRUITING_LEAVES.asItem());
                output.accept(LYCHEELEAVE_FRUITING_LEAVES.asItem());
                output.accept(MANGOLEAVE_FRUITING_LEAVES.asItem());
                output.accept(PAWPAWLEAVE_FRUITING_LEAVES.asItem());
                output.accept(TANGERINELEAVE_FRUITING_LEAVES.asItem());
                output.accept(ORANGELEAVE_FRUITING_LEAVES.asItem());
                output.accept(APPLELEAVE_FRUITING_LEAVES.asItem());
                output.accept(HONEYPEACHLEAVE_FRUITING_LEAVES.asItem());
                output.accept(NECTARINELEAVE_FRUITING_LEAVES.asItem());
                output.accept(PEARLEAVE_FRUITING_LEAVES.asItem());
                output.accept(SWEETMELONLEAVE_FRUITING_LEAVES.asItem());
                output.accept(PISTACHIONUTLEAVE_FRUITING_LEAVES.asItem());
                output.accept(REDDATELEAVE_FRUITING_LEAVES.asItem());
                output.accept(WALNUTLEAVE_FRUITING_LEAVES.asItem());
                output.accept(WINTERJUJUBELEAVE_FRUITING_LEAVES.asItem());
                output.accept(KIWIFRUITSSLEAVE_FRUITING_LEAVES.asItem());
                output.accept(MANGOSTEENLEAVE_FRUITING_LEAVES.asItem());
                output.accept(MULBERRYLEAVE_FRUITING_LEAVES.asItem());
                // ===== ���� =====
                output.accept(TRELLIS.asItem());
                output.accept(GRAPESEED.get());
                // ===== �������� =====
                output.accept(CUCUMBERSEEDS.get()); output.accept(CUCUMBER.get());
                output.accept(WAX_GOURD_SEED_BLOCK.get()); output.accept(WAXGOURD.get());
                output.accept(KIDNEYBEANSEED.get()); output.accept(KIDNEYBEAN.get());
                output.accept(AUBERGINESEEDBLOCK.get()); output.accept(AUBERGINE.get());
                output.accept(TOMATOSEED.get()); output.accept(TOMATO.get());
                output.accept(COWPEABEANSEED.get()); output.accept(COWPEA.get());
                output.accept(GREENGRAESEED.get()); output.accept(GREENGRAPE.get());
                output.accept(LOOFAHSEED.get()); output.accept(LOOFAH.get());
                // ===== ����??=====
                output.accept(DEADCATTLE.get());
                output.accept(DEADSHEEP.get());
                output.accept(DEADPIG.get());
                output.accept(DEADCHICKEN.get());
                output.accept(RAWCATTLEBLOOD.get()); output.accept(RAWCATTLEFACE.get()); output.accept(RAWCATTLEJOINT.get()); output.accept(RAWCATTLESTOMACH.get());
                output.accept(RAWCATTLELUNG.get()); output.accept(RAWCATTLELIVER.get()); output.accept(RAWCATTLEINTESTINE.get()); output.accept(RAWCATTLEHEART.get());
                output.accept(RAWCATTLEFAT.get()); output.accept(RAWDICEDCATTLEMEAT.get()); output.accept(RAWSNOWFLAKEBEEF.get()); output.accept(RAWCATTLELEG.get());
                output.accept(RAWCATTLEFEET.get()); output.accept(RAWCATTLETENDON.get()); output.accept(BULLHORN.get()); output.accept(BOVINEBONE.get());
                output.accept(ANIMALSKULL.get());
                output.accept(RAWPIGBLOOD.get()); output.accept(RAWDICEDPIGMEAT.get()); output.accept(RAWDICEDPIGMEAT_2.get()); output.accept(RAWPIGHEAD.get());
                output.accept(RAWPIGEAR.get()); output.accept(RAWPIGNOSE.get()); output.accept(RAWPIGTAIL.get()); output.accept(RAWPIGSKIN.get());
                output.accept(RAWPIGSTREAKYPORK.get()); output.accept(RAWPIGTENDERLOIN.get()); output.accept(RAWPIGSPARERIB.get()); output.accept(RAWPIGLEG.get());
                output.accept(RAWPIGFEET.get()); output.accept(RAWPIGFAT.get()); output.accept(RAWPIGSTOMACH.get()); output.accept(RAWPIGLUNG.get());
                output.accept(RAWPIGLIVER.get()); output.accept(RAWPIGKIDNEY.get()); output.accept(RAWPIGHEART.get()); output.accept(RAWPIGINTESTINE.get());
                output.accept(RAWPIGCEREBRUM.get());
                output.accept(RAWSHEEPBLOOD.get()); output.accept(RAWDICEDSHEEPMEAT.get()); output.accept(RAWSHEEPFACE.get()); output.accept(RAWSHEEPTAILFAT.get());
                output.accept(RAWSHEEPFAT.get()); output.accept(RAWSHEEPEYE.get()); output.accept(RAWSHEEPSTOMACH.get()); output.accept(RAWSHEEPLIVER.get());
                output.accept(RAWSHEEPINTESTINE.get()); output.accept(RAWSHEEPKIDNEY.get()); output.accept(RAWSHEEPHEART.get()); output.accept(RAWSHEEPSPARERIB.get());
                output.accept(RAWSHEEPSPINE.get()); output.accept(RAWSHEEPFEET.get()); output.accept(RAWSHEEPLEG.get()); output.accept(SHEEPBREAD.get());
                output.accept(RAWCHICKENBLOOD.get()); output.accept(RAWCHICKENHEAD.get()); output.accept(RAWCHICKENNECK.get()); output.accept(RAWCHICKENPIECEPIECE.get());
                output.accept(RAWCHICKENBREAST.get()); output.accept(RAWCHICKENPIECE.get()); output.accept(RAWCHICKENWING.get()); output.accept(RAWCHICKENWINGTIP.get());
                output.accept(RAWCHICKENFEET.get()); output.accept(RAWCHICKENLEGWITHLEG.get()); output.accept(RAWCHICKENLEG.get()); output.accept(RAWCHICKENLEAN.get());
                output.accept(RAWCHICKENFORK.get()); output.accept(RAWCHICKENASS.get()); output.accept(RAWCHICKENHEART.get()); output.accept(RAWCHICKENLIVER.get());
                output.accept(RAWCHICKENGIZZARD.get()); output.accept(CHICKENWITHOUTFEATHER.get()); output.accept(CHICKENWITHOUTBLOOD.get());
                output.accept(GREENMANGO.get());
                output.accept(AGRAPE.get()); output.accept(AGREENGRAPE.get()); output.accept(CAULIFLOWER.get()); output.accept(GARLICPEDICEL.get());
                output.accept(PEANUT.get()); output.accept(POMEGRANATE_SEED.get()); output.accept(RAWHALFOFCHICKENLEG.get()); output.accept(RAWSHEEPTAIL.get());
                output.accept(REDREPPER.get()); output.accept(SACLLION.get()); output.accept(SHEEPMELON.get()); output.accept(SPINACH.get());
                output.accept(SWEETPOTATO.get()); output.accept(YELLOWPEACH.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FOOD_TAB = REGISTRY.register("food", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.food"))
            .withTabsBefore(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "ingredient"))
            .icon(() -> STIRFRIEDBOILEDPORKSLICESINHOTSAUCE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // ===== ���ȣ���ǰ��??=====
                output.accept(DRAWNEGGPLANT.get());
                // �ɷ��ò�??
                output.accept(LINYIFRIEDCHICKEN.get()); output.accept(MEATBALLSOUP.get()); output.accept(PRESERVEDEGGSALAD.get()); output.accept(TOMATOSALAD.get());
                output.accept(STEAMED_CHICKENWITH_CHILI_SAUCE.get()); output.accept(COLACHICKENWINGS.get()); output.accept(FOURJOYMEATBALLS.get()); output.accept(STIRFRIEDBOILEDPORKSLICESINHOTSAUCE.get());
                output.accept(SAUTEED_POTATO_GREEN_PEPPER_EGGPLANT.get()); output.accept(FRIEDMEATWITHCUMINONION.get()); output.accept(KUNGPAOCHICKEN.get()); output.accept(STIRFRIEDSTRINGBEANS.get());
                output.accept(MIXEDCOLDDISHES.get()); output.accept(JAPANESEBRAISEDTOFU.get()); output.accept(SCRAMBLEDEGGSWITHFUNGUSANDCUCUMBER.get()); output.accept(PLEUROTUSERYNGIIWITHSALTANDPEPPER.get());
                output.accept(POACHED_SPICY_SLICESOF_PORK.get()); output.accept(SLICED_FISHIN_HOT_CHILI_OIL.get()); output.accept(SAUTEEDMUSHROOMSWITHRAPESEED.get()); output.accept(STEAMEDFISH.get());
                output.accept(FRIEDCOWPEA.get()); output.accept(FRIEDSPICYCHICKEN.get()); output.accept(BOILED_CHICKENWITH_SAUCE.get()); output.accept(STEWEDPORKWITHBROWNSAUCE.get());
                output.accept(FRIEDLIVERTIPWITHSPINACH.get()); output.accept(PINEAPPLE_SWEETAND_SOUR_PORK.get()); output.accept(CHICKENWITH_SCALLION_OIL.get()); output.accept(SALTEDEGGYOLKFRIEDCAULIFLOWER.get());
                output.accept(ZUCCHINISNACKMEAT.get()); output.accept(BOILED_FISHWITH_PICKLED_CABBAGEAND_CHILI.get()); output.accept(FRIEDSHREDDEDPORKWITHSWEETANDSOURSAUCE.get()); output.accept(SPICYTOFU.get());
                output.accept(BEANWITHSESAMESAUCE.get()); output.accept(SPICYCABBAGE.get());
                // �򵥲�??
                output.accept(BAKEDWHITEMUSHROOMSWITHCREAM.get()); output.accept(BRAISEDBEANSPROUTSWITHVERMICELLI.get()); output.accept(CANTONESERICEROLLS.get()); output.accept(CONGEEWITH_MINCED_PORKAND_PRESERVED_EGG.get());
                output.accept(CURRYSTEWEDCHICKEN.get()); output.accept(GUMBOSOUP.get()); output.accept(REDDATESANDTREMELLAPORRIDGE.get()); output.accept(SA.get());
                output.accept(SCRAMBLEDEGGWITHTOMATO.get()); output.accept(SHEEPGIBLETSSOUP.get()); output.accept(STEAMEDEGGCUSTARD.get()); output.accept(STEWEDCHICKENSOUPWITHMUSHROOMS.get());
                output.accept(STEWEDCHICKENWITHWAXGOURD.get()); output.accept(STEWEDEGGSWITHLOOFAH.get()); output.accept(STEWEDPORKOFFAL.get()); output.accept(WINTERJUJUBEANDWAXGOURDSOUP.get());
                // ===== ѩ�� =====
                output.accept(APPLEPOPSICLE.get());
                output.accept(APRICOTPOPSICLE.get());
                output.accept(BANANAPOPSICLE.get());
                output.accept(BLUEBERRYPOPSICLE.get());
                output.accept(CARAMBOLAPOPSICLE.get());
                output.accept(CHERRYPOPSICLE.get());
                output.accept(CHOCOLATEPOPSICLE.get());
                output.accept(COCONUTPOPSICLE.get());
                output.accept(COFFEEPOPSICLE.get());
                output.accept(DRAGONFRUITPOPSICLE.get());
                output.accept(DURIANPOPSICLE.get());
                output.accept(GRAPEPOPSICLE.get());
                output.accept(GREENGRAPEPOPSICLE.get());
                output.accept(GREENPLUMPOPSICLE.get());
                output.accept(HAMIMELONPOPSICLE.get());
                output.accept(HAWTHORNPOPSICLE.get());
                output.accept(HONEYPEACHPOPSICLE.get());
                output.accept(KIWIFRUITPOPSICLE.get());
                output.accept(LEMONPOPSICLE.get());
                output.accept(LOQUATPOPSICLE.get());
                output.accept(LYCHEEPOPSICLE.get());
                output.accept(MANGOPOPSICLE.get());
                output.accept(MANGOSTEEPOPSICLE.get());
                output.accept(MULBERRYPOPSICLE.get());
                output.accept(NECTARINEPOPSICLE.get());
                output.accept(ORANGEPOPSICLE.get());
                output.accept(PAWPAWPOPSICLE.get());
                output.accept(PEARPOPSICLE.get());
                output.accept(PINEAPPLEPOPSICLE.get());
                output.accept(PLUMPOPSICLE.get());
                output.accept(POMEGRANATEPOPSICLE.get());
                output.accept(POPSICLE.get());
                output.accept(STRAWBERRYPOPSICLE.get());
                output.accept(SWEETBERRYPOPSICLE.get());
                output.accept(SWEETMELONPOPSICLE.get());
                output.accept(TANGERINEPOPSICLE.get());
                output.accept(WATERMELONPOPSICLE.get());
                output.accept(WINTERJUJUBEPOPSICLE.get());
                // ���Ice Creamϵ��
                output.accept(APPLEICECREAM.get());
                output.accept(APRICOTICECREAM.get());
                output.accept(BANANAICECREAM.get());
                output.accept(BLUEBERRYICECREAM.get());
                output.accept(CARAMBOLAICECREAM.get());
                output.accept(CHERRYICECREAM.get());
                output.accept(CHOCOLATEICECREAM.get());
                output.accept(COCONUTICECREAM.get());
                output.accept(COFFEEICECREAM.get());
                output.accept(DRAGONFRUITICECREAM.get());
                output.accept(DURIANICECREAM.get());
                output.accept(GRAPEICECREAM.get());
                output.accept(GREENGRAPEICECREAM.get());
                output.accept(GREENPLUMICECREAM.get());
                output.accept(HAMIMELONICECREAM.get());
                output.accept(HAWTHORNICECREAM.get());
                output.accept(HONEYPEACHICECREAM.get());
                output.accept(KIWIFRUITICECREAM.get());
                output.accept(LEMONICECREAM.get());
                output.accept(LOQUATICECREAM.get());
                output.accept(LYCHEEICECREAM.get());
                output.accept(MANGOICECREAM.get());
                output.accept(MANGOSTEENICECREAM.get());
                output.accept(MILKICECREAM.get());
                output.accept(MULBERRYICECREAM.get());
                output.accept(NECTARINEICECREAM.get());
                output.accept(ORANGEICECREAM.get());
                output.accept(PAWPAWICECREAM.get());
                output.accept(PEARICECREAM.get());
                output.accept(PINEAPPLEICECREAM.get());
                output.accept(PLUMICECREAM.get());
                output.accept(POMEGRANATEICECREAM.get());
                output.accept(STRAWBERRYICECREAM.get());
                output.accept(SWEETBERRYICECREAM.get());
                output.accept(SWEETMELONICECREAM.get());
                output.accept(TANGERINEICECREAM.get());
                output.accept(WATERMELONICECREAM.get());
                output.accept(WINTERJUJUBEICECREAM.get());
                // ===== ��֭ =====
                output.accept(HAMIMELONJUICE.get());
                output.accept(HAWTHORNJUIVE.get());
                output.accept(DRAGONFRUIEJUICE.get());
                output.accept(CARAMBOLAJUICE.get());
                output.accept(WINTERJUJUBEJUICE.get());
                output.accept(POMEGRANATEJUICE.get());
                output.accept(SWEETBERRYJUICE.get());
                output.accept(DURIANJUICE.get());
                output.accept(LEMONJUICE.get());
                output.accept(BLUEBERRYJUICE.get());
                output.accept(SWEETMELONJUICE.get());
                output.accept(GREENGRAPEJUICE.get());
                output.accept(TANGERINEJUICE.get());
                output.accept(MANGOSTEENJUICE.get());
                output.accept(CHERRYJUICE.get());
                output.accept(STRAWBERRYJUICE.get());
                output.accept(ORANGEJUICE.get());
                output.accept(GREENPLUMJUICE.get());
                output.accept(APPLEJUICE.get());
                output.accept(HONEYPEACHJUIVE.get());
                output.accept(BANANAJUICE.get());
                output.accept(LYCHEEJUICE.get());
                output.accept(APRICOTJUICE.get());
                output.accept(GRAPEJUICE.get());
                output.accept(MULBERRYJUIVE.get());
                output.accept(PKUMJUICE.get());
                output.accept(MANGOJUICE.get());
                output.accept(WATERMELONJUICE.get());
                output.accept(NECTARINEJUICE.get());
                output.accept(PINEAPPLEJUICE.get());
                output.accept(LOQUATJUICE.get());
                output.accept(KIWIFRUITJUICE.get());
                output.accept(PEARJUICE.get());
                output.accept(PAWPAWJUICE.get());
                output.accept(COCONUTJUICE.get());
                // ===== ���� =====
                output.accept(PINEAPPLEJAM.get());
                output.accept(STRAWBERRYJAM.get());
                output.accept(ORANGEJAM.get());
                output.accept(WINTERJUJUBEJAM.get());
                output.accept(HAMIMELONJAM.get());
                output.accept(DRAGONFRUITJAM.get());
                output.accept(TANGERINEJAM.get());
                output.accept(BLUEBERRYJAM.get());
                output.accept(PEARJAM.get());
                output.accept(LYCHEEJAM.get());
                output.accept(PLUMJAM.get());
                output.accept(DURIANJAM.get());
                output.accept(MANGOJAM.get());
                output.accept(KIWIFRUITJAM.get());
                output.accept(PAWPAWJAM.get());
                output.accept(LEMONJAM.get());
                output.accept(LOQUATJAM.get());
                output.accept(APPLEJAM.get());
                output.accept(GRAPEJAM.get());
                output.accept(GREEMPLUMJAM.get());
                output.accept(GREENGRAPEJAM.get());
                output.accept(MULBERRYJAM.get());
                output.accept(HAWTHORNJAM.get());
                output.accept(MANGOSTEENJAM.get());
                output.accept(POMEGRANATEJAM.get());
                output.accept(HONEYPEACHJAM.get());
                output.accept(SWEETMELONJAM.get());
                output.accept(SWEETBERRYJAM.get());
                output.accept(WATERMELONJAM.get());
                output.accept(BANANAJAM.get());
                output.accept(APRICOTJAM.get());
                output.accept(CARAMBOLAJAM.get());
                output.accept(COCONUTJAM.get());
                output.accept(CHERRYJAM.get());
                output.accept(NECTARINEJAM.get());
                // ===== ����??=====
                output.accept(COOKEDCATTLEHEART.get()); output.accept(COOKEDCATTLEJOINT.get()); output.accept(COOKEDCATTLESTOMACH.get()); output.accept(COOKEDCATTLELUNG.get());
                output.accept(COOKEDCATTLELIVER.get()); output.accept(COOKEDCATTLEINTESTINE.get()); output.accept(COOKEDDICEDCATTLE.get()); output.accept(COOKEDCATTLEFACE.get());
                output.accept(COOKEDCATTLEMARBLEDBEEF.get()); output.accept(COOKEDCATTLELEG.get()); output.accept(COOKEDCATTLEFEET.get()); output.accept(COOKEDCATTLETENDON.get());
                output.accept(COOKEDDICEDPIGMEAT.get()); output.accept(COOKEDPIGSHREDS.get()); output.accept(COOKEDPIGEAR.get()); output.accept(COOKEDPIGNOSE.get());
                output.accept(COOKEDPIGTAIL.get()); output.accept(COOKEDPIGSKIN.get()); output.accept(COOKEDPIGSTREAKYPORK.get()); output.accept(COOKEDPIGTENDERLOIN.get());
                output.accept(COOKEDPIGSPARERIB.get()); output.accept(COOKEDPIGLEG.get()); output.accept(COOKEDPIGFEET.get()); output.accept(COOKEDPIGFAT.get());
                output.accept(COOKEDPIGSTOMACH.get()); output.accept(COOKEDPIGLUNG.get()); output.accept(COOKEDPIGLIVER.get()); output.accept(COOKEDPIGKIDNEY.get());
                output.accept(COOKEDPIGHEART.get()); output.accept(COOKEDPIGCEREBRUM.get()); output.accept(COOKEDPIGINTESTINE.get()); output.accept(COOKEDPIGFACE.get());
                output.accept(COOKEDDICEDSHEEPMEAT.get()); output.accept(COOKEDSHEEPFACE.get()); output.accept(COOKEDSHEEPEYE.get()); output.accept(COOKEDSHEEPTAIL.get());
                output.accept(COOKEDSHEEPSTOMACH.get()); output.accept(COOKEDSHEEPLIVER.get()); output.accept(COOKEDSHEEPINTESTINE.get()); output.accept(COOKEDSHEEPKIDNEY.get());
                output.accept(COOKEDSHEEPHEART.get()); output.accept(COOKEDSHEEPSPARERLIP.get()); output.accept(COOKEDSHEEPSPINE.get()); output.accept(COOKEDSHEEPFEET.get());
                output.accept(COOKEDSHEEPLEG.get()); output.accept(COOKEDSHEEPFAT.get());
                output.accept(COOKEDCHICKENDICED.get()); output.accept(COOKEDCHICKENPIECEPIECE.get()); output.accept(COOKEDCHICKENHEAD.get()); output.accept(COOKEDCHICKENNECK.get());
                output.accept(COOKEDCHICKENWING.get()); output.accept(COOKEDCHICKENWINGTIP.get()); output.accept(COOKEDCHICKENFEET.get()); output.accept(COOKEDCHICKENLEGWITHLEG.get());
                output.accept(COOKEDCHICKENLEG.get()); output.accept(COOKEDCHICKENPIECE.get()); output.accept(COOKEDCHICKENSTEAK.get()); output.accept(COOKEDCHICKENFORK.get());
                output.accept(COOKEDCHICKENASS.get()); output.accept(COOKEDCHICKENHEART.get()); output.accept(COOKEDCHICKENLIVER.get()); output.accept(COOKEDCHICKENGIZZARD.get());

                // ===== Snacks =====
                output.accept(FIVEPOINTEDCARAMBOLADELIGHT.get());
                output.accept(DICEDHAMIMELON.get());
                output.accept(FRIED_TOFU_SKIN_ROLLS.get());
                output.accept(CHICKENFEETWITHPEPPERS.get());
                output.accept(COOKEDHAWTHORN.get());
                output.accept(SWEETTOUFUCURD.get());
                output.accept(TIGERGREENPEPPER.get());
                output.accept(PICEDTOUFU.get());
                output.accept(DRIEDTOUFU.get());
                output.accept(TOFU_PUFFS.get());
                output.accept(TOFUCURD.get());
                output.accept(CHINESESPICYSNACKFOOD.get());
                output.accept(SPICYPEANUT.get());
                output.accept(COOKEDSAUSAGE.get());
                output.accept(M_SECRETDELIGHT.get());

                // ===== Desserts =====
                output.accept(RIPEPEARWITHROCKSUGAR.get());
                output.accept(SUGARCOATEDHAWS.get());
                output.accept(CHINESEYAMANDSUGAR.get());
                output.accept(CHOCOLATEPOPCORN.get());
                output.accept(CHOCOLATEBEAN.get());
                output.accept(CARAMELPOPCORN.get());
                output.accept(POPCORN.get());
                output.accept(JELLY.get());
                output.accept(TANG_ZI_XIAO_ZAO.get());
                output.accept(SUGARCOATEDWALNUT.get());
                output.accept(GOLDRICECAKE.get());

                // ===== Cooked Meat =====
                output.accept(STEWEDPIGLEG.get());
                output.accept(STEWEDHALFCHICKENLEG.get());
                output.accept(STEWEDCHICKENHEART.get());
                output.accept(STEWEDCHICKENLIVERS.get());
                output.accept(STEWEDCHICKENLEG.get());
                output.accept(SAUCINGBEEF.get());
                output.accept(BONELESSLEMONCHICKENFEET.get());
                output.accept(SOMKEDCHICKENBREAST.get());
                output.accept(COOKEDPRESERVEDEGG.get());
                output.accept(MEATFLOSS.get());
                output.accept(FISHSASHIMI.get());

                // ===== Cooked Food =====
                output.accept(NEWYEARCAKE.get());
                output.accept(COLONELCHICKENNUGGETS.get());
                output.accept(FRIEDGULTINOUSRICESTRIPS.get());
                output.accept(FRIEDPEANUTS.get());
                output.accept(FRIEDMUSHROOM.get());
                output.accept(DRIEDRICECAKE.get());
                output.accept(FIREDSPRINGROLL.get());
                output.accept(FRIEDDOUGHSTICK.get());
                output.accept(DRIEDMILK.get());
                output.accept(DRIEDMEATBALL.get());
                output.accept(DRIEDDICEDAUBERGINE.get());
                output.accept(FRIEDGIZZARD.get());
                output.accept(FRENCHFRIES.get());
                output.accept(DRIEDLOTUSROOT.get());
                output.accept(FRIEDDRIEDTOUFU.get());
                output.accept(FRIEDTOUFU.get());
                output.accept(DRIEDBREAD.get());
                output.accept(DRIEDDICEDBREAD.get());
                output.accept(DRIEDDICEDSTEAMEDBREAD.get());
                output.accept(FRIEDSANZI.get());
                output.accept(FRIEDHEMPBALL.get());
                output.accept(FRIEDDOUGHTWIST.get());
                output.accept(CRISPYPISTOLLEG.get());
                output.accept(ROASTEDPURPLEPOTATO.get());
                output.accept(ROASTEDPEANUT.get());
                output.accept(COOKEDCHINESEYAM.get());
                output.accept(COOKEDVERMICELLI_0.get());
                output.accept(COOKEDZONGZI.get());
                output.accept(COOKEDGLUTINOUSRICE.get());
                output.accept(COOKEDGLUTINOUSRICEPOUNDEDINTOPASTE.get());
                output.accept(AIKUI.get());
                output.accept(STEAMEDBLOOD.get());
                output.accept(DICEDBREAD.get());
                output.accept(DRYCOOKEDNOODLES.get());
                output.accept(COOKEDVERMICELLI.get());
                output.accept(MOONCAKE.get());
                output.accept(GRAINSPANCAKE.get());
                output.accept(HAMBURGERBREAD.get());
                output.accept(YIMENGPANCAKES.get());
                output.accept(SACHIMA.get());
                output.accept(SHUMAI.get());
                output.accept(HOTDOG.get());
                output.accept(STEAMEDGLUTINOUSRICECAKE.get());
                output.accept(MEATFLOURROOL.get());
                output.accept(CRISPYEGGCAKE.get());
                output.accept(STEAMEDTWISTEDROLL.get());
                output.accept(SCALLIONOILPANCAKE.get());
                output.accept(NOODLES.get());
                output.accept(PIE.get());
                output.accept(STEAMEDBREAD.get());
                output.accept(DICEDSTEAMEDBREAD.get());
                output.accept(EGGPUFFS.get());
                output.accept(EGGVERMICELLI.get());

                // ===== Baked Food =====
                output.accept(ORLEANWING.get());
                output.accept(ORLEANLEG.get());
                output.accept(ROASTEDMUSHROOM.get());
                output.accept(ROASTED_FLAMMULINAVELUTIPES.get());
                output.accept(ROASTEDCHINESECHIVES.get());
                output.accept(ROASTEDDICEDFISH.get());

                // ===== Drinks =====
                output.accept(CREAMPOPCORN.get());
                output.accept(BOILEDCORN.get());
                output.accept(SOYAMILK.get());
                output.accept(DRINKINGWATER.get());
                output.accept(CAPPUCCINO.get());
                output.accept(CAFELATTE.get());
                output.accept(MOCHACAFE.get());
                output.accept(HOTWATER.get());
                output.accept(CAFE_AMERICANO.get());
                output.accept(LEMONREDTEA.get());
                output.accept(GRENTEAMILKWITHPEARL.get());
                output.accept(GREEN_TEA_LATTES.get());
                output.accept(LEMONTEAWITHGLUTINOUSRICEFLAVOR.get());
                output.accept(REDTEA.get());
                output.accept(GREENTEA.get());
                output.accept(BUCKWHEATTEA.get());
                output.accept(ASSAMMILK_TEA.get());
                output.accept(GREENPLUMTEA.get());
                output.accept(REDTEAMILKWITHPEARL.get());
                output.accept(KAOLIANGWINE.get());
                output.accept(KWEICHOW_MOUTAI.get());
                output.accept(TSINGTAO_BEER.get());
                output.accept(ICEDBLACKTEABLUE.get());
                output.accept(COLA.get());
                output.accept(SUGARFREICEDTEA.get());
                output.accept(SPRITE.get());

                // ===== Pickled =====
                output.accept(YOGURT.get());
                output.accept(SALTEGGYOLK.get());
                output.accept(SALTTOFUCURD.get());
                output.accept(DICEDPICKLEDVEGETABLE.get());
                output.accept(CUREDMEAT.get());
                output.accept(CUREDSAUSAGE.get());
                output.accept(SALTYDICEDCURUMBER.get());
                output.accept(PICKLEDVEGETABLE.get());
                output.accept(SLICEDSALTYCUCUMBER.get());
                output.accept(HOTANDSOURRICENOODLES.get());

                // ===== Ingredients =====
                output.accept(GOLDENGRAPE.get());

                // ===== expand: ʳ�� =====
                output.accept(BAISED_TOFU_SLICES.get()); output.accept(BOILEDRICEFLOURNOODLES.get()); output.accept(BONESOUP.get()); output.accept(BUCKWHEATRICE.get()); output.accept(CHOCOLATE.get());
                output.accept(CONGEEWITH_MINCE_PORKAND_PRESERVED_EGG.get()); output.accept(COOKEDCHICKENBREAST.get()); output.accept(COOKEDCHICKENLEAN.get()); output.accept(COOKEDCORNBATTER.get()); output.accept(COOKEDCRISPYPORK.get());
                output.accept(COOKEDFLAKEBEEF.get()); output.accept(COOKEDHALFOFCHCIKENLEG.get()); output.accept(COOKEDPIGSTREAKMEAT.get()); output.accept(DRIED_TOFU_SKIN.get()); output.accept(EATENPINEAPPLE.get());
                output.accept(EGGBISCUIT.get()); output.accept(EGGPANCAKE.get()); output.accept(EGGTART.get()); output.accept(EIGHTTREASURECONGEE.get()); output.accept(ELECTROLYTEBEVERAGE.get());
                output.accept(FANHUAROLL.get()); output.accept(FRIEDCHICKENCHOP.get()); output.accept(FRIEDCHICKENCORN.get()); output.accept(FRIEDCHICKENLEG.get()); output.accept(FRIEDCHICKENWING.get());
                output.accept(FRIEDSAUSAGE.get()); output.accept(GRAPEWINE.get()); output.accept(GREENBEANPORRIDGE.get()); output.accept(HANDGRABBEDPANCAKE.get()); output.accept(HEALTHLOQUATCREAM.get());
                output.accept(HEALTHPEANUTMILK.get()); output.accept(HEALTHWALNUTDEW.get()); output.accept(ICEDBLACKTEA.get()); output.accept(KAOLIANGPORRIDGE.get()); output.accept(KAOLIANGRICE.get());
                output.accept(LABAPORRIDGE.get()); output.accept(MILLIETPORRIDGE.get()); output.accept(MILLIETRICE.get()); output.accept(NOILEDRICENOODLE.get()); output.accept(OAKPORRIDGE.get());
                output.accept(OAKRICE.get()); output.accept(OMELETTE.get()); output.accept(PANADAPANCAKE.get()); output.accept(PEAFLOURCAKE.get()); output.accept(POTATOCHIPS.get());
                output.accept(REDBEANEGGTART.get()); output.accept(REDBEANSTUFFINGDORAYAKI.get()); output.accept(RICE.get()); output.accept(RICE_PORRIDGE.get()); output.accept(RICEPASTESOUP.get());
                output.accept(ROASTCHICKENFORK.get()); output.accept(ROASTEDSWEETPOTATO.get()); output.accept(SHELLEDBOILEDEGG.get()); output.accept(SHOU_KAI_XIN_GUO.get()); output.accept(SMALLMICEDMEATCAKE.get());
                output.accept(STEAMEDSALTORANGE.get()); output.accept(STEAMEDVERMICELLIROLL_0.get()); output.accept(STRAWBERRYCAKEROLL.get()); output.accept(SWEETMILK.get()); output.accept(TOFU_SAUSAGE.get());
                output.accept(TOFU_STICKS.get()); output.accept(WALNUTCAKE.get()); output.accept(WALNUTSHORTBREAD.get()); output.accept(WHEATMILK.get()); output.accept(YUBA.get());
                output.accept(TOUFU.get()); output.accept(EGGCAKE.get()); output.accept(GOLDRICECAKEMAX.get()); output.accept(GOLDENGRAPEMAX.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> INGREDIENT_TAB = REGISTRY.register("ingredient", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.ingredient"))
            .withTabsBefore(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "fi_dtools"))
            .icon(() -> WHEATFLOUR.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CUT_CHINESE_CABBAGE.get());

                // �й��Ĳ�
                output.accept(DICEDLOOFACH.get()); output.accept(DICEDWAXGOURD.get()); output.accept(DICEDORANGE.get()); output.accept(DICEDPLEUROTUSERYNGII.get()); output.accept(DICEDONION.get()); output.accept(DICEDSAUTEEDGREENBEANS.get()); output.accept(SHREDDEDPOTATO_2.get()); output.accept(DICEDPOTATO.get()); output.accept(CHOPPEDPOTATO_1.get()); output.accept(SLICEDPOTATO.get()); output.accept(DICEDCHINESEYAM.get()); output.accept(DICEDCASSAVA.get()); output.accept(SLICEDLEMON.get()); output.accept(DICEDFISH.get()); output.accept(DICEDPURPLESWEETPOTATO.get()); output.accept(SHREDDEDCARROTS.get()); output.accept(DICEDCARROTS.get()); output.accept(DICEDCELERY.get()); output.accept(SHREDDERABUERGINE.get()); output.accept(SLICEDABUERGINE.get()); output.accept(SHREDDERRADISH.get()); output.accept(DICEDRADISH.get()); output.accept(DIECEDRADISH.get()); output.accept(SLICEDRADISH.get()); output.accept(SCALLION_2.get()); output.accept(DICEDLOTUSROOT.get()); output.accept(SLICEDLOTUSROOT.get()); output.accept(DICEDZUCCHINI.get()); output.accept(CHOPPEDCOWPEA.get()); output.accept(DICEDSWEETPEPPER.get()); output.accept(SHREDDERCUCUMBER.get()); output.accept(DICEDCUCUMBER.get()); output.accept(SLICEDCUCUMBER.get()); output.accept(CHINESE_LEAVES.get());

                // �й��Ĳ�-����
                output.accept(DIECEDPRESERVEDEGG.get()); output.accept(CUTDIECEDPRESERVEEGG.get()); output.accept(DICEDSWEETPOTATO.get()); output.accept(PURPLEDICEDSWEETPOTATO.get()); output.accept(PROCESSEDCABBAGE.get()); output.accept(DICEDTOMATO.get()); output.accept(CUTCABBAGE.get()); output.accept(CASSAVACHUNKS.get()); output.accept(SLICEDZUCCHINI.get()); output.accept(CARROTCHUNKS.get()); output.accept(CELERYLEAF.get());

                // ���永��
                output.accept(BAOZI_SKIN.get()); output.accept(COMPACTEDRICEBRICK.get()); output.accept(DRIEDVERMICELLI.get()); output.accept(DRYPOWDERSTRIP.get()); output.accept(DRYPOWDERSKIN.get()); output.accept(MACARONI.get()); output.accept(BEATGLUTINOUSRICEFLOURPASTE.get()); output.accept(WALNUTFLOURPASTE.get()); output.accept(OILEDFLOURSKIN.get()); output.accept(SHUMAISKIN.get()); output.accept(DRYRICECAKE.get()); output.accept(RAWMOONCAKE.get()); output.accept(RAWSHUMAI.get()); output.accept(RAWCOARSENOODLES.get()); output.accept(RAWGLUTINOUSPASTE.get()); output.accept(RAWGLUTINOUSDOUGH.get()); output.accept(RAWGLUTINOUSNOODLES.get()); output.accept(RAWGLUTINOUSSKIN.get()); output.accept(RAWGLUTINOUSRICEFLOURPANCAKE.get()); output.accept(RAWFLOURROLL.get()); output.accept(RAWNOODLES.get()); output.accept(RAWFLOURPANCAKE.get()); output.accept(RAWPIE.get()); output.accept(RAWEGGPASTE.get()); output.accept(RAWEGGSKIN.get()); output.accept(RAWEGGPANCAKE.get()); output.accept(RAWRICENOODLES.get()); output.accept(RICENOODLES_2.get()); output.accept(SWEETPOTATODOUGH.get()); output.accept(VERMICELLIROLL.get()); output.accept(STEAMEDRICEBRICK.get()); output.accept(NOODLESWRAPPEDINSYRUP.get()); output.accept(EGGNOODLESWRAPPEDINSYRUP.get()); output.accept(FLOURPASTE.get()); output.accept(DOUGH.get()); output.accept(DUMPLING_SKIN.get()); output.accept(FLOURSKIN.get()); output.accept(RAWEGGNOODLES.get()); output.accept(EGGDOUGH.get());

                // ���永��-����
                output.accept(CUTPANCAKE.get()); output.accept(FERMENTEDFLOURPASTE.get()); output.accept(CUTEGGPANCAKE.get()); output.accept(RICESLURRYWRAPPEDINFILTERCLOTH.get()); output.accept(POUNDEDGLUTINOUSPASTE.get()); output.accept(LONGDOUGH.get()); output.accept(LONGEGGPANCAKE.get());

                // �࣬���ϣ���
                output.accept(WAXGROUDPASTE.get()); output.accept(JUJUBEPASTE.get()); output.accept(BROKENWALNUT.get()); output.accept(PEPPERANDSALTMASS.get()); output.accept(FRUITFLAVOREDMOONCAKESTUFFING.get()); output.accept(MEATPASTE.get()); output.accept(VEGETABLEPASTE.get()); output.accept(REDBEANPASTE.get()); output.accept(MEATANDVEGETABLESTUFFING.get()); output.accept(MEATANDEGGPASTE.get()); output.accept(SEASAMEGLUTINOUSRICEBALLS.get()); output.accept(SESAMEANDPEANUTBALLS.get()); output.accept(PEASTUFFING.get()); output.accept(EGGSTUFFING.get());

                // �࣬���ϣ���-����
                output.accept(PEAPASTE.get()); output.accept(SESAMEGLUTINOUSRICEBALLS.get()); output.accept(MEATANDEGGFILLING.get()); output.accept(MEATANDVEGETABLEFILLING.get()); output.accept(MUNGBEANPASTE.get()); output.accept(WAXGOURDPASTE.get()); output.accept(UNCOMMONSTUFFING.get()); output.accept(VEGETABLEANDEGGSTUFFING.get());

                // Һ�壬������
                output.accept(COCOASAUCE.get()); output.accept(CREAM.get()); output.accept(MILKBOTTLE.get()); output.accept(MULTIGRAINBATTER.get()); output.accept(CONCENTRATEDSYRUP.get()); output.accept(SWEETEGGLIQUID.get()); output.accept(RAWSOYBEANMILK.get()); output.accept(TIDYWATER.get()); output.accept(GLUTINOUSRICEBATTER.get()); output.accept(PEANUTJAM.get()); output.accept(EGGLIQUID.get()); output.accept(PANADA.get()); output.accept(GELATIN.get()); output.accept(EGGBATTER.get());

                // Һ�壬������-����
                output.accept(SALTYWATER.get()); output.accept(COCOAMASS.get());

                // ����ը�����??
                output.accept(CRISPYPORKBELLY.get()); output.accept(COOKED_BAOZI.get()); output.accept(RAW_STUFFEDGREENPEPPER.get()); output.accept(CREAMCORNKERNELS.get()); output.accept(CHOCOLATECORNKERNELS.get()); output.accept(MUSHROOMSWRAPPEDINBATTER.get()); output.accept(RAWCHICKENHALFLEGWITHBATTER.get()); output.accept(RAWCHICKENWINGWITHBATTER.get()); output.accept(RAWCHICKENMEATWITHBATTER.get()); output.accept(RAWCHICKENLEGWITHLEG.get()); output.accept(RAWSAUSAGE.get()); output.accept(NORMALMEATROLL.get()); output.accept(CARAMELCORNKENNELS.get()); output.accept(RAWSPRINGROLL.get()); output.accept(RAW_TANGYUAN.get()); output.accept(RAWAZONGZI.get()); output.accept(SWEETREDBEANEGGTART.get()); output.accept(MEATFLOURROOL.get()); output.accept(RAWFLOURPASTEWITHDRIEDMEATFLOSS.get()); output.accept(RAWCOUPLING.get()); output.accept(RAWEGGTART.get()); output.accept(RAWSPICYGLUTEN.get()); output.accept(RAWDORAYAKI.get()); output.accept(RAWAIKUI.get()); output.accept(RAWSALTYEGG.get()); output.accept(LAMBROLL.get()); output.accept(FATBEEFROLL.get()); output.accept(RAWDICEDCHICKENWITHBATTER.get()); output.accept(RAWMEATBALLWITHEGGBALL.get()); output.accept(COOKEDDUMPLING.get()); output.accept(WONTON.get());

                // ����ը�����??����
                output.accept(EGGCOATEDMEATBALLS.get()); output.accept(BATTEREDCHICKENBREASTCHUNKS.get()); output.accept(RAWMEATFLOSSDOUGH.get()); output.accept(RAWMEATZONGZI.get()); output.accept(BATTEREDMUSHROOMS.get()); output.accept(RAWSWEETZONGZI.get()); output.accept(RAWWRAPPEDMILK.get());

                // ��ʳ��Ʒ
                output.accept(SALTYEGG.get()); output.accept(SALTYRADDISH.get()); output.accept(TANGYUAN.get()); output.accept(THOUSAND_LAYER_TOFU_SKIN.get()); output.accept(SPAGHETTI.get());

                // ����
                output.accept(COCOAPOWDER.get()); output.accept(COFFEEPOWDER.get()); output.accept(TAPIOCAFLOUR.get()); output.accept(WALNUTPOWDER.get()); output.accept(GULTINOUSRICEPOWDER.get()); output.accept(SWEETPOTATSTARCH.get()); output.accept(SESAMEPOWDER.get()); output.accept(GULTINOUSRICESASAMEPOWDER.get()); output.accept(PEANUTPOWDER.get()); output.accept(PEANUTSESAMEPOWDER.get()); output.accept(MODULATEDWHEATFLOUR.get()); output.accept(PEAMEAL.get()); output.accept(WHEATFLOUR.get());

                // ����-����
                output.accept(PEAFLOUR.get());

                // ��ζ??
                output.accept(CRYSTALSUGAR.get()); output.accept(HALOGENBAG.get()); output.accept(CURRY.get()); output.accept(WHITESUGARSYRUP.get()); output.accept(SWEETFLOURASUVE.get()); output.accept(TOMATO_HOT_POT_BASE.get()); output.accept(SALT.get()); output.accept(SALTPIECE.get()); output.accept(BROWNSUGAR.get()); output.accept(PEPPER_HOT_POT_BASE.get()); output.accept(THICKBROADBEANSAUCE.get()); output.accept(SOY.get()); output.accept(VINEGAR.get()); output.accept(COOKINGOIL.get()); output.accept(SESAMEOIL.get()); output.accept(BONESOUPESSENCE.get()); output.accept(MAJUICE.get()); output.accept(SPICY_HOT_POT_BASE.get()); output.accept(BUTTER.get());

                // ��ζ??����
                output.accept(BROWNSUGARSYRUP.get()); output.accept(HOTPOTBASETEMPLATE.get());

                // ��ζ�Ϸ�
                output.accept(LILACPOWDER.get()); output.accept(FIVESPICEPOWDER.get()); output.accept(ANISEEDPOWDER.get()); output.accept(ORLEANSPOWDER.get()); output.accept(GROUNDPOWDER.get()); output.accept(CUMINPOWDER.get()); output.accept(CINNAMONPOWDER.get()); output.accept(PEPPEREDSALT.get()); output.accept(ONIONPOWDER_2.get()); output.accept(REDTEAPOWDER.get()); output.accept(GREENTEAPOWDER.get()); output.accept(CHINESEPICKLYASHPOWDER.get()); output.accept(FENNELPOWDER.get()); output.accept(ONIONPOWDER.get()); output.accept(GARLICPOWDER.get()); output.accept(CHILLIPOWDER.get());

                // ��������
                output.accept(SORBET.get()); output.accept(FROZENMILK.get()); output.accept(NAHCO_3.get()); output.accept(WRESTLING_GUN.get()); output.accept(CASSAVAPEARL.get()); output.accept(SOAKEDSOYBEANS.get()); output.accept(TIDYREEDLEAF.get()); output.accept(RAWSOYSHREDDEDMEAT.get()); output.accept(PROBIOTICS.get()); output.accept(LANDPLASTER.get()); output.accept(GRAVELPASTE.get()); output.accept(RAWSHEEPOFFAL.get()); output.accept(MEATFLOSS.get()); output.accept(CASING.get()); output.accept(REEDLEAF.get()); output.accept(BRAN.get());

                // ��������-����
                output.accept(SOYBEANPROTEIN.get()); output.accept(RAW_PIGOFFAL.get()); output.accept(DEBONEDCHICKENFEET.get()); output.accept(EGGWRAPPEDINGRAVEL.get()); output.accept(BEANSPROUT.get());

                // ===== expand: ʳ�� =====
                output.accept(DRYANISEED.get()); output.accept(DRYCINNAMON.get()); output.accept(DRYCOFFEEBEAN.get()); output.accept(DRYLILAC.get()); output.accept(DRYNUTMEG.get());
                output.accept(DRYSCALION.get()); output.accept(DRYSICHUANPEPPER.get()); output.accept(DICEDBROCCOIL.get()); output.accept(DICEDCAULIFLOWER.get()); output.accept(EGGSHELL.get());
                output.accept(OATGRAIN.get()); output.accept(POLISHEDGLUTINOUSRICE_2.get()); output.accept(RAW_BAOZI.get()); output.accept(RAW_DUMPLING.get()); output.accept(RAW_WONTON.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DTOOLS_TAB = REGISTRY.register("fi_dtools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.fi_dtools"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> KITCHENSCISSOR.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BIGHOOK.asItem());
                output.accept(WOODBASIN.asItem());
                output.accept(SHARPKNIFE.get());
                output.accept(WIDEEDGEDKNIFE.get());
                output.accept(BONECUTTERKNIFE.get());
                output.accept(KITCHENKNIFE.get());
                output.accept(KITCHENSCISSOR.get());
                output.accept(SPATULA.get());
                output.accept(MALLET.get());
                output.accept(COARSECLOTH.get());
                output.accept(MOONCAKEMOLD.get());
                output.accept(ROLLINGPIN.get());
                output.accept(FILTERVAT.get());
                output.accept(EEGGPUFFSMOULD.get());
                output.accept(AGRICULTURALAPPRAISALMACHINE.asItem());
                output.accept(FRIDGE.asItem());
                output.accept(EGGBREAKINGMACHINE.asItem());
                output.accept(MINCER_COVER.get());
                output.accept(TEAPOTCOVER.get());
                //װ��
                output.accept(FAIRY_SPARKLER.get());
                output.accept(COLORFUL_FIREWORKS_BOX.asItem());
                output.accept(WRESTLING_GUN.get());
                output.accept(HONEYCOMBBRIQUET.get());
                output.accept(LEFT_DOOR_PAPER.asItem());
                output.accept(RIGHT_DOOR_PAPER.asItem());
                output.accept(CHINESE_KNOTTING.asItem());
                output.accept(LAMP_CABINET.asItem());
                output.accept(CANVAS_SCREEN_1.asItem());
                output.accept(CANVAS_SCREEN_2.asItem());
                output.accept(INCENSE_BURNER.asItem());
                output.accept(PLANK_HANGING_LIGHT.asItem());
                output.accept(REDLANTERN.asItem());
                output.accept(GOLDLANTERN.asItem());
                output.accept(STONE_LION.asItem());
                output.accept(WINDOW_PAPER_ITEM.get());
                output.accept(ANTITHETICAL_COUPLET_1.asItem());
                output.accept(ANTITHETICAL_COUPLET_2.asItem());
                //ľ��
                output.accept(SOLARWOOD_PLANKS.asItem());
                output.accept(SOLARWOOD_STAIRS.asItem());
                output.accept(SOLARWOOD_SLAB.asItem());
                output.accept(SOLARWOOD_FENCE.asItem());
                output.accept(SOLARWOOD_FENCE_GATE.asItem());
                output.accept(SOLARWOOD_BUTTON.asItem());
                output.accept(SOLARWOOD_PRESSURE_PLATE.asItem());
                output.accept(ORCHARD_HEARTWOOD_PLANKS.asItem());
                output.accept(ORCHARD_HEARTWOOD_STAIRS.asItem());
                output.accept(ORCHARD_HEARTWOOD_SLAB.asItem());
                output.accept(ORCHARD_HEARTWOOD_FENCE.asItem());
                output.accept(ORCHARD_HEARTWOOD_FENCE_GATE.asItem());
                output.accept(ORCHARD_HEARTWOOD_BUTTON.asItem());
                output.accept(ORCHARD_HEARTWOOD_PRESSURE_PLATE.asItem());
                output.accept(STONEBARK_PLANKS.asItem());
                output.accept(STONEBARK_STAIRS.asItem());
                output.accept(STONEBARK_SLAB.asItem());
                output.accept(STONEBARK_FENCE.asItem());
                output.accept(STONEBARK_FENCE_GATE.asItem());
                output.accept(STONEBARK_BUTTON.asItem());
                output.accept(STONEBARK_PRESSURE_PLATE.asItem());
                output.accept(VINEHEART_TIMBER_PLANKS.asItem());
                output.accept(VINEHEART_TIMBER_STAIRS.asItem());
                output.accept(VINEHEART_TIMBER_SLAB.asItem());
                output.accept(VINEHEART_TIMBER_FENCE.asItem());
                output.accept(VINEHEART_TIMBER_FENCE_GATE.asItem());
                output.accept(VINEHEART_TIMBER_BUTTON.asItem());
                output.accept(VINEHEART_TIMBER_PRESSURE_PLATE.asItem());
                output.accept(VERDANT_GRACE_PLANKS.asItem());
                output.accept(VERDANT_GRACE_STAIRS.asItem());
                output.accept(VERDANT_GRACE_SLAB.asItem());
                output.accept(VERDANT_GRACE_FENCE.asItem());
                output.accept(VERDANT_GRACE_FENCE_GATE.asItem());
                output.accept(VERDANT_GRACE_BUTTON.asItem());
                output.accept(VERDANT_GRACE_PRESSURE_PLATE.asItem());
                output.accept(SOLARWOODCHAIR.asItem());
                output.accept(ORCHARDCHAIR.asItem());
                output.accept(STONEBARKCHAIR.asItem());
                output.accept(VINEHEARTCHAIR.asItem());
                output.accept(VERDANTGRACECHAIR.asItem());

            }).build());
}
