package fossilfind.scifi.container.slot;

import fossilfind.scifi.util.IFluidIntake;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FluidIntakeSlot extends Slot
{
	IFluidIntake te;
	
	public FluidIntakeSlot(IFluidIntake te, IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
		this.te = te;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return te.isIngredientBucket(stack);
	}
}