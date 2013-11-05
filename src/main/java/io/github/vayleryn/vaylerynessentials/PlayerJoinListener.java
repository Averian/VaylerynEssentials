package io.github.vayleryn.vaylerynessentials;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	
	private VaylerynEssentials plugin;
	
	public PlayerJoinListener(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Welcome to " + plugin.getServer().getServerName() + "!");
		if (!event.getPlayer().hasPlayedBefore()) {
			event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "It appears this is your first time playing here.");
			event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "We hope you enjoy your time roleplaying with us!");
			plugin.getServer().broadcastMessage(plugin.getPrefix() + ChatColor.GREEN + event.getPlayer().getName() + " just joined for the first time, be sure to give them a warm welcome to " + plugin.getServer().getServerName() + "!");
		}
		event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + plugin.getServer().getMotd());
	}
	
}
