package com.startraveler.celebratoryspruce;

import com.startraveler.celebratoryspruce.client.ItemRenderingBlockEntityRenderer;
import com.startraveler.celebratoryspruce.datagen.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = CelebratorySpruce.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = CelebratorySpruce.MODID, value = Dist.CLIENT)
public class CelebratorySpruceClient {

    public static final int SPRUCE_LEAVES_TINT = -10380959;

    public CelebratorySpruceClient(ModContainer container) {
        @NotNull IEventBus modBus = Objects.requireNonNull(container.getEventBus());

        modBus.addListener(CelebratorySpruceClient::gatherData);
        modBus.addListener(CelebratorySpruceClient::registerTints);

        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CELEBRATORY_SPRUCE_SAPLING.get(), ChunkSectionLayer.CUTOUT);
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.POTTED_CELEBRATORY_SPRUCE_SAPLING.get(),
                    ChunkSectionLayer.CUTOUT
            );
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.GOLD_STAR.get(),
                    ChunkSectionLayer.CUTOUT
            );

        });
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntityTypes.ITEM_RENDERING_BLOCK.get(),
                ItemRenderingBlockEntityRenderer::new
        );
    }

    public static void registerTints(final RegisterColorHandlersEvent.Block event) {
        event.register(
                (blockState, blockAndTintGetter, blockPos, i) -> SPRUCE_LEAVES_TINT,
                ModBlocks.DECORATED_SPRUCE_LEAVES.get(), ModBlocks.FESTIVE_SPRUCE_LEAVES.get()
        );
    }

    public static void gatherData(final GatherDataEvent.Client event) {
        try {

            // Store some frequently-used fields for later use.
            DataGenerator generator = event.getGenerator();
            PackOutput packOutput = generator.getPackOutput();
            CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

            // Loot tables.
            generator.addProvider(
                    true, new LootTableProvider(
                            packOutput,
                            Collections.emptySet(),
                            List.of(new LootTableProvider.SubProviderEntry(
                                    CelebratorySpruceBlockLootTableProvider::new,
                                    LootContextParamSets.BLOCK
                            )),
                            lookupProvider
                    ) {
                        @Override
                        protected void validate(@NotNull WritableRegistry<LootTable> writableregistry, @NotNull ValidationContext context, ProblemReporter.@NotNull Collector collector) {
                            // Do not validate at all, per what people online said.
                        }
                    }
            );

            // Generate data for the recipes
            generator.addProvider(true, new CelebratorySpruceRecipeProvider.Runner(packOutput, lookupProvider));

            // Generate data for the tags
            BlockTagsProvider blockTagsProvider = new CelebratorySpruceBlockTagsProvider(packOutput, lookupProvider);
            generator.addProvider(true, blockTagsProvider);
            MobEffectTagProvider mobEffectTagsProvider = new CelebratorySpruceMobEffectTagProvider(
                    packOutput,
                    lookupProvider
            );
            generator.addProvider(true, mobEffectTagsProvider);
            generator.addProvider(true, new CelebratorySpruceItemTagsProvider(packOutput, lookupProvider));

            // Generate block and item models.
            generator.addProvider(true, new CelebratorySpruceModelProvider(packOutput));

            // Generate lang file
            generator.addProvider(true, new CelebratorySpruceEnglishUSLanguageProvider(packOutput));

            // Generate advancements
            generator.addProvider(
                    true, new AdvancementProvider(
                            packOutput, lookupProvider,
                            // Add generators here
                            List.of(CelebratorySpruceAdvancementProvider::generate)
                    )
            );

            // Generate data maps for furnace fuel, composters, and such; only used on the NeoForge side.
            generator.addProvider(true, new CelebratorySpruceDataMapProvider(packOutput, lookupProvider));

        } catch (RuntimeException e) {
            CelebratorySpruce.LOGGER.error("Failed to generate data.", e);
        }
    }

}
