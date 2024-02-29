package uk.co.jcox.oe.common.setup;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import uk.co.jcox.oe.client.FilteredStorageBakedModel;
import uk.co.jcox.oe.client.FilteredStorageUnitBlockEntityRenderer;
import uk.co.jcox.oe.client.FilteredStorageUnitScreen;

public class ClientSetup {

    private ClientSetup() {

    }

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Registration.CONTAINER_FILTERED_STORAGE_UNIT.get(), FilteredStorageUnitScreen::new);
        });
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers registryEvent) {
        registryEvent.registerBlockEntityRenderer(Registration.TILE_FILTERED_STORAGE_UNIT.get(), FilteredStorageUnitBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void bakeModelEvent(ModelEvent.ModifyBakingResult event) {
        for (BlockState blockState: Registration.BLOCK_FILTERED_STORAGE_UNIT.get().getStateDefinition().getPossibleStates()) {
            ModelResourceLocation modelResourceLocation = BlockModelShaper.stateToModelLocation(blockState);
            BakedModel preExistingModel = event.getModels().get(modelResourceLocation);
            if (preExistingModel == null) {
                throw new RuntimeException("Did not find obsidian model in registry");
            } else {
                FilteredStorageBakedModel filteredStorageBakedModel = new FilteredStorageBakedModel(preExistingModel);
                event.getModels().put(modelResourceLocation, filteredStorageBakedModel);
            }
        }
    }
}
