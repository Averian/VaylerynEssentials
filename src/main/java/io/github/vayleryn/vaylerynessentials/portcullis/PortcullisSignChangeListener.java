package io.github.vayleryn.vaylerynessentials.portcullis;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class PortcullisSignChangeListener implements Listener {
	
	private VaylerynEssentials plugin;
	
	public PortcullisSignChangeListener(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equalsIgnoreCase("[portcullis]")) {
			if (event.getPlayer().hasPermission("vayleryn.essentials.portcullissigns.create")) {
				event.setLine(0, ChatColor.GRAY + "[portcullis]");
			} else {
				event.getBlock().breakNaturally();
				event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to create portcullis signs!");
			}
		}
	}

}
