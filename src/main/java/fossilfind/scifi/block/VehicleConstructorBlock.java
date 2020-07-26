package fossilfind.scifi.block;

import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.tileentity.VehicleConstructorTileEntity;
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

public class VehicleConstructorBlock extends Block
{
	public VehicleConstructorBlock(Properties properties)
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
		return TileEntityInit.VEHICLE_CONSTRUCTOR.get().create();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!worldIn.isRemote)
		{
			TileEntity te = worldIn.getTileEntity(pos);
			
			if(te instanceof VehicleConstructorTileEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, (VehicleConstructorTileEntity) te, pos);
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.FAIL;
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(!worldIn.isRemote)
		{
			TileEntity te = worldIn.getTileEntity(pos);
			
			if(te instanceof VehicleConstructorTileEntity)
				InventoryHelper.dropItems(worldIn, pos, ((VehicleConstructorTileEntity) te).getItems());
		}
	}
}