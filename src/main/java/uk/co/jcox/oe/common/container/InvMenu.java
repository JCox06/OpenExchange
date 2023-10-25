package uk.co.jcox.oe.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.ValueRange;

/**
 * Extend this class as an alternative to AbstractContainerMenu
 * When the constructor is called it calls the method slotLayouts();
 * slotLayouts() calls layoutEntitySlots then layoutPlayerSlots
 * in that order
 */
public abstract class InvMenu extends AbstractContainerMenu {


    private final Player player;
    private final BlockPos blockPos;
    private final ContainerData dataAccess;
    private final Block relatedBlock;

    private ValueRange ownerRange;
    private ValueRange playerRange;


    protected InvMenu(@NotNull MenuType<?> type, @NotNull Block relatedBlock, int windowID, @NotNull Player player, @NotNull BlockPos pos, @Nullable ContainerData dataAccess) {
        super(type, windowID);
        this.player = player;
        this.blockPos = pos;
        this.dataAccess= dataAccess;
        this.relatedBlock = relatedBlock;

        if (dataAccess != null) {
            addDataSlots(dataAccess);
        }

        slotLayouts();
    }


    private void slotLayouts() {
        int startGroupedIndex = 0;
        int endEntityIndex = layoutEntitySlots(player.level().getBlockEntity(pos())) + startGroupedIndex;
        this.ownerRange = ValueRange.of(startGroupedIndex, endEntityIndex);
        int startPlayerIndex = endEntityIndex + 1;
        int endPlayerIndex = layoutPlayerSlots(new InvWrapper(player.getInventory())) + startPlayerIndex;
        this.playerRange = ValueRange.of(startPlayerIndex, endPlayerIndex);
    }


    /**
     * Override this method to add slots to the inventory screen belonging to this container owner only
     * The starting index should always be 0.
     * @param entity The block entity at the blockPos of this container menu
     * @return the last index used (you can pass the return value from the uniform method)
     */
    protected abstract int layoutEntitySlots( BlockEntity entity);


    /**
     * Override this method to add slots to the inventory screen belonging to the player main inventory and hotbar only
     * If the method is not overriden, then default slots will be added with standard dimensions
     * @param playerInv a new InvWrapper representing the player inventory
     * @return the last index used (you can pass the return from uniform method)
     */
    protected int layoutPlayerSlots(IItemHandler playerInv) {
        int startingIndex = 0;
        startingIndex = uniform(playerInv, 0, 8, 142, 1, 9, 18, 0);
        return uniform(playerInv, startingIndex, 8, 84, 3, 9, 18, 18);
    }


    /**
     * Lays slots out uniformly in a square or rectangle
     * For example the player inventory has a uniform layout
     * that this method allows easy generation for.
     *
     * @param handler The item handler you want these slots to be part of
     * @param index The starting index of the first slot added
     * @param startX The X Position of the first slot
     * @param startY The Y position of the first slot
     * @param rows The number of rows to add (dimensionsY)
     * @param cols The number of columns to add (dimensionsX)
     * @param dx The change in position on each iteration of the x position
     * @param dy The change in position of each iteration of the y position
     * @return That last index that was used
     */
    protected int uniform(@NotNull IItemHandler handler, int index, int startX, int startY, int rows, int cols, int dx, int dy) {

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
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), blockPos), player, relatedBlock);
    }


    protected Block getRelated() {
        return relatedBlock;
    }

    protected Player player() {
        return player;
    }


    protected BlockPos pos() {
        return blockPos;
    }

    protected ContainerData dataAccess() {
        return dataAccess;
    }


    /**
     * Allows you to see if a container index slot is part of the owner's inventory
     * @return the ranges of indexes for the owners inventory
     */
    public ValueRange getOwnerRange() {
        return ownerRange;
    }

    /**
     * Allows you to see if a container index slot is part of the player's inventory
     * @return the ranges of indexes for the players inventory
     */
    public ValueRange getPlayerRange() {
        return playerRange;
    }

}