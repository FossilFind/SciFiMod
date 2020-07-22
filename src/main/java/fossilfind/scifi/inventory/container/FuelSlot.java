package fossilfind.scifi.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FuelSlot extends Slot
{
	private final Container container;
	
	public FuelSlot(Container container, IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
		
		this.container = container;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) 
	{
		if(container instanceof RefineryContainer) return ((RefineryContainer) container).isItemFuel(stack);
		return false;
	}
}