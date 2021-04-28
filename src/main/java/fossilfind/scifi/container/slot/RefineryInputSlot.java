package fossilfind.scifi.container.slot;

import fossilfind.scifi.init.RecipeInit;
import fossilfind.scifi.tileentity.RefineryTileEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class RefineryInputSlot extends Slot
{
	private RefineryTileEntity te;
	
	public RefineryInputSlot(RefineryTileEntity te, int index, int x, int y)
	{
		super(te, index, x, y);
		
		this.te = te;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		boolean valid = false;
		
		for(ItemStack item : RefineryTileEntity.getAllRecipeInputs(RecipeInit.REFINERY_TYPE, te.getWorld()))
		{
			if(item.getItem() == stack.getItem())
				valid = true;
		}
		
		return valid;
	}
}