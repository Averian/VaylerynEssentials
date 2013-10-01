package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RunAsCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public RunAsCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.runas")) {
			if (args.length >= 2) {
				if (plugin.getServer().getPlayer(args[0]) != null) {
					Player player = plugin.getServer().getPlayer(args[0]);
					String commandToRun = "";
					for (String arg : args) {
						commandToRun += arg;
					}
					plugin.getServer().dispatchCommand(player, commandToRun);
					sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dispatched command.");
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player and a command.");
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission");
		}
		return true;
	}
	
}
