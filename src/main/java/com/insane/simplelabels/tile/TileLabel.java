package com.insane.simplelabels.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

import com.insane.simplelabels.MessageLabelUpdate;
import com.insane.simplelabels.PacketHandler;
import com.insane.simplelabels.SimpleLabels;
import com.insane.simplelabels.Util;

public class TileLabel extends TileEntity implements ITickable
{

    private ItemStack storedItem, storedItemForRender;
    private IDeepStorageUnit dsu;
    private EnumFacing dsuDirection = null;
    private int placedDirection;
    
    private long clickTime = -20;

    public void init(int meta)
    {
        dsuDirection = EnumFacing.getFront(meta);
    }

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote)
        {
            dsu = getDSU();
            if (dsu != null && !ItemStack.areItemStacksEqual(getLabelStack(false), dsu.getStoredItemType()))
            {
                setLabelStack(dsu.getStoredItemType());
                this.markDirty();
                this.sendPacket();
                IBlockState state = worldObj.getBlockState(pos);
                worldObj.setBlockState(pos, state);
            }
        }
    }
    
    public boolean onRightClick(boolean sneaking, EntityPlayer player)
    {
    	if ( (dsu=getDSU()) == null)
    		return false;
    	
    	ItemStack stored = dsu.getStoredItemType();
    	
    	if (stored == null)
    		return false;
    	
    	int extractAmount = sneaking == true ? stored.getMaxStackSize() : 1;
    	
    	if (extractAmount > stored.stackSize)
    		extractAmount = stored.stackSize;
    	
    	ItemStack dropStack = stored.copy(); dropStack.stackSize = extractAmount;
    	
    	Util.dropItemInWorld(this, player, dropStack, 0.02);
    	
    	dsu.setStoredItemCount(stored.stackSize - extractAmount);
    	
    	return true;
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
    	
    	if (stored.isItemEqualIgnoreDurability(stack) && ItemStack.areItemStackTagsEqual(stored, stack))
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

    public IDeepStorageUnit getDSU()
    {
    	BlockPos pos = new BlockPos(this.pos.getX() - dsuDirection.getFrontOffsetX(), this.pos.getY() - dsuDirection.getFrontOffsetY(), this.pos.getZ()
                - dsuDirection.getFrontOffsetZ());
    	TileEntity te =  this.worldObj.getTileEntity(pos);
    	if (te instanceof IDeepStorageUnit)
    		return (IDeepStorageUnit) this.worldObj.getTileEntity(pos);
    	else 
			return null;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return dsu == null ? super.getRenderBoundingBox() : ((TileEntity) dsu).getRenderBoundingBox();
    }

    private void sendPacket()
    {
        PacketHandler.INSTANCE.sendToDimension(new MessageLabelUpdate(this.pos.getX(), this.pos.getY(), this.pos.getZ(), getLabelStack(false)), worldObj.provider.getDimension());
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

    public EnumFacing getDsuDirection()
    {
        return this.dsuDirection;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), tag);
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
    	NBTTagCompound tag = writeToNBT(new NBTTagCompound());
    	return tag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
        this.dsu = getDSU();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
    	NBTTagCompound labelTag = new NBTTagCompound();
        if (getLabelStack(false) != null)
        {
            NBTTagCompound item = new NBTTagCompound();
            item = getLabelStack(false).writeToNBT(item);
            item.setInteger("actualSize", getLabelStack(false).stackSize);
            labelTag.setTag("storedItem", item);
        }
        labelTag.setString("dsuDir", dsuDirection.getName());
        labelTag.setInteger("renderDirection", placedDirection);
        labelTag.setLong("clickTime", clickTime);
        tag.setTag("labelTag", labelTag);
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
    	NBTTagCompound labelTag = (NBTTagCompound) tag.getTag("labelTag");
        if (labelTag.hasKey("storedItem"))
        {
            NBTTagCompound item = labelTag.getCompoundTag("storedItem");
            ItemStack stack = ItemStack.loadItemStackFromNBT(item);
            stack.stackSize = item.getInteger("actualSize");
            setLabelStack(stack);
        }
        this.dsuDirection = EnumFacing.byName(labelTag.getString("dsuDir"));
        this.placedDirection = labelTag.getInteger("renderDirection");
        this.clickTime = labelTag.getLong("clickTime");
        super.readFromNBT(tag);
    }
    

	
}
