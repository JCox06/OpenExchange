package uk.co.jcox.oe.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
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

        //Block Items
        blockParent(Registration.ITEM_FILTERED_STORAGE_UNIT);
        blockParent(Registration.ITEM_WHITE_SETTING_CONCRETE);
        blockParent(Registration.ITEM_WHITE_CONCRETE);
    }


    private void blockParent(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(), modLoc("block/" + item.getId().getPath()));
    }

}
