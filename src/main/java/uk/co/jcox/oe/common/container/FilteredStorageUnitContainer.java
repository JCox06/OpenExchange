package uk.co.jcox.oe.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import uk.co.jcox.oe.common.block.entity.FilteredStorageUnitBlockEntity;
import uk.co.jcox.oe.common.setup.Registration;

public class FilteredStorageUnitContainer extends AbstractContainerMenu {

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
        uniform(entity.getItemStorageInventory(), 0, 8, 36, 5, 9, 18, 18);

        addSlot(new SlotItemHandler(entity.getItemSelectInventory(), 0, 152, 11));

        IItemHandler invWrapper = new InvWrapper(player.getInventory());
        int startingIndex = 0;
        startingIndex = uniform(invWrapper, 0, 8, 198, 1, 9, 18, 0);
        uniform(invWrapper, startingIndex, 8, 140, 3, 9, 18, 18);
    }


    private int uniform(@NotNull IItemHandler handler, int index, int startX, int startY, int rows, int cols, int dx, int dy) {

        final int originalX = startX;

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                addSlot(new SlotItemHandler(handler, index, startX, startY));
                startX += dx;
                index++;
            }
            startY += dy;
            startX = originalX;
        }
        return index;
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.BLOCK_FILTERED_STORAGE_UNIT.get());
    }
}
