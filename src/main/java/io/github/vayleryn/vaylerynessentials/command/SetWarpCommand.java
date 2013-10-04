package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public SetWarpCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.setwarp")) {
			if (sender instanceof Player) {
				if (args.length >= 1) {
					Player player = (Player) sender;
					plugin.getWarps().put(args[0], player.getLocation());
					sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Set warp " + args[0] + " in " + player.getWorld() + " at " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());
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