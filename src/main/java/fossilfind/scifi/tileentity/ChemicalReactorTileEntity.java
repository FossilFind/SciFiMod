package fossilfind.scifi.tileentity;

import javax.annotation.Nonnull;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.block.ChemicalReactorBlock;
import fossilfind.scifi.container.ChemicalReactorContainer;
import fossilfind.scifi.init.ItemInit;
import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.util.IFluidIntake;
import fossilfind.scifi.util.recipes.ChemicalReactorRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ChemicalReactorTileEntity extends LockableLootTileEntity implements ITickableTileEntity, IFluidIntake
{
	private FluidTank tank1, tank2;
	public int cookTime;
	public ChemicalReactorRecipe recipe;
	private int tank1Fill = 0, tank2Fill = 0;
	
	private NonNullList<ItemStack> contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private int numPlayersUsing;
	private IItemHandlerModifiable items = createHandler();
	private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);
	
	public ChemicalReactorTileEntity(TileEntityType<?> typeIn)
	{
		super(typeIn);
		
		tank1 = new FluidTank(20000);
		tank2 = new FluidTank(20000);
	}
	
	public ChemicalReactorTileEntity()
	{
		this(TileEntityInit.CHEMICAL_REACTOR.get());
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
		
		if(isIngredientBucket(getStackInSlot(1)))
		{
			if(tank2Fill >= 10)
			{
				if(getStackInSlot(1).getItem() instanceof BucketItem)
				{
					tank2.fill(new FluidStack(((BucketItem) getStackInSlot(1).getItem()).getFluid(), 1000), FluidAction.EXECUTE);
					setInventorySlotContents(1, new ItemStack(Items.BUCKET));
				}
				else
				{
					if(getStackInSlot(1).getTag().contains("fluid"))
					{
						CompoundNBT nbt = getStackInSlot(1).getTag();
						
						FluidStack fluid = FluidStack.loadFluidStackFromNBT(getStackInSlot(1).getTag().getCompound("fluid"));
						
						if(fluid.getAmount() > 1000)
						{
							tank2.fill(new FluidStack(fluid.getFluid(), 1000), FluidAction.EXECUTE);
							fluid.shrink(1000);
							nbt.put("fluid", fluid.writeToNBT(new CompoundNBT()));
						}
						else
						{
							tank2.fill(fluid, FluidAction.EXECUTE);
							setInventorySlotContents(1, new ItemStack(ItemInit.CANISTER.get()));
						}
						
						getStackInSlot(1).setTag(nbt);
					}
				}
				
				tank2Fill = 0;
			}
			
			tank2Fill++;
		}
		else
			tank2Fill = 0;
		
		recipe = ChemicalReactorRecipe.getRecipe(tank1.getFluid(), tank2.getFluid(), getStackInSlot(2), getStackInSlot(3), getStackInSlot(4));
		
		if(recipe != null)
		{
			if(canCook())
			{
				if(cookTime == recipe.getCookTime())
				{
					if(tank1.getFluid().getFluid() == recipe.getFluidIngredient1().getFluid())
					{
						tank1.drain(recipe.getFluidIngredient1().getAmount(), FluidAction.EXECUTE);
						tank2.drain(recipe.getFluidIngredient2().getAmount(), FluidAction.EXECUTE);
					}
					else
					{
						tank1.drain(recipe.getFluidIngredient2().getAmount(), FluidAction.EXECUTE);
						tank2.drain(recipe.getFluidIngredient1().getAmount(), FluidAction.EXECUTE);
					}
					
					if(getStackInSlot(2).getItem() == recipe.getIngredient1().getItem())
					{
						getStackInSlot(2).shrink(recipe.getIngredient1().getCount());
					}
					else if(getStackInSlot(3).getItem() == recipe.getIngredient1().getItem())
					{
						getStackInSlot(3).shrink(recipe.getIngredient1().getCount());
					}
					else
					{
						getStackInSlot(4).shrink(recipe.getIngredient1().getCount());
					}
					
					if(getStackInSlot(2).getItem() == recipe.getIngredient2().getItem())
					{
						getStackInSlot(2).shrink(recipe.getIngredient2().getCount());
					}
					else if(getStackInSlot(3).getItem() == recipe.getIngredient2().getItem())
					{
						getStackInSlot(3).shrink(recipe.getIngredient2().getCount());
					}
					else
					{
						getStackInSlot(4).shrink(recipe.getIngredient2().getCount());
					}
					
					if(getStackInSlot(2).getItem() == recipe.getIngredient3().getItem())
					{
						getStackInSlot(2).shrink(recipe.getIngredient3().getCount());
					}
					else if(getStackInSlot(3).getItem() == recipe.getIngredient3().getItem())
					{
						getStackInSlot(3).shrink(recipe.getIngredient3().getCount());
					}
					else
					{
						getStackInSlot(4).shrink(recipe.getIngredient3().getCount());
					}
					
					if(getStackInSlot(5).isEmpty())
						setInventorySlotContents(5, new ItemStack(recipe.getResult().getItem()));
					else if(getStackInSlot(5).getItem() == recipe.getResult().getItem() && getStackInSlot(5).getCount() <= recipe.getResult().getCount() + getStackInSlot(5).getCount())
						getStackInSlot(5).grow(recipe.getResult().getCount());
					else if(getStackInSlot(6).isEmpty())
						setInventorySlotContents(6, new ItemStack(recipe.getResult().getItem()));
					else if(getStackInSlot(6).getItem() == recipe.getResult().getItem() && getStackInSlot(6).getCount() <= recipe.getResult().getCount() + getStackInSlot(6).getCount())
						getStackInSlot(6).grow(recipe.getResult().getCount());
					else if(getStackInSlot(7).isEmpty())
						setInventorySlotContents(7, new ItemStack(recipe.getResult().getItem()));
					else
						getStackInSlot(7).grow(recipe.getResult().getCount());
					
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
		return 8;
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
		return new TranslationTextComponent("container." + SciFiMod.MODID + ".chemical_reactor");
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new ChemicalReactorContainer(id, player, this);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		if(!checkLootAndWrite(compound))
			ItemStackHelper.saveAllItems(compound, contents);
		compound.putInt("cookTime", cookTime);
		compound.put("recipe", recipe.writeToNBT(new CompoundNBT()));
		compound.put("tank1", tank1.writeToNBT(new CompoundNBT()));
		compound.put("tank2", tank2.writeToNBT(new CompoundNBT()));
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		if(!checkLootAndRead(compound))
			ItemStackHelper.loadAllItems(compound, contents);
		cookTime = compound.getInt("cookTime");
		recipe = ChemicalReactorRecipe.readFromNBT(compound.getCompound("recipe"));
		tank1 = new FluidTank(20000);
		tank2 = new FluidTank(20000);
		tank1.readFromNBT(compound.getCompound("tank1"));
		tank2.readFromNBT(compound.getCompound("tank2"));
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
			numPlayersUsing--;
			
			onOpenOrClose();
		}
	}
	
	protected void onOpenOrClose()
	{
		Block block = getBlockState().getBlock();
		
		if(block instanceof ChemicalReactorBlock)
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
			if(te instanceof ChemicalReactorTileEntity)
				return ((ChemicalReactorTileEntity) te).numPlayersUsing;
		}
		
		return 0;
	}
	
	public static void swapContents(ChemicalReactorTileEntity te, ChemicalReactorTileEntity otherTe)
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
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();
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
	}
	
	@Override
	public boolean isIngredientBucket(ItemStack stack)
	{
		if(stack.getItem() == ItemInit.STEAM_CANISTER.get())
			return true;
		if(stack.getItem() == ItemInit.WATER_CANISTER.get())
			return true;
		if(stack.getItem() == Items.WATER_BUCKET)
			return true;
				
		return false;
	}
	
	private boolean canCook()
	{
		return roomInSlot(5, recipe.getResult()) || roomInSlot(6, recipe.getResult()) || roomInSlot(7, recipe.getResult());
	}
	
	private boolean roomInSlot(int slot, ItemStack stack)
	{
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
		default:
			return tank1;
		}
	}
}