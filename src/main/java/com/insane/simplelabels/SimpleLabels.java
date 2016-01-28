package com.insane.simplelabels;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.insane.simplelabels.block.BlockLabel;
import com.insane.simplelabels.block.BlockVastStorageUnit;
import com.insane.simplelabels.block.itemblock.ItemBlockLabel;
import com.insane.simplelabels.tile.TileLabel;
import com.insane.simplelabels.tile.TileVastStorageUnit;

@Mod(modid=SimpleLabels.MODID, name="SimpleLabels", version="0.0.1", dependencies="required-after:Forge@[11.15.1.1724,),after:MineFactoryReloaded")
public class SimpleLabels {
	
	public static final String MODID = "SimpleLabels";
	
	@Mod.Instance(MODID)
	public SimpleLabels instance;
	
	@SidedProxy(clientSide="com.insane.simplelabels.client.ClientProxy", serverSide="com.insane.simplelabels.CommonProxy")
	public static CommonProxy proxy;
	
	public static BlockLabel label;
	public static BlockVastStorageUnit vsu;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		label = new BlockLabel();
		GameRegistry.registerBlock(label, ItemBlockLabel.class);
		GameRegistry.registerTileEntity(TileLabel.class, "TileLabel");
		
		vsu = new BlockVastStorageUnit();
		GameRegistry.registerTileEntity(TileVastStorageUnit.class, "TileVSU");
		
		label.setCreativeTab(CreativeTabs.tabDecorations);
		vsu.setCreativeTab(CreativeTabs.tabDecorations);
		
		proxy.registerRenderers();
		proxy.initModels();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(label), new Object[] {"xxx","xyx","xxx", 'x', Items.paper, 'y', "slabWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(vsu), new Object[]{"xxx","y y","zzz", 'x', Blocks.light_weighted_pressure_plate, 'y', Blocks.iron_block, 'z', Blocks.obsidian}));
		PacketHandler.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

}
