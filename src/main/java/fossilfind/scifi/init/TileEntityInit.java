package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.tileentity.ChemicalReactorTileEntity;
import fossilfind.scifi.tileentity.ElectrolyzerTileEntity;
import fossilfind.scifi.tileentity.RefineryTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit
{
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, SciFiMod.MODID);
	
	public static final RegistryObject<TileEntityType<RefineryTileEntity>> REFINERY = TILE_ENTITIES.register("refinery", () ->  TileEntityType.Builder.create(RefineryTileEntity::new, BlockInit.REFINERY.get()).build(null));
	public static final RegistryObject<TileEntityType<ElectrolyzerTileEntity>> ELECTROLYZER = TILE_ENTITIES.register("electrolyzer", () -> TileEntityType.Builder.create(ElectrolyzerTileEntity::new, BlockInit.ELECTROLYZER.get()).build(null));
	public static final RegistryObject<TileEntityType<ChemicalReactorTileEntity>> CHEMICAL_REACTOR = TILE_ENTITIES.register("chemical_reactor", () -> TileEntityType.Builder.create(ChemicalReactorTileEntity::new, BlockInit.CHEMICAL_REACTOR.get()).build(null));
	public static final RegistryObject<TileEntityType<FluidCompressorTileEntity>> FLUID_COMPRESSOR = TILE_ENTITIES.register("fluid_compressor", () -> TileEntityType.Builder.create(FluidCompressorTileEntity::new, BlockInit.FLUID_COMPRESSOR.get()).build(null));
}