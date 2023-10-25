package uk.co.jcox.oe;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import uk.co.jcox.oe.common.setup.Registration;

@Mod(OpenExchange.MODID)
public class OpenExchange {

    public static final String MODID = "openexchange";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public OpenExchange() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //Registration
        Registration.registerAll(modEventBus);
    }
}
