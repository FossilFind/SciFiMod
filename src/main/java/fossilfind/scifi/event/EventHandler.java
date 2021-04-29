package fossilfind.scifi.event;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.init.DimensionInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SciFiMod.MODID, bus = Bus.FORGE)
public class EventHandler
{
	public static final AttributeModifier MOON_GRAVITY = new AttributeModifier("Gravity", (1.62 / 9.8) - 1, AttributeModifier.Operation.MULTIPLY_BASE).setSaved(false);
	
	@SubscribeEvent
	public static void livingUpdateEvent(LivingUpdateEvent event)
	{
		IAttributeInstance attribute = event.getEntityLiving().getAttribute(LivingEntity.ENTITY_GRAVITY);
		
		if(event.getEntityLiving().dimension.getModType() == DimensionInit.MOON.get())
			if(!attribute.hasModifier(MOON_GRAVITY))
				attribute.applyModifier(MOON_GRAVITY);
		else
			if(attribute.hasModifier(MOON_GRAVITY))
				attribute.removeModifier(MOON_GRAVITY);
	}
}