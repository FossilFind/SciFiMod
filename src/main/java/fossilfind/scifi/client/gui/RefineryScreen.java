package fossilfind.scifi.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.container.RefineryContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class RefineryScreen extends ContainerScreen<RefineryContainer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(SciFiMod.MODID, "textures/gui/refinery.png");
	
	public RefineryScreen(RefineryContainer container, PlayerInventory inv, ITextComponent title)
	{
		super(container, inv, title);
		
		guiLeft = 0;
		guiTop = 0;
		xSize = 175;
		ySize = 165;
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
		
		if(container.isBurning())
		{
			int i = container.getBurnLeftScaled();
			blit(guiLeft + 56, guiTop + 37 + 12 - i, 176, 12 - i, 14, i + 1);
		}
		
		blit(guiLeft + 79, guiTop + 34, 176, 14, container.getCookProgressionScaled() + 1, 16);
	}
}