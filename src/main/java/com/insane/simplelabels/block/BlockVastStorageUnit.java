package com.insane.simplelabels.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
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
import net.minecraft.entity.player.EntityPlayer;
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
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileVastStorageUnit te = (TileVastStorageUnit) world.getTileEntity(pos);
		if (stack.hasTagCompound() && te != null)
		{
		    if (stack.getTagCompound().hasKey("storedItem"))
		    {
    			ItemStack stored = ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("storedItem"));
    			int amount = stack.getTagCompound().getInteger("storedCount");
    			te.setStoredItemType(stored, amount);
		    }
		    else
		    {
		        // Legacy handling
		        te.readFromNBT(stack.getTagCompound());
		        te.setPos(pos);
		    }
		}
		
		super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) 
    {
        if (willHarvest) 
        {
            return true;
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) 
    {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) 
    {
        TileVastStorageUnit te = (TileVastStorageUnit) world.getTileEntity(pos);
        if (te != null)
        {
            ItemStack stack = new ItemStack(this);

            if (te.getStoredItemType() != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                ItemStack stored = te.getStoredItemType();
                tag.setInteger("storedCount", stored.stackSize);
                tag.setTag("storedItem", stored.writeToNBT(new NBTTagCompound()));
                
                stack.setTagCompound(tag);
            }
            
            return Lists.newArrayList(stack);
        }
        return Collections.emptyList();
    }

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
