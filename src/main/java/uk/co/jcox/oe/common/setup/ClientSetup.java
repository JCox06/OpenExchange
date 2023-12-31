package uk.co.jcox.oe.common.setup;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
}
