package fossilfind.scifi.world.biome;

import net.minecraft.world.biome.Biome;

public class MoonBiome extends Biome
{
	public MoonBiome(Builder biomeBuilder)
	{
		super(biomeBuilder);
	}
	
	@Override
	public int getSkyColor()
	{
		return 0;
	}
}