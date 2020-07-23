package fossilfind.scifi.client.gui;

import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.container.ChemicalReactorContainer;
import fossilfind.scifi.init.FluidInit;
import fossilfind.scifi.util.helpers.KeyboardHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class ChemicalReactorScreen extends ContainerScreen<ChemicalReactorContainer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(SciFiMod.MODID, "textures/gui/chemical_reactor.png");
	private static final ResourceLocation FLUIDS = new ResourceLocation(SciFiMod.MODID, "textures/gui/fluids.png");
	
	public ChemicalReactorScreen(ChemicalReactorContainer screenContainer, PlayerInventory inv, ITextComponent title)
	{
		super(screenContainer, inv, title);
	}
	
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks)
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
		blit(guiLeft + 77, guiTop + 35, 176, 0, container.getCookProgressionScaled(), 16);
		
		drawTanks(mouseX, mouseY);
	}
	
	private void drawToolTips(int mouseX, int mouseY)
	{
		FluidStack tank1 = container.getTank(1).getFluid();
		FluidStack tank2 = container.getTank(2).getFluid();
		
		ArrayList<String> tank1Text = new ArrayList<String>();
		tank1Text.add(tank1.getFluid() == Fluids.EMPTY ? "Empty" : tank1.getDisplayName().getFormattedText());
		if(KeyboardHelper.isHoldingShift())
			tank1Text.add(tank1.getAmount() + " / 20000mB");
		else
			tank1Text.add(tank1.getAmount() / 1000 + " / 20B");
		
		ArrayList<String> tank2Text = new ArrayList<String>();
		tank2Text.add(tank2.getFluid() == Fluids.EMPTY ? "Empty" : tank2.getDisplayName().getFormattedText());
		if(KeyboardHelper.isHoldingShift())
			tank2Text.add(tank2.getAmount() + " / 20000mB");
		else
			tank2Text.add(tank2.getAmount() / 1000 + " / 20B");
		
		if(isMouseInBounds(guiLeft + 17, guiTop + 38, 16, 32, mouseX, mouseY)) renderTooltip(tank1Text, mouseX - guiLeft, mouseY - guiTop);
		if(isMouseInBounds(guiLeft + 53, guiTop + 38, 16, 32, mouseX, mouseY)) renderTooltip(tank2Text, mouseX - guiLeft, mouseY - guiTop);
	}
	
	private boolean isMouseInBounds(int x, int y, int width, int height, int mouseX, int mouseY)
	{
		return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
	}
	
	private void drawTanks(int mouseX, int mouseY)
	{
		FluidStack tank1 = container.getTank(1).getFluid();
		FluidStack tank2 = container.getTank(2).getFluid();
		
		minecraft.getTextureManager().bindTexture(FLUIDS);
		
		if(tank1 != null)
		{
			int fluidX = 0;
			
			if(tank1.getFluid() == Fluids.WATER) fluidX = 0;
			else if(tank1.getFluid() == FluidInit.SEAWATER.get()) fluidX = 16;
			else if(tank1.getFluid() == FluidInit.HYDROGEN.get()) fluidX = 32;
			else if(tank1.getFluid() == FluidInit.OXYGEN.get()) fluidX = 48;
			else if(tank1.getFluid() == FluidInit.CHLORINE.get()) fluidX = 64;
			else if(tank1.getFluid() == FluidInit.STEAM.get()) fluidX = 80;
			
			blit(guiLeft + 17, guiTop + 38 + 32 - (tank1.getAmount() * 32 / 20000), fluidX, 32 - tank1.getAmount() * 32 / 20000, 16, tank1.getAmount() * 32 / 20000);
		}
		
		if(tank2 != null)
		{
			int fluidX = 0;
			
			if(tank2.getFluid() == Fluids.WATER) fluidX = 0;
			else if(tank2.getFluid() == FluidInit.SEAWATER.get()) fluidX = 16;
			else if(tank2.getFluid() == FluidInit.HYDROGEN.get()) fluidX = 32;
			else if(tank2.getFluid() == FluidInit.OXYGEN.get()) fluidX = 48;
			else if(tank2.getFluid() == FluidInit.CHLORINE.get()) fluidX = 64;
			else if(tank2.getFluid() == FluidInit.STEAM.get()) fluidX = 80;
			
			blit(guiLeft + 53, guiTop + 38 + 32 - (tank2.getAmount() * 32 / 20000), fluidX, 32 - tank2.getAmount() * 32 / 20000, 16, tank2.getAmount() * 32 / 20000);
		}
	}
}