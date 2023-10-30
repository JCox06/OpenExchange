package uk.co.jcox.oe.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.container.FilteredStorageUnitContainer;

public class FilteredStorageUnitScreen extends AbstractContainerScreen<FilteredStorageUnitContainer> {


    private static final ResourceLocation TEXTURE = new ResourceLocation(OpenExchange.MODID, "textures/gui/container/filtered_storage_unit.png");

    public FilteredStorageUnitScreen(FilteredStorageUnitContainer container, Inventory inventory, Component component) {
        super(container, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelX = 7;
        this.inventoryLabelY = 127;
    }


    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {


        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }


    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
