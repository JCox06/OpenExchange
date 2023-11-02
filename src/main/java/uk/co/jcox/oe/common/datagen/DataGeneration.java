package uk.co.jcox.oe.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import uk.co.jcox.oe.OpenExchange;

import java.util.Collections;
import java.util.List;

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

        OpenExchange.LOGGER.info("Starting Generators for SERVER DATA");
        BlockTagsProvider blockTagsProvider = new OpenExBlockTagsProvider(packOutput, event.getLookupProvider(), existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(OpenExBlockLootSubProvider::new, LootContextParamSets.BLOCK))));
        generator.addProvider(event.includeServer(), new OpenExRecipeProvider(packOutput));
    }
}
