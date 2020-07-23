package fossilfind.scifi.container.slot;

import fossilfind.scifi.tileentity.RefineryTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class RefineryInputSlot extends Slot
{
	public RefineryInputSlot(IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return RefineryTileEntity.isItemIngredient(stack);
	}
}