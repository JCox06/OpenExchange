package uk.co.jcox.oe.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelDataManager;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.caphelpers.EasyItemStore;
import uk.co.jcox.oe.common.caphelpers.ItemCapUtils;
import uk.co.jcox.oe.common.container.FilteredStorageUnitContainer;
import uk.co.jcox.oe.common.setup.Registration;

public class FilteredStorageUnitBlockEntity extends BlockEntity implements MenuProvider {

    public static final String MENU_DISPLAY_COMPONENT = "menu." + OpenExchange.MODID + ".filtered_storage_unit";


    public static final int ITEM_SELECT_INV_SIZE = 1;
    public static final int ITEM_SELECT_STACK_SIZE = 1;

    public static final int ITEM_STORE_INV_SIZE = 45;

    public static final String NBT_SELECT_INV = "itemSelectInv";
    public static final String NBT_STORE_INV = "itemStorageInv";

    //Model property for baked model
    public static final ModelProperty<Item> FILTER = new ModelProperty<>();

    private final EasyItemStore itemSelectInventory = new EasyItemStore((ItemStack stack) -> true, () -> {
        this.setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }, ITEM_SELECT_INV_SIZE) {

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            //Once an item is in the select inventory
            //Lock it in place to prevent accidental removal
            //The only way to remove this is by breaking the block and replacing
            if (! getStackInSlot(0).isEmpty()) {
                return ItemStack.EMPTY;
            }
            return super.extractItem(slot, amount, simulate);
        }
    };
    private final EasyItemStore itemStorageInventory = new EasyItemStore((ItemStack stack) -> stack.is(itemSelectInventory.getStackInSlot(0).getItem()), this::setChanged, ITEM_STORE_INV_SIZE);

    private final LazyOptional<IItemHandler> itemSelectItemHandler = LazyOptional.of(() -> itemSelectInventory);
    private final LazyOptional<IItemHandler> itemStoreItemHandler = LazyOptional.of(() -> itemStorageInventory);

    public FilteredStorageUnitBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Registration.TILE_FILTERED_STORAGE_UNIT.get(), blockPos, blockState);
        this.itemSelectInventory.setStackLimit(ITEM_SELECT_STACK_SIZE);
        this.itemSelectInventory.setEnforceCustomLimit(true);
    }


    public void tickServer() {
        if (level.getGameTime() % 8 != 0) {
            return;
        }

        BlockEntity below = level.getBlockEntity(getBlockPos().below());

        if (below == null) {
            return;
        }

        below.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).map(handler -> {
            int nextIndex = ItemCapUtils.getNextItemIndex(itemStorageInventory, 0);
            if (nextIndex == -1) {
                return false;
            }

            ItemStack toTransfer = itemStorageInventory.getStackInSlot(nextIndex);
            ItemCapUtils.attemptItemTransfer(handler, toTransfer);
            return true;
        });
    }



    public ItemStack getFilteredItem() {
        return this.itemSelectInventory.getStackInSlot(0);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        saveInventorySelect(compound);
        compound.put(NBT_STORE_INV, itemStorageInventory.serializeNBT());
    }


    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);

        loadInventorySelect(compound);

        if (compound.contains(NBT_STORE_INV)) {
            this.itemStorageInventory.deserializeNBT(compound.getCompound(NBT_STORE_INV));
        }
    }


    //This pair of methods is called when a client receives a new chunk it has not seen before
    //This has to be used, as when the client FIRST joins the world, it needs the item select data

    //Called on the server, prepares data for the client
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = super.getUpdateTag();
        saveInventorySelect(compound);
        return compound;
    }


    //Called on the client, unpacks data sent from the server
    @Override
    public void handleUpdateTag(CompoundTag compound) {
        if (compound != null) {
            loadInventorySelect(compound);
        }
    }


    //Following pair of methods will be called during a block update


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag compound = pkt.getTag();
        handleUpdateTag(compound);
        requestModelDataUpdate();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    private void saveInventorySelect(@NotNull CompoundTag compound) {
        compound.put(NBT_SELECT_INV, itemSelectInventory.serializeNBT());
    }

    private void loadInventorySelect(@NotNull CompoundTag compound) {
        if (compound.contains(NBT_SELECT_INV)) {
            this.itemSelectInventory.deserializeNBT(compound.getCompound(NBT_SELECT_INV));
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
    public @NotNull ModelData getModelData() {
        return ModelData.builder()
                .with(FILTER, itemSelectInventory.getStackInSlot(0).getItem())
                .build();
    }

    public void dropInvContents() {
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
