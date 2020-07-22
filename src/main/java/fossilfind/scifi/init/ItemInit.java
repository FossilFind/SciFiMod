package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.SciFiMod.SciFiItemGroup;
import fossilfind.scifi.item.CanisterItem;
import fossilfind.scifi.item.CausticSodaItem;
import fossilfind.scifi.item.WaterGlassItem;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, SciFiMod.MODID);

	public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot", () -> new Item(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot", () -> new Item(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> TANTALUM_CARBIDE_INGOT = ITEMS.register("tantalum_carbide_ingot", () -> new Item(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> TANTALUM_INGOT = ITEMS.register("tantalum_ingot", () -> new Item(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> GRAPHITE = ITEMS.register("graphite", () -> new Item(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> SILICA_FIBER = ITEMS.register("silica_fiber", () -> new Item(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> WATER_GLASS = ITEMS.register("water_glass", () -> new WaterGlassItem(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> CAUSTIC_SODA = ITEMS.register("caustic_soda", () -> new CausticSodaItem(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> HEAT_TILE = ITEMS.register("heat_tile", () -> new Item(new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> SEAWATER_BUCKET = ITEMS.register("seawater_bucket", () -> new BucketItem(FluidInit.SEAWATER, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> CANISTER = ITEMS.register("canister", () -> new CanisterItem(() -> Fluids.EMPTY, new Item.Properties().group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> WATER_CANISTER = ITEMS.register("water_canister", () -> new CanisterItem(() -> Fluids.WATER, new Item.Properties().containerItem(CANISTER.get()).maxStackSize(1).group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> SEAWATER_CANISTER = ITEMS.register("seawater_canister", () -> new CanisterItem(FluidInit.SEAWATER, new Item.Properties().containerItem(CANISTER.get()).maxStackSize(1).group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> HYDROGEN_CANISTER = ITEMS.register("hydrogen_canister", () -> new CanisterItem(FluidInit.HYDROGEN, new Item.Properties().containerItem(CANISTER.get()).maxStackSize(1).group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> OXYGEN_CANISTER = ITEMS.register("oxygen_canister", () -> new CanisterItem(FluidInit.OXYGEN, new Item.Properties().containerItem(CANISTER.get()).maxStackSize(1).group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> CHLORINE_CANISTER = ITEMS.register("chlorine_canister", () -> new CanisterItem(FluidInit.CHLORINE, new Item.Properties().containerItem(CANISTER.get()).maxStackSize(1).group(SciFiItemGroup.instance)));
	public static final RegistryObject<Item> STEAM_CANISTER = ITEMS.register("steam_canister", () -> new CanisterItem(FluidInit.STEAM, new Item.Properties().containerItem(CANISTER.get()).maxStackSize(1).group(SciFiItemGroup.instance)));
}