package com.insane.simplelabels.tile;

import net.minecraft.item.ItemStack;
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

}
