package uk.co.jcox.oe.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.setup.Registration;

import java.util.concurrent.CompletableFuture;

public class OpenExBlockTagsProvider extends BlockTagsProvider {

    public OpenExBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, OpenExchange.MODID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.BLOCK_FILTERED_STORAGE_UNIT.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(Registration.BLOCK_FILTERED_STORAGE_UNIT.get());
    }
}
