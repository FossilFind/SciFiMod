package fossilfind.scifi.world.gen;

import fossilfind.scifi.init.FeatureInit;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class SciFiFeatureGeneration
{
	public static void generateFeatures()
	{
		Biomes.OCEAN.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeatureInit.SALT_DEPOSIT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		Biomes.LUKEWARM_OCEAN.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeatureInit.SALT_DEPOSIT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		Biomes.WARM_OCEAN.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeatureInit.SALT_DEPOSIT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		Biomes.DEEP_OCEAN.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeatureInit.SALT_DEPOSIT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		Biomes.DEEP_LUKEWARM_OCEAN.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeatureInit.SALT_DEPOSIT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		Biomes.DEEP_WARM_OCEAN.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeatureInit.SALT_DEPOSIT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
	}
}