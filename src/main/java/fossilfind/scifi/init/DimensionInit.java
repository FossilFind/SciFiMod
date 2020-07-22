package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.world.dimension.MoonDimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DimensionInit
{
	public static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, SciFiMod.MODID);
	
	public static final RegistryObject<ModDimension> MOON = DIMENSIONS.register("moon", () -> ModDimension.withFactory(MoonDimension::new));
	
	public static void registerDimensions() 
	{
		registerDimension(MOON);
	}
	
	private static void registerDimension(RegistryObject<ModDimension> dimension) 
	{
		if(DimensionType.byName(dimension.get().getRegistryName()) == null)
		{
			DimensionManager.registerDimension(dimension.get().getRegistryName(), dimension.get(), null, true);
		}
	}
}