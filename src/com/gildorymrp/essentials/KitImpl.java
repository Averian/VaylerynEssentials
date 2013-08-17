package com.gildorymrp.essentials;

import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gildorymrp.api.plugin.essentials.Kit;

public class KitImpl extends HashSet<ItemStack> implements Kit {
	
	private static final long serialVersionUID = -8250134040212605617L;
	
	@Override
	public void give(Player player) {
		player.getInventory().addItem((ItemStack[]) this.toArray());
	}
	
}
