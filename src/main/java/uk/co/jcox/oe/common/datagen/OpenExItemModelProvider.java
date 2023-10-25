package uk.co.jcox.oe.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.setup.Registration;

public class OpenExItemModelProvider extends ItemModelProvider {

    public OpenExItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, OpenExchange.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        //Basic Items
        basicItem(Registration.ITEM_IPU_STANDARD.get());
        basicItem(Registration.ITEM_IPU_FAST.get());
        basicItem(Registration.ITEM_ITEM_FILTER.get());
    }
}
