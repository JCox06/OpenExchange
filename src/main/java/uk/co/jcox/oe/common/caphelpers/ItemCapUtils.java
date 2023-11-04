package uk.co.jcox.oe.common.caphelpers;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

public class ItemCapUtils {


    /**
     * Create new read only merged item handler
     */
    @NotNull
    public static CombinedInvWrapper observableMerged(@NotNull ItemStackHandler...handler) {
        return new CombinedInvWrapper(handler) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }
        };
    }


    /**
     *
     * @param handler The inventory to query
     * @param index The starting index of search
     * @return -1 if nothing is found, or if there is an item, then it will return the index slot of that item
     */
    public static int getNextItemIndex(@NotNull IItemHandler handler, int index) {
        if (index >= handler.getSlots() - 1) {
            return -1;
        }

        ItemStack item = handler.getStackInSlot(index);
        if (item.isEmpty()) {
            return getNextItemIndex(handler, ++index);
        }
        return index;
    }


    public static int getNextAvailableIndex(@NotNull IItemHandler handler, @NotNull Item itemCheck, int startingIndex) {
        if (startingIndex > handler.getSlots() - 1) {
            return -1;
        }

        ItemStack item = handler.getStackInSlot(startingIndex);

        if (item.is(itemCheck) && item.getCount() < Math.min(handler.getSlotLimit(startingIndex), item.getMaxStackSize())) {
            return startingIndex;
        }

        if (item.isEmpty()) {
            return startingIndex;
        }

        return getNextAvailableIndex(handler, itemCheck, ++startingIndex);
    }



    //Transfers one item from a stack to another container
    public static boolean attemptItemTransfer(IItemHandler handler, ItemStack stack) {

        int nextIndex = getNextAvailableIndex(handler, stack.getItem(), 0);

        if (nextIndex == -1) {
            return false;
        }

        handler.insertItem(nextIndex, new ItemStack(stack.getItem()), false);
        stack.shrink(1);
        return true;
    }
}
