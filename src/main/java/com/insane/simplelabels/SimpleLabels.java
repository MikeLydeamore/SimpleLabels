package com.insane.simplelabels;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.insane.simplelabels.block.BlockLabel;
import com.insane.simplelabels.block.itemblock.ItemBlockLabel;
import com.insane.simplelabels.tile.TileLabel;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=SimpleLabels.MODID, name="SimpleLabels", version="0.0.1", dependencies="after:MineFactoryReloaded")
public class SimpleLabels {
	
	public static final String MODID = "SimpleLabels";
	
	@Mod.Instance(MODID)
	public SimpleLabels instance;
	
	@SidedProxy(clientSide="com.insane.simplelabels.client.ClientProxy", serverSide="com.insane.simplelabels.CommonProxy")
	public static CommonProxy proxy;
	
	public static BlockLabel label;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		label = new BlockLabel();
		GameRegistry.registerBlock(label, ItemBlockLabel.class, "label");
		GameRegistry.registerTileEntity(TileLabel.class, "TileLabel");
		
		proxy.registerRenderers();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(label), new Object[] {"xxx","xyx","xxx", 'x', Items.paper, 'y', "slabWood"}));
		
		PacketHandler.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}

}
