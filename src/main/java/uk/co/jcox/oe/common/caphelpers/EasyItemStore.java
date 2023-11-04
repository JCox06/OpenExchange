package uk.co.jcox.oe.common.caphelpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class EasyItemStore extends ItemStackHandler{

    /**
     * Java Function Predicate to test an item stack if it valid in this inventory
     * Note that the slot is currently ignored
     */
    private final Predicate<ItemStack> validFunc;

    private final Changeable onChange;

    //
    private int stackLimit = 64;
    private boolean enforceCustomLimit = false;

    public EasyItemStore(@NotNull Predicate<ItemStack> validFunc, Changeable onChange, int size) {
        super(size);
        this.validFunc = validFunc;
        this.onChange = onChange;
    }


    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        if (! enforceCustomLimit) {
            return super.getStackLimit(slot, stack);
        }

        return stackLimit;
    }

    public int getStackLimit() {
        return stackLimit;
    }

    public boolean isEnforceCustomLimit() {
        return enforceCustomLimit;
    }

    public void setStackLimit(int stackLimit) {
        this.stackLimit = stackLimit;
    }

    public void setEnforceCustomLimit(boolean enforceCustomLimit) {
        this.enforceCustomLimit = enforceCustomLimit;
    }

    @Override
    public final boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return validFunc.test(stack);
    }


    @Override
    protected final void onContentsChanged(int slot) {
        this.onChange.markDirty();
    }


    public void dropContents(@Nullable Level level, @NotNull BlockPos pos) {
        if (level == null) {
            return;
        }
        for (int i =0 ; i < this.getSlots(); i++) {
            ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), this.getStackInSlot(i));
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
        }
    }

}