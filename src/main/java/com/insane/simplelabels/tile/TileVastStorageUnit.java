package com.insane.simplelabels.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

public class TileVastStorageUnit extends TileEntity implements ITickable, IDeepStorageUnit {

	VSUHandler handler = new VSUHandler();

	@Override
	public ItemStack getStoredItemType() 
	{	
		return handler.getStack();
	}

	@Override
	public void setStoredItemCount(int amount) 
	{
		ItemStack stack = handler.getStack();
		stack.stackSize = amount;
		handler.setStackInSlot(0, stack);
		this.markDirty();
	}

	@Override
	public void setStoredItemType(ItemStack type, int amount) 
	{
		if (type == null)
		{
			handler.setStackInSlot(0,  null);
		}
		else
		{
		    type = type.copy();
		    type.stackSize = amount;
			handler.setStackInSlot(0, type);
		}
		this.markDirty();
	}

	@Override
	public int getMaxStoredCount() 
	{
		return Integer.MAX_VALUE;
	}


	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) handler;

		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound handlerTag = handler.serializeNBT();
		tag.setTag("handler", handlerTag);
		
		return super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		if (tag.hasKey("handler"))
		{
			handler.deserializeNBT((NBTTagCompound) tag.getTag("handler"));
		}
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() 
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(getPos(), 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) 
	{
		NBTTagCompound tag = packet.getNbtCompound();
		if (tag != null)
			readFromNBT(tag);
	}
	
	@Override
    public NBTTagCompound getUpdateTag() {
    	NBTTagCompound tag = writeToNBT(new NBTTagCompound());
    	return tag;
    }

	@Override
	public void update() 
	{
		if (!this.worldObj.isRemote && handler.getStack() != null)
			if (handler.getStack().stackSize <= 0)
			{
				setStoredItemType(null, 0);
				this.markDirty();
			}
	}

		

}
