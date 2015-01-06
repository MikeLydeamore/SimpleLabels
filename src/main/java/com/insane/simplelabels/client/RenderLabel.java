package com.insane.simplelabels.client;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.insane.simplelabels.tile.TileLabel;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;

public class RenderLabel extends TileEntitySpecialRenderer {

	private static EntityItem item;
	@Override
	public void renderTileEntityAt(TileEntity tile, double x,
			double y, double z, float f) 
	{
		TileLabel te = (TileLabel) tile;
		if (item == null)
			item = new EntityItem(te.getWorldObj());

		if (te.getLabelStack() != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(x, y+0.5, z+0.5);
			item.setEntityItemStack(te.getLabelStack());
			item.hoverStart=0.0f;
			GL11.glScalef(0.8f, 0.8f, 0.8f);
			RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(x, y+0.3, x+0.5);
			GL11.glScalef(0.3f, 0.3f, 0.3f);
			int stringWidth = this.func_147498_b().getStringWidth("test");
			this.func_147498_b().drawString("test", -stringWidth, 0, Color.BLACK.getRGB());
			GL11.glPopMatrix();
		}


	}

}
