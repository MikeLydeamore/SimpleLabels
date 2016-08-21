package com.insane.simplelabels.client;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import com.insane.simplelabels.tile.TileLabel;


import static org.lwjgl.opengl.GL11.*;

public class RenderLabel extends TileEntitySpecialRenderer<TileLabel>
{
    //private static RenderItem renderItem = new RenderItem(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getRenderItem());

    @Override
    public void renderTileEntityAt(TileLabel te, double x, double y, double z, float f, int stage)
    {
        ItemStack stack = te.getLabelStack(false);
        if (stack!= null)
        {
            GlStateManager.pushMatrix();
            glPushAttrib(GL_LIGHTING_BIT);
            
            // Enable flags needed for item renders
            GlStateManager.disableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();
            GlStateManager.enableCull();
            
            
            
            float inset = 0.5f - 0.0101f;
            
            EnumFacing side = te.getDsuDirection();

            int ambientLight = te.getWorld().getLightFor(EnumSkyBlock.BLOCK, te.getPos().add(side.getFrontOffsetX(),  side.getFrontOffsetY(),  side.getFrontOffsetZ()));
            int lightX = ambientLight % 65536;
            int lightY = ambientLight / 65536;
            float mult = 1.0F;
            //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightX * mult, lightY * mult);
            
            // Move to the block
            glTranslated(x + 0.5, y + 0.5, z + 0.5);
            glRotatef(180, 1, 0, 0);
            glRotatef(180, 0, 1, 0);

            // Rotate it for the face we are rendering on
            rotateToDir(te.getDsuDirection());
            if (te.getDsuDirection() == EnumFacing.DOWN || te.getDsuDirection() == EnumFacing.UP)
            	rotateText(te.getPlacedDirection());
            GlStateManager.translate(-0.2f, -0.15f, inset);

            // Flatten and invert it
            GlStateManager.scale(0.025f, 0.025f, -0.0001f);

            // Render the item
            Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(te.getLabelStack(true), 0, 0);

            // Clean up

            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotate(180, 0, 0, 1);
            rotateToDir(te.getDsuDirection());
            if (te.getDsuDirection() == EnumFacing.DOWN || te.getDsuDirection() == EnumFacing.UP)
            	rotateText(te.getPlacedDirection());
            GlStateManager.translate(0.0075f, -0.25f, inset);
            GlStateManager.scale(0.011f, 0.011f, 0.11f);
            int overflow = 0;
            if (stack.stackSize < 0) {
                overflow = Integer.MAX_VALUE + stack.stackSize + 1;
            }
            int maxStack = stack.getMaxStackSize();
            int stacks = (stack.stackSize < 0 ? Integer.MAX_VALUE : stack.stackSize) / maxStack;
            String stacksStr = "" + stacks;
            if (stacks >= 10000000) {
                stacksStr = (stacks / 1000000) + "M";
            }
            else if (stacks >= 1000000) {
                int num = stacks / 100000;
                float dec = ((float) num) / 10;
                stacksStr = dec + "M";
            }
            else if (stacks >= 10000) {
                stacksStr = (stacks / 1000) + "K";
            }
            int leftover = (stack.stackSize - (stacks * maxStack)) + overflow;
            String str = stacksStr + "*" + maxStack + " + " + leftover;
            int stringWidth = this.getFontRenderer().getStringWidth(str);
            this.getFontRenderer().drawString(str, -(stringWidth / 2), 0, Color.BLACK.getRGB());
            GlStateManager.translate(-0.75f, -0.5f, -0.023f);
            this.getFontRenderer().drawString(str, -(stringWidth / 2), 0, Color.WHITE.getRGB());
            GlStateManager.popMatrix();

            GlStateManager.popAttrib();
        }
    }

    private void rotateToDir(EnumFacing dsuDirection)
    {
        switch (dsuDirection)
        {
        case EAST:
            GlStateManager.rotate(90, 0, 1, 0);
            break;
        case SOUTH:
        	GlStateManager.rotate(180, 0, 1, 0);
            break;
        case WEST:
        	GlStateManager.rotate(-90, 0, 1, 0);
            break;
        case UP:
        	GlStateManager.rotate(-90, 1, 0, 0);
            break;
        case DOWN:
        	GlStateManager.rotate(90, 1, 0, 0);
            break;
        default:
            break;
        }
    }
    
    private void rotateText(int direction)
    {
    	switch (direction)
    	{
    	case 0:
    		GlStateManager.rotate(180, 0, 0, 1);
    		break;
    		
    	case 1:
    		GlStateManager.rotate(-90, 0, 0, 1);
    		break;
    	
    	case 2:
    		break;
    		
    	case 3:
    		GlStateManager.rotate(90, 0, 0, 1);
    		break;

    	default:
    		break;
    	}
    }

    /*@Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        Tessellator tes = Tessellator.instance;
        IIcon icon = item.getIconIndex();
        float uDiff = icon.getMaxU() - icon.getMinU();
        float vDiff = icon.getMaxV() - icon.getMinV();
        float uStart = icon.getMinU() + (uDiff * BlockLabel.BOUNDS_MIN);
        float vStart = icon.getMinV() + (vDiff * BlockLabel.BOUNDS_MIN);
        float uEnd = icon.getMinU() + (uDiff * BlockLabel.BOUNDS_MAX);
        float vEnd = icon.getMinV() + (vDiff * BlockLabel.BOUNDS_MAX);

        glPushMatrix();
        glScalef(16, 16, 16);
        tes.startDrawingQuads();
        tes.addVertexWithUV(BlockLabel.BOUNDS_MIN, BlockLabel.BOUNDS_MIN, 0, uStart, vStart);
        tes.addVertexWithUV(BlockLabel.BOUNDS_MIN, BlockLabel.BOUNDS_MAX, 0, uStart, vEnd);
        tes.addVertexWithUV(BlockLabel.BOUNDS_MAX, BlockLabel.BOUNDS_MAX, 0, uEnd, vEnd);
        tes.addVertexWithUV(BlockLabel.BOUNDS_MAX, BlockLabel.BOUNDS_MIN, 0, uEnd, vStart);
        tes.draw();
        glPopMatrix();
    }*/
}
