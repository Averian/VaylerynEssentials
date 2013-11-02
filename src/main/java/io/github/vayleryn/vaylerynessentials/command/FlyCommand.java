package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public FlyCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.fly")) {
			Player player = null;
			if (sender instanceof Player) {
				player = (Player) sender;
			}
			if (args.length >= 1) {
				if (plugin.getServer().getPlayer(args[0]) != null) {
					player = plugin.getServer().getPlayer(args[0]);
				}
			}
			if (player != null) {
				player.setAllowFlight(!player.getAllowFlight());
				if (player.getAllowFlight()) {
					player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Fly mode enabled.");
					sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Allowed " + player.getName() + " to fly.");
				} else {
					player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Fly mode disabled.");
					sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Disallowed " + player.getName() + " to fly.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "When using from console, you must also specify a player to give fly mode.");
			}
		}
		return true;
	}
	
}
