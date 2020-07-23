package fossilfind.scifi.container;

import java.util.Objects;

import fossilfind.scifi.container.slot.FuelSlot;
import fossilfind.scifi.container.slot.RefineryInputSlot;
import fossilfind.scifi.container.slot.ResultSlot;
import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.ContainerInit;
import fossilfind.scifi.tileentity.RefineryTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RefineryContainer extends Container
{
	public final RefineryTileEntity te;
	private final IWorldPosCallable canInteractWithCallable;
	
	public RefineryContainer(final int id, final PlayerInventory inventory, RefineryTileEntity te)
	{
		super(ContainerInit.REFINERY.get(), id);
		
		this.te = te;
		canInteractWithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());
		
		addSlot(new RefineryInputSlot(te, 0, 56, 17));
		addSlot(new FuelSlot(te, 1, 56, 53));
		addSlot(new ResultSlot(te, 2, 116, 24));
		addSlot(new ResultSlot(te, 3, 116, 46));
		
		for(int row = 0; row < 3; row++)
		{
			for(int col = 0; col < 9; col++)
			{
				addSlot(new Slot(inventory, 9 + row * 9 + col, 8 + col * 18, 84 + row * 18));
			}
		}
		
		for(int col = 0; col < 9; col++)
		{
			addSlot(new Slot(inventory, col, 8 + col * 18, 142));
		}
	}
	
	private static RefineryTileEntity getTileEntity(final PlayerInventory inventory, final PacketBuffer data)
	{
		Objects.requireNonNull(inventory, "PlayerInventory can not be null");
		Objects.requireNonNull(data, "PacketBuffer can not be null");
		
		final TileEntity te = inventory.player.world.getTileEntity(data.readBlockPos());
		
		if(te instanceof RefineryTileEntity) return (RefineryTileEntity) te;
		
		throw new IllegalStateException(te + " is not the correct TileEntity");
	}
	
	public RefineryContainer(final int id, final PlayerInventory inventory, final PacketBuffer data)
	{
		this(id, inventory, getTileEntity(inventory, data));
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.REFINERY.get());
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) 
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index < 27 && !mergeItemStack(itemstack1, 36, inventorySlots.size(), true)) return ItemStack.EMPTY;
			else if(!mergeItemStack(itemstack1, 0, 36, false)) return ItemStack.EMPTY;
			
			if(itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();
		}
		
		return itemstack;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getCookProgressionScaled()
	{
		int i = te.cookTime;
		int j = te.recipe != null ? te.recipe.getCookTime() : 0;
		
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getBurnLeftScaled()
	{
		int i = te.lastBurnTime;
		
		if(i == 0) i = 200;
		
		return te.burnTime * 13 / i;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean isBurning()
	{
		return te.burnTime > 0;
	}
}