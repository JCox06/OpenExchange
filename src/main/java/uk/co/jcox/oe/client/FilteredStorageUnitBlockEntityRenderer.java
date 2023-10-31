package uk.co.jcox.oe.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import uk.co.jcox.oe.common.block.FilteredStorageUnitBlock;
import uk.co.jcox.oe.common.block.entity.FilteredStorageUnitBlockEntity;

public class FilteredStorageUnitBlockEntityRenderer implements BlockEntityRenderer<FilteredStorageUnitBlockEntity> {

    private final ItemRenderer itemDispatcher;

    public FilteredStorageUnitBlockEntityRenderer(BlockEntityRendererProvider.Context renderingContext) {
        this.itemDispatcher = renderingContext.getItemRenderer();
    }

    @Override
    public void render(FilteredStorageUnitBlockEntity entity, float partialTicks, PoseStack pose, MultiBufferSource buffers, int packedLight, int overlay) {

        final ItemStack filteredItem = entity.getFilteredItem();
        if (filteredItem.isEmpty()) {
            return;
        }

        Direction direction = entity.getBlockState().getValue(FilteredStorageUnitBlock.FACING);
        pose.pushPose();
        pose.translate(0.5f, 0.5f, 0.5f); //Translate to centre
        pose.translate(-0.5f * direction.getStepX(), 0.0f, -0.5f * direction.getStepZ());

        if (direction.equals(Direction.WEST) || direction.equals(Direction.EAST)) {
            pose.mulPose(new Quaternionf().rotateY((float) (Math.PI / 2)));
        }
        pose.scale(0.5f, 0.5f, 0.5f);

        this.itemDispatcher.renderStatic(filteredItem, ItemDisplayContext.FIXED, 15728850, OverlayTexture.NO_OVERLAY, pose, buffers, entity.getLevel(), (int) entity.getBlockPos().asLong());
        pose.popPose();
    }
}
