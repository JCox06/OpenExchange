package uk.co.jcox.oe.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import uk.co.jcox.oe.common.block.entity.FilteredStorageUnitBlockEntity;
import uk.co.jcox.oe.common.setup.Registration;

public class FilteredStorageUnitContainer extends AbstractContainerMenu {

    public static final int ENTITY_STORAGE_START = 0;
    public static final int ENTITY_STORAGE_END = ENTITY_STORAGE_START + FilteredStorageUnitBlockEntity.ITEM_STORE_INV_SIZE;

    public static final int ENTITY_SELECT_START = ENTITY_STORAGE_END;
    public static final int ENTITY_SELECT_END = ENTITY_SELECT_START + FilteredStorageUnitBlockEntity.ITEM_SELECT_INV_SIZE;

    public static final int PLAYER_START = ENTITY_SELECT_END;
    public static final int PLAYER_END = ENTITY_SELECT_END + 36;


    private final Player player;
    private final BlockPos pos;


    public FilteredStorageUnitContainer(int windowId, Player player, BlockPos pos) {
        super(Registration.CONTAINER_FILTERED_STORAGE_UNIT.get(), windowId);
        this.player = player;
        this.pos = pos;
        layoutSlots();
    }


    private void layoutSlots() {

        FilteredStorageUnitBlockEntity entity = (FilteredStorageUnitBlockEntity) player.level().getBlockEntity(pos);
        uniform(entity.getItemStorageInventory(), 0, 8, 36, 5, 9, 18);

        addSlot(new SlotItemHandler(entity.getItemSelectInventory(), 0, 152, 11));

        IItemHandler invWrapper = new InvWrapper(player.getInventory());
        int startingIndex;
        startingIndex = uniform(invWrapper, 0, 8, 198, 1, 9, 18);
        uniform(invWrapper, startingIndex, 8, 140, 3, 9, 18);
    }


    private int uniform(@NotNull IItemHandler handler, int index, int startX, int startY, int rows, int cols, int delta) {

        final int originalX = startX;

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                addSlot(new SlotItemHandler(handler, index, startX, startY));
                startX += delta;
                index++;
            }
            startY += delta;
            startX = originalX;
        }
        return index;
    }


    //This is a bit confusing but...
    //QuickMoveItemStack will not respect ItemStackHandler#getStackLimit()
    //Therefore this method also has to validate and ensure the stack size for the select slot is not greater than 1
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = getSlot(slotIndex);

        if (!slot.hasItem()) {
            return stack;
        }

        ItemStack item = slot.getItem();
        stack = item.copy();

        //Check to see if the index is the storage inventory
        //If so move it to the player
        if (slotIndex < ENTITY_STORAGE_END) {
            if (! this.moveItemStackTo(item, PLAYER_START, PLAYER_END, false)) {
                return ItemStack.EMPTY;
            }
        }


        //Check to see if the index is in the select inventory
        //If so move it to the player
        if (slotIndex >= ENTITY_SELECT_START && slotIndex < ENTITY_SELECT_END) {
            if (! this.moveItemStackTo(item, PLAYER_START, PLAYER_END, false)) {
                return ItemStack.EMPTY;
            }
        }

        //Check to see if item is from player inventory
        if (slotIndex >= PLAYER_START && slotIndex < PLAYER_END) {

            //If select slot is empty then move item to select
            if (! getSlot(ENTITY_SELECT_START).hasItem()) {

                //As the stack is limited to one we have to check the stack size of the item
                int stackSize = item.getCount();
                if (stackSize > 1) {
                    item.shrink(1);
                    this.moveItemStackTo(new ItemStack(item.getItem()), ENTITY_SELECT_START, ENTITY_SELECT_END, false);
                } else {
                    //Else if the stack is already 1, just send the whole stack over
                    if (this.moveItemStackTo(item, ENTITY_SELECT_START, ENTITY_SELECT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                //If select slot is full, them ove item to storage
                if (! this.moveItemStackTo(item, ENTITY_STORAGE_START, ENTITY_STORAGE_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        return stack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.BLOCK_FILTERED_STORAGE_UNIT.get());
    }
}
