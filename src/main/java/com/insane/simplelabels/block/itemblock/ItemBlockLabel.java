package com.insane.simplelabels.block.itemblock;

import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

import com.insane.simplelabels.SimpleLabels;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlockLabel extends ItemBlock {

	public ItemBlockLabel(Block block) 
	{
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata (int damageValue) 
	{
		return damageValue;
	}

	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{

		if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof IDeepStorageUnit)
		{
			return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
		}
		return false;
	}
}