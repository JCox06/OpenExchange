package uk.co.jcox.oe.common.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.setup.Registration;

import java.util.function.Function;

public class OpenExBlockStateProvider extends BlockStateProvider {

    public OpenExBlockStateProvider(PackOutput packedOutput, ExistingFileHelper exFileHelper) {
        super(packedOutput, OpenExchange.MODID, exFileHelper);
    }


    @Override
    protected void registerStatesAndModels() {

        horizontalRot(Registration.BLOCK_FILTERED_STORAGE_UNIT, (blockState -> {
            final String loc = Registration.BLOCK_FILTERED_STORAGE_UNIT.getId().getPath();
            return models().getBuilder(loc)
                    .parent(models().getExistingFile(mcLoc("block/orientable")))
                    .texture("front", modLoc("block/" + loc + "_front"))
                    .texture("side", modLoc("block/" + loc + "_side"))
                    .texture("top", modLoc("block/" + loc + "_top"));
        }));
    }



    private void horizontalRot(RegistryObject<Block> blockRegistryObject, Function<BlockState, ModelFile> prepareModelFile) {
        this.getVariantBuilder(blockRegistryObject.get())
                .forAllStates( (BlockState blockState)  -> {
                   final Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
                   final ModelFile file = prepareModelFile.apply(blockState);

                   return ConfiguredModel.builder()
                           .modelFile(file)
                           .rotationY((int) direction.toYRot())
                           .build();
                });
    }
}
