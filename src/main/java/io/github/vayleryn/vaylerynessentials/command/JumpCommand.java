package io.github.vayleryn.vaylerynessentials.command;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class JumpCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public JumpCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.jump")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Block block = getTargetBlock(player, null, 64);
				if (block != null) {
					player.teleport(block.getLocation());
					sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Teleported to first block in line of sight.");
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a block within 64 blocks in your line of sight.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You may not use this command from console.");
			}
		} else {
			sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
		}
		return true;
	}
	
	private List<Block> getLineOfSight(LivingEntity entity, HashSet<Material> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        Iterator<Block> itr = new BlockIterator(entity, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (transparent == null) {
                if (material != Material.AIR) {
                    break;
                }
            } else {
                if (!transparent.contains(material)) {
                    break;
                }
            }
        }
        return blocks;
    }
	
	private Block getTargetBlock(LivingEntity entity, HashSet<Material> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(entity, transparent, maxDistance, 1);
        return blocks.get(0);
    }
	
}
