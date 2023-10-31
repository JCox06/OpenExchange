package uk.co.jcox.oe.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.caphelpers.EasyItemStore;
import uk.co.jcox.oe.common.container.FilteredStorageUnitContainer;
import uk.co.jcox.oe.common.setup.Registration;

public class FilteredStorageUnitBlockEntity extends BlockEntity implements MenuProvider {

    public static final String MENU_DISPLAY_COMPONENT = "menu." + OpenExchange.MODID + ".filtered_storage_unit";


    public static final int ITEM_SELECT_INV_SIZE = 1;
    public static final int ITEM_SELECT_STACK_SIZE = 1;

    public static final int ITEM_STORE_INV_SIZE = 45;

    public static final String NBT_SELECT_INV = "itemSelectInv";
    public static final String NBT_STORE_INV = "itemStorageInv";

    private final EasyItemStore itemSelectInventory = new EasyItemStore((ItemStack stack) -> true, this::setChanged, ITEM_SELECT_INV_SIZE);
    private final EasyItemStore itemStorageInventory = new EasyItemStore((ItemStack stack) -> stack.is(itemSelectInventory.getStackInSlot(0).getItem()), this::setChanged, ITEM_STORE_INV_SIZE);

    private final LazyOptional<IItemHandler> itemSelectItemHandler = LazyOptional.of(() -> itemSelectInventory);
    private final LazyOptional<IItemHandler> itemStoreItemHandler = LazyOptional.of(() -> itemStorageInventory);

    public FilteredStorageUnitBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Registration.TILE_FILTERED_STORAGE_UNIT.get(), blockPos, blockState);
        this.itemSelectInventory.setStackLimit(ITEM_SELECT_STACK_SIZE);
        this.itemSelectInventory.setEnforceCustomLimit(true);
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put(NBT_SELECT_INV, itemSelectInventory.serializeNBT());
        compound.put(NBT_STORE_INV, itemStorageInventory.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);

        if (compound.contains(NBT_SELECT_INV)) {
            this.itemSelectInventory.deserializeNBT(compound.getCompound(NBT_SELECT_INV));
        }

        if (compound.contains(NBT_STORE_INV)) {
            this.itemStorageInventory.deserializeNBT(compound.getCompound(NBT_STORE_INV));
        }
    }

    @Override
    public void invalidateCaps() {
        this.itemStoreItemHandler.invalidate();
        this.itemSelectItemHandler.invalidate();
    }


    public EasyItemStore getItemSelectInventory() {
        return itemSelectInventory;
    }

    public EasyItemStore getItemStorageInventory() {
        return itemStorageInventory;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.itemSelectInventory.dropContents(level, getBlockPos());
        this.itemStorageInventory.dropContents(level, getBlockPos());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {

            if (side == Direction.UP || side == Direction.DOWN) {
                return itemStoreItemHandler.cast();
            }

            //If no side is given, return just the main store
            if (side == null) {
                return itemStoreItemHandler.cast();
            }
        }

        return super.getCapability(cap, side);
    }



    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(MENU_DISPLAY_COMPONENT);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int winId, @NotNull Inventory inv, @NotNull Player player) {
        return new FilteredStorageUnitContainer(winId, player, getBlockPos());
    }
}
