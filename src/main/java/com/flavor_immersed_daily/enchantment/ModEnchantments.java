package com.flavor_immersed_daily.enchantment;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

// 本模组附魔的资源键（附魔本体为数据驱动，定义在 data/flavor_immersed_daily/enchantment/）
public final class ModEnchantments {

    private ModEnchantments() {
    }

// 无限砍伐：破坏原木后有概率在原位置还原该原木
    public static final ResourceKey<Enchantment> INFINITE_LOGGING = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "infinite_logging"));

// 服务端注册表里的「无限砍伐」Holder。
// 附魔是数据驱动注册表，客户端与服务端各持一份<b>互不相通</b>的实例。把某一端的 Holder
// 写到另一端的物品上，存档或发包序列化时会因该注册表里查不到这个附魔而抛
// Can't find id for ...，进而崩溃。因此两端必须各缓存各的 Holder，
// 物品在哪个端生成就用哪个端的 Holder。
    @Nullable
    private static Holder<Enchantment> serverHolder;

// 客户端注册表里的「无限砍伐」Holder
    @Nullable
    private static Holder<Enchantment> clientHolder;

// 服务端数据包加载后刷新服务端 Holder
    public static void bindServerHolder(HolderLookup.Provider registries) {
        serverHolder = resolve(registries);
    }

// 客户端收到服务端同步的数据包后刷新客户端 Holder
    public static void bindClientHolder(HolderLookup.Provider registries) {
        clientHolder = resolve(registries);
    }

    @Nullable
    private static Holder<Enchantment> resolve(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(Registries.ENCHANTMENT)
                .get(INFINITE_LOGGING)
                .orElse(null);
    }

// 取当前所在逻辑端对应的「无限砍伐」Holder：客户端主线程用客户端注册表的，
// 其余（服务端主线程、服务端工作线程等）用服务端注册表的。数据包尚未加载时返回 null。
// Holder.Reference 由注册表按 ResourceKey 缓存，同一端每次取到的都是同一实例，
// 因此可直接与物品组件里存着的 Holder 比对。
    @Nullable
    public static Holder<Enchantment> holderForCurrentSide() {
        if (FMLEnvironment.dist.isClient()) {
            Minecraft client = Minecraft.getInstance();
            if (client != null && client.isSameThread()) {
                return clientHolder;
            }
        }
        return serverHolder;
    }
}
