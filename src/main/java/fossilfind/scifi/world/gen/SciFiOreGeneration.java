package fossilfind.scifi.world.gen;

import fossilfind.scifi.init.BlockInit;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class SciFiOreGeneration
{
	public static void generateOres()
	{
		for(Biome biome : ForgeRegistries.BIOMES)
		{
			ConfiguredPlacement<CountRangeConfig> customConfig = Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 5, 5, 256));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockInit.BAUXITE.get().getDefaultState(), 10)).withPlacement(customConfig));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockInit.TANTINITE.get().getDefaultState(), 10)).withPlacement(customConfig));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockInit.RUTILE.get().getDefaultState(), 10)).withPlacement(customConfig));
		}
	}
}