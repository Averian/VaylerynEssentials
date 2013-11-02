package io.github.vayleryn.vaylerynessentials.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class SpawnerCommand implements CommandExecutor {
	
	private VaylerynEssentials plugin;
	
	public SpawnerCommand(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("vayleryn.essentials.command.spawner")) {
			if (args.length >= 1) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					Block block = getTargetBlock(player, null, 32);
					if (block.getType() == Material.MOB_SPAWNER) {
						CreatureSpawner spawner = (CreatureSpawner) block.getState();
						try {
							EntityType entityType = EntityType.valueOf(args[0].toUpperCase());
							spawner.setSpawnedType(entityType);
							sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Spawner type set.");
						} catch (IllegalArgumentException exception) {
							sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That entity does not exist.");
						}
					} else {
						sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That's not a mob spawner.");
					}
				} else {
					sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
				}
			} else {
				sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the mob type.");
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
