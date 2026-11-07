package com.fidtest.item;

import com.fidtest.block.TablewareBlock;
import com.fidtest.block.entity.TablewareBlockEntity;
import com.fidtest.block.entity.TablewareBlockEntity.Kind;
import com.fidtest.block.entity.TablewareBlockEntity.Part;
import com.fidtest.registration.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.List;

public class TablewareItem extends Item {
    private final Kind kind;

    public TablewareItem(Properties properties, Kind kind) {
        super(properties);
        this.kind = kind;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        var player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        BlockPos clicked = context.getClickedPos();
        boolean existing = level.getBlockEntity(clicked) instanceof TablewareBlockEntity;
        BlockPlaceContext placement = new BlockPlaceContext(context);
        BlockPos pos = existing ? clicked : placement.getClickedPos();
        existing = level.getBlockEntity(pos) instanceof TablewareBlockEntity;
        if (!TablewareBlock.canInteract(level, pos, player) || level.isOutsideBuildHeight(pos)
                || !player.mayUseItemAt(pos, context.getClickedFace(), context.getItemInHand())) return InteractionResult.FAIL;
        if (!existing && (!placement.canPlace() || !level.getBlockState(pos).canBeReplaced(placement)
                || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP))) return InteractionResult.FAIL;
        var state = ModBlocks.TABLEWARE.get().defaultBlockState();
        TablewareBlockEntity table = existing ? (TablewareBlockEntity) level.getBlockEntity(pos) : new TablewareBlockEntity(pos, state);
        Part part = table.createPart(kind, context.getClickLocation().x - pos.getX(),
                context.getClickLocation().z - pos.getZ(), player.getDirection());
        if (part == null || !level.isUnobstructed(null, Shapes.create(part.bounds()).move(pos.getX(), pos.getY(), pos.getZ()))) return InteractionResult.FAIL;
        if (!level.isClientSide) {
            if (!existing) {
                if (!level.setBlock(pos, state, 3) || !(level.getBlockEntity(pos) instanceof TablewareBlockEntity)) return InteractionResult.FAIL;
                table = (TablewareBlockEntity) level.getBlockEntity(pos);
            }
            table.getParts().add(part);
            table.setChanged();
            if (!player.isCreative()) context.getItemInHand().shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.fidworkblock.tableware_placeable"));
        tooltip.add(Component.translatable("tooltip.fidworkblock." + switch (kind) {
            case PLATE -> "plate_hint";
            case LONG_PLATE -> "long_plate_hint";
            case SAUCER -> "small_plate_hint";
        }));
    }
}
