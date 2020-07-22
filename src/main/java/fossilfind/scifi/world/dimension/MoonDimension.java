package fossilfind.scifi.world.dimension;

import fossilfind.scifi.world.biome.provider.MoonBiomeProvider;
import fossilfind.scifi.world.gen.MoonChunkGenerator;
import fossilfind.scifi.world.gen.MoonGenerationSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;

public class MoonDimension extends Dimension
{
	public MoonDimension(World world, DimensionType type) 
	{
		super(world, type, 0f);
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator() 
	{
		return new MoonChunkGenerator(world, new MoonBiomeProvider(), new MoonGenerationSettings());
	}

	@Override
	public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) 
	{
		return null;
	}

	@Override
	public BlockPos findSpawn(int posX, int posZ, boolean checkValid) 
	{
		return null;
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 270;
	}

	@Override
	public boolean isSurfaceWorld() 
	{
		return true;
	}

	@Override
	public Vec3d getFogColor(float celestialAngle, float partialTicks) 
	{
		return Vec3d.ZERO;
	}

	@Override
	public boolean canRespawnHere() 
	{
		return true;
	}

	@Override
	public boolean doesXZShowFog(int x, int z) 
	{
		return false;
	}
	
	@Override
	public SleepResult canSleepAt(PlayerEntity player, BlockPos pos) 
	{
		return SleepResult.ALLOW;
	}
	
	@Override
	public boolean isSkyColored()
	{
		return true;
	}
}