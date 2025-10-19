package com.vivek.trialmod.datagen;

import com.vivek.trialmod.TrialMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@EventBusSubscriber(modid = TrialMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        LootTableProvider.SubProviderEntry subProviderEntry = new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK);

        List<LootTableProvider.SubProviderEntry> subProvidersList = List.of(subProviderEntry);

        LootTableProvider provider = new LootTableProvider(
                packOutput,
                Collections.emptySet(),
                subProvidersList,
                lookupProvider
        );

        generator.addProvider(event.includeServer(), provider);
    }
}
