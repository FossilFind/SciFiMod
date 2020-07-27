package fossilfind.scifi.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class RocketEntity extends Entity
{
	public RocketEntity(EntityType<?> entityTypeIn, World worldIn)
	{
		super(entityTypeIn, worldIn);
	}
	
	@Override
	protected void registerData()
	{
	}
	
	@Override
	public boolean processInitialInteract(PlayerEntity player, Hand hand)
	{
		if(player.isSecondaryUseActive())
			return false;
		else
			return !world.isRemote && player.startRiding(this);
	}
	
	@Override
	protected void readAdditional(CompoundNBT compound)
	{
	}
	
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return null;
	}
}