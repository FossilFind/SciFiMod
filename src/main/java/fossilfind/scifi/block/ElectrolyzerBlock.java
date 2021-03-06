package fossilfind.scifi.block;

import java.util.List;

import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.tileentity.ElectrolyzerTileEntity;
import fossilfind.scifi.util.helpers.KeyboardHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ElectrolyzerBlock extends Block
{
	public ElectrolyzerBlock(Properties properties)
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
		return TileEntityInit.ELECTROLYZER.get().create();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!world.isRemote)
		{
			TileEntity te = world.getTileEntity(pos);
			
			if(te instanceof ElectrolyzerTileEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, (ElectrolyzerTileEntity) te, pos);
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
			
			if(te instanceof ElectrolyzerTileEntity) InventoryHelper.dropItems(world, pos, ((ElectrolyzerTileEntity) te).getItems());
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(KeyboardHelper.isHoldingShift())
		{
			tooltip.add(new StringTextComponent("\u00A7aApplies an electrical charge to"));
			tooltip.add(new StringTextComponent("\u00A7athe material within to seperate"));
			tooltip.add(new StringTextComponent("\u00A7ait into it's seperate parts"));
		}
		else
		{
			tooltip.add(new StringTextComponent("\u00A75Hold SHIFT"));
		}
	}
}