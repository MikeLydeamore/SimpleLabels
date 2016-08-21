package com.insane.simplelabels;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SimpleLabels.MODID);
	private static int id = 0;
	
	public static void init()
	{
		INSTANCE.registerMessage(MessageLabelUpdate.Handler.class, MessageLabelUpdate.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageVSUUpdate.Handler.class, MessageVSUUpdate.class, id++, Side.SERVER);
	}
}
