package uk.co.jcox.oe.common.datagen;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import uk.co.jcox.oe.common.setup.Registration;

import java.util.function.Consumer;

public class OpenExRecipeProvider extends RecipeProvider {

    public OpenExRecipeProvider(PackOutput packedOutput) {
        super(packedOutput);
    }


    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Registration.BLOCK_FILTERED_STORAGE_UNIT.get())
                .pattern("###")
                .pattern("#A#")
                .pattern("#B#")
                .define('#', Items.IRON_INGOT)
                .define('B', Items.CHEST)
                .define('A', Registration.ITEM_ITEM_FILTER.get())
                .unlockedBy("criteria", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.ITEM_ITEM_FILTER.get()));

        builder.save(writer);
    }
}
