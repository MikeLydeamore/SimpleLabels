package com.insane.simplelabels.client;

import com.insane.simplelabels.CommonProxy;
import com.insane.simplelabels.tile.TileLabel;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileLabel.class, new RenderLabel());
	}

}
