package uk.co.jcox.oe.client;

import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import uk.co.jcox.oe.common.block.FilteredStorageUnitBlock;
import uk.co.jcox.oe.common.block.entity.FilteredStorageUnitBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class FilteredStorageBakedModel implements IDynamicBakedModel {


    private final BakedModel baseModel;

    public FilteredStorageBakedModel(BakedModel baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState baseStateProperties, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        quads.addAll(baseModel.getQuads(baseStateProperties, side, rand, extraData, renderType));

        if (extraData.has(FilteredStorageUnitBlockEntity.FILTER)) {
            Item selectedItem = extraData.get(FilteredStorageUnitBlockEntity.FILTER);
            if (selectedItem != Items.AIR) {
                Minecraft minecraft = Minecraft.getInstance();
                BakedModel selectedItemBakedModel = minecraft.getItemRenderer().getItemModelShaper().getItemModel(selectedItem);
                //Render with block state of null as it is an item
                //Origin of first quad is at North West side
                List<BakedQuad> selectedItemBakedQuads = new ArrayList<>(selectedItemBakedModel.getQuads(null, side, rand, extraData, renderType));
                quads.addAll(selectedItemBakedQuads);
            }
        }
        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return null;
    }
}
