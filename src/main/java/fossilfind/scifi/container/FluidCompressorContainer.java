package fossilfind.scifi.container;

import java.util.Objects;

import fossilfind.scifi.container.slot.FluidIntakeSlot;
import fossilfind.scifi.container.slot.FluidOutakeSlot;
import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.ContainerInit;
import fossilfind.scifi.tileentity.FluidCompressorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidCompressorContainer extends Container
{
	private FluidCompressorTileEntity te;
	private IWorldPosCallable canInteractwithCallable;
	
	public FluidCompressorContainer(int id, PlayerInventory inventory, FluidCompressorTileEntity te)
	{
		super(ContainerInit.FLUID_COMPRESSOR.get(), id);
		
		this.te = te;
		canInteractwithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());
		
		addSlot(new FluidIntakeSlot(te, te, 0, 32, 35));
		addSlot(new FluidOutakeSlot(te, 1, 128, 35));
		
		for(int row = 0; row < 3; row++)
		{
			for(int col = 0; col < 9; col++)
			{
				addSlot(new Slot(inventory, 9 + row * 9 + col, 8 + col * 18, 84 + row * 18));
			}
		}
		
		for(int col = 0; col < 9; col++)
		{
			addSlot(new Slot(inventory, col, 8 + col * 18, 142));
		}
	}
	
	private static FluidCompressorTileEntity getTileEntity(final PlayerInventory inventory, final PacketBuffer data)
	{
		Objects.requireNonNull(inventory, "PlayerInventory can not be null");
		Objects.requireNonNull(data, "PacketBuffer can not be null");
		
		final TileEntity te = inventory.player.world.getTileEntity(data.readBlockPos());
		
		if(te instanceof FluidCompressorTileEntity) return (FluidCompressorTileEntity) te;
		
		throw new IllegalStateException(te + " is not the correct TileEntity");
	}
	
	public FluidCompressorContainer(int id, PlayerInventory inventory, PacketBuffer data)
	{
		this(id, inventory, getTileEntity(inventory, data));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(canInteractwithCallable, playerIn, BlockInit.FLUID_COMPRESSOR.get());
	}
	
	public FluidTank getTank()
	{
		return te.getTank();
	}
}