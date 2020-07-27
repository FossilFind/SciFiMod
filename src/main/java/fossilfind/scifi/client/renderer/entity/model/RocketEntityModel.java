package fossilfind.scifi.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fossilfind.scifi.entity.RocketEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class RocketEntityModel extends EntityModel<RocketEntity>
{
	private final ModelRenderer Rocket;
	private final ModelRenderer Nose;
	private final ModelRenderer Cab;
	private final ModelRenderer NWall;
	private final ModelRenderer Thruster;
	private final ModelRenderer Layer1;
	private final ModelRenderer Layer2;
	private final ModelRenderer Layer3;
	private final ModelRenderer Layer4;

	public RocketEntityModel()
	{
		textureWidth = 128;
		textureHeight = 128;

		Rocket = new ModelRenderer(this);
		Rocket.setRotationPoint(0.0F, 24.0F, 0.0F);

		Nose = new ModelRenderer(this);
		Nose.setRotationPoint(0.0F, 0.0F, 0.0F);
		Rocket.addChild(Nose);
		Nose.setTextureOffset(0, 0).addBox(-7.0F, -35.0F, -7.0F, 14, 2, 14, 0.0F, false);
		Nose.setTextureOffset(0, 16).addBox(-6.0F, -37.0F, -6.0F, 12, 2, 12, 0.0F, false);
		Nose.setTextureOffset(0, 30).addBox(-5.0F, -39.0F, -5.0F, 10, 2, 10, 0.0F, false);
		Nose.setTextureOffset(0, 42).addBox(-4.0F, -41.0F, -4.0F, 8, 2, 8, 0.0F, false);
		Nose.setTextureOffset(0, 52).addBox(-3.0F, -43.0F, -3.0F, 6, 2, 6, 0.0F, false);
		Nose.setTextureOffset(0, 60).addBox(-2.0F, -45.0F, -2.0F, 4, 2, 4, 0.0F, false);
		Nose.setTextureOffset(0, 66).addBox(-1.0F, -60.0F, -1.0F, 2, 15, 2, 0.0F, false);

		Cab = new ModelRenderer(this);
		Cab.setRotationPoint(0.0F, 0.0F, 0.0F);
		Rocket.addChild(Cab);
		Cab.setTextureOffset(92, 26).addBox(-8.0F, -32.0F, 7.0F, 16, 25, 1, 0.0F, false);
		Cab.setTextureOffset(98, 52).addBox(7.0F, -32.0F, -7.0F, 1, 25, 14, 0.0F, false);
		Cab.setTextureOffset(68, 52).addBox(-8.0F, -32.0F, -7.0F, 1, 25, 14, 0.0F, false);
		Cab.setTextureOffset(64, 91).addBox(-8.0F, -7.0F, -8.0F, 16, 1, 16, 0.0F, false);
		Cab.setTextureOffset(64, 108).addBox(-8.0F, -33.0F, -8.0F, 16, 1, 16, 0.0F, false);

		NWall = new ModelRenderer(this);
		NWall.setRotationPoint(0.0F, 0.0F, 0.0F);
		Cab.addChild(NWall);
		NWall.setTextureOffset(120, 0).addBox(-8.0F, -32.0F, -8.0F, 3, 25, 1, 0.0F, false);
		NWall.setTextureOffset(112, 0).addBox(5.0F, -32.0F, -8.0F, 3, 25, 1, 0.0F, false);
		NWall.setTextureOffset(90, 0).addBox(-5.0F, -17.0F, -8.0F, 10, 10, 1, 0.0F, false);
		NWall.setTextureOffset(90, 11).addBox(-5.0F, -32.0F, -8.0F, 10, 4, 1, 0.0F, false);

		Thruster = new ModelRenderer(this);
		Thruster.setRotationPoint(0.0F, 0.0F, 0.0F);
		Rocket.addChild(Thruster);

		Layer1 = new ModelRenderer(this);
		Layer1.setRotationPoint(0.0F, 0.0F, 0.0F);
		Thruster.addChild(Layer1);
		Layer1.setTextureOffset(0, 124).addBox(2.0F, -3.0F, -3.0F, 1, 3, 1, 0.0F, false);
		Layer1.setTextureOffset(4, 124).addBox(-2.0F, -3.0F, 3.0F, 4, 3, 1, 0.0F, false);
		Layer1.setTextureOffset(4, 120).addBox(-2.0F, -3.0F, -4.0F, 4, 3, 1, 0.0F, false);
		Layer1.setTextureOffset(4, 113).addBox(-4.0F, -3.0F, -2.0F, 1, 3, 4, 0.0F, false);
		Layer1.setTextureOffset(4, 106).addBox(3.0F, -3.0F, -2.0F, 1, 3, 4, 0.0F, false);
		Layer1.setTextureOffset(0, 120).addBox(-3.0F, -3.0F, -3.0F, 1, 3, 1, 0.0F, false);
		Layer1.setTextureOffset(0, 116).addBox(-3.0F, -3.0F, 2.0F, 1, 3, 1, 0.0F, false);
		Layer1.setTextureOffset(0, 112).addBox(2.0F, -3.0F, 2.0F, 1, 3, 1, 0.0F, false);

		Layer2 = new ModelRenderer(this);
		Layer2.setRotationPoint(0.0F, -3.0F, 0.0F);
		Thruster.addChild(Layer2);
		Layer2.setTextureOffset(14, 126).addBox(-2.0F, -1.0F, 2.0F, 4, 1, 1, 0.0F, false);
		Layer2.setTextureOffset(14, 124).addBox(-2.0F, -1.0F, -3.0F, 4, 1, 1, 0.0F, false);
		Layer2.setTextureOffset(14, 119).addBox(-3.0F, -1.0F, -2.0F, 1, 1, 4, 0.0F, false);
		Layer2.setTextureOffset(14, 114).addBox(2.0F, -1.0F, -2.0F, 1, 1, 4, 0.0F, false);

		Layer3 = new ModelRenderer(this);
		Layer3.setRotationPoint(0.0F, 0.0F, 0.0F);
		Thruster.addChild(Layer3);
		Layer3.setTextureOffset(24, 126).addBox(-1.0F, -5.0F, -3.0F, 2, 1, 1, 0.0F, false);
		Layer3.setTextureOffset(24, 124).addBox(-1.0F, -5.0F, 2.0F, 2, 1, 1, 0.0F, false);
		Layer3.setTextureOffset(24, 121).addBox(-3.0F, -5.0F, -1.0F, 1, 1, 2, 0.0F, false);
		Layer3.setTextureOffset(24, 118).addBox(2.0F, -5.0F, -1.0F, 1, 1, 2, 0.0F, false);
		Layer3.setTextureOffset(30, 126).addBox(-2.0F, -5.0F, 1.0F, 1, 1, 1, 0.0F, false);
		Layer3.setTextureOffset(30, 124).addBox(-2.0F, -5.0F, -2.0F, 1, 1, 1, 0.0F, false);
		Layer3.setTextureOffset(30, 122).addBox(1.0F, -5.0F, -2.0F, 1, 1, 1, 0.0F, false);
		Layer3.setTextureOffset(30, 120).addBox(1.0F, -5.0F, 1.0F, 1, 1, 1, 0.0F, false);

		Layer4 = new ModelRenderer(this);
		Layer4.setRotationPoint(0.0F, 0.0F, 0.0F);
		Thruster.addChild(Layer4);
		Layer4.setTextureOffset(20, 115).addBox(-2.0F, -6.0F, -1.0F, 2, 1, 2, 0.0F, false);
		Layer4.setTextureOffset(20, 112).addBox(0.0F, -6.0F, -1.0F, 2, 1, 2, 0.0F, false);
		Layer4.setTextureOffset(14, 112).addBox(-1.0F, -6.0F, -2.0F, 2, 1, 1, 0.0F, false);
		Layer4.setTextureOffset(14, 110).addBox(-1.0F, -6.0F, 1.0F, 2, 1, 1, 0.0F, false);
	}
	
	@Override
	public void render(MatrixStack arg0, IVertexBuilder arg1, int arg2, int arg3, float arg4, float arg5, float arg6, float arg7)
	{
		Rocket.render(arg0, arg1, arg2, arg3);
	}
	
	@Override
	public void setRotationAngles(RocketEntity arg0, float arg1, float arg2, float arg3, float arg4, float arg5)
	{
		
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	public ModelRenderer getRocket()
	{
		return Rocket;
	}
	
	public ModelRenderer getNose()
	{
		return Nose;
	}
	
	public ModelRenderer getCab()
	{
		return Cab;
	}
	
	public ModelRenderer getNWall()
	{
		return NWall;
	}
	
	public ModelRenderer getThruster()
	{
		return Thruster;
	}
	
	public ModelRenderer getLayer1()
	{
		return Layer1;
	}
	
	public ModelRenderer getLayer2()
	{
		return Layer2;
	}
	
	public ModelRenderer getLayer3()
	{
		return Layer3;
	}
	
	public ModelRenderer getLayer4()
	{
		return Layer4;
	}
}