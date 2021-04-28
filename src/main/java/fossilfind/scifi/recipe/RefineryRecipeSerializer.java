package fossilfind.scifi.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RefineryRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RefineryRecipe>
{
	@SuppressWarnings("deprecation")
	@Override
	public RefineryRecipe read(ResourceLocation recipeId, JsonObject json)
	{
		if (!json.has("ingredient")) throw new JsonSyntaxException("Missing ingredient, expected to find a string or object");
		Ingredient input;
		if(json.get("ingredient").isJsonObject())
			input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
		else
		{
			String item = JSONUtils.getString(json, "ingredient");
			ResourceLocation location = new ResourceLocation(item);
			input = Ingredient.fromStacks(new ItemStack(Registry.ITEM.getValue(location).orElseThrow(() ->
			{
				return new IllegalStateException("Item: " + item + " does not exist");
			})));
		}
		
		if (!json.has("result")) throw new JsonSyntaxException("Missing result, expected to find a string or object");
		ItemStack output;
		if(json.get("result").isJsonObject())
			output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		else
		{
			String item = JSONUtils.getString(json, "result");
			ResourceLocation location = new ResourceLocation(item);
			output = new ItemStack(Registry.ITEM.getValue(location).orElseThrow(() ->
			{
				return new IllegalStateException("Item: " + item + " does not exist");
			}));
		}
		
		int cookingTime = JSONUtils.getInt(json, "cookingtime");
		
		return new RefineryRecipe(recipeId, input, output, cookingTime);
	}
	
	@Override
	public RefineryRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
	{
		ItemStack output = buffer.readItemStack();
		Ingredient input = Ingredient.read(buffer);
		int cookingTime = buffer.readInt();

		return new RefineryRecipe(recipeId, input, output, cookingTime);
	}
	
	@Override
	public void write(PacketBuffer buffer, RefineryRecipe recipe)
	{
		Ingredient input = recipe.getIngredients().get(0);
		input.write(buffer);

		buffer.writeItemStack(recipe.getRecipeOutput(), false);
		
		buffer.writeInt(recipe.getCookingTime());
	}
}