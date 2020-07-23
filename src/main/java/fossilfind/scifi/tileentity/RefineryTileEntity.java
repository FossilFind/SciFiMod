package fossilfind.scifi.tileentity;

import javax.annotation.Nonnull;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.block.RefineryBlock;
import fossilfind.scifi.container.RefineryContainer;
import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.util.recipes.RefineryRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class RefineryTileEntity extends LockableLootTileEntity implements ITickableTileEntity
{
	public int cookTime, burnTime = 0, lastBurnTime;
	public RefineryRecipe recipe;
	
	private NonNullList<ItemStack> contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	protected int numPlayersUsing;
	private IItemHandlerModifiable items = createHandler();
	private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);
	
	public RefineryTileEntity(TileEntityType<?> type)
	{
		super(type);
	}
	
	public RefineryTileEntity()
	{
		this(TileEntityInit.REFINERY.get());
	}
	
	@Override
	public void tick()
	{
		recipe = RefineryRecipe.getRecipe(getStackInSlot(0));
		
		if(recipe != null)
		{
			if(canCook())
			{
				if(isBurning())
				{
					if(cookTime == recipe.getCookTime())
					{
						getStackInSlot(0).shrink(1);
						
						if(getStackInSlot(2).isEmpty())
							setInventorySlotContents(2, new ItemStack(recipe.getResult1().getItem(), recipe.getResult1().getCount()));
						else
							getStackInSlot(2).grow(1);
						
						if(getStackInSlot(3).isEmpty())
							setInventorySlotContents(3, new ItemStack(recipe.getResult2().getItem(), recipe.getResult2().getCount()));
						else
							getStackInSlot(3).grow(1);
						
						cookTime = 0;
					}
					else
						cookTime++;
				}
				
				if(burnTime == 0 && isFueled() && !isBurning())
				{
					burnTime = ForgeHooks.getBurnTime(getStackInSlot(1));
					getStackInSlot(1).shrink(1);
					lastBurnTime = burnTime;
				}
			}
			else
				cookTime = 0;
		}
		else
			cookTime = 0;
		
		if(isBurning())
			burnTime--;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 4;
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
		return new TranslationTextComponent("container." + SciFiMod.MODID + ".refinery");
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new RefineryContainer(id, player, this);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("cookTime", cookTime);
		compound.putInt("burnTime", burnTime);
		compound.putInt("lastBurnTime", lastBurnTime);
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
		burnTime = compound.getInt("burnTime");
		lastBurnTime = compound.getInt("lastBurnTime");
		recipe = RefineryRecipe.readFromNBT(compound.getCompound("recipe"));
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
		
		if(block instanceof RefineryBlock)
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
			if(te instanceof RefineryTileEntity)
				return ((RefineryTileEntity) te).numPlayersUsing;
		}
		
		return 0;
	}
	
	public static void swapContents(RefineryTileEntity te, RefineryTileEntity otherTe)
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
	
	public static boolean isItemIngredient(ItemStack stack)
	{
		Item item = stack.getItem();
		
		if(item == Items.IRON_ORE)
			return true;
		if(item == Items.GOLD_ORE)
			return true;
		if(item == Items.NETHER_QUARTZ_ORE)
			return true;
		if(item == BlockInit.BAUXITE.get().asItem())
			return true;
		if(item == BlockInit.RUTILE.get().asItem())
			return true;
		if(item == BlockInit.TANTINITE.get().asItem())
			return true;
		
		return false;
	}
	
	private boolean isItemFuel(ItemStack stack)
	{
		return ForgeHooks.getBurnTime(stack) > 0;
	}
	
	private boolean isFueled()
	{
		return isItemFuel(getStackInSlot(1));
	}
	
	private boolean isBurning()
	{
		return burnTime > 0;
	}
	
	private boolean canCook()
	{
		return roomInSlot(2, recipe.getResult1()) && roomInSlot(3, recipe.getResult2());
	}
	
	private boolean roomInSlot(int slot, ItemStack stack)
	{
		return getStackInSlot(slot).isEmpty() || (getStackInSlot(slot).getItem() == stack.getItem() && getStackInSlot(slot).getCount() <= stack.getCount() + getStackInSlot(slot).getCount());
	}
}