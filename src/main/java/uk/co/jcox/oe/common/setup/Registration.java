package uk.co.jcox.oe.common.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import uk.co.jcox.oe.OpenExchange;
import uk.co.jcox.oe.common.block.FilteredStorageUnitBlock;
import uk.co.jcox.oe.common.block.entity.FilteredStorageUnitBlockEntity;
import uk.co.jcox.oe.common.datagen.DataGeneration;
import uk.co.jcox.oe.common.item.ItemProcessingUnit;

public class Registration {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OpenExchange.MODID);

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OpenExchange.MODID);

    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OpenExchange.MODID);

    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OpenExchange.MODID);

    public static void registerAll(IEventBus modEventBus) {

        //Deferred Registers Registration
        ITEMS.register(modEventBus);
        TILES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        BLOCKS.register(modEventBus);

        //Event Registration
        modEventBus.register(DataGeneration.class);
    }

    private Registration() { }


    //IPUs - Item Processing Units and filters
    public static final RegistryObject<Item> ITEM_IPU_STANDARD = ITEMS.register("ipu_standard", () -> new ItemProcessingUnit(new Item.Properties(), 10));
    public static final RegistryObject<Item> ITEM_IPU_FAST = ITEMS.register("ipu_fast", () -> new ItemProcessingUnit(new Item.Properties(), 50));

    public static final RegistryObject<Item> ITEM_ITEM_FILTER = ITEMS.register("item_filter", () -> new Item(new Item.Properties()));


    //Filtered Storage Unit
    public static final RegistryObject<Block> BLOCK_FILTERED_STORAGE_UNIT = BLOCKS.register("filtered_storage_unit", () -> new FilteredStorageUnitBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(1.5f, 8.0f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Item> ITEM_FILTERED_STORAGE_UNIT = ITEMS.register("filtered_storage_unit", () -> new BlockItem(BLOCK_FILTERED_STORAGE_UNIT.get(), new Item.Properties()));

    public static final RegistryObject<BlockEntityType<FilteredStorageUnitBlockEntity>> TILE_FILTERED_STORAGE_UNIT = TILES.register("filtered_stroage_unit", () ->
            BlockEntityType.Builder.of(FilteredStorageUnitBlockEntity::new, BLOCK_FILTERED_STORAGE_UNIT.get()).build(null));


    //Creative Menu Screen
    public static final String TITLE_MAIN_MENU = "inventory." + OpenExchange.MODID + ".main_menu";
    private static final RegistryObject<CreativeModeTab> MAIN_MENU = CREATIVE_TABS.register("main_menu", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable(TITLE_MAIN_MENU))
                    .icon(() -> new ItemStack(ITEM_IPU_STANDARD.get()))
                    .displayItems( (params, out) -> {
                        out.accept(new ItemStack(ITEM_ITEM_FILTER.get()));
                        out.accept(new ItemStack(ITEM_IPU_STANDARD.get()));
                        out.accept(new ItemStack(ITEM_IPU_FAST.get()));
                        out.accept(new ItemStack(ITEM_FILTERED_STORAGE_UNIT.get()));
                    })
                    .build());
}
