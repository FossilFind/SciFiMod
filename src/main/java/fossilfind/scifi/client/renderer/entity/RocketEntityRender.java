package fossilfind.scifi.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import fossilfind.scifi.SciFiMod;
import fossilfind.scifi.client.renderer.entity.model.RocketEntityModel;
import fossilfind.scifi.entity.RocketEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RocketEntityRender extends EntityRenderer<RocketEntity>
{
	protected static final ResourceLocation TEXTURE = new ResourceLocation(SciFiMod.MODID, "textures/entity/rocket.png");
	protected final RocketEntityModel model = new RocketEntityModel();
	
	public RocketEntityRender(EntityRendererManager renderManager)
	{
		super(renderManager);
	}
	
	@Override
	public void render(RocketEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		matrixStackIn.push();
		model.render(matrixStackIn, bufferIn.getBuffer(model.getRenderType(TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getEntityTexture(RocketEntity entity)
	{
		return TEXTURE;
	}
}