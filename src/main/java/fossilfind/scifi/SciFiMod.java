package fossilfind.scifi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fossilfind.scifi.client.gui.ChemicalReactorScreen;
import fossilfind.scifi.client.gui.ElectrolyzerScreen;
import fossilfind.scifi.client.gui.FluidCompressorScreen;
import fossilfind.scifi.client.gui.RefineryScreen;
import fossilfind.scifi.client.gui.VehicleConstructorScreen;
import fossilfind.scifi.client.renderer.entity.RocketEntityRender;
import fossilfind.scifi.init.BiomeInit;
import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.ContainerInit;
import fossilfind.scifi.init.DimensionInit;
import fossilfind.scifi.init.EntityInit;
import fossilfind.scifi.init.FeatureInit;
import fossilfind.scifi.init.FluidInit;
import fossilfind.scifi.init.ItemInit;
import fossilfind.scifi.init.RecipeInit;
import fossilfind.scifi.init.TileEntityInit;
import fossilfind.scifi.world.gen.SciFiFeatureGeneration;
import fossilfind.scifi.world.gen.SciFiOreGeneration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(SciFiMod.MODID)
@Mod.EventBusSubscriber(modid = SciFiMod.MODID, bus = Bus.MOD)
public class SciFiMod
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "scifi";
	public static SciFiMod instance;
	public static final ResourceLocation MOON_DIMENSION_TYPE = new ResourceLocation(MODID, "moon");
	
	public SciFiMod()
	{
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		
		ItemInit.ITEMS.register(bus);
		RecipeInit.RECIPE_SERIALIZERS.register(bus);
		BlockInit.BLOCKS.register(bus);
		TileEntityInit.TILE_ENTITIES.register(bus);
		ContainerInit.CONTAINERS.register(bus);
		EntityInit.ENTITIES.register(bus);
		FluidInit.BLOCKS.register(bus);
		FluidInit.FLUIDS.register(bus);
		BiomeInit.BIOMES.register(bus);
		FeatureInit.FEATURES.register(bus);
		DimensionInit.DIMENSIONS.register(bus);
		
		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);
		
		instance = this;
		MinecraftForge.EVENT_BUS.register(bus);
	}
	
	public void clientSetup(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(ContainerInit.REFINERY.get(), RefineryScreen::new);
    	ScreenManager.registerFactory(ContainerInit.ELECTROLYZER.get(), ElectrolyzerScreen::new);
    	ScreenManager.registerFactory(ContainerInit.CHEMICAL_REACTOR.get(), ChemicalReactorScreen::new);
    	ScreenManager.registerFactory(ContainerInit.FLUID_COMPRESSOR.get(), FluidCompressorScreen::new);
    	ScreenManager.registerFactory(ContainerInit.VEHICLE_CONSTRUCTOR.get(), VehicleConstructorScreen::new);
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityInit.ROCKET.get(), RocketEntityRender::new);
	}
	
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		SciFiOreGeneration.generateOres();
		SciFiFeatureGeneration.generateFeatures();
	}
	
	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
    	final IForgeRegistry<Item> registry = event.getRegistry();
    	
    	BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> 
    	{
    		final Item.Properties properties = new Item.Properties().group(SciFiItemGroup.instance);
    		final BlockItem item = new BlockItem(block, properties);
    		item.setRegistryName(block.getRegistryName());
    		registry.register(item);
    	});
    	
    	LOGGER.debug("Registered BlockItems");
    }
	
	@SubscribeEvent
    public static void onRegisterBiomes(final RegistryEvent.Register<Biome> event)
    {
    	BiomeInit.registerBiomes();
    }
	
	public static class SciFiItemGroup extends ItemGroup
	{
		public static final SciFiItemGroup instance = new SciFiItemGroup(ItemGroup.GROUPS.length, "scifi_tab");
		
		public SciFiItemGroup(int index, String label)
		{
			super(index, label);
		}
		
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ItemInit.ALUMINUM_INGOT.get());
		}
	}
}