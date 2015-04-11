package com.insane.simplelabels.tile;

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

public class TileLabel extends TileEntity
{

    private ItemStack storedItem, storedItemForRender;
    private IDeepStorageUnit dsu;
    private ForgeDirection dsuDirection = ForgeDirection.UNKNOWN;

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

    private IDeepStorageUnit getDSU()
    {
        return (IDeepStorageUnit) this.worldObj.getTileEntity(this.xCoord - dsuDirection.offsetX, this.yCoord - dsuDirection.offsetY, this.zCoord
                - dsuDirection.offsetZ);
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
        super.readFromNBT(tag);
    }

}
