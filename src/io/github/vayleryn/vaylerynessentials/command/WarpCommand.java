package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;

	public WarpCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.warp")) {
			if (sender instanceof Player) {
				if (args.length >= 1) {
					Player player = (Player) sender;
					Location warp = plugin.getWarp(args[0]);
					player.teleport(warp);
					sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Warped to " + args[0]);
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the name of the warp.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
			}
		}
		return true;
	}
	
}
