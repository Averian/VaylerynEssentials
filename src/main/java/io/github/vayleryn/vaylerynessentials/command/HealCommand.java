package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;

	public HealCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.heal")) {
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
				player.setHealth(player.getMaxHealth());
				player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Healed.");
				sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + " was healed.");
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "When using from console, you must also specify a player to heal.");
			}
		}
		return true;
	}
	
}
