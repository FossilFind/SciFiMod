package fossilfind.scifi.world.biome.provider;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import fossilfind.scifi.init.BiomeInit;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;

public class MoonBiomeProvider extends BiomeProvider
{
	private static final Set<Biome> biomes = ImmutableSet.of(BiomeInit.MOON.get());
	
	@SuppressWarnings("unused")
	private Random rand;
	
	public MoonBiomeProvider() 
	{
		super(biomes);
		rand = new Random();
	}

	@Override
	public Biome getNoiseBiome(int arg0, int arg1, int arg2) 
	{
		return BiomeInit.MOON.get();
	}
}