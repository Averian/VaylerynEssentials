package com.gildorymrp.essentials;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equalsIgnoreCase("[boat]")) {
			if (event.getPlayer().hasPermission("gildorym.essentials.boatsigns.create")) {
				event.setLine(0, ChatColor.BLUE + "[boat]");
			} else {
				event.getBlock().breakNaturally();
				event.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to create boat signs!");
			}
		}
	}

}
