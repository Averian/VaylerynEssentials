package io.github.vayleryn.vaylerynessentials;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class PortcullisManager {
	
	private boolean entityMovingEnabled;
	private int hoistingDelay, droppingDelay;
	private Set<Material> portcullisMaterials, powerBlocks;
	private boolean allowFloating, allPowerBlocksAllowed;
	public PortcullisManager(VaylerynEssentials plugin) {
		plugin.saveDefaultPortcullisConfig();
		FileConfiguration config = plugin.getPortcullisConfig();
		entityMovingEnabled = config.getBoolean("entityMoving");
		hoistingDelay = config.getInt("hoistingDelay");
		droppingDelay = config.getInt("droppingDelay");
		portcullisMaterials = new HashSet<Material>();
		for (String material : config.getStringList("portcullisMaterials")) {
			portcullisMaterials.add(Material.matchMaterial(material));
		}
		portcullisMaterials = Collections.unmodifiableSet(portcullisMaterials);
		allowFloating = config.getBoolean("allowFloating");
		powerBlocks = new HashSet<Material>();
		for (String material : config.getStringList("powerBlocks")) {
			powerBlocks.add(Material.matchMaterial(material));
		}
		powerBlocks = Collections.unmodifiableSet(powerBlocks);
		allPowerBlocksAllowed = powerBlocks.isEmpty();
	}
	
	public boolean isEntityMovingEnabled() {
		return entityMovingEnabled;
	}
	
	public int getDroppingDelay() {
		return droppingDelay;
	}
	
	public int getHoistingDelay() {
		return hoistingDelay;
	}
	
	public Set<Material> getPortcullisMaterials() {
		return portcullisMaterials;
	}
	
	public Set<Material> getPowerBlocks() {
		return powerBlocks;
	}
	
	public boolean isAllowFloating() {
		return allowFloating;
	}
	
	public boolean isAllPowerBlocksAllowed() {
		return allPowerBlocksAllowed;
	}
	
}
