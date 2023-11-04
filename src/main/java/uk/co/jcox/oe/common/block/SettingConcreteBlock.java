package uk.co.jcox.oe.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import uk.co.jcox.oe.common.setup.Registration;

public class SettingConcreteBlock extends Block {


    public SettingConcreteBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @SuppressWarnings("deprecated")
    @Override
    public void randomTick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource random) {
        if (canDry(pLevel, pPos)) {
            if (random.nextInt(100) == 0) {
                setStateDry(pLevel, pPos);
            }
        }
    }

    private boolean canDry(Level level, BlockPos pos) {
        if (level.isRainingAt(pos.above())) {
            return false;
        }
        return true;
    }


    private void setStateDry(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, Registration.BLOCK_WHITE_CONCRETE.get().defaultBlockState());
    }


    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(blockState, new Vec3(0.8f, 0.5f, 0.8f));
    }
}
