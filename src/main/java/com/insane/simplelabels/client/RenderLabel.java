package com.insane.simplelabels.client;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import com.insane.simplelabels.block.BlockLabel;
import com.insane.simplelabels.tile.TileLabel;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import static org.lwjgl.opengl.GL11.*;

public class RenderLabel extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer
{
    private static RenderItem renderItem = new RenderItem();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        TileLabel te = (TileLabel) tile;
        Minecraft mc = Minecraft.getMinecraft();

        ItemStack stack = te.getLabelStack(false);
        if (stack!= null)
        {
            glPushMatrix();
            glPushAttrib(GL_LIGHTING_BIT);
            // Enable flags needed for item renders
            glDisable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_LIGHTING);
            glEnable(GL_CULL_FACE);
            
            float inset = 0.5f - 0.0101f;
            
            ForgeDirection side = te.getDsuDirection();

            // Fix lighting pos
            int ambientLight = tile.getWorldObj().getLightBrightnessForSkyBlocks(tile.xCoord + side.offsetX, tile.yCoord + side.offsetY,
                    tile.zCoord + side.offsetZ, 0);
            int lightX = ambientLight % 65536;
            int lightY = ambientLight / 65536;
            float mult = 1.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightX * mult, lightY * mult);
            
            // Move to the block
            glTranslated(x + 0.5, y + 0.5, z + 0.5);
            glRotatef(180, 1, 0, 0);
            glRotatef(180, 0, 1, 0);

            // Rotate it for the face we are rendering on
            rotateToDir(te.getDsuDirection());
            if (te.getDsuDirection() == ForgeDirection.DOWN || te.getDsuDirection() == ForgeDirection.UP)
            	rotateText(te.getPlacedDirection());
            glTranslatef(-0.2f, -0.15f, inset);

            // Flatten and invert it
            glScalef(0.025f, 0.025f, -0.0001f);

            // Render the item
            renderItem.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), te.getLabelStack(true), 0, 0);

            // Clean up

            glPopMatrix();

            glPushMatrix();
            glTranslated(x + 0.5, y + 0.5, z + 0.5);
            glRotatef(180, 0, 0, 1);
            rotateToDir(te.getDsuDirection());
            if (te.getDsuDirection() == ForgeDirection.DOWN || te.getDsuDirection() == ForgeDirection.UP)
            	rotateText(te.getPlacedDirection());
            glTranslated(0.0075f, -0.25f, inset);
            glScalef(0.011f, 0.011f, 0.11f);
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
            int stringWidth = this.func_147498_b().getStringWidth(str);
            this.func_147498_b().drawString(str, -(stringWidth / 2), 0, Color.BLACK.getRGB());
            glTranslatef(-0.75f, -0.5f, -0.01f);
            this.func_147498_b().drawString(str, -(stringWidth / 2), 0, Color.WHITE.getRGB());
            glPopMatrix();

            glPopAttrib();
        }
    }

    private void rotateToDir(ForgeDirection dsuDirection)
    {
        switch (dsuDirection)
        {
        case EAST:
            glRotatef(90, 0, 1, 0);
            break;
        case SOUTH:
            glRotatef(180, 0, 1, 0);
            break;
        case WEST:
            glRotatef(-90, 0, 1, 0);
            break;
        case UP:
            glRotatef(-90, 1, 0, 0);
            //glRotatef(90, 0, 0, 1);
            break;
        case DOWN:
            glRotatef(90, 1, 0, 0);
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
    		glRotatef(180, 0, 0, 1);
    		break;
    		
    	case 1:
    		glRotatef(-90, 0, 0, 1);
    		break;
    	
    	case 2:
    		break;
    		
    	case 3:
    		glRotatef(90, 0, 0, 1);
    		break;

    	default:
    		break;
    	}
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        ;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        renderer.renderStandardBlock(block, x, y, z);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return BlockLabel.renderId;
    }
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return false;
    }

    @Override
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
    }
}
