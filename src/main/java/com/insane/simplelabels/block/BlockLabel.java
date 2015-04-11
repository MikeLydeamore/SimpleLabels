package com.insane.simplelabels.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.insane.simplelabels.tile.TileLabel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLabel extends Block implements ITileEntityProvider
{

    public static float BOUNDS_MIN = 2f / 16f;
    public static float BOUNDS_MAX = 14f / 16f;
    public static float WIDTH = 0.01f;
    public static int renderId;

    public BlockLabel()
    {
        super(Material.wood);
        this.setBlockName("label");
        this.setStepSound(soundTypeWood);
    }

    @Override
    public int damageDropped(int meta)
    {
        return 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs tab, List subItems)
    {
        for (int ix = 0; ix < 6; ix++)
        {
            subItems.add(new ItemStack(this, 1, ix));
        }
    }

    @Override
    public boolean hasTileEntity(int meta)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        TileLabel te = new TileLabel();
        te.init(meta);
        return te;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
    {
        return side;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        switch (meta)
        {
        case 0:
            this.setBlockBounds(BOUNDS_MIN, 1 - WIDTH, BOUNDS_MIN, BOUNDS_MAX, 1, BOUNDS_MAX);
            break;
        case 1:
            this.setBlockBounds(BOUNDS_MIN, 0, BOUNDS_MIN, BOUNDS_MAX, WIDTH, BOUNDS_MAX);
            break;
        case 2:
            this.setBlockBounds(BOUNDS_MIN, BOUNDS_MIN, 1 - WIDTH, BOUNDS_MAX, BOUNDS_MAX, 1);
            break;
        case 3:
            this.setBlockBounds(BOUNDS_MIN, BOUNDS_MIN, 0, BOUNDS_MAX, BOUNDS_MAX, WIDTH);
            break;
        case 4:
            this.setBlockBounds(1 - WIDTH, BOUNDS_MIN, BOUNDS_MIN, 1, BOUNDS_MAX, BOUNDS_MAX);
            break;
        case 5:
            this.setBlockBounds(0, BOUNDS_MIN, BOUNDS_MIN, WIDTH, BOUNDS_MAX, BOUNDS_MAX);
            break;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(1 - WIDTH, BOUNDS_MIN, BOUNDS_MIN, 1, BOUNDS_MAX, BOUNDS_MAX);
    }

    @Override
    public int getRenderType()
    {
        return renderId;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_)
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.planks.getIcon(side, 0);
    }
}
