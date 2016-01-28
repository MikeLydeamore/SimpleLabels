package com.insane.simplelabels.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class VSUHandler extends ItemStackHandler {

	public VSUHandler()
	{
		super(1);
	}
	
	@Override
	protected int getStackLimit(int slot, ItemStack stack)
	{
		return Integer.MAX_VALUE;
	}

	public ItemStack getStack()
	{
		return this.getStackInSlot(0);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < stacks.length; i++)
		{
			if (stacks[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger("Slot", i);
				itemTag.setInteger("SizeSpecial", stacks[i].stackSize);
				stacks[i].writeToNBT(itemTag);
				nbtTagList.appendTag(itemTag);
			}
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Items", nbtTagList);
		nbt.setInteger("Size", stacks.length);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.length);
		NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
			int slot = itemTags.getInteger("Slot");

			if (slot >= 0 && slot < stacks.length)
			{
				stacks[slot] = ItemStack.loadItemStackFromNBT(itemTags);
				stacks[slot].stackSize = itemTags.getInteger("SizeSpecial");
			}
		}
		onLoad();
	}

}
