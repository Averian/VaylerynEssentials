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
		event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Welcome to Vayleryn!");
		if (!event.getPlayer().hasPlayedBefore()) {
			event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "It appears this is your first time playing here.");
			event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Feel free to ask for help by using /help [message]");
			event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Quick in-game command references can be found by using /?, or for a specific plugin, /? [plugin]");
			event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "More comprehensive help can be found at:");
			event.getPlayer().sendMessage("" + ChatColor.BLUE + ChatColor.UNDERLINE + "http://vayleryn.enjin.com/forum/m/16118280/viewforum/3094173");
			event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "We hope you enjoy your time roleplaying with us!");
			plugin.getServer().broadcastMessage(plugin.getPrefix() + ChatColor.GREEN + event.getPlayer().getName() + " just joined for the first time, be sure to give them a warm welcome to Vayleryn!");
		}
		event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + plugin.getServer().getMotd());
	}
	
}
