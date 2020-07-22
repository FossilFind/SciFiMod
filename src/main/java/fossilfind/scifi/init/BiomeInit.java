package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.world.biome.MoonBiome;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeInit
{
	public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, SciFiMod.MODID);
	
	public static final RegistryObject<Biome> MOON = BIOMES.register("moon", () -> new MoonBiome(new Biome.Builder().precipitation(RainType.NONE).downfall(0f).scale(0.1f).temperature(1).waterColor(4159204).waterFogColor(329011).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(BlockInit.LUNAR_REGOLITH.get().getDefaultState(), BlockInit.LUNAR_REGOLITH.get().getDefaultState(), BlockInit.LUNAR_REGOLITH.get().getDefaultState())).category(Category.PLAINS).depth(0.01f).parent(null)));
	
	public static void registerBiomes()
	{
		registerBiome(MOON, BiomeManager.BiomeType.DESERT, 1, false, Type.DEAD);
	}
	
	private static void registerBiome(RegistryObject<Biome> biome, BiomeManager.BiomeType temp, int rarity, boolean isInOverworld, Type... types) 
	{
		BiomeDictionary.addTypes(biome.get(), types);
		BiomeManager.addSpawnBiome(biome.get());
		if(isInOverworld) BiomeManager.addBiome(temp, new BiomeManager.BiomeEntry(biome.get(), rarity));
	}
}