package fossilfind.scifi.tileentity;

import javax.annotation.Nonnull;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.block.ElectrolyzerBlock;
import fossilfind.scifi.container.ElectrolyzerContainer;
import fossilfind.scifi.init.ItemInit;
import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.util.IFluidIntake;
import fossilfind.scifi.util.recipes.ElectrolyzerRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ElectrolyzerTileEntity extends LockableLootTileEntity implements ITickableTileEntity, ISidedInventory, IFluidIntake
{
	public FluidTank tank1, tank2, tank3, tank4, tank5;
	public int cookTime, tank1Fill;
	public ElectrolyzerRecipe recipe;
	
	private NonNullList<ItemStack> contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	protected int numPlayersUsing;
	private IItemHandlerModifiable items = createHandler();
	private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);
	private LazyOptional<IFluidHandler> tank1Handler = LazyOptional.of(() -> tank1);
	private LazyOptional<IFluidHandler> tank2Handler = LazyOptional.of(() -> tank2);
	private LazyOptional<IFluidHandler> tank3Handler = LazyOptional.of(() -> tank3);
	private LazyOptional<IFluidHandler> tank4Handler = LazyOptional.of(() -> tank4);
	private LazyOptional<IFluidHandler> tank5Handler = LazyOptional.of(() -> tank5);
	
	public ElectrolyzerTileEntity(TileEntityType<?> type)
	{
		super(type);
		
		tank1 = new FluidTank(20000);
		tank2 = new FluidTank(20000);
		tank3 = new FluidTank(20000);
		tank4 = new FluidTank(20000);
		tank5 = new FluidTank(20000);
	}
	
	public ElectrolyzerTileEntity()
	{
		this(TileEntityInit.ELECTROLYZER.get());
	}
	
	@Override
	public void tick()
	{
		if(isIngredientBucket(getStackInSlot(0)))
		{
			if(tank1Fill >= 10)
			{
				if(getStackInSlot(0).getItem() instanceof BucketItem)
				{
					tank1.fill(new FluidStack(((BucketItem) getStackInSlot(0).getItem()).getFluid(), 1000), FluidAction.EXECUTE);
					setInventorySlotContents(0, new ItemStack(Items.BUCKET));
				}
				else
				{
					if(getStackInSlot(0).getTag().contains("fluid"))
					{
						CompoundNBT nbt = getStackInSlot(0).getTag();
						
						FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt.getCompound("fluid"));
						
						if(fluid.getAmount() > 1000)
						{
							tank1.fill(new FluidStack(fluid.getFluid(), 1000), FluidAction.EXECUTE);
							fluid.shrink(1000);
							nbt.put("fluid", fluid.writeToNBT(new CompoundNBT()));
						}
						else
						{
							tank1.fill(fluid, FluidAction.EXECUTE);
							setInventorySlotContents(0, new ItemStack(ItemInit.CANISTER.get()));
						}
						
						getStackInSlot(0).setTag(nbt);
					}
				}
				
				tank1Fill = 0;
			}
			
			tank1Fill++;
		}
		else
			tank1Fill = 0;
		
		recipe = ElectrolyzerRecipe.getRecipe(tank1.getFluid());
		
		if(recipe != null)
		{
			if(canCook())
			{
				if(cookTime == recipe.getCookTime())
				{
					tank1.drain(recipe.getIngredient().getAmount(), FluidAction.EXECUTE);
					tank2.fill(recipe.getFluidResult1(), FluidAction.EXECUTE);
					tank3.fill(recipe.getFluidResult2(), FluidAction.EXECUTE);
					tank4.fill(recipe.getFluidResult3(), FluidAction.EXECUTE);
					tank5.fill(recipe.getFluidResult4(), FluidAction.EXECUTE);
					
					if(recipe.getResult1() != ItemStack.EMPTY)
					{
						if(getStackInSlot(1).isEmpty())
							setInventorySlotContents(1, new ItemStack(recipe.getResult1().getItem(), recipe.getResult1().getCount()));
						else
							getStackInSlot(1).grow(recipe.getResult1().getCount());
					}
					
					if(recipe.getResult2() != ItemStack.EMPTY)
					{
						if(getStackInSlot(2).isEmpty())
							setInventorySlotContents(2, new ItemStack(recipe.getResult2().getItem(), recipe.getResult2().getCount()));
						else
							getStackInSlot(2).grow(recipe.getResult2().getCount());
					}
					
					if(recipe.getResult3() != ItemStack.EMPTY)
					{
						if(getStackInSlot(3).isEmpty())
							setInventorySlotContents(3, new ItemStack(recipe.getResult3().getItem(), recipe.getResult3().getCount()));
						else
							getStackInSlot(3).grow(recipe.getResult3().getCount());
					}
					
					if(recipe.getResult4() != ItemStack.EMPTY)
					{
						if(getStackInSlot(4).isEmpty())
							setInventorySlotContents(4, new ItemStack(recipe.getResult4().getItem(), recipe.getResult4().getCount()));
						else
							getStackInSlot(4).grow(recipe.getResult4().getCount());
					}
					
					cookTime = 0;
				}
				else
					cookTime++;
			}
			else
				cookTime = 0;
		}
		else
			cookTime = 0;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 5;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		return contents;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> items)
	{
		contents = items;
	}
	
	@Override
	protected ITextComponent getDefaultName()
	{
		return new TranslationTextComponent("container." + SciFiMod.MODID + ".electrolyzer");
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new ElectrolyzerContainer(id, player, this);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("cookTime", cookTime);
		compound.put("tank1", tank1.writeToNBT(new CompoundNBT()));
		compound.put("tank2", tank2.writeToNBT(new CompoundNBT()));
		compound.put("tank3", tank3.writeToNBT(new CompoundNBT()));
		compound.put("tank4", tank4.writeToNBT(new CompoundNBT()));
		compound.put("tank5", tank5.writeToNBT(new CompoundNBT()));
		compound.put("recipe", recipe.writeToNBT(new CompoundNBT()));
		if(!checkLootAndWrite(compound))
			ItemStackHelper.saveAllItems(compound, contents);
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		cookTime = compound.getInt("cookTime");
		tank1.readFromNBT(compound.getCompound("tank1"));
		tank2.readFromNBT(compound.getCompound("tank2"));
		tank3.readFromNBT(compound.getCompound("tank3"));
		tank4.readFromNBT(compound.getCompound("tank4"));
		tank5.readFromNBT(compound.getCompound("tank5"));
		recipe = ElectrolyzerRecipe.readFromNBT(compound.getCompound("recipe"));
		contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		if(!checkLootAndRead(compound))
			ItemStackHelper.loadAllItems(compound, contents);
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		read(tag);
	}
	
	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		if(id == 1)
		{
			numPlayersUsing = type;
			return true;
		}
		
		return super.receiveClientEvent(id, type);
	}
	
	@Override
	public void openInventory(PlayerEntity player)
	{
		if(!player.isSpectator())
		{
			if(numPlayersUsing < 0) numPlayersUsing = 0;
			numPlayersUsing++;
			onOpenOrClose();
		}
	}
	
	@Override
	public void closeInventory(PlayerEntity player)
	{
		if(!player.isSpectator())
		{
			numPlayersUsing++;
			onOpenOrClose();
		}
	}
	
	protected void onOpenOrClose()
	{
		Block block = getBlockState().getBlock();
		
		if(block instanceof ElectrolyzerBlock)
		{
			world.addBlockEvent(pos, block, 1, numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, block);
		}
	}
	
	public static int getPlayersUsing(IBlockReader reader, BlockPos pos)
	{
		if(reader.getBlockState(pos).hasTileEntity())
		{
			TileEntity te = reader.getTileEntity(pos);
			if(te instanceof ElectrolyzerTileEntity)
				return ((ElectrolyzerTileEntity) te).numPlayersUsing;
		}
		
		return 0;
	}
	
	public static void swapContents(ElectrolyzerTileEntity te, ElectrolyzerTileEntity otherTe)
	{
		NonNullList<ItemStack> list = te.getItems();
		te.setItems(otherTe.getItems());
		otherTe.setItems(list);
	}
	
	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		
		if(itemHandler != null)
		{
			itemHandler.invalidate();
			itemHandler = null;
		}
		
		if(tank1Handler != null)
		{
			tank1Handler.invalidate();
			tank1Handler = null;
		}
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == Direction.UP)
			tank1Handler.cast();
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == Direction.NORTH)
			return tank2Handler.cast();
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == Direction.EAST)
			return tank3Handler.cast();
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == Direction.SOUTH)
			return tank4Handler.cast();
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == Direction.WEST)
			return tank5Handler.cast();
		return super.getCapability(cap);
	}
	
	private IItemHandlerModifiable createHandler()
	{
		return new InvWrapper(this);
	}
	
	@Override
	public void remove()
	{
		super.remove();
		
		if(itemHandler != null)
			itemHandler.invalidate();
		if(tank1Handler != null)
			tank1Handler.invalidate();
	}
	
	@Override
	public int[] getSlotsForFace(Direction side)
	{
		if(side == Direction.DOWN)
			return new int[] {1, 2, 3, 4};
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, Direction direction)
	{
		return index == 0 && isIngredientBucket(stack) && direction == Direction.UP;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction)
	{
		return direction == Direction.DOWN && (index == 1 || index == 2 || index == 3 || index == 4);
	}
	
	@Override
	public boolean isIngredientBucket(ItemStack stack)
	{
		if(stack.getItem() == Items.WATER_BUCKET)
			return true;
		if(stack.getItem() == ItemInit.SEAWATER_BUCKET.get())
			return true;
		if(stack.getItem() == ItemInit.SEAWATER_CANISTER.get())
			return true;
		if(stack.getItem() == ItemInit.WATER_CANISTER.get())
			return true;
		return false;
	}
	
	private boolean canCook()
	{
		return roomInTank(2, recipe.getFluidResult1()) && roomInTank(3, recipe.getFluidResult2()) && roomInTank(4, recipe.getFluidResult3()) && roomInTank(5, recipe.getFluidResult4())
				&& roomInSlot(1, recipe.getResult1()) && roomInSlot(2, recipe.getResult2()) && roomInSlot(3, recipe.getResult3()) && roomInSlot(4, recipe.getResult4());
	}
	
	private boolean roomInTank(int tank, FluidStack stack)
	{
		if(stack.isEmpty())
			return true;
		else
			return getTank(tank).isEmpty() || (getTank(tank).getFluid().getFluid() == stack.getFluid() && getTank(tank).getFluidAmount() <= stack.getAmount() + getTank(tank).getFluidAmount());
	}
	
	private boolean roomInSlot(int slot, ItemStack stack)
	{
		if(stack.isEmpty())
			return true;
		else
			return getStackInSlot(slot).isEmpty() || (getStackInSlot(slot).getItem() == stack.getItem() && getStackInSlot(slot).getCount() <= stack.getCount() + getStackInSlot(slot).getCount());
	}
	
	public FluidTank getTank(int index)
	{
		switch(index)
		{
		case 1:
			return tank1;
		case 2:
			return tank2;
		case 3:
			return tank3;
		case 4:
			return tank4;
		case 5:
			return tank5;
		default:
			return tank1;
		}
	}
}