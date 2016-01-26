package com.insane.simplelabels;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommonProxy {
	
	public void registerRenderers() {}
	
	public void updatePlayerInventory(EntityPlayer player) 
	{
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			playerMP.sendContainerToPlayer(playerMP.inventoryContainer);
		}
	}
	
	public void initModels() {}

}
