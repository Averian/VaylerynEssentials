package io.github.vayleryn.vaylerynessentials;

import io.github.vayleryn.vaylerynlib.util.serialisation.SerialisableLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class WarpManager {
	
	private VaylerynEssentials plugin;
	private Map<String, Location> warps = new HashMap<String, Location>();
	
	public WarpManager(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	public Map<String, Location> getWarps() {
		return warps;
	}
	
	public Location getWarp(String name) {
		return warps.get(name);
	}
	
	public void save() {
		File warpFile = new File(plugin.getDataFolder().getPath() + File.separator + "warps.yml");
		YamlConfiguration warpConfig = new YamlConfiguration();
		for (String warpName : warps.keySet()) {
			warpConfig.set(warpName, new SerialisableLocation(warps.get(warpName)));
		}
		try {
			warpConfig.save(warpFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void load() {
		File warpFile = new File(plugin.getDataFolder().getPath() + File.separator + "warps.yml");
		if (warpFile.exists()) {
			YamlConfiguration warpConfig = new YamlConfiguration();
			try {
				warpConfig.load(warpFile);
			} catch (FileNotFoundException exception) {
				exception.printStackTrace();
			} catch (IOException exception) {
				exception.printStackTrace();
			} catch (InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			for (String warpName : warpConfig.getKeys(false)) {
				warps.put(warpName, ((SerialisableLocation) warpConfig.get(warpName)).toLocation());
			}
		}
	}
	
}
