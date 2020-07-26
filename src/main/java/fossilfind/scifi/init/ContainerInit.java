package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.container.ChemicalReactorContainer;
import fossilfind.scifi.container.ElectrolyzerContainer;
import fossilfind.scifi.container.FluidCompressorContainer;
import fossilfind.scifi.container.RefineryContainer;
import fossilfind.scifi.container.VehicleConstructorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit
{
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, SciFiMod.MODID);
	
	public static final RegistryObject<ContainerType<RefineryContainer>> REFINERY = CONTAINERS.register("refinery", () -> IForgeContainerType.create(RefineryContainer::new));
	public static final RegistryObject<ContainerType<ElectrolyzerContainer>> ELECTROLYZER = CONTAINERS.register("electrolyzer", () -> IForgeContainerType.create(ElectrolyzerContainer::new));
	public static final RegistryObject<ContainerType<ChemicalReactorContainer>> CHEMICAL_REACTOR = CONTAINERS.register("chemical_reactor", () -> IForgeContainerType.create(ChemicalReactorContainer::new));
	public static final RegistryObject<ContainerType<FluidCompressorContainer>> FLUID_COMPRESSOR = CONTAINERS.register("fluid_compressor", () -> IForgeContainerType.create(FluidCompressorContainer::new));
	public static final RegistryObject<ContainerType<VehicleConstructorContainer>> VEHICLE_CONSTRUCTOR = CONTAINERS.register("vehicle_constructor", () -> IForgeContainerType.create(VehicleConstructorContainer::new));
}