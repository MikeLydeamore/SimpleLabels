package com.insane.simplelabels.block.itemblock;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

public class ItemBlockLabel extends ItemBlock {

	public ItemBlockLabel(Block block) 
	{
		super(block);
		this.setRegistryName("label");
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata (int damageValue) 
	{
		return damageValue;
	}

	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof IDeepStorageUnit)
		{
			return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
		}
		return EnumActionResult.PASS;
	}
}