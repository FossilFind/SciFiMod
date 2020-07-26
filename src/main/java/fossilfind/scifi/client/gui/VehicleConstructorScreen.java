package fossilfind.scifi.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.container.VehicleConstructorContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class VehicleConstructorScreen extends ContainerScreen<VehicleConstructorContainer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(SciFiMod.MODID, "textures/gui/vehicle_constructor.png");
	
	public VehicleConstructorScreen(VehicleConstructorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
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
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(TEXTURE);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		blit(x, y, 0, 0, xSize, ySize);
	}
}