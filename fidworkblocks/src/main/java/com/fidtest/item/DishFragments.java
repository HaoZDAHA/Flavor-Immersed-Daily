package com.fidtest.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class DishFragments {
    private DishFragments() {}

    public static CompoundTag data(ItemStack stack) {
        CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
        return custom == null ? new CompoundTag() : custom.copyTag().getCompound("WokDish");
    }

    public static ListTag fragments(ItemStack stack) {
        return data(stack).getList("Fragments", Tag.TAG_COMPOUND);
    }

    public static CompoundTag snapshotBite(ItemStack stack) {
        CompoundTag data = data(stack);
        CompoundTag bite = new CompoundTag();
        ListTag biteFragments = new ListTag();
        int bites = data.getInt("Bites");
        if (bites < 3) {
            ListTag fragments = data.getList("Fragments", Tag.TAG_COMPOUND);
            int remove = (fragments.size() + (3 - bites) - 1) / Math.max(1, 3 - bites);
            for (int i = 0; i < Math.min(3, remove); i++) {
                biteFragments.add(fragments.getCompound(fragments.size() - 1 - i).copy());
            }
        }
        bite.put("Fragments", biteFragments);
        return bite;
    }

    public static void generate(CompoundTag data, List<String> ingredients, long seed) {
        Random random = new Random(seed);
        List<CompoundTag> pieces = new ArrayList<>();
        for (String ingredient : ingredients) {
            if (pieces.size() >= 40) break;
            int count = Math.min(4 + random.nextInt(3), 40 - pieces.size());
            int[] owners = new int[256];
            Arrays.fill(owners, -1);
            List<Integer> frontier = new ArrayList<>();
            for (int region = 0; region < count; region++) {
                int pixel;
                do { pixel = random.nextInt(256); } while (owners[pixel] != -1);
                owners[pixel] = region;
                frontier.add(pixel);
            }
            while (!frontier.isEmpty()) {
                int pixel = frontier.remove(random.nextInt(frontier.size()));
                int x = pixel % 16;
                int y = pixel / 16;
                int[] neighbors = {x > 0 ? pixel - 1 : -1, x < 15 ? pixel + 1 : -1,
                        y > 0 ? pixel - 16 : -1, y < 15 ? pixel + 16 : -1};
                for (int next : neighbors) {
                    if (next >= 0 && owners[next] == -1) {
                        owners[next] = owners[pixel];
                        frontier.add(next);
                    }
                }
            }
            int[] sizes = new int[count];
            for (int owner : owners) sizes[owner]++;
            for (int region = 0; region < count; region++) {
                if (sizes[region] == 0 || sizes[region] >= 4) continue;
                int target = -1;
                for (int pixel = 0; pixel < 256 && target < 0; pixel++) {
                    if (owners[pixel] != region) continue;
                    int x = pixel % 16, y = pixel / 16;
                    int[] neighbors = {x > 0 ? pixel - 1 : -1, x < 15 ? pixel + 1 : -1,
                            y > 0 ? pixel - 16 : -1, y < 15 ? pixel + 16 : -1};
                    for (int next : neighbors) {
                        if (next >= 0 && owners[next] != region) {
                            target = owners[next];
                            break;
                        }
                    }
                }
                if (target >= 0) {
                    for (int pixel = 0; pixel < 256; pixel++) {
                        if (owners[pixel] == region) owners[pixel] = target;
                    }
                    sizes[target] += sizes[region];
                    sizes[region] = 0;
                }
            }
            for (int region = 0; region < count; region++) {
                if (sizes[region] == 0) continue;
                List<Integer> pixels = new ArrayList<>();
                int minU = 16, minV = 16, maxU = 0, maxV = 0;
                for (int pixel = 0; pixel < 256; pixel++) {
                    if (owners[pixel] != region) continue;
                    pixels.add(pixel);
                    minU = Math.min(minU, pixel % 16);
                    minV = Math.min(minV, pixel / 16);
                    maxU = Math.max(maxU, pixel % 16 + 1);
                    maxV = Math.max(maxV, pixel / 16 + 1);
                }
                CompoundTag piece = new CompoundTag();
                piece.putString("Ingredient", ingredient);
                piece.putIntArray("Pixels", pixels);
                piece.putInt("MinU", minU);
                piece.putInt("MinV", minV);
                piece.putInt("MaxU", maxU);
                piece.putInt("MaxV", maxV);
                pieces.add(piece);
            }
        }
        if (!pieces.isEmpty()) {
            List<CompoundTag> originals = new ArrayList<>(pieces);
            while (pieces.size() < 40) {
                pieces.add(originals.get((pieces.size() - originals.size()) % originals.size()).copy());
            }
        }
        Collections.shuffle(pieces, random);
        for (int i = 0; i < pieces.size(); i++) {
            CompoundTag piece = pieces.get(i);
            float x = 0.5F, z = 0.5F;
            float bestDistance = -1F;
            for (int candidate = 0; candidate < 16; candidate++) {
                float cx = 0.26F + random.nextFloat() * 0.48F;
                float cz = 0.26F + random.nextFloat() * 0.48F;
                float distance = Float.MAX_VALUE;
                for (int j = 0; j < i; j++) {
                    float dx = cx - pieces.get(j).getFloat("X");
                    float dz = cz - pieces.get(j).getFloat("Z");
                    distance = Math.min(distance, dx * dx + dz * dz);
                }
                if (distance > bestDistance) {
                    bestDistance = distance;
                    x = cx;
                    z = cz;
                }
            }
            float central = 1F - Math.min(1F, (float)Math.hypot(x - 0.5F, z - 0.5F) / 0.34F);
            piece.putFloat("X", x);
            piece.putFloat("Z", z);
            piece.putFloat("Y", 0.08F + central * 0.08F + random.nextFloat() * 0.025F);
            piece.putFloat("Angle", random.nextFloat() * (float)(Math.PI * 2));
            piece.putFloat("Scale", 0.022F + random.nextFloat() * 0.003F);
        }
        Collections.shuffle(pieces, random);
        ListTag fragments = new ListTag();
        fragments.addAll(pieces);
        data.put("Fragments", fragments);
        data.putLong("Seed", seed);
        data.putInt("Bites", 0);
    }

    public static boolean takeBite(ItemStack stack) {
        CompoundTag root = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag data = root.getCompound("WokDish");
        int bites = Math.min(3, data.getInt("Bites"));
        ListTag fragments = data.getList("Fragments", Tag.TAG_COMPOUND);
        int remove = (fragments.size() + (3 - bites) - 1) / Math.max(1, 3 - bites);
// 数据保护：碎块列表缺失或少于预期时不要越界删除，避免进食崩溃
        for (int i = 0; i < remove && !fragments.isEmpty(); i++) fragments.remove(fragments.size() - 1);
        data.put("Fragments", fragments);
        data.putInt("Bites", Math.min(3, bites + 1));
        root.put("WokDish", data);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
        return bites >= 2;
    }
}
