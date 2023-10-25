package uk.co.jcox.oe.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import uk.co.jcox.oe.OpenExchange;

public class DataGeneration {


    @SubscribeEvent
    public static void gatherDataListener(GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        final PackOutput packOutput = generator.getPackOutput();

        OpenExchange.LOGGER.info("Starting Generators for CLIENT ASSETS");
        generator.addProvider(event.includeClient(), new OpenExBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new OpenExItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new OpenExLangProvider(packOutput, "en_us"));
    }
}
