package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.block.ChemicalReactorBlock;
import fossilfind.scifi.block.ElectrolyzerBlock;
import fossilfind.scifi.block.FluidCompressorBlock;
import fossilfind.scifi.block.RefineryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit
{
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, SciFiMod.MODID);
	
	public static final RegistryObject<Block> ALUMINUM_BLOCK = BLOCKS.register("aluminum_block", () -> new Block(Block.Properties.from(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> TITANIUM_BLOCK = BLOCKS.register("titanium_block", () -> new Block(Block.Properties.from(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> TANTULUM_BLOCK = BLOCKS.register("tantulum_block", () -> new Block(Block.Properties.from(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> TANTULUM_CARBIDE_BLOCK = BLOCKS.register("tantulum_carbide_block", () -> new Block(Block.Properties.from(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> BAUXITE = BLOCKS.register("bauxite", () -> new Block(Block.Properties.from(Blocks.IRON_ORE)));
	public static final RegistryObject<Block> TANTINITE = BLOCKS.register("tantinite", () -> new Block(Block.Properties.from(Blocks.IRON_ORE)));
	public static final RegistryObject<Block> RUTILE = BLOCKS.register("rutile", () -> new Block(Block.Properties.from(Blocks.IRON_ORE)));
	public static final RegistryObject<Block> ALUMINUM_FRAME = BLOCKS.register("aluminum_frame", () -> new Block(Block.Properties.from(Blocks.IRON_BLOCK).notSolid()));
	public static final RegistryObject<Block> IRON_FRAME = BLOCKS.register("iron_frame", () -> new Block(Block.Properties.from(ALUMINUM_FRAME.get())));
	public static final RegistryObject<Block> TITANIUM_FRAME = BLOCKS.register("titanium_frame", () -> new Block(Block.Properties.from(ALUMINUM_FRAME.get())));
	public static final RegistryObject<Block> TANTULUM_FRAME = BLOCKS.register("tantulum_frame", () -> new Block(Block.Properties.from(ALUMINUM_FRAME.get())));
	public static final RegistryObject<Block> TANTULUM_CARBIDE_FRAME = BLOCKS.register("tantulum_carbide_frame", () -> new Block(Block.Properties.from(ALUMINUM_FRAME.get())));
	public static final RegistryObject<Block> LUNAR_REGOLITH = BLOCKS.register("lunar_regolith", () -> new Block(Block.Properties.from(Blocks.SAND)));
	public static final RegistryObject<Block> REFINERY = BLOCKS.register("refinery", () -> new RefineryBlock(Block.Properties.from(Blocks.FURNACE).lightValue(0).sound(SoundType.ANVIL)));
	public static final RegistryObject<Block> ELECTROLYZER = BLOCKS.register("electrolyzer", () -> new ElectrolyzerBlock(Block.Properties.from(REFINERY.get())));
	public static final RegistryObject<Block> CHEMICAL_REACTOR = BLOCKS.register("chemical_reactor", () -> new ChemicalReactorBlock(Block.Properties.from(REFINERY.get())));
	public static final RegistryObject<Block> FLUID_COMPRESSOR = BLOCKS.register("fluid_compressor", () -> new FluidCompressorBlock(Block.Properties.from(REFINERY.get())));
}