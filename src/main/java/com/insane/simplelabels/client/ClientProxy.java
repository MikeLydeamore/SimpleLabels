package com.insane.simplelabels.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    }

}
