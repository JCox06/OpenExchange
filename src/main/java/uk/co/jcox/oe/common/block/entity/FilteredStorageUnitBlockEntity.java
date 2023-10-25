package uk.co.jcox.oe.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uk.co.jcox.oe.common.setup.Registration;

public class FilteredStorageUnitBlockEntity extends BlockEntity {


    public FilteredStorageUnitBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Registration.TILE_FILTERED_STORAGE_UNIT.get(), blockPos, blockState);
    }



}
