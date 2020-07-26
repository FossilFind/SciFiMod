package fossilfind.scifi.container;

import java.util.Objects;

import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.ContainerInit;
import fossilfind.scifi.tileentity.VehicleConstructorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class VehicleConstructorContainer extends Container
{
	private IWorldPosCallable canInteractWithCallable;
	
	public VehicleConstructorContainer(int id, PlayerInventory inventory, VehicleConstructorTileEntity te)
	{
		super(ContainerInit.VEHICLE_CONSTRUCTOR.get(), id);
		
		canInteractWithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());
		
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 4; col++)
				addSlot(new Slot(te, row * 4 + col, 8 + col * 18, 17 + row * 18));
		
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 9; col++)
				addSlot(new Slot(inventory, 9 + row * 9 + col, 8 + col * 18, 84 + row * 18));
		
		for(int col = 0; col < 9; col++)
			addSlot(new Slot(inventory, col, 8 + col * 18, 142));
	}
	
	private static VehicleConstructorTileEntity getTileEntity(final PlayerInventory inventory, final PacketBuffer data)
	{
		Objects.requireNonNull(inventory, "PlayerInventory can not be null");
		Objects.requireNonNull(data, "PacketBuffer can not be null");
		
		final TileEntity te = inventory.player.world.getTileEntity(data.readBlockPos());
		
		if(te instanceof VehicleConstructorTileEntity)
			return (VehicleConstructorTileEntity) te;
		
		throw new IllegalStateException(te + " is not the correct TileEntity");
	}
	
	public VehicleConstructorContainer(int id, PlayerInventory inventory, PacketBuffer data)
	{
		this(id, inventory, getTileEntity(inventory, data));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.VEHICLE_CONSTRUCTOR.get());
	}
}