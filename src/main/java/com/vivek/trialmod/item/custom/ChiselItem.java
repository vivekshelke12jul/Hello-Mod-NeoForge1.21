package com.vivek.trialmod.item.custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ChiselItem extends Item{

    private static final Map<Block, Block> CHISEL_MAP = Map.of(
            Blocks.STONE, Blocks.STONE_BRICKS,
            Blocks.STONE_BRICKS, Blocks.STONE_BRICK_WALL,
            Blocks.STONE_BRICK_WALL, Blocks.SMOOTH_STONE,
            Blocks.SMOOTH_STONE, Blocks.COBBLESTONE,
            Blocks.COBBLESTONE, Blocks.STONE
    );

    public ChiselItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        Block clickedBlock = blockState.getBlock();

        if(CHISEL_MAP.containsKey(clickedBlock)){
            if(!level.isClientSide()){
                Block transformIntoBlock = CHISEL_MAP.get(clickedBlock);
                level.setBlockAndUpdate(blockPos, transformIntoBlock.defaultBlockState());

                Consumer<Item> itemConsumer = (item) -> {
                    context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND);
                };
                context.getItemInHand().hurtAndBreak(1, (ServerLevel)level, context.getPlayer(), itemConsumer);

                level.playSound(null, blockPos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()){
            tooltipComponents.add(Component.translatable("tooltip.vivekmod.chisel"));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.vivekmod.chisel.shift_down"));
        }
    }
}
