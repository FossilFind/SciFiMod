package fossilfind.scifi.util.recipes;

import net.minecraft.item.ItemStack;

public class RefineryRecipe
{
	private int cookTime;
	private ItemStack ingredient, result1, result2;
	
	public RefineryRecipe(int cookTime, ItemStack ingredient, ItemStack result1, ItemStack result2)
	{
		this.cookTime = cookTime;
		this.ingredient = ingredient;
		this.result1 = result1;
		this.result2 = result2;
	}
	
	public int getCookTime()
	{
		return cookTime;
	}
	
	public ItemStack getIngredient()
	{
		return ingredient;
	}
	
	public ItemStack getResult1()
	{
		return result1;
	}
	
	public ItemStack getResult2()
	{
		return result2;
	}
}