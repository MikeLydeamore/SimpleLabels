package com.insane.simplelabels.client;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.insane.simplelabels.CommonProxy;
import com.insane.simplelabels.SimpleLabels;
import com.insane.simplelabels.block.BlockLabel;
import com.insane.simplelabels.tile.TileLabel;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{

    @Override
    public void registerRenderers()
    {
        BlockLabel.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderLabel renderer = new RenderLabel();
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SimpleLabels.label), renderer);
        RenderingRegistry.registerBlockHandler(renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileLabel.class, renderer);
    }

}
