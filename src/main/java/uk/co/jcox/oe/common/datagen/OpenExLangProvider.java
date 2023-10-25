package uk.co.jcox.oe.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.item.ItemProcessingUnit;
import uk.co.jcox.oe.common.setup.Registration;

public class OpenExLangProvider extends LanguageProvider {

    public OpenExLangProvider(PackOutput output, String locale) {
        super(output, OpenExchange.MODID, locale);
    }


    @Override
    protected void addTranslations() {

        //Main Menu
        add(Registration.TITLE_MAIN_MENU, "OpenEX");

        //Items
        add(Registration.ITEM_IPU_STANDARD.get(), "Standard IPU");
        add(Registration.ITEM_IPU_FAST.get(), "Fast IPU");
        add(ItemProcessingUnit.IPU_NAME_COMPONENT, "Item Processing Unit");
        add(ItemProcessingUnit.EXTRA_POWER_CONSUMPTION_COMPONENT, "Requires %1$s flux");
        add(Registration.ITEM_ITEM_FILTER.get(), "Item Filter");
        add(Registration.ITEM_FILTERED_STORAGE_UNIT.get(), "Filtered Storage Unit");

    }
}
