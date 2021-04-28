package fossilfind.scifi.block;

import java.util.List;
import java.util.Random;

import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.tileentity.RefineryTileEntity;
import fossilfind.scifi.util.helpers.KeyboardHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class RefineryBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	
	public RefineryBlock(Properties properties)
	{
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH).with(LIT, false));
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
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(FACING, LIT);
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot)
	{
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getLightValue(BlockState state)
	{
		return state.get(LIT) ? super.getLightValue(state) : 0;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		if(stack.hasDisplayName())
		{
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof RefineryTileEntity)
				((RefineryTileEntity) te).setCustomName(stack.getDisplayName());
		}
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.get(LIT))
		{
			double x = (double) pos.getX() + 0.5d;
			double y = (double) pos.getY();
			double z = (double) pos.getZ() + 0.5d;
			
			if(rand.nextDouble() < 0.1d)
				worldIn.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1, 1, false);
			
			Direction direction =  stateIn.get(FACING);
			Direction.Axis axis = direction.getAxis();
			
			double xzModifier = rand.nextDouble() * 0.6d - 0.3d;
			double xOffset = axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52d : xzModifier;
			double yOffset = rand.nextDouble() * 0.6d / 16;
			double zOffset = axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52d : xzModifier;
			
			worldIn.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0, 0, 0);
			worldIn.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0, 0, 0);
		}
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!world.isRemote)
		{
			TileEntity te = world.getTileEntity(pos);
			
			if(te instanceof RefineryTileEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, pos);
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
			
			if(te instanceof RefineryTileEntity)
			{
				RefineryTileEntity refinery = (RefineryTileEntity) te;
				NonNullList<ItemStack> items = NonNullList.create();
				for(int i = 0; i < refinery.getSizeInventory(); i++)
					items.add(refinery.getStackInSlot(i));
				
				items.forEach(item ->
				{
					ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), item);
					world.addEntity(itemEntity);
				});
			}
			
			if(state.hasTileEntity())
				world.removeTileEntity(pos);
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(KeyboardHelper.isHoldingShift())
		{
			tooltip.add(new StringTextComponent("\u00A7aSmelts ores in a more pure way"));
			tooltip.add(new StringTextComponent("\u00A7athan the furnace to double the output"));
		}
		else
		{
			tooltip.add(new StringTextComponent("\u00A75Hold SHIFT"));
		}
	}
}