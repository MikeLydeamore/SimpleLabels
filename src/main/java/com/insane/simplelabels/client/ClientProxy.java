package com.insane.simplelabels.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import com.insane.simplelabels.CommonProxy;
import com.insane.simplelabels.SimpleLabels;
import com.insane.simplelabels.tile.TileLabel;

public class ClientProxy extends CommonProxy
{

    @Override
    public void registerRenderers()
    {
        RenderLabel renderer = new RenderLabel();
        ClientRegistry.bindTileEntitySpecialRenderer(TileLabel.class, renderer);
    }
    
    @Override
    public void initModels()
    {
    	SimpleLabels.vsu.initModel();
    	SimpleLabels.label.initModel();
    }

}
