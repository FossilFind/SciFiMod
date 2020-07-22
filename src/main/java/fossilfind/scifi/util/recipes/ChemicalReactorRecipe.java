package fossilfind.scifi.util.recipes;

import fossilfind.scifi.init.FluidInit;
import fossilfind.scifi.init.ItemInit;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fluids.FluidStack;

public class ChemicalReactorRecipe
{
	public static final ChemicalReactorRecipe SILICA_FIBER = new ChemicalReactorRecipe(60, new FluidStack(FluidInit.STEAM.get(), 1000), new FluidStack(Fluids.WATER, 1000), new ItemStack(Items.QUARTZ, 1), new ItemStack(ItemInit.CAUSTIC_SODA.get(), 1), ItemStack.EMPTY, new ItemStack(ItemInit.SILICA_FIBER.get(), 1));
	
	private int cookTime;
	private FluidStack fluidIngredient1, fluidIngredient2;
	private ItemStack ingredient1, ingredient2, ingredient3, result;
	
	public ChemicalReactorRecipe(int cookTime, FluidStack fluidIngredient1, FluidStack fluidIngredient2, ItemStack ingredient1, ItemStack ingredient2, ItemStack ingredient3, ItemStack result)
	{
		this.cookTime = cookTime;
		this.fluidIngredient1 = fluidIngredient1;
		this.fluidIngredient2 = fluidIngredient2;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
		this.result = result;
	}
	
	public int getCookTime()
	{
		return cookTime;
	}
	
	public FluidStack getFluidIngredient1()
	{
		return fluidIngredient1;
	}
	
	public FluidStack getFluidIngredient2()
	{
		return fluidIngredient2;
	}
	
	public ItemStack getIngredient1()
	{
		return ingredient1;
	}
	
	public ItemStack getIngredient2()
	{
		return ingredient2;
	}
	
	public ItemStack getIngredient3()
	{
		return ingredient3;
	}
	
	public ItemStack getResult()
	{
		return result;
	}
}