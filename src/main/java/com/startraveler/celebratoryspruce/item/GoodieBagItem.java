package com.startraveler.celebratoryspruce.item;

import com.startraveler.celebratoryspruce.ModDataComponentTypes;
import com.startraveler.celebratoryspruce.item.component.GoodieBagContents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class GoodieBagItem extends Item {
    public GoodieBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);


        if (!level.isClientSide() && stack.get(ModDataComponentTypes.GOODIE_BAG_CONTENTS.get()) instanceof GoodieBagContents goodieBagContents) {
            boolean hasNameAndNameDoesNotMatchPlayer = stack.get(DataComponents.CUSTOM_NAME) instanceof Component customName && customName.tryCollapseToString() instanceof String customNameString && !customNameString.equals(
                    player.getPlainTextName());

            if (!hasNameAndNameDoesNotMatchPlayer) {
                boolean anySucceeded = false;
                ArrayList<ItemStack> remainders = new ArrayList<>();
                for (ItemStack gift : goodieBagContents.copyStacks()) {
                    boolean itemSuccessfullyAdded = player.addItem(gift);
                    anySucceeded |= itemSuccessfullyAdded;
                    if (!itemSuccessfullyAdded) {
                        remainders.add(gift);
                    }
                }
                if (anySucceeded) {

                    GoodieBagContents newGoodieBagContents = remainders.isEmpty() ? null : new GoodieBagContents(
                            remainders,
                            goodieBagContents.maxSize()
                    );
                    stack.set(ModDataComponentTypes.GOODIE_BAG_CONTENTS.get(), newGoodieBagContents);
                    level.playSound(
                            null,
                            player.blockPosition(),
                            SoundEvents.BUNDLE_DROP_CONTENTS,
                            SoundSource.PLAYERS,
                            0.8F,
                            0.8F + level.getRandom().nextFloat() * 0.4F
                    );
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public boolean overrideStackedOnOther(@NotNull ItemStack stack, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player) {

        ItemStack inSlot = slot.getItem();
        if (stack.getCount() != 1 || inSlot.isEmpty()) {
            return false;
        } else {
            int size = stack.getOrDefault(ModDataComponentTypes.DEFAULT_GOODIE_BAG_SIZE, 1);

            GoodieBagContents contents = stack.getOrDefault(
                    ModDataComponentTypes.GOODIE_BAG_CONTENTS.get(),
                    new GoodieBagContents(size)
            );

            GoodieBagContents newContents = contents.withAdditionalOrNull(inSlot.copy());

            Level level = player.level();
            if (newContents != null) {
                stack.set(ModDataComponentTypes.GOODIE_BAG_CONTENTS.get(), newContents);

                inSlot.setCount(0);
                level.playSound(
                        null,
                        player.blockPosition(),
                        SoundEvents.BUNDLE_INSERT,
                        SoundSource.PLAYERS,
                        0.8F,
                        0.8F + level.getRandom().nextFloat() * 0.4F
                );
                this.broadcastChangesOnContainerMenu(player);
                return true;
            } else {
                level.playSound(
                        null,
                        player.blockPosition(),
                        SoundEvents.BUNDLE_INSERT_FAIL,
                        SoundSource.PLAYERS,
                        0.8F,
                        0.8F + level.getRandom().nextFloat() * 0.4F
                );
            }
        }
        return super.overrideStackedOnOther(stack, slot, clickAction, player);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag) {
        if (stack.getCustomName() instanceof Component customName) {
            tooltipAdder.accept(Component.translatable("item.celebratoryspruce.goodie_bag.for").append(customName));
        }
        if (stack.get(ModDataComponentTypes.GOODIE_BAG_CONTENTS.get()) instanceof GoodieBagContents contents) {
            int n = contents.stacks().size();
            if (n == 1) {
                tooltipAdder.accept(Component.translatable("item.celebratoryspruce.goodie_bag.contains_one"));
            } else {
                tooltipAdder.accept(Component.translatable("item.celebratoryspruce.goodie_bag.contains_n", n));

            }
        } else {
            tooltipAdder.accept(Component.translatable("item.celebratoryspruce.goodie_bag.empty"));
        }
    }

    private void broadcastChangesOnContainerMenu(Player player) {
        if (player.containerMenu instanceof AbstractContainerMenu abstractContainerMenu) {
            abstractContainerMenu.slotsChanged(player.getInventory());
        }
    }
}
