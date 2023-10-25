package uk.co.jcox.oe.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import uk.co.jcox.oe.common.block.entity.FilteredStorageUnitBlockEntity;
import uk.co.jcox.oe.common.setup.Registration;

public class FilteredStorageUnitContainer extends InvMenu{

    public FilteredStorageUnitContainer(int windowID, @NotNull Player player, @NotNull BlockPos pos) {
        super(Registration.CONTAINER_FILTERED_STORAGE_UNIT.get(), Registration.BLOCK_FILTERED_STORAGE_UNIT.get(), windowID, player, pos, null);
    }


    @Override
    protected int layoutEntitySlots(BlockEntity entity) {

        int toReturn = 1;

        if (entity instanceof FilteredStorageUnitBlockEntity storage) {

            //Selection Slot
            addSlot(new SlotItemHandler(storage.getItemSelectInventory(), 0, 172, 37));


           toReturn = uniform(storage.getItemStorageInventory(), 0, 10, 19, 3, 9, 18, 18);
        }

        return toReturn;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }
}
