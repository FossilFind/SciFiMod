package fossilfind.scifi.tileentity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.block.RefineryBlock;
import fossilfind.scifi.container.RefineryContainer;
import fossilfind.scifi.init.RecipeInit;
import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.recipe.RefineryRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class RefineryTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity
{
	private static final int[] SLOTS_UP = new int[] {0};
	private static final int[] SLOTS_DOWN = new int[] {2, 3};
	private static final int[] SLOTS_HORIZANTAL = new int[] {1};
	public int cookTime = 0, cookTimeTotal, burnTime = 0, lastBurnTime;
	private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
	private LazyOptional<? extends IItemHandler>[] handler = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	public RefineryContainer container;
	
	public RefineryTileEntity(TileEntityType<?> type)
	{
		super(type);
	}
	
	public RefineryTileEntity()
	{
		this(TileEntityInit.REFINERY.get());
	}
	
	@Override
	public Container createMenu(int windowId, PlayerInventory inventory)
	{
		container = new RefineryContainer(windowId, inventory, this);
		return container;
	}
	
	@Override
	public void tick()
	{
		boolean burning = isBurning();
		boolean dirty = false;
		
		if(this.isBurning())
			burnTime--;
		
		ItemStack fuel = items.get(1);
		
		if(isBurning() || !fuel.isEmpty() && !items.get(0).isEmpty())
		{
			RefineryRecipe recipe = getRecipe(items.get(0));
			
			if(!isBurning() && canSmelt(recipe))
			{
				burnTime = ForgeHooks.getBurnTime(fuel);
				lastBurnTime = burnTime;
				
				if(isBurning())
				{
					dirty = true;
					
					if(fuel.hasContainerItem())
						items.set(1, fuel.getContainerItem());
					else if(!fuel.isEmpty())
					{
						fuel.shrink(1);
						if(fuel.isEmpty())
							items.set(1, fuel.getContainerItem());
					}
				}
			}
			
			if(isBurning() && canSmelt(recipe))
			{
				cookTime++;
				
				if(cookTime == cookTimeTotal)
				{
					cookTime = 0;
					cookTimeTotal = recipe.getCookingTime();
					smelt(recipe);
					dirty = true;
				}
			}
			else
				cookTime = 0;
		}
		else if(!isBurning() && cookTime > 0)
			cookTime = MathHelper.clamp(cookTime - 2, 0, cookTimeTotal);
		
		if(burning != isBurning())
		{
			dirty = true;
			world.setBlockState(pos, world.getBlockState(pos).with(RefineryBlock.LIT, Boolean.valueOf(isBurning())), 3);
		}
		
		if(dirty)
		{
			markDirty();
		}
	}
	
	private boolean isBurning()
	{
		return burnTime > 0;
	}
	
	private boolean canSmelt(@Nullable RefineryRecipe recipe)
	{
		if(!items.get(0).isEmpty() && recipe != null)
		{
			ItemStack output = recipe.getRecipeOutput();
			
			if(!output.isEmpty())
			{
				ItemStack slot1 = items.get(2);
				ItemStack slot2 = items.get(3);
				
				boolean slot1good;
				boolean slot2good;
				
				if(slot1.isEmpty())
					slot1good = true;
				else if(!slot1.isItemEqual(output))
					slot1good = false;
				else if(slot1.getCount() + output.getCount() <= getInventoryStackLimit() && slot1.getCount() + output.getCount() <= slot1.getMaxStackSize())
					slot1good = true;
				else
					slot1good = slot1.getCount() + output.getCount() <= slot1.getMaxStackSize();
				
				if(slot2.isEmpty())
					slot2good = true;
				else if(!slot2.isItemEqual(output))
					slot2good = false;
				else if(slot2.getCount() + output.getCount() <= getInventoryStackLimit() && slot2.getCount() + output.getCount() <= slot2.getMaxStackSize())
					slot2good = true;
				else
					slot2good = slot2.getCount() + output.getCount() <= slot2.getMaxStackSize();
				
				return slot1good && slot2good;
			}
			else
				return false;
		}
		
		return false;
	}
	
	private void smelt(@Nullable RefineryRecipe recipe)
	{
		if(recipe != null && canSmelt(recipe))
		{
			ItemStack input = items.get(0);
			ItemStack result = recipe.getRecipeOutput();
			ItemStack output1 = items.get(2);
			ItemStack output2 = items.get(3);
			
			if(output1.isEmpty())
				items.set(2, result.copy());
			else if(output1.getItem() == result.getItem())
				output1.grow(result.getCount());
			
			if(output2.isEmpty())
				items.set(3, result.copy());
			else if(output2.getItem() == result.getItem())
				output2.grow(result.getCount());
			
			input.shrink(1);
		}
	}
	
	@Override
	protected ITextComponent getDefaultName()
	{
		return new TranslationTextComponent("container." + SciFiMod.MODID + ".refinery");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		ItemStackHelper.saveAllItems(compound, items);
		
		compound.putInt("cookTime", cookTime);
		compound.putInt("cookTimeTotal", cookTimeTotal);
		compound.putInt("burnTime", burnTime);
		
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		
		items = NonNullList.withSize(4, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, items);
		
		cookTime = compound.getInt("cookTime");
		cookTimeTotal = compound.getInt("cookTimeTotal");
		burnTime = compound.getInt("burnTime");
		lastBurnTime = ForgeHooks.getBurnTime(items.get(1));
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 0, write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(pkt.getNbtCompound());
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT nbt)
	{
		read(nbt);
	}
	
	@Override
	public int[] getSlotsForFace(Direction side)
	{
		if(side == Direction.DOWN)
			return SLOTS_DOWN;
		
		if(side == Direction.UP)
			return SLOTS_UP;
		
		return SLOTS_HORIZANTAL;
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction)
	{
		if(direction == Direction.DOWN && index == 1)
			return false;
		
		return true;
	}
	
	@Override
	public int getSizeInventory()
	{
		return items.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		for(ItemStack stack : items)
		{
			if(!stack.isEmpty())
				return false;
		}
		
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return items.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(items, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(items, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack slot = items.get(index);
		boolean addIngredient = !stack.isEmpty() && stack.isItemEqual(slot) && ItemStack.areItemStackTagsEqual(stack, slot);
		
		items.set(index, stack);
		
		if(stack.getCount() > this.getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());
		
		if(index == 0 && !addIngredient)
		{
			cookTimeTotal = getRecipe(stack) != null ? getRecipe(stack).getCookingTime() : 0;
			cookTime = 0;
			markDirty();
		}
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		if(world.getTileEntity(pos) != this)
			return false;
		
		return player.getDistanceSq((double)pos.getX() + 0.5d, (double)pos.getY() + 0.5d, (double)pos.getZ() + 0.5d) <= 64;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if(index == 2 || index == 3)
			return false;
		
		if(index != 1)
			return true;
		
		return ForgeHooks.getBurnTime(stack) > 0;
	}
	
	@Override
	public void clear()
	{
		items.clear();
	}
	
	@Nullable
	private RefineryRecipe getRecipe(ItemStack stack)
	{
		if(stack == null)
			return null;
		
		Set<IRecipe<?>> recipes = findRecipesByType(RecipeInit.REFINERY_TYPE, world);
		for(IRecipe<?> iRecipe : recipes)
		{
			RefineryRecipe recipe = (RefineryRecipe) iRecipe;
			if(recipe.matches(this, world))
				return recipe;
		}
		
		return null;
	}
	
	public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> type, World world)
	{
		return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toSet()) : Collections.emptySet();
	}
	
	@OnlyIn(Dist.CLIENT)
	public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> type)
	{
		@SuppressWarnings("resource")
		ClientWorld world = Minecraft.getInstance().world;
		return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toSet()) : Collections.emptySet();
	}
	
	public static Set<ItemStack> getAllRecipeInputs(IRecipeType<?> type, World world)
	{
		Set<ItemStack> inputs = new HashSet<ItemStack>();
		Set<IRecipe<?>> recipes = findRecipesByType(type, world);
		
		for(IRecipe<?> recipe : recipes)
		{
			NonNullList<Ingredient> ingredients = recipe.getIngredients();
			ingredients.forEach(ingredient ->
			{
				for(ItemStack stack : ingredient.getMatchingStacks())
					inputs.add(stack);
			});
		}
		
		return inputs;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		if(!removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(side == Direction.UP)
				return handler[0].cast();
			else if(side == Direction.DOWN)
				return handler[1].cast();
			else
				return handler[2].cast();
		}
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void remove()
	{
		super.remove();
		
		for(int i = 0; i < handler.length; i++)
			handler[i].invalidate();
	}
}