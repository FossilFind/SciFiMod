package fossilfind.scifi.util.recipes;

import net.minecraftforge.fluids.FluidStack;

public class ElectrolyzerRecipe
{
	private FluidStack ingredient, result1, result2, result3, result4;
	private int cookTime;
	
	public ElectrolyzerRecipe(int cookTime, FluidStack ingredient, FluidStack result1, FluidStack result2, FluidStack result3, FluidStack result4)
	{
		this.cookTime = cookTime;
		this.ingredient = ingredient;
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
	
	public FluidStack getResult1()
	{
		return result1;
	}
	
	public FluidStack getResult2()
	{
		return result2;
	}
	
	public FluidStack getResult3()
	{
		return result3;
	}
	
	public FluidStack getResult4()
	{
		return result4;
	}
}