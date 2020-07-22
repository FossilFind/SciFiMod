package fossilfind.scifi.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class CausticSodaItem extends Item
{
	public CausticSodaItem(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) 
	{
		tooltip.add(new StringTextComponent("\u00A78" + "Sodium Hydroxide NaOH"));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}