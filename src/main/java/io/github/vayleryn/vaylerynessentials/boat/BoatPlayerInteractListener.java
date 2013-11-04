package io.github.vayleryn.vaylerynessentials.boat;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BoatPlayerInteractListener implements Listener {
	
	private VaylerynEssentials plugin;
	
	public BoatPlayerInteractListener(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[boat]")) {
					Integer cost = Integer.parseInt(sign.getLine(1));
					World world = Bukkit.getServer().getWorld(sign.getLine(2));
					String coords = sign.getLine(3);
					Double x = Double.parseDouble(coords.split(",")[0]);
					Double y = Double.parseDouble(coords.split(",")[1]);
					Double z = Double.parseDouble(coords.split(",")[2]);
					Location location = new Location(world, x, y, z);
					Economy economy = plugin.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
					Player player = event.getPlayer();
					if (economy.getBalance(player.getName()) >= cost) {
						economy.withdrawPlayer(player.getName(), cost);
						player.teleport(location);
						player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
					} else {
						player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have enough money to make this boat journey!");
					}
				}
			}
		}
		
	}
	
}
