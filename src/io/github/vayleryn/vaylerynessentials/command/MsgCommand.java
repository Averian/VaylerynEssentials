package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public MsgCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.msg")) {
			if (args.length >= 2) {
				if (plugin.getServer().getPlayer(args[0]) != null) {
					Player recipient = plugin.getServer().getPlayer(args[0]);
					String message = "";
					for (int i = 1; i < args.length; i++) {
						message += args[i] + " ";
					}
					recipient.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + sender.getName() + ChatColor.DARK_GRAY + " ==> " + ChatColor.BLUE + recipient.getName() + ChatColor.GRAY + "] " + ChatColor.WHITE + message);
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a player by that name.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player and a message.");
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
		}
		return true;
	}
	
}
