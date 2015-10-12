package com.insane.simplelabels.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

import com.insane.simplelabels.MessageLabelUpdate;
import com.insane.simplelabels.PacketHandler;
import com.insane.simplelabels.SimpleLabels;

public class TileLabel extends TileEntity
{

	private ItemStack storedItem, storedItemForRender;
	private IDeepStorageUnit dsu;
	private ForgeDirection dsuDirection = ForgeDirection.UNKNOWN;
	private int placedDirection;

	private long clickTime = -20;

	public void init(int meta)
	{
		dsuDirection = ForgeDirection.getOrientation(meta);
	}

	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			dsu = getDSU();
			if (dsu != null && !ItemStack.areItemStacksEqual(getLabelStack(false), dsu.getStoredItemType()))
			{
				setLabelStack(dsu.getStoredItemType());
				this.markDirty();
				this.sendPacket();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}

	public boolean onRightClick(boolean sneaking)
	{
		if ( (dsu=getDSU()) == null)
			return false;

		ItemStack stored = dsu.getStoredItemType();

		if (stored == null)
			return false;

		if (this.worldObj.getTotalWorldTime() - this.clickTime > 2L)
		{

			int extractAmount = sneaking == true ? stored.getMaxStackSize() : 1;

			if (extractAmount > stored.stackSize)
				extractAmount = stored.stackSize;

			EntityItem dropItem = new EntityItem(this.worldObj);
			ItemStack dropStack = stored.copy(); dropStack.stackSize = extractAmount;

			dropItem.setEntityItemStack(dropStack);
			ForgeDirection dir = ForgeDirection.getOrientation(placedDirection);
			dropItem.setPosition(xCoord+0.5+dir.offsetX, yCoord+0.5+dir.offsetY, zCoord+0.5+dir.offsetZ);

			float var15 = 0.05F;
			dropItem.motionX = (float)this.worldObj.rand.nextGaussian() * var15;
			dropItem.motionY = (float)this.worldObj.rand.nextGaussian() * var15 + 0.2F;
			dropItem.motionZ = (float)this.worldObj.rand.nextGaussian() * var15;

			this.worldObj.spawnEntityInWorld(dropItem);

			dsu.setStoredItemCount(stored.stackSize - extractAmount);

			this.clickTime = this.worldObj.getTotalWorldTime();
			
			return true;
		}
		return false;
	}

	public void addFromPlayer(EntityPlayer player)
	{
		ItemStack heldStack = player.inventory.getCurrentItem();
		if (heldStack != null)
		{
			heldStack.stackSize -= this.addStack(heldStack);

			if (heldStack.stackSize == 0)
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
		}

		if (this.worldObj.getTotalWorldTime() - this.clickTime < 10L)
		{
			InventoryPlayer playerInv = player.inventory;
			for (int invSlot = 0 ; invSlot < playerInv.getSizeInventory(); ++invSlot)
			{
				ItemStack slotStack = playerInv.getStackInSlot(invSlot);

				int input = this.addStack(slotStack);
				if (input > 0)
				{
					slotStack.stackSize -= input;
					if (slotStack.stackSize == 0)
						playerInv.setInventorySlotContents(invSlot, (ItemStack) null);
				}
			}
		}

		this.clickTime = this.worldObj.getTotalWorldTime();
		SimpleLabels.proxy.updatePlayerInventory(player);

		this.markDirty();
	}

	private int addStack(ItemStack stack)
	{
		if (stack == null)
			return 0;

		if ( (dsu=getDSU()) == null)
			return 0;

		ItemStack stored = dsu.getStoredItemType();
		if (stored == null)
		{
			dsu.setStoredItemType(stack, stack.stackSize);
			return stack.stackSize;
		}

		if (stored.getItem() == stack.getItem() && stored.getItemDamage() == stack.getItemDamage())
		{
			int addAmount = stack.stackSize;
			if (dsu.getMaxStoredCount() < stored.stackSize + stack.stackSize)
				addAmount = dsu.getMaxStoredCount() - stored.stackSize;

			dsu.setStoredItemCount(stored.stackSize + addAmount);

			return addAmount;
		}

		return 0;

	}

	public void setPlacedDirection(int newDirection)
	{
		this.placedDirection = newDirection;
	}

	public int getPlacedDirection()
	{
		return this.placedDirection;
	}

	private IDeepStorageUnit getDSU()
	{
		return (IDeepStorageUnit) this.worldObj.getTileEntity(this.xCoord - dsuDirection.offsetX, this.yCoord - dsuDirection.offsetY, this.zCoord
				- dsuDirection.offsetZ);
	}

	public boolean hasDSU()
	{
		return getDSU() != null;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return dsu == null ? super.getRenderBoundingBox() : ((TileEntity) dsu).getRenderBoundingBox();
	}

	private void sendPacket()
	{
		PacketHandler.INSTANCE.sendToDimension(new MessageLabelUpdate(xCoord, yCoord, zCoord, getLabelStack(false)), worldObj.provider.dimensionId);
	}

	public ItemStack getLabelStack(boolean forRender)
	{
		return forRender ? storedItemForRender : storedItem;
	}

	public void setLabelStack(ItemStack inputStack)
	{
		if (worldObj != null)
		{
			this.dsu = getDSU();
		}

		if (inputStack != null)
		{
			this.storedItem = inputStack.copy();
			this.storedItemForRender = inputStack.copy();
			this.storedItemForRender.stackSize = 1;
		}
		else
		{
			this.storedItem = this.storedItemForRender = null;
		}
	}

	public ForgeDirection getDsuDirection()
	{
		return this.dsuDirection;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.func_148857_g();
		this.readFromNBT(tag);
		this.dsu = getDSU();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		if (getLabelStack(false) != null)
		{
			NBTTagCompound item = new NBTTagCompound();
			getLabelStack(false).writeToNBT(item);
			item.setInteger("actualSize", getLabelStack(false).stackSize);
			tag.setTag("storedItem", item);
		}
		tag.setString("dsuDir", dsuDirection.name());
		tag.setInteger("renderDirection", placedDirection);
		tag.setLong("clickTime", clickTime);
		super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		if (tag.hasKey("storedItem"))
		{
			NBTTagCompound item = tag.getCompoundTag("storedItem");
			ItemStack stack = ItemStack.loadItemStackFromNBT(item);
			stack.stackSize = item.getInteger("actualSize");
			setLabelStack(stack);
		}
		this.dsuDirection = ForgeDirection.valueOf(tag.getString("dsuDir"));
		this.placedDirection = tag.getInteger("renderDirection");
		this.clickTime = tag.getLong("clickTime");
		super.readFromNBT(tag);
	}

}
