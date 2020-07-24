package fossilfind.scifi.container.slot;

import fossilfind.scifi.init.ItemInit;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class FluidOutakeSlot extends Slot
{
	public FluidOutakeSlot(IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() == Items.BUCKET || stack.getItem() == ItemInit.CANISTER.get();
	}
}