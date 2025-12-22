package com.startraveler.celebratoryspruce;

import com.mojang.logging.LogUtils;
import com.startraveler.celebratoryspruce.block.RandomizedDecoratedLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

// Perfect. Put these in music disks.
// https://commons.wikimedia.org/wiki/File%3AJohn_Baptiste_Calkin_-_I_Heard_the_Bells_on_Christmas_Day.ogg
// https://www.loc.gov/item/jukebox-26061/ (silent night)
// https://en.wikipedia.org/wiki/File:What_Child_is_this.mid

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CelebratorySpruce.MODID)
public class CelebratorySpruce {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "celebratoryspruce";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CelebratorySpruce(final IEventBus modEventBus, final ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModBlocks.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ModItems.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (CelebratorySpruce) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(this::useItemOnBlock);

        // Register the item to a creative tab

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MODID, name);
    }

    public void useItemOnBlock(final UseItemOnBlockEvent event) {
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        @Nullable Player player = event.getPlayer();

        if (stack.is(ModItems.ORNAMENT) && state.is(Blocks.SPRUCE_LEAVES)) {
            if (player == null || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.setBlockAndUpdate(
                    pos,
                    RandomizedDecoratedLeavesBlock.applyRandomVariant(
                            ModBlocks.DECORATED_SPRUCE_LEAVES.get().withPropertiesOf(state),
                            pos,
                            RandomizedDecoratedLeavesBlock.VARIANT
                    )
            );
            event.cancelWithResult(InteractionResult.SUCCESS);
        } else if (stack.isEmpty() && state.is(ModBlocks.DECORATED_SPRUCE_LEAVES)) {
            if (player != null) {
                player.addItem(new ItemStack(ModItems.ORNAMENT.get(), 1));
            }
            level.setBlockAndUpdate(
                    pos,
                    Blocks.SPRUCE_LEAVES.withPropertiesOf(state)
            );
            event.cancelWithResult(InteractionResult.SUCCESS);
        } else if (stack.is(ModItems.FESTIVE_LIGHT) && state.is(Blocks.SPRUCE_LEAVES)) {
            if (player == null || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.setBlockAndUpdate(
                    pos,
                    ModBlocks.FESTIVE_SPRUCE_LEAVES.get().withPropertiesOf(state)
            );
            event.cancelWithResult(InteractionResult.SUCCESS);
        } else if (stack.isEmpty() && state.is(ModBlocks.FESTIVE_SPRUCE_LEAVES)) {
            if (player != null) {
                player.addItem(new ItemStack(ModItems.FESTIVE_LIGHT.get(), 1));
            }
            level.setBlockAndUpdate(
                    pos,
                    Blocks.SPRUCE_LEAVES.withPropertiesOf(state)
            );
            event.cancelWithResult(InteractionResult.SUCCESS);
        }

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event) {
        // Do something when the server starts
    }
}
