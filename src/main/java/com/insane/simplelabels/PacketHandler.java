package com.insane.simplelabels;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SimpleLabels.MODID);
	private static int id = 0;
	
	public static void init()
	{
		INSTANCE.registerMessage(MessageLabelUpdate.class, MessageLabelUpdate.class, id++, Side.CLIENT);
	}


}
