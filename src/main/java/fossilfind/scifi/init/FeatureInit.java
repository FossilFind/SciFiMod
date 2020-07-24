package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.world.gen.feature.SaltDepositFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FeatureInit
{
	public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, SciFiMod.MODID);
	
	public static final RegistryObject<SaltDepositFeature> SALT_DEPOSIT = FEATURES.register("salt_deposit", () -> new SaltDepositFeature(NoFeatureConfig::deserialize));
}