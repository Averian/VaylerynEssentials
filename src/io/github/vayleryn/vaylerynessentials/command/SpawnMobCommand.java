package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public SpawnMobCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.spawnmob")) {
			if (args.length >= 2) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (EntityType.valueOf(args[0]) != null) {
						EntityType entityType = EntityType.valueOf(args[0]);
						try {
							int amount = Integer.parseInt(args[1]);
							for (int i = 0; i < amount; i++) {
								player.getLocation().getWorld().spawnEntity(player.getLocation(), entityType);
							}
							sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Mobs spawned.");
						} catch (NumberFormatException exception) {
							sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The amount of mobs must be an integer.");
						}
					} else {
						sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That entity type does not exist.");
					}
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify an entity type and an amount.");
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
		}
		return true;
	}
	
}
