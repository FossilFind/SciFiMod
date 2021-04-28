package fossilfind.scifi.util.recipes;

import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public class OldRefineryRecipe
{
	public static final OldRefineryRecipe IRON_ORE = new OldRefineryRecipe(60, new ItemStack(Items.IRON_ORE, 1), new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.IRON_INGOT, 1));
	public static final OldRefineryRecipe GOLD_ORE = new OldRefineryRecipe(60, new ItemStack(Items.GOLD_ORE, 1), new ItemStack(Items.GOLD_INGOT, 1), new ItemStack(Items.GOLD_INGOT, 1));
	public static final OldRefineryRecipe NETHER_QUARTZ_ORE = new OldRefineryRecipe(60, new ItemStack(Items.NETHER_QUARTZ_ORE, 1), new ItemStack(Items.QUARTZ, 1), new ItemStack(ItemInit.GRAPHITE.get(), 1));
	public static final OldRefineryRecipe BAUXITE = new OldRefineryRecipe(60, new ItemStack(BlockInit.BAUXITE.get().asItem(), 1), new ItemStack(ItemInit.ALUMINUM_INGOT.get(), 1), new ItemStack(ItemInit.ALUMINUM_INGOT.get(), 1));
	public static final OldRefineryRecipe RUTILE = new OldRefineryRecipe(60, new ItemStack(BlockInit.RUTILE.get().asItem(), 1), new ItemStack(ItemInit.TITANIUM_INGOT.get(), 1), new ItemStack(ItemInit.TITANIUM_INGOT.get(), 1));
	public static final OldRefineryRecipe TANTINITE = new OldRefineryRecipe(60, new ItemStack(BlockInit.TANTINITE.get().asItem(), 1), new ItemStack(ItemInit.TANTALUM_INGOT.get(), 1), new ItemStack(ItemInit.TANTALUM_INGOT.get(), 1));
	
	private int cookTime;
	private ItemStack ingredient, result1, result2;
	
	public OldRefineryRecipe(int cookTime, ItemStack ingredient, ItemStack result1, ItemStack result2)
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
	
	public CompoundNBT writeToNBT(CompoundNBT compound)
	{
		compound.put("ingredient", ingredient.write(new CompoundNBT()));
		return compound;
	}
	
	public static OldRefineryRecipe readFromNBT(CompoundNBT compound)
	{
		return getRecipe(ItemStack.read(compound.getCompound("ingredient")));
	}
	
	public static OldRefineryRecipe getRecipe(ItemStack ingredient)
	{
		if(ingredient.getItem() == Items.IRON_ORE)
			return OldRefineryRecipe.IRON_ORE;
		if(ingredient.getItem() == Items.GOLD_ORE)
			return OldRefineryRecipe.GOLD_ORE;
		if(ingredient.getItem() == Items.NETHER_QUARTZ_ORE)
			return OldRefineryRecipe.NETHER_QUARTZ_ORE;
		if(ingredient.getItem() == BlockInit.BAUXITE.get().asItem())
			return OldRefineryRecipe.BAUXITE;
		if(ingredient.getItem() == BlockInit.RUTILE.get().asItem())
			return OldRefineryRecipe.RUTILE;
		if(ingredient.getItem() == BlockInit.TANTINITE.get().asItem())
			return OldRefineryRecipe.TANTINITE;
		
		return null;
	}
}