package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;

	public SetSpawnCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.setspawn")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
				sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Spawn location of " + player.getWorld().getName() + " set.");
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You don't have permission.");
		}
		return true;
	}
	
}
