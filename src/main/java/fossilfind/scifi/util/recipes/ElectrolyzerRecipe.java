package fossilfind.scifi.util.recipes;

import fossilfind.scifi.init.FluidInit;
import fossilfind.scifi.init.ItemInit;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

public class ElectrolyzerRecipe
{
	public static final ElectrolyzerRecipe WATER = new ElectrolyzerRecipe(60, new FluidStack(Fluids.WATER, 1000),
			new FluidStack(FluidInit.HYDROGEN.get(), 500), new FluidStack(FluidInit.OXYGEN.get(), 250), FluidStack.EMPTY, FluidStack.EMPTY,
			ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
	public static final ElectrolyzerRecipe SEAWATER = new ElectrolyzerRecipe(60, new FluidStack(FluidInit.SEAWATER.get(), 1000),
			new FluidStack(FluidInit.HYDROGEN.get(), 500), new FluidStack(FluidInit.CHLORINE.get(), 500), FluidStack.EMPTY, FluidStack.EMPTY,
			ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(ItemInit.CAUSTIC_SODA.get(), 1), ItemStack.EMPTY);
	
	private FluidStack ingredient, fluidResult1, fluidResult2, fluidResult3, fluidResult4;
	private ItemStack result1, result2, result3, result4;
	private int cookTime;
	
	public ElectrolyzerRecipe(int cookTime, FluidStack ingredient, FluidStack fluidResult1, FluidStack fluidResult2, FluidStack fluidResult3, FluidStack fluidResult4,
			ItemStack result1, ItemStack result2, ItemStack result3, ItemStack result4)
	{
		this.cookTime = cookTime;
		this.ingredient = ingredient;
		this.fluidResult1 = fluidResult1;
		this.fluidResult2 = fluidResult2;
		this.fluidResult3 = fluidResult3;
		this.fluidResult4 = fluidResult4;
		this.result1 = result1;
		this.result2 = result2;
		this.result3 = result3;
		this.result4 = result4;
	}
	
	public int getCookTime()
	{
		return cookTime;
	}
	
	public FluidStack getIngredient()
	{
		return ingredient;
	}

	public FluidStack getFluidResult1()
	{
		return fluidResult1;
	}

	public FluidStack getFluidResult2()
	{
		return fluidResult2;
	}

	public FluidStack getFluidResult3()
	{
		return fluidResult3;
	}

	public FluidStack getFluidResult4()
	{
		return fluidResult4;
	}

	public ItemStack getResult1()
	{
		return result1;
	}

	public ItemStack getResult2()
	{
		return result2;
	}

	public ItemStack getResult3()
	{
		return result3;
	}

	public ItemStack getResult4()
	{
		return result4;
	}
	
	public CompoundNBT writeToNBT(CompoundNBT compound)
	{
		compound.put("ingredient", ingredient.writeToNBT(new CompoundNBT()));
		return compound;
	}
	
	public static ElectrolyzerRecipe readFromNBT(CompoundNBT compound)
	{
		FluidStack stack = FluidStack.loadFluidStackFromNBT(compound.getCompound("ingredient"));
		
		return getRecipe(stack);
	}
	
	public static ElectrolyzerRecipe getRecipe(FluidStack ingredient)
	{
		if(ingredient.getFluid() == Fluids.WATER)
			return ElectrolyzerRecipe.WATER;
		if(ingredient.getFluid() == FluidInit.SEAWATER.get())
			return ElectrolyzerRecipe.SEAWATER;
		
		return null;
	}
}