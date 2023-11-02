package uk.co.jcox.oe.common.datagen;

import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.setup.Registration;

import java.util.Map;
import java.util.stream.Collectors;

public class OpenExBlockLootSubProvider extends VanillaBlockLoot {


    @Override
    protected void generate() {
        this.dropSelf(Registration.BLOCK_FILTERED_STORAGE_UNIT.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(OpenExchange.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
