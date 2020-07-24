package fossilfind.scifi.world.gen.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import fossilfind.scifi.init.BlockInit;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class SaltDepositFeature extends Feature<NoFeatureConfig>
{
	public SaltDepositFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		int chance = rand.nextInt(10);
		if(chance != 1)
			return false;
		
		int y = 256;
		
		int i = 0;
		while(i == 0)
		{
			if(y == 0)
			{
				i = 1;
				continue;
			}
			
			BlockPos pos1 = new BlockPos(pos.getX(), y, pos.getZ());
			if(world.isAirBlock(pos1)
					|| world.getBlockState(pos1).getBlock() == Blocks.WATER
					|| world.getBlockState(pos1).getBlock() == Blocks.SEAGRASS
					|| world.getBlockState(pos1).getBlock() == Blocks.TALL_SEAGRASS
					|| world.getBlockState(pos1).getBlock() == Blocks.KELP_PLANT
					|| world.getBlockState(pos1).getBlock() == Blocks.KELP)
				y--;
			else
				i = 1;
		}
		
		BlockPos pos1 = new BlockPos(pos.getX() + rand.nextInt(16), y + 1, pos.getZ() + rand.nextInt(16));
		
		world.setBlockState(pos1, BlockInit.SALT_BLOCK.get().getDefaultState(), 1);
		world.setBlockState(pos1.up(), BlockInit.SALT_BLOCK.get().getDefaultState(), 1);
		world.setBlockState(pos1.north(), BlockInit.SALT_BLOCK.get().getDefaultState(), 1);
		world.setBlockState(pos1.east(), BlockInit.SALT_BLOCK.get().getDefaultState(), 1);
		world.setBlockState(pos1.south(), BlockInit.SALT_BLOCK.get().getDefaultState(), 1);
		world.setBlockState(pos1.west(), BlockInit.SALT_BLOCK.get().getDefaultState(), 1);
		
		return true;
	}
}