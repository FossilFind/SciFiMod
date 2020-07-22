package fossilfind.scifi.block;

import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.tileentity.RefineryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class RefineryBlock extends Block
{
	public RefineryBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return TileEntityInit.REFINERY.get().create();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!world.isRemote)
		{
			TileEntity te = world.getTileEntity(pos);
			
			if(te instanceof RefineryTileEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, (RefineryTileEntity) te, pos);
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.FAIL;
	}
	
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			TileEntity te = world.getTileEntity(pos);
			
			if(te instanceof RefineryTileEntity) InventoryHelper.dropItems(world, pos, ((RefineryTileEntity) te).getItems());
		}
	}
}