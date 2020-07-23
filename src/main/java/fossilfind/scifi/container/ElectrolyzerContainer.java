package fossilfind.scifi.container;

import java.util.Objects;

import fossilfind.scifi.container.slot.FluidIntakeSlot;
import fossilfind.scifi.container.slot.FluidOutakeSlot;
import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.ContainerInit;
import fossilfind.scifi.tileentity.ElectrolyzerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ElectrolyzerContainer extends Container
{
	public ElectrolyzerTileEntity te;
	public IWorldPosCallable canInteractWithCallable;
	
	public ElectrolyzerContainer(int id, PlayerInventory inventory, ElectrolyzerTileEntity te)
	{
		super(ContainerInit.ELECTROLYZER.get(), id);
		
		this.te = te;
		canInteractWithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());
		
		addSlot(new FluidIntakeSlot(te, te, 0, 8, 16));
		addSlot(new FluidOutakeSlot(te, 1, 44, 16));
		addSlot(new FluidOutakeSlot(te, 2, 80, 16));
		addSlot(new FluidOutakeSlot(te, 3, 116, 16));
		addSlot(new FluidOutakeSlot(te, 4, 152, 16));
		
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
	
	private static ElectrolyzerTileEntity getTileEntity(final PlayerInventory inventory, final PacketBuffer data)
	{
		Objects.requireNonNull(inventory, "PlayerInventory can not be null");
		Objects.requireNonNull(data, "PacketBuffer can not be null");
		
		final TileEntity te = inventory.player.world.getTileEntity(data.readBlockPos());
		
		if(te instanceof ElectrolyzerTileEntity) return (ElectrolyzerTileEntity) te;
		
		throw new IllegalStateException(te + " is not the correct TileEntity");
	}
	
	public ElectrolyzerContainer(final int id, final PlayerInventory inventory, final PacketBuffer data)
	{
		this(id, inventory, getTileEntity(inventory, data));
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.ELECTROLYZER.get());
	}
	
	public FluidTank getTank(int index)
	{
		return te.getTank(index);
	}
	
	public int getCookProgressionScaled()
	{
		int i = te.cookTime;
		int j = te.recipe != null ? te.recipe.getCookTime() : 0;
		
		return i != 0 && j != 0 ? i * 125 / j : 0;
	}
}