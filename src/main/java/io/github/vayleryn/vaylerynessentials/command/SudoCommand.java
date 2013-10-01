package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public SudoCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.sudo")) {
			if (sender instanceof Player) {
				sender.setOp(true);
			}
			String sudoCommand = "";
			for (String arg : args) {
				sudoCommand += arg + " ";
			}
			plugin.getServer().dispatchCommand(sender, sudoCommand);
			if (sender instanceof Player) {
				sender.setOp(false);
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
		}
		return true;
	}
	
}
