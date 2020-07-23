package fossilfind.scifi.client.gui.screen.inventory;

import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.init.FluidInit;
import fossilfind.scifi.inventory.container.ElectrolyzerContainer;
import fossilfind.scifi.util.helpers.KeyboardHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class ElectrolyzerScreen extends ContainerScreen<ElectrolyzerContainer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(SciFiMod.MODID, "textures/gui/electrolyzer.png");
	private static final ResourceLocation FLUIDS = new ResourceLocation(SciFiMod.MODID, "textures/gui/fluids.png");
	
	public ElectrolyzerScreen(ElectrolyzerContainer screenContainer, PlayerInventory inv, ITextComponent title)
	{
		super(screenContainer, inv, title);
		
		guiLeft = 0;
		guiTop = 0;
		xSize = 176;
		ySize = 166;
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
		blit(guiLeft + 26, guiTop + 36, 0, 166, container.getCookProgressionScaled(), 35);
		
		drawTanks(mouseX, mouseY);
	}
	
	private void drawToolTips(int mouseX, int mouseY)
	{
		FluidStack tank1 = container.getTank(1).getFluid();
		FluidStack tank2 = container.getTank(2).getFluid();
		FluidStack tank3 = container.getTank(3).getFluid();
		FluidStack tank4 = container.getTank(4).getFluid();
		FluidStack tank5 = container.getTank(5).getFluid();
		
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
		
		ArrayList<String> tank3Text = new ArrayList<String>();
		tank3Text.add(tank3.getFluid() == Fluids.EMPTY ? "Empty" : tank3.getDisplayName().getFormattedText());
		if(KeyboardHelper.isHoldingShift())
			tank3Text.add(tank3.getAmount() + " / 20000mB");
		else
			tank3Text.add(tank3.getAmount() / 1000 + " / 20B");
		
		ArrayList<String> tank4Text = new ArrayList<String>();
		tank4Text.add(tank4.getFluid() == Fluids.EMPTY ? "Empty" : tank4.getDisplayName().getFormattedText());
		if(KeyboardHelper.isHoldingShift())
			tank4Text.add(tank4.getAmount() + " / 20000mB");
		else
			tank4Text.add(tank4.getAmount() / 1000 + " / 20B");
		
		ArrayList<String> tank5Text = new ArrayList<String>();
		tank5Text.add(tank5.getFluid() == Fluids.EMPTY ? "Empty" : tank5.getDisplayName().getFormattedText());
		if(KeyboardHelper.isHoldingShift())
			tank5Text.add(tank5.getAmount() + " / 20000mB");
		else
			tank5Text.add(tank5.getAmount() / 1000 + " / 20B");
		
		if(isMouseInBounds(guiLeft + 8, guiTop + 38, 16, 32, mouseX, mouseY)) renderTooltip(tank1Text, mouseX - guiLeft, mouseY - guiTop);
		if(isMouseInBounds(guiLeft + 44, guiTop + 38, 16, 32, mouseX, mouseY)) renderTooltip(tank2Text, mouseX - guiLeft, mouseY - guiTop);
		if(isMouseInBounds(guiLeft + 80, guiTop + 38, 16, 32, mouseX, mouseY)) renderTooltip(tank3Text, mouseX - guiLeft, mouseY - guiTop);
		if(isMouseInBounds(guiLeft + 116, guiTop + 38, 16, 32, mouseX, mouseY)) renderTooltip(tank4Text, mouseX - guiLeft, mouseY - guiTop);
		if(isMouseInBounds(guiLeft + 152, guiTop + 38, 16, 32, mouseX, mouseY)) renderTooltip(tank5Text, mouseX - guiLeft, mouseY - guiTop);
	}
	
	private boolean isMouseInBounds(int x, int y, int width, int height, int mouseX, int mouseY)
	{
		return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
	}
	
	private void drawTanks(int mouseX, int mouseY)
	{
		FluidStack tank1 = container.getTank(1).getFluid();
		FluidStack tank2 = container.getTank(2).getFluid();
		FluidStack tank3 = container.getTank(3).getFluid();
		FluidStack tank4 = container.getTank(4).getFluid();
		FluidStack tank5 = container.getTank(5).getFluid();
		
		minecraft.getTextureManager().bindTexture(FLUIDS);
		
		if(tank1 != null)
		{
			int fluidX = 0;
			
			if(tank1.getFluid() == Fluids.WATER) fluidX = 0;
			else if(tank1.getFluid() == FluidInit.SEAWATER.get()) fluidX = 16;
			else if(tank1.getFluid() == FluidInit.HYDROGEN.get()) fluidX = 32;
			else if(tank1.getFluid() == FluidInit.OXYGEN.get()) fluidX = 48;
			else if(tank1.getFluid() == FluidInit.CHLORINE.get()) fluidX = 64;
			
			blit(guiLeft + 8, guiTop + 38 + 32 - (tank1.getAmount() * 32 / 20000), fluidX, 32 - tank1.getAmount() * 32 / 20000, 16, tank1.getAmount() * 32 / 20000);
		}
		
		if(tank2 != null)
		{
			int fluidX = 0;
			
			if(tank2.getFluid() == Fluids.WATER) fluidX = 0;
			else if(tank2.getFluid() == FluidInit.SEAWATER.get()) fluidX = 16;
			else if(tank2.getFluid() == FluidInit.HYDROGEN.get()) fluidX = 32;
			else if(tank2.getFluid() == FluidInit.OXYGEN.get()) fluidX = 48;
			else if(tank2.getFluid() == FluidInit.CHLORINE.get()) fluidX = 64;
			
			blit(guiLeft + 44, guiTop + 38 + 32 - (tank2.getAmount() * 32 / 20000), fluidX, 32 - tank2.getAmount() * 32 / 20000, 16, tank2.getAmount() * 32 / 20000);
		}
		
		if(tank3 != null)
		{
			int fluidX = 0;
			
			if(tank3.getFluid() == Fluids.WATER) fluidX = 0;
			else if(tank3.getFluid() == FluidInit.SEAWATER.get()) fluidX = 16;
			else if(tank3.getFluid() == FluidInit.HYDROGEN.get()) fluidX = 32;
			else if(tank3.getFluid() == FluidInit.OXYGEN.get()) fluidX = 48;
			else if(tank3.getFluid() == FluidInit.CHLORINE.get()) fluidX = 64;
			
			blit(guiLeft + 80, guiTop + 38 + 32 - (tank3.getAmount() * 32 / 20000), fluidX, 32 - tank3.getAmount() * 32 / 20000, 16, tank3.getAmount() * 32 / 20000);
		}
		
		if(tank4 != null)
		{
			int fluidX = 0;
			
			if(tank4.getFluid() == Fluids.WATER) fluidX = 0;
			else if(tank4.getFluid() == FluidInit.SEAWATER.get()) fluidX = 16;
			else if(tank4.getFluid() == FluidInit.HYDROGEN.get()) fluidX = 32;
			else if(tank4.getFluid() == FluidInit.OXYGEN.get()) fluidX = 48;
			else if(tank4.getFluid() == FluidInit.CHLORINE.get()) fluidX = 64;
			
			blit(guiLeft + 116, guiTop + 38 + 32 - (tank4.getAmount() * 32 / 20000), fluidX, 32 - tank4.getAmount() * 32 / 20000, 16, tank4.getAmount() * 32 / 20000);
		}
		
		if(tank5 != null)
		{
			int fluidX = 0;
			
			if(tank5.getFluid() == Fluids.WATER) fluidX = 0;
			else if(tank5.getFluid() == FluidInit.SEAWATER.get()) fluidX = 16;
			else if(tank5.getFluid() == FluidInit.HYDROGEN.get()) fluidX = 32;
			else if(tank5.getFluid() == FluidInit.OXYGEN.get()) fluidX = 48;
			else if(tank5.getFluid() == FluidInit.CHLORINE.get()) fluidX = 64;
			
			blit(guiLeft + 152, guiTop + 38 + 32 - (tank5.getAmount() * 32 / 20000), fluidX, 32 - tank5.getAmount() * 32 / 20000, 16, tank5.getAmount() * 32 / 20000);
		}
	}
}