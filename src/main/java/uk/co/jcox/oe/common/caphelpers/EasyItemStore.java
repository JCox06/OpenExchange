package uk.co.jcox.oe.common.caphelpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
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

    public EasyItemStore(@NotNull Predicate<ItemStack> validFunc, Changeable onChange, int size) {
        super(size);
        this.validFunc = validFunc;
        this.onChange = onChange;
    }

    @Override
    public final boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return validFunc.test(stack);
    }


    @Override
    protected final void onContentsChanged(int slot) {
        this.onChange.invoke();
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


    @NotNull
    public static CombinedInvWrapper observableMerged(@NotNull ItemStackHandler ...handler) {
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
}