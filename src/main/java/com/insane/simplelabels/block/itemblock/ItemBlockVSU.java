package com.insane.simplelabels.block.itemblock;

import java.text.NumberFormat;
import java.util.List;

import com.ibm.icu.text.DecimalFormat;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockVSU extends ItemBlock {

    public ItemBlockVSU(Block block) {
        super(block);
        setHasSubtypes(true);
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("storedItem")) {
                ItemStack stored = ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("storedItem"));
                if (stored != null) {
                    tooltip.add(String.format("Stored: %sx %s", NumberFormat.getInstance().format(stack.getTagCompound().getInteger("storedCount")), stored.getDisplayName()));
                }
            } else {
                tooltip.add("Outdated NBT data: Place and break to update.");
            }
        }
    }

}
