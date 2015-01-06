package com.insane.simplelabels.tile;


import com.insane.simplelabels.MessageLabelUpdate;
import com.insane.simplelabels.PacketHandler;
import com.insane.simplelabels.SimpleLabels;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileLabel extends TileEntity {

	private ItemStack storedItem;
	private IDeepStorageUnit dsu;
	private int dsuX, dsuY, dsuZ;

	public TileLabel()
	{

	}

	public void init(int meta)
	{
		ForgeDirection dsuDirection = ForgeDirection.getOrientation(meta);
		this.dsuX = dsuDirection.offsetX;
		this.dsuY = dsuDirection.offsetY;
		this.dsuZ = dsuDirection.offsetZ;
	}

	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			dsu = (IDeepStorageUnit) this.worldObj.getTileEntity(this.xCoord-dsuX, this.yCoord-dsuY, this.zCoord-dsuZ);
			if (dsu != null && storedItem != dsu.getStoredItemType())
			{
				this.markDirty();
				this.sendPacket();
				this.storedItem = dsu.getStoredItemType();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}

		}
	}

	private void sendPacket() 
	{
		PacketHandler.INSTANCE.sendToAllAround(new MessageLabelUpdate(xCoord, yCoord, zCoord, getLabelStack()), getPacketRange());		
	}

	private TargetPoint getPacketRange() 
	{
		return new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 5);
	}

	public ItemStack getLabelStack()
	{
		return storedItem;
	}
	
	public void setLabelStack(ItemStack inputStack)
	{
		this.storedItem = inputStack;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		if (storedItem!=null)
		{
			tag.setInteger(SimpleLabels.MODID+"ID", Item.getIdFromItem(storedItem.getItem()));
			tag.setInteger(SimpleLabels.MODID+"meta", storedItem.getItemDamage());
		}
		tag.setInteger(SimpleLabels.MODID+"dsuX", dsuX);
		tag.setInteger(SimpleLabels.MODID+"dsuY", dsuY);
		tag.setInteger(SimpleLabels.MODID+"dsuZ", dsuZ);
		this.writeToNBT(tag);

		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.func_148857_g();
		if (tag.hasKey(SimpleLabels.MODID+"ID"))
			this.storedItem = new ItemStack(Item.getItemById(tag.getInteger(SimpleLabels.MODID+"ID")), 1, tag.getInteger(SimpleLabels.MODID+"meta"));
		this.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		if (storedItem!=null)
		{
			tag.setInteger(SimpleLabels.MODID+"ID", Item.getIdFromItem(storedItem.getItem()));
			tag.setInteger(SimpleLabels.MODID+"meta", storedItem.getItemDamage());
		}
		tag.setInteger(SimpleLabels.MODID+"dsuX", dsuX);
		tag.setInteger(SimpleLabels.MODID+"dsuY", dsuY);
		tag.setInteger(SimpleLabels.MODID+"dsuZ", dsuZ);
		super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		if (tag.hasKey(SimpleLabels.MODID+"ID"))
			this.storedItem = new ItemStack(Item.getItemById(tag.getInteger(SimpleLabels.MODID+"ID")), 1, tag.getInteger(SimpleLabels.MODID+"meta"));
		this.dsuX = tag.getInteger(SimpleLabels.MODID+"dsuX");
		this.dsuY = tag.getInteger(SimpleLabels.MODID+"dsuY");
		this.dsuZ = tag.getInteger(SimpleLabels.MODID+"dsuZ");
		
		super.readFromNBT(tag);
	}

}
