package com.insane.simplelabels.block;

import java.util.ArrayList;

import com.insane.simplelabels.Util;
import com.insane.simplelabels.tile.TileVastStorageUnit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVastStorageUnit extends Block implements ITileEntityProvider {

	public BlockVastStorageUnit()
	{
		super(Material.ROCK);
		this.setUnlocalizedName("blockVSU");
		this.setRegistryName("blockVSU");
		this.setHardness(3f);
		GameRegistry.<Block>register(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileVastStorageUnit();
	}
	
	@Override
	public final ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		
		return ret;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		if (stack.getTagCompound() != null)
		{
			TileVastStorageUnit te = (TileVastStorageUnit) world.getTileEntity(pos);
			te.readFromNBT(stack.getTagCompound());
		}
		
		super.onBlockPlacedBy(world, pos, state, placer, stack);
    }
	
	@Override
	 public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
		TileVastStorageUnit te = (TileVastStorageUnit) world.getTileEntity(pos);
		NBTTagCompound tag = new NBTTagCompound();
		te.writeToNBT(tag);
		
		ItemStack stack = new ItemStack(this);
		stack.setTagCompound(tag);
		
		EntityItem drop = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ());
		drop.setEntityItemStack(stack);
		world.spawnEntityInWorld(drop);
        super.breakBlock(world, pos, state);
    }

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
