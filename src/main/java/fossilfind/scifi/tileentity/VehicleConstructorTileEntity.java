package fossilfind.scifi.tileentity;

import javax.annotation.Nonnull;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.block.VehicleConstructorBlock;
import fossilfind.scifi.container.VehicleConstructorContainer;
import fossilfind.scifi.init.TileEntityInit;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class VehicleConstructorTileEntity extends LockableLootTileEntity
{
	private NonNullList<ItemStack> contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private int numPlayersUsing = 0;
	private IItemHandlerModifiable items = createHandler();
	private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);
	
	public VehicleConstructorTileEntity(TileEntityType<?> type)
	{
		super(type);
	}
	
	public VehicleConstructorTileEntity()
	{
		this(TileEntityInit.VEHICLE_CONSTRUCTOR.get());
	}
	
	@Override
	public int getSizeInventory()
	{
		return 12;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		return contents;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> arg0)
	{
		contents = arg0;
	}
	
	@Override
	protected ITextComponent getDefaultName()
	{
		return new TranslationTextComponent("container." + SciFiMod.MODID + ".vehicle_constructor");
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new VehicleConstructorContainer(id, player, this);
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
		Block block = world.getBlockState(pos).getBlock();
		
		if(block instanceof VehicleConstructorBlock)
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
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();
		return super.getCapability(cap);
	}
	
	@Override
	public void remove()
	{
		super.remove();
		
		if(itemHandler != null)
			itemHandler.invalidate();
	}
	
	private IItemHandlerModifiable createHandler()
	{
		return new InvWrapper(this);
	}
}