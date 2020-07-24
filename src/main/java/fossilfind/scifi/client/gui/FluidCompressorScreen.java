package fossilfind.scifi.client.gui;

import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.container.FluidCompressorContainer;
import fossilfind.scifi.init.FluidInit;
import fossilfind.scifi.util.helpers.KeyboardHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class FluidCompressorScreen extends ContainerScreen<FluidCompressorContainer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(SciFiMod.MODID, "textures/gui/fluid_compressor.png");
	private static final ResourceLocation FLUIDS = new ResourceLocation(SciFiMod.MODID, "textures/gui/fluids.png");
	
	public FluidCompressorScreen(FluidCompressorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		font.drawString(title.getFormattedText(), 8f, 6f, 4210752);
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 8f, 74f, 4210752);
		
		drawToolTips(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(TEXTURE);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		blit(x, y, 0, 0, xSize, ySize);
		
		drawTank(mouseX, mouseY);
	}
	
	private void drawToolTips(int mouseX, int mouseY)
	{
		FluidStack tank = container.getTank().getFluid();
		
		ArrayList<String> tankText = new ArrayList<String>();
		tankText.add(tank.getFluid() == Fluids.EMPTY ? "Empty" : tank.getDisplayName().getFormattedText());
		if(KeyboardHelper.isHoldingShift())
			tankText.add(tank.getAmount() + " / 20000mB");
		else
			tankText.add(tank.getAmount() / 1000 + " / 20B");
		
		if(isMouseInBounds(guiLeft + 80, guiTop + 27, 16, 32, mouseX, mouseY))
			renderTooltip(tankText, mouseX - guiLeft, mouseY - guiTop);
	}
	
	private boolean isMouseInBounds(int x, int y, int width, int height, int mouseX, int mouseY)
	{
		return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
	}
	
	private void drawTank(int mouseX, int mouseY)
	{
		FluidStack tank = container.getTank().getFluid();;
		
		minecraft.getTextureManager().bindTexture(FLUIDS);
		
		if(tank != null)
		{
			int fluidX = 0;
			
			if(tank.getFluid() == Fluids.WATER) fluidX = 0;
			else if(tank.getFluid() == FluidInit.SEAWATER.get()) fluidX = 16;
			else if(tank.getFluid() == FluidInit.HYDROGEN.get()) fluidX = 32;
			else if(tank.getFluid() == FluidInit.OXYGEN.get()) fluidX = 48;
			else if(tank.getFluid() == FluidInit.CHLORINE.get()) fluidX = 64;
			else if(tank.getFluid() == FluidInit.STEAM.get()) fluidX = 80;
			
			blit(guiLeft + 80, guiTop + 27 + 32 - (tank.getAmount() * 32 / 20000), fluidX, 32 - tank.getAmount() * 32 / 20000, 16, tank.getAmount() * 32 / 20000);
		}
	}
}