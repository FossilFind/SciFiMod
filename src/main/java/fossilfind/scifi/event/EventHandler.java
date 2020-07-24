package fossilfind.scifi.event;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.init.DimensionInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SciFiMod.MODID, bus = Bus.FORGE)
public class EventHandler
{
	@SubscribeEvent
	public static void playerTickEvent(PlayerTickEvent event)
	{
		if(event.player.dimension.getModType() == DimensionInit.MOON.get())
			LivingEntity.ENTITY_GRAVITY = new RangedAttribute(null, "forge.entity_gravity", 0.04D, -8.0D, 8.0D).setShouldWatch(true);
		else
			LivingEntity.ENTITY_GRAVITY = new RangedAttribute(null, "forge.entity_gravity", 0.08D, -8.0D, 8.0D).setShouldWatch(true);
	}
}