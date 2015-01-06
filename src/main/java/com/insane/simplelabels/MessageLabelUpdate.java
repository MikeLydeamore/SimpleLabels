package com.insane.simplelabels;

import com.insane.simplelabels.tile.TileLabel;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageLabelUpdate implements IMessage, IMessageHandler<MessageLabelUpdate, IMessage> {

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
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.storedStack = ByteBufUtils.readItemStack(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		ByteBufUtils.writeItemStack(buf, this.storedStack);
	}
	
	public static final class Handler implements IMessageHandler<MessageLabelUpdate, IMessage>
	{
		public Handler() {}
		
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

	@Override
	public IMessage onMessage(MessageLabelUpdate message, MessageContext ctx) {
		return null;
	}
}
