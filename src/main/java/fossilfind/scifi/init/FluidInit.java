package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
//import fossilfind.scifi.fluid.SeawaterFluid;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidInit
{
	public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, SciFiMod.MODID);
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, SciFiMod.MODID);
	
	public static final RegistryObject<FlowingFluidBlock> SEAWATER_BLOCK = BLOCKS.register("seawater", () -> new FlowingFluidBlock(FluidInit.SEAWATER, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100).noDrops()));
	public static final RegistryObject<FlowingFluid> SEAWATER = FLUIDS.register("seawater", () ->  new ForgeFlowingFluid.Source(FluidInit.SEAWATER_PROPERTIES));
	public static final RegistryObject<FlowingFluid> SEAWATER_FLOW = FLUIDS.register("seawater_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.SEAWATER_PROPERTIES));
	public static final ForgeFlowingFluid.Properties SEAWATER_PROPERTIES = new ForgeFlowingFluid.Properties(SEAWATER, SEAWATER_FLOW, FluidAttributes.builder(new ResourceLocation(SciFiMod.MODID, "block/seawater_still"), new ResourceLocation(SciFiMod.MODID, "block/seawater_flow"))).bucket(ItemInit.SEAWATER_BUCKET).block(SEAWATER_BLOCK);
	
	public static final RegistryObject<FlowingFluid> HYDROGEN = FLUIDS.register("hydrogen", () -> new ForgeFlowingFluid.Source(FluidInit.HYDROGEN_PROPERTIES));
	public static final RegistryObject<FlowingFluid> HYDROGEN_FLOW = FLUIDS.register("hydrogen_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.HYDROGEN_PROPERTIES));
	public static final ForgeFlowingFluid.Properties HYDROGEN_PROPERTIES = new ForgeFlowingFluid.Properties(HYDROGEN, HYDROGEN_FLOW, FluidAttributes.builder(new ResourceLocation(SciFiMod.MODID, "block/hydrogen_still"), new ResourceLocation(SciFiMod.MODID, "block/hydrogen_flow")));
	
	public static final RegistryObject<FlowingFluid> OXYGEN = FLUIDS.register("oxygen", () -> new ForgeFlowingFluid.Source(FluidInit.OXYGEN_PROPERTIES));
	public static final RegistryObject<FlowingFluid> OXYGEN_FLOW = FLUIDS.register("oxygen_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.OXYGEN_PROPERTIES));
	public static final ForgeFlowingFluid.Properties OXYGEN_PROPERTIES = new ForgeFlowingFluid.Properties(OXYGEN, OXYGEN_FLOW, FluidAttributes.builder(new ResourceLocation(SciFiMod.MODID, "block/oxygen_still"), new ResourceLocation(SciFiMod.MODID, "block/oxygen_flow")));

	public static final RegistryObject<FlowingFluid> CHLORINE = FLUIDS.register("chlorine", () -> new ForgeFlowingFluid.Source(FluidInit.CHLORINE_PROPERTIES));
	public static final RegistryObject<FlowingFluid> CHLORINE_FLOW = FLUIDS.register("chlorine_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.CHLORINE_PROPERTIES));
	public static final ForgeFlowingFluid.Properties CHLORINE_PROPERTIES = new ForgeFlowingFluid.Properties(CHLORINE, CHLORINE_FLOW, FluidAttributes.builder(new ResourceLocation(SciFiMod.MODID, "block/chlorine_still"), new ResourceLocation(SciFiMod.MODID, "block/chlorine_flow")));
	
	public static final RegistryObject<FlowingFluid> STEAM = FLUIDS.register("steam", () -> new ForgeFlowingFluid.Source(FluidInit.STEAM_PROPERTIES));
	public static final RegistryObject<FlowingFluid> STEAM_FLOW = FLUIDS.register("steam_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.STEAM_PROPERTIES));
	public static final ForgeFlowingFluid.Properties STEAM_PROPERTIES = new ForgeFlowingFluid.Properties(STEAM, STEAM_FLOW, FluidAttributes.builder(new ResourceLocation(SciFiMod.MODID, "block/steam_still"), new ResourceLocation(SciFiMod.MODID, "block/steam_flow")));
	
	public static class Tags
	{
		public static final Tag<Fluid> SEAWATER = new FluidTags.Wrapper(new ResourceLocation(SciFiMod.MODID, "seawater"));
	}
}