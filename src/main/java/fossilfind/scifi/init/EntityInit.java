package fossilfind.scifi.init;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.entity.RocketEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, SciFiMod.MODID);
	
	public static final RegistryObject<EntityType<RocketEntity>> ROCKET = ENTITIES.register("rocket", () -> EntityType.Builder.<RocketEntity>create(RocketEntity::new, EntityClassification.MISC).size(1, 2).build(new ResourceLocation(SciFiMod.MODID, "rocket").toString()));
}