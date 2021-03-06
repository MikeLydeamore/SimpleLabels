package com.insane.simplelabels;

import com.insane.simplelabels.block.BlockLabel;
import com.insane.simplelabels.block.BlockVastStorageUnit;
import com.insane.simplelabels.block.itemblock.ItemBlockLabel;
import com.insane.simplelabels.block.itemblock.ItemBlockVSU;
import com.insane.simplelabels.tile.TileLabel;
import com.insane.simplelabels.tile.TileVastStorageUnit;

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

@Mod(modid=SimpleLabels.MODID, name="SimpleLabels", version="0.0.1", dependencies="after:MineFactoryReloaded")
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
		GameRegistry.register(new ItemBlockLabel(label));
		GameRegistry.registerTileEntity(TileLabel.class, "TileLabel");
		
		vsu = new BlockVastStorageUnit();
		GameRegistry.register(new ItemBlockVSU(vsu).setRegistryName("blockVSU"));
		GameRegistry.registerTileEntity(TileVastStorageUnit.class, "TileVSU");
		
		label.setCreativeTab(CreativeTabs.DECORATIONS);
		vsu.setCreativeTab(CreativeTabs.DECORATIONS);
		
		proxy.registerRenderers();
		proxy.initModels();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(label), new Object[] {"xxx","xyx","xxx", 'x', Items.PAPER, 'y', "slabWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(vsu), new Object[]{"xxx","y y","zzz", 'x', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 'y', Blocks.IRON_BLOCK, 'z', Blocks.OBSIDIAN}));
		PacketHandler.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

}
