package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.block.FluidCompressorBlock;
import fossilfind.scifi.container.FluidCompressorContainer;
import fossilfind.scifi.item.CanisterItem;
import fossilfind.scifi.util.IFluidIntake;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FluidCompressorTileEntity extends LockableLootTileEntity implements ITickableTileEntity, IFluidIntake
{
	private FluidTank tank;
	private int tankFill = 0, tankDrain = 0;
	
	private NonNullList<ItemStack> contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private int numPlayersUsing;
	private IItemHandlerModifiable items = createHandler();
	private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);
	
	public FluidCompressorTileEntity(TileEntityType<?> typeIn)
	{
		super(typeIn);
		
		tank = new FluidTank(20000);
	}
	
	public FluidCompressorTileEntity()
	{
		this(TileEntityInit.FLUID_COMPRESSOR.get());
	}
	
	@Override
	public void tick()
	{
		if(isIngredientBucket(getStackInSlot(0)))
		{
			if(tankFill >= 10)
			{
				if(getStackInSlot(0).getItem() instanceof BucketItem)
				{
					tank.fill(new FluidStack(((BucketItem) getStackInSlot(0).getItem()).getFluid(), 1000), FluidAction.EXECUTE);
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
							tank.fill(new FluidStack(fluid.getFluid(), 1000), FluidAction.EXECUTE);
							fluid.shrink(1000);
							nbt.put("fluid", fluid.writeToNBT(new CompoundNBT()));
						}
						else
						{
							tank.fill(fluid, FluidAction.EXECUTE);
							setInventorySlotContents(0, new ItemStack(ItemInit.CANISTER.get()));
						}
						
						getStackInSlot(0).setTag(nbt);
					}
				}
				
				tankFill = 0;
			}
			
			tankFill++;
		}
		else
			tankFill = 0;
		
		if(getStackInSlot(1).getItem() == Items.BUCKET && !tank.isEmpty())
		{
			if(tank.getFluid().getFluid() == Fluids.WATER)
				setInventorySlotContents(1, new ItemStack(Items.WATER_BUCKET));
			if(tank.getFluid().getFluid() == FluidInit.SEAWATER.get())
				setInventorySlotContents(1, new ItemStack(ItemInit.SEAWATER_BUCKET.get()));
			
			tank.drain(1000, FluidAction.EXECUTE);
		}
		
		if(getStackInSlot(1).getItem() == ItemInit.CANISTER.get() && !tank.isEmpty())
		{
			if(tank.getFluid().getFluid() == Fluids.WATER)
			{
				ItemStack stack = new ItemStack(ItemInit.WATER_CANISTER.get());
				
				CompoundNBT nbt = stack.getOrCreateTag();
				nbt.put("fluid", new FluidStack(((CanisterItem) stack.getItem()).getFluid().get(), 1000).writeToNBT(new CompoundNBT()));
				stack.setTag(nbt);
				
				setInventorySlotContents(1, stack);
				
				tank.drain(1000, FluidAction.EXECUTE);
			}
			else if(tank.getFluid().getFluid() == FluidInit.SEAWATER.get())
			{
				ItemStack stack = new ItemStack(ItemInit.SEAWATER_CANISTER.get());
				
				CompoundNBT nbt = stack.getOrCreateTag();
				nbt.put("fluid", new FluidStack(((CanisterItem) stack.getItem()).getFluid().get(), 1000).writeToNBT(new CompoundNBT()));
				stack.setTag(nbt);
				
				setInventorySlotContents(1, stack);
				
				tank.drain(1000, FluidAction.EXECUTE);
			}
		}
		else
		{
			if(getStackInSlot(1).getItem() == ItemInit.WATER_CANISTER.get() || getStackInSlot(1).getItem() == ItemInit.SEAWATER_CANISTER.get())
			{
				if(!tank.isEmpty())
				{
					if(tankDrain >= 10)
					{
					
						CompoundNBT nbt = getStackInSlot(1).getOrCreateTag();
					
						FluidStack stack = FluidStack.loadFluidStackFromNBT(nbt.getCompound("fluid"));
						stack.grow(1000);
						nbt.put("fluid", stack.writeToNBT(new CompoundNBT()));
						
						getStackInSlot(1).setTag(nbt);
						
						tank.drain(1000, FluidAction.EXECUTE);
						
						tankDrain = 0;
					}
					else
						tankDrain++;
				}
			}
			else
				tankDrain = 0;
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return 2;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		return contents;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn)
	{
		contents = itemsIn;
	}
	
	@Override
	protected ITextComponent getDefaultName()
	{
		return new TranslationTextComponent("container." + SciFiMod.MODID + ".fluid_compressor");
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new FluidCompressorContainer(id, player, this);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
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
			if(numPlayersUsing < 0)
				numPlayersUsing = 0;
			
			numPlayersUsing++;
			
			onOpenOrClose();
		}
	}
	
	@Override
	public void closeInventory(PlayerEntity player)
	{
		if(!player.isSpectator())
		{
			numPlayersUsing--;
			
			onOpenOrClose();
		}
	}
	
	protected void onOpenOrClose()
	{
		Block block = getBlockState().getBlock();
		
		if(block instanceof FluidCompressorBlock)
		{
			world.addBlockEvent(pos, block, 1, numPlayersUsing);
			world.notifyNeighborsOfStateChange(pos, block);
		}
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
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();
		return super.getCapability(cap, side);
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
	}
	
	@Override
	public boolean isIngredientBucket(ItemStack stack)
	{
		if(stack.getItem() == Items.WATER_BUCKET)
			return true;
		if(stack.getItem() == ItemInit.SEAWATER_BUCKET.get())
			return true;
		if(stack.getItem() == ItemInit.WATER_CANISTER.get())
			return true;
		if(stack.getItem() == ItemInit.SEAWATER_CANISTER.get())
			return true;
		
		return false;
	}
	
	public FluidTank getTank()
	{
		return tank;
	}
}