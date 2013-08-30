package io.github.vayleryn.vaylerynessentials.command;

import java.util.List;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemMetaCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public ItemMetaCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.itemmeta")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
					if (args.length >= 2) {
						ItemMeta meta = player.getItemInHand().getItemMeta();
						if (args[0].equalsIgnoreCase("setname")) {
							meta.setDisplayName(args[1]);
						} else if (args[0].equalsIgnoreCase("addlore")) {
							List<String> lore = meta.getLore();
							lore.add(args[1]);
							meta.setLore(lore);
						} else if (args[0].equalsIgnoreCase("removelore")) {
							List<String> lore = meta.getLore();
							lore.remove(args[1]);
							meta.setLore(lore);
						} else {
							sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /itemmeta [setname|addlore|removelore] [name|lore]");
						}
						player.getItemInHand().setItemMeta(meta);
					} else {
						sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /itemmeta [setname|addlore|removelore] [name|lore]");
					}
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding something to enchant it.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "This command cannot be used from console.");
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
		}
		return true;
	}

}
