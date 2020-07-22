package fossilfind.scifi.item;

import java.util.List;
import java.util.function.Supplier;

import fossilfind.scifi.util.helpers.KeyboardHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class CanisterItem extends Item
{
	private Supplier<? extends Fluid> supplier;
	
	public CanisterItem(Supplier<? extends Fluid> supplier, Properties builder)
	{
		super(builder);
		
		this.supplier = supplier;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		CompoundNBT nbt;
		
		if(stack.hasTag())
		{
			nbt = stack.getTag();
		}
		else
		{
			nbt = new CompoundNBT();
		}
		
		if(nbt.contains("fluid"))
		{
			int i;
			
			if(supplier.get() == Fluids.EMPTY)
				i = 0;
			else
				i = FluidStack.loadFluidStackFromNBT(nbt.getCompound("fluid")).getAmount();
			
			if(KeyboardHelper.isHoldingShift())
			{
				tooltip.add(new StringTextComponent(i + " / 20000mB"));
			}
			else
			{
				tooltip.add(new StringTextComponent(i / 1000 + " / 20B"));
			}
		}
		else
		{
			nbt.put("fluid", new FluidStack(supplier.get(), 20000).writeToNBT(new CompoundNBT()));
		}
		
		stack.setTag(nbt);
		
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	public Supplier<? extends Fluid> getFluid()
	{
		return supplier;
	}
}