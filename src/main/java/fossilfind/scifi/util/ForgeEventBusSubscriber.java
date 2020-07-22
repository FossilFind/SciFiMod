package fossilfind.scifi.util;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.init.DimensionInit;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SciFiMod.MODID, bus = Bus.FORGE)
public class ForgeEventBusSubscriber
{
	@SubscribeEvent
	public static void onRegisterDimension(final RegisterDimensionsEvent event)
	{
		DimensionInit.registerDimensions();
	}
}