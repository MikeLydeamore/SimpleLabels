package com.insane.simplelabels.block;

import java.util.List;

import com.insane.simplelabels.tile.TileLabel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLabel extends Block implements ITileEntityProvider {

	public BlockLabel() 
	{
		super(Material.wood);
		this.setBlockName("label");
		this.setStepSound(soundTypeWood);
	}
	
	@Override
	public int damageDropped (int meta) 
	{
		return 0;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs tab, List subItems) 
	{
		for (int ix = 0; ix < 6; ix++) {
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
		switch (meta) {
		case 0:
			this.setBlockBounds(0.2f, 0.99f, 0.2f, 0.8f, 1, 0.8f);
			break;
		case 1:	
			this.setBlockBounds(0.2f, 0, 0.2f, 0.8f, 0.01f, 0.8f);
			break;
		case 2:
			this.setBlockBounds(0.2f, 0.2f, 0.99f, 0.8f, 0.8f, 1);
			break;
		case 3:
			this.setBlockBounds(0.2f, 0.2f, 0, 0.8f, 0.8f, 0.01f);
			break;
		case 4:
			this.setBlockBounds(0.99f, 0.2f, 0.2f, 1, 0.8f, 0.8f);
			break;
		case 5:
			this.setBlockBounds(0, 0.2f, 0.2f, 0.01f, 0.8f, 0.8f);
			break;
		}
	}
	
	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0.99f, 0.1f, 0.1f, 1, 0.7f, 0.9f);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return Blocks.planks.getIcon(side, 0);
	}


}
