package com.insane.simplelabels.block.itemblock;

import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

import com.insane.simplelabels.SimpleLabels;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];

		if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof IDeepStorageUnit)
		{
			return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}
		return false;
	}
}