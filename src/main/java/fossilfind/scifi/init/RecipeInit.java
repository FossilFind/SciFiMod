package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.recipe.IRefineryRecipe;
import fossilfind.scifi.recipe.RefineryRecipe;
import fossilfind.scifi.recipe.RefineryRecipeSerializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeInit
{
	public static final IRecipeSerializer<RefineryRecipe> REFINERY_RECIPE_SERIALIZER = new RefineryRecipeSerializer();
	public static final IRecipeType<IRefineryRecipe> REFINERY_TYPE = registerType(IRefineryRecipe.RECIPE_TYPE_ID);

	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, SciFiMod.MODID);

	public static final RegistryObject<IRecipeSerializer<?>> REFINERY_SERIALIZER = RECIPE_SERIALIZERS.register("refining", () -> REFINERY_RECIPE_SERIALIZER);

	private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T extends IRecipeType> T registerType(ResourceLocation recipeTypeId) {
		return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
	}
}