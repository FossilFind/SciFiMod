package fossilfind.scifi.tileentity;

import javax.annotation.Nonnull;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.block.RefineryBlock;
import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.ItemInit;
import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.inventory.container.RefineryContainer;
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
	public int cookTime, cookTimeTotal, burnTime, lastBurnTime;
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
		if(canCook())
		{
			if(recipe == null)
			{
				recipe = getRecipe(getStackInSlot(0));
				
				cookTime = 0;
				cookTimeTotal = recipe.getCookTime();
			}
			
			cookTime++;
			
			if(cookTime == cookTimeTotal)
			{
				cook();
				recipe = null;
			}
			
			if(burnTime == 0 && isFueled() && !isBurning())
			{
				burnTime = ForgeHooks.getBurnTime(getStackInSlot(1));
				getStackInSlot(1).shrink(1);
				lastBurnTime = burnTime;
			}
		}
		else
		{
			cookTime = 0;
		}
		
		if(isFueled() || isBurning())
		{	
			burnTime--;
		}
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
		//compound.put("initValues", NBTHelper.toNBT(this));
		if(!checkLootAndWrite(compound)) ItemStackHelper.saveAllItems(compound, contents);
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		CompoundNBT initValues = compound.getCompound("initValues");
		if(initValues != null)
		{
			cookTime = initValues.getInt("cookTime");
			cookTimeTotal = initValues.getInt("cookTimeTotal");
			burnTime = initValues.getInt("burnTime");
			lastBurnTime = initValues.getInt("lastBurnTime");
			
			contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
			if(!checkLootAndRead(compound)) ItemStackHelper.loadAllItems(compound, contents);
		}
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
			if(te instanceof RefineryTileEntity) return ((RefineryTileEntity) te).numPlayersUsing;
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
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return itemHandler.cast();
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
		
		if(itemHandler != null) itemHandler.invalidate();
	}
	
	public boolean isItemFuel(ItemStack stack)
	{
		return ForgeHooks.getBurnTime(stack) > 0;
	}
	
	public boolean isItemIngredient(ItemStack stack)
	{
		Item item = stack.getItem();
		
		if(item == Items.IRON_ORE) return true;
		if(item == Items.GOLD_ORE) return true;
		if(item == Items.NETHER_QUARTZ_ORE) return true;
		if(item == BlockInit.BAUXITE.get().asItem()) return true;
		if(item == BlockInit.RUTILE.get().asItem()) return true;
		if(item == BlockInit.TANTINITE.get().asItem()) return true;
		
		return false;
	}
	
	private boolean isFueled()
	{
		return isItemFuel(getStackInSlot(1));
	}
	
	private boolean isBurning()
	{
		return burnTime > 0;
	}
	
	private RefineryRecipe getRecipe(ItemStack ingredient)
	{
		if(ingredient.getItem() == Items.IRON_ORE) return new RefineryRecipe(60, ingredient, new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT));
		if(ingredient.getItem() == Items.GOLD_ORE) return new RefineryRecipe(60, ingredient, new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT));
		if(ingredient.getItem() == Items.NETHER_QUARTZ_ORE) return new RefineryRecipe(60, ingredient, new ItemStack(Items.QUARTZ), new ItemStack(ItemInit.GRAPHITE.get()));
		if(ingredient.getItem() == BlockInit.BAUXITE.get().asItem()) return new RefineryRecipe(60, ingredient, new ItemStack(ItemInit.ALUMINUM_INGOT.get()), new ItemStack(ItemInit.ALUMINUM_INGOT.get()));
		if(ingredient.getItem() == BlockInit.RUTILE.get().asItem()) return new RefineryRecipe(60, ingredient, new ItemStack(ItemInit.TITANIUM_INGOT.get()), new ItemStack(ItemInit.TITANIUM_INGOT.get()));
		if(ingredient.getItem() == BlockInit.TANTINITE.get().asItem()) return new RefineryRecipe(60, ingredient, new ItemStack(ItemInit.TANTALUM_INGOT.get()), new ItemStack(ItemInit.TANTALUM_INGOT.get()));
		
		return null;
	}
	
	private boolean canCook()
	{
		if(isItemIngredient(getStackInSlot(0)))
		{
			RefineryRecipe recipe = getRecipe(getStackInSlot(0));
			
			ItemStack slot1 = getStackInSlot(2);
			ItemStack slot2 = getStackInSlot(3);
			
			ItemStack result1 = recipe.getResult1();
			ItemStack result2 = recipe.getResult2();
			
			return (slot1.getItem() == result1.getItem() || slot1.isEmpty()) && slot1.getCount() <= result1.getMaxStackSize() && (slot2.getItem() == result2.getItem() || slot2.isEmpty()) && slot2.getCount() <= result2.getMaxStackSize() && (isFueled() || isBurning());
		}
		
		return false;
	}
	
	private void cook()
	{
		getStackInSlot(0).shrink(1);
		
		if(getStackInSlot(2).isEmpty()) setInventorySlotContents(2, recipe.getResult1());
		else getStackInSlot(2).grow(1);
		
		if(getStackInSlot(3).isEmpty()) setInventorySlotContents(3, recipe.getResult2());
		else getStackInSlot(3).grow(1);
	}
}