package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RepairCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public RepairCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.repair")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.getItemInHand() != null) {
					player.getItemInHand().setDurability((short) 0);
					sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Item repaired.");
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding something to repair.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
		}
		return true;
	}
	
}
