package com.flavor_immersed_daily.block.blockentity;

import com.flavor_immersed_daily.all.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// 灯谜牌方块实体 — 保存牌面竖排四字、放置者、谜面（问题/答案）、
// 16 格奖励物品栏，以及每个玩家是否已领过奖励
public class RaddlesBlockEntity extends BlockEntity implements Container {

// 牌面可书写的汉字数量
    public static final int CHAR_COUNT = 4;
// 奖励物品栏格数
    public static final int REWARD_SLOTS = 16;

    private static final String TAG_CHAR_PREFIX = "char_";
    private static final String TAG_QUESTION = "question";
    private static final String TAG_ANSWER = "answer";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_OWNER_NAME = "owner_name";
    private static final String TAG_CLAIMED = "claimed";

    private String[] chars = new String[]{"", "", "", ""};
    private String question = "";
    private String answer = "";
    private UUID owner;
    private String ownerName = "";
    private final Set<UUID> claimed = new HashSet<>();
    private NonNullList<ItemStack> items = NonNullList.withSize(REWARD_SLOTS, ItemStack.EMPTY);

    public RaddlesBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RADDLES_ENTITY.get(), pos, state);
    }

// 牌面文字

    public String[] getChars() {
        return chars;
    }

    public String getChar(int index) {
        return index >= 0 && index < CHAR_COUNT ? chars[index] : "";
    }

    public void setChars(String[] chars) {
        this.chars = new String[]{"", "", "", ""};
        if (chars != null) {
            System.arraycopy(chars, 0, this.chars, 0, Math.min(chars.length, CHAR_COUNT));
        }
        sync();
    }

// 谜面

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

// 问题与答案都已填写才算设置好谜底
    public boolean hasRiddle() {
        return !question.isBlank() && !answer.isBlank();
    }

    public void setRiddle(String question, String answer) {
        this.question = question != null ? question : "";
        this.answer = answer != null ? answer : "";
        sync();
    }

// 校验玩家提交的答案，忽略首尾空白
    public boolean matchesAnswer(String input) {
        return input != null && !answer.isBlank() && answer.equals(input.trim());
    }

// 放置者

    public UUID getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwner(UUID owner, String name) {
        this.owner = owner;
        this.ownerName = name != null ? name : "";
        sync();
    }

    public boolean isOwner(Player player) {
        return owner != null && owner.equals(player.getUUID());
    }

// 放置者自己或没有放置者记录时，都可视为可编辑
    public boolean canEdit(Player player) {
        return owner == null || isOwner(player);
    }

// 领取记录

    public boolean isClaimed(UUID playerId) {
        return claimed.contains(playerId);
    }

    public void markClaimed(UUID playerId) {
        if (claimed.add(playerId)) {
            sync();
        }
    }

// 奖励物品

// 从奖励物品栏随机取出 1 个物品；没有奖励时返回空
    public ItemStack takeRandomReward(RandomSource random) {
        List<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < REWARD_SLOTS; i++) {
            if (!items.get(i).isEmpty()) {
                candidates.add(i);
            }
        }
        if (candidates.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int slot = candidates.get(random.nextInt(candidates.size()));
        ItemStack stack = items.get(slot);
        ItemStack reward = stack.copyWithCount(1);
        stack.shrink(1);
        if (stack.isEmpty()) {
            items.set(slot, ItemStack.EMPTY);
        }
        sync();
        return reward;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

// 答题界面展示用的奖励图标（每个非空格只取 1 个，隐藏实际数量）
    public List<ItemStack> getRewardPreview() {
        List<ItemStack> preview = new ArrayList<>();
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                preview.add(stack.copyWithCount(1));
            }
        }
        return preview;
    }

// Container

    @Override
    public int getContainerSize() {
        return REWARD_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = ContainerHelper.removeItem(items, index, count);
        if (!result.isEmpty()) {
            sync();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        sync();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
// 逐格清空，保持奖励栏固定 16 格
        for (int i = 0; i < REWARD_SLOTS; i++) {
            items.set(i, ItemStack.EMPTY);
        }
        sync();
    }

// 同步

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

// NBT

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for (int i = 0; i < CHAR_COUNT; i++) {
            tag.putString(TAG_CHAR_PREFIX + i, chars[i] != null ? chars[i] : "");
        }
        tag.putString(TAG_QUESTION, question);
        tag.putString(TAG_ANSWER, answer);
        if (owner != null) {
            tag.putUUID(TAG_OWNER, owner);
        }
        tag.putString(TAG_OWNER_NAME, ownerName);
        ContainerHelper.saveAllItems(tag, items, registries);

        ListTag claimedList = new ListTag();
        for (UUID id : claimed) {
            claimedList.add(StringTag.valueOf(id.toString()));
        }
        tag.put(TAG_CLAIMED, claimedList);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.chars = new String[]{"", "", "", ""};
        for (int i = 0; i < CHAR_COUNT; i++) {
            chars[i] = tag.getString(TAG_CHAR_PREFIX + i);
        }
        question = tag.getString(TAG_QUESTION);
        answer = tag.getString(TAG_ANSWER);
        owner = tag.hasUUID(TAG_OWNER) ? tag.getUUID(TAG_OWNER) : null;
        ownerName = tag.getString(TAG_OWNER_NAME);

        this.items = NonNullList.withSize(REWARD_SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);

        claimed.clear();
        ListTag claimedList = tag.getList(TAG_CLAIMED, Tag.TAG_STRING);
        for (int i = 0; i < claimedList.size(); i++) {
            try {
                claimed.add(UUID.fromString(claimedList.getString(i)));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }
}
