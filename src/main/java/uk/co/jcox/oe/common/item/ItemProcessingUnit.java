package uk.co.jcox.oe.common.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.jcox.oe.OpenExchange;

import java.util.List;

public class ItemProcessingUnit extends Item {

    private final int powerConsumption;
    public static final String IPU_NAME_COMPONENT = "tooltip." + OpenExchange.MODID + ".ipu_name";
    public static final String EXTRA_POWER_CONSUMPTION_COMPONENT = "tooltip." + OpenExchange.MODID + "ipu_power_req";

    public ItemProcessingUnit(Properties p_41383_, int power) {
        super(p_41383_);
        this.powerConsumption = power;
    }


    public int getPowerConsumption() {
        return powerConsumption;
    }


    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> components, @NotNull TooltipFlag flag) {
        components.add(Component.translatable(IPU_NAME_COMPONENT));
        if (Screen.hasShiftDown()) {
            components.add(Component.translatable(EXTRA_POWER_CONSUMPTION_COMPONENT, powerConsumption));
        }
    }
}
