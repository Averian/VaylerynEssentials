package io.github.vayleryn.vaylerynessentials;

import io.github.vayleryn.vaylerynlib.plugin.essentials.Kit;

import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitImpl extends HashSet<ItemStack> implements Kit {
	
	private static final long serialVersionUID = -8250134040212605617L;
	
	@Override
	public void give(Player player) {
		player.getInventory().addItem((ItemStack[]) this.toArray());
	}
	
}
