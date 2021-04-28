package fossilfind.scifi.recipe;

import fossilfind.scifi.init.RecipeInit;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RefineryRecipe implements IRefineryRecipe
{
	private final ResourceLocation id;
	private Ingredient input;
	private final ItemStack output;
	private int cookingTime;
	
	public RefineryRecipe(ResourceLocation id, Ingredient input, ItemStack output, int cookingTime)
	{
		this.id = id;
		this.output = output;
		this.input = input;
		this.cookingTime = cookingTime;
	}

	@Override
	public boolean matches(LockableTileEntity inv, World worldIn)
	{
		if(input.test(inv.getStackInSlot(0)))
			return true;
		return false;
	}
	
	@Override
	public ItemStack getCraftingResult(LockableTileEntity inv)
	{
		return output;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return output;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return RecipeInit.REFINERY_SERIALIZER.get();
	}
	
	@Override
	public Ingredient getInput()
	{
		return input;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return NonNullList.from(null, input);
	}
	
	public int getCookingTime()
	{
		return cookingTime;
	}
}