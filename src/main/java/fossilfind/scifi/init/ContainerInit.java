package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.inventory.container.ChemicalReactorContainer;
import fossilfind.scifi.inventory.container.ElectrolyzerContainer;
import fossilfind.scifi.inventory.container.RefineryContainer;
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
}