package io.github.vayleryn.vaylerynessentials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class PortcullisManager {
	
	private boolean entityMovingEnabled;
	private int hoistingDelay, droppingDelay;
	private Set<Material> portcullisMaterials, powerBlocks;
	private boolean allowFloating, allPowerBlocksAllowed;
	private static final int DEFAULT_HOISTING_DELAY = 40, DEFAULT_DROPPING_DELAY = 10;
	private static final Set<Material> DEFAULT_PORTCULLIS_MATERIALS = new HashSet<Material>(Arrays.asList(Material.FENCE, Material.IRON_FENCE, Material.NETHER_FENCE));
	private static final boolean DEFAULT_ALLOW_FLOATING = true;
	
	public PortcullisManager(VaylerynEssentials plugin) {
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
		List<String> warnings = new ArrayList<String>();
		if (hoistingDelay != DEFAULT_HOISTING_DELAY) {
			warnings.add("hoisting speed " + hoistingDelay);
		}
		if (droppingDelay != DEFAULT_DROPPING_DELAY) {
			warnings.add("dropping speed " + droppingDelay);
		}
		if (!portcullisMaterials.equals(DEFAULT_PORTCULLIS_MATERIALS)) {
			warnings.add("portcullis materials " + portcullisMaterials);
		}
		if (allowFloating != DEFAULT_ALLOW_FLOATING) {
			warnings.add("floating not allowed");
		}
		if (!allPowerBlocksAllowed) {
			warnings.add("power blocks allowed " + powerBlocks);
		}
		if (!warnings.isEmpty()) {
			StringBuilder sb = new StringBuilder("[PorteCoulissante] Non-standard configuration items loaded from config file: ");
			boolean first = true;
			for (String warning : warnings) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(warning);
			}
		}
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
