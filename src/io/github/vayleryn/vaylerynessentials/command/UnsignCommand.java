package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class UnsignCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public UnsignCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.unsign")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.getItemInHand() != null) {
					if (player.getItemInHand().getType() == Material.WRITTEN_BOOK) {
						BookMeta meta = (BookMeta) player.getItemInHand().getItemMeta();
						player.getItemInHand().setType(Material.BOOK_AND_QUILL);
						player.getItemInHand().setItemMeta(meta);
						sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Book unsigned.");
					} else {
						sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding a written book to unsign.");
					}
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding a written book to unsign.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to execute this command.");
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
		}
		return true;
	}
	
}
