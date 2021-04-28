package fossilfind.scifi.recipe;

import javax.annotation.Nonnull;

import fossilfind.scifi.SciFiMod;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface IRefineryRecipe extends IRecipe<LockableTileEntity>
{
	ResourceLocation RECIPE_TYPE_ID = new ResourceLocation(SciFiMod.MODID, "refining");
	
	@Nonnull
	@Override
	default IRecipeType<?> getType()
	{
		return Registry.RECIPE_TYPE.getValue(RECIPE_TYPE_ID).get();
	}
	
	@Override
	default boolean canFit(int width, int height)
	{
		return false;
	}
	
	Ingredient getInput();
}