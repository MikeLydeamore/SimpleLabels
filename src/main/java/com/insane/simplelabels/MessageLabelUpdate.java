package com.insane.simplelabels;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.insane.simplelabels.tile.TileLabel;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageLabelUpdate implements IMessage {

	public MessageLabelUpdate() {}
	
	public int x,y,z;
	public ItemStack storedStack;
	public MessageLabelUpdate(int x, int y, int z, ItemStack storedStack)
	{
		this.x = x;
		this.y = y;
        this.z = z;
        this.storedStack = storedStack;
    }
	
    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeItemStack(buf, this.storedStack);
        if (storedStack != null)
        {
            buf.writeInt(storedStack.stackSize);
        }
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.storedStack = ByteBufUtils.readItemStack(buf);
        if (storedStack != null)
        {
            storedStack.stackSize = buf.readInt();
        }
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

	public static final class Handler implements IMessageHandler<MessageLabelUpdate, IMessage>
	{		
		@Override
		public IMessage onMessage(MessageLabelUpdate message, MessageContext ctx)
		{
			TileEntity te =  Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
			if (te != null && te instanceof TileLabel)
			{
				((TileLabel) te).setLabelStack(message.storedStack);
			}
			return null;
		}
	}
}
