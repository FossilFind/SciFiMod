package fossilfind.scifi.container;

import java.util.Objects;

import fossilfind.scifi.container.slot.FluidIntakeSlot;
import fossilfind.scifi.container.slot.ResultSlot;
import fossilfind.scifi.init.BlockInit;
import fossilfind.scifi.init.ContainerInit;
import fossilfind.scifi.tileentity.ChemicalReactorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ChemicalReactorContainer extends Container
{
	private ChemicalReactorTileEntity te;
	private IWorldPosCallable canInteractWithCallable;
	
	public ChemicalReactorContainer(int id, PlayerInventory inventory, ChemicalReactorTileEntity te)
	{
		super(ContainerInit.CHEMICAL_REACTOR.get(), id);
		
		this.te = te;
		canInteractWithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());
		
		addSlot(new FluidIntakeSlot(te, te, 0, 17, 16));
		addSlot(new FluidIntakeSlot(te, te, 1, 53, 16));
		addSlot(new Slot(te, 2, 107, 17));
		addSlot(new Slot(te, 3, 107, 35));
		addSlot(new Slot(te, 4, 107, 53));
		addSlot(new ResultSlot(te, 5, 143, 17));
		addSlot(new ResultSlot(te, 6, 143, 35));
		addSlot(new ResultSlot(te, 7, 143, 53));
		
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
	
	private static ChemicalReactorTileEntity getTileEntity(final PlayerInventory inventory, final PacketBuffer data)
	{
		Objects.requireNonNull(inventory, "PlayerInventory can not be null");
		Objects.requireNonNull(data, "PacketBuffer can not be null");
		
		final TileEntity te = inventory.player.world.getTileEntity(data.readBlockPos());
		
		if(te instanceof ChemicalReactorTileEntity) return (ChemicalReactorTileEntity) te;
		
		throw new IllegalStateException(te + " is not the correct TileEntity");
	}
	
	public ChemicalReactorContainer(int id, PlayerInventory inventory, PacketBuffer data)
	{
		this(id, inventory, getTileEntity(inventory, data));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.CHEMICAL_REACTOR.get());
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		Slot slot = getSlot(index);
		ItemStack stack = ItemStack.EMPTY;
		
		if(slot != null && slot.getHasStack())
		{
			stack = slot.getStack();
			
			if(stack.getItem() instanceof BucketItem && te.isIngredientBucket(stack))
			{
				if(getSlot(0).getStack().isEmpty())
				{
					getSlot(0).putStack(stack);
					stack = ItemStack.EMPTY;
				}
				else if(getSlot(1).getStack().isEmpty())
				{
					getSlot(1).putStack(stack);
					stack = ItemStack.EMPTY;
				}
			}
			else if(mergeItemStack(stack, 2, 4, false)) stack = ItemStack.EMPTY;
		}
		
		return stack;
	}
	
	public FluidTank getTank(int index)
	{
		return te.getTank(index);
	}
	
	public int getCookProgressionScaled()
	{
		int i = te.cookTime;
		int j = te.recipe != null ? te.recipe.getCookTime() : 0;
		
		return i != 0 && j != 0 ? i * 22 / j : 0;
	}
}