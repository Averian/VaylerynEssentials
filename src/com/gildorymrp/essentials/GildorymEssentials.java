package com.gildorymrp.essentials;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gildorymrp.api.Gildorym;
import com.gildorymrp.api.plugin.essentials.GildorymEssentialsPlugin;
import com.gildorymrp.api.plugin.essentials.Kit;

public class GildorymEssentials extends JavaPlugin implements GildorymEssentialsPlugin {
	
	private Map<String, Location> warps = new HashMap<String, Location>();
	private Map<String, Kit> kits = new HashMap<String, Kit>();
	
	@Override
	public void onEnable() {
		Gildorym.registerEssentialsPlugin(this);
		ConfigurationSerialization.registerClass(SerialisableLocation.class);
		File warpFile = new File(this.getDataFolder() + File.separator + "warps.yml");
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
		File kitFile = new File(this.getDataFolder() + File.separator + "kits.yml");
		YamlConfiguration kitConfig = new YamlConfiguration();
		try {
			kitConfig.load(kitFile);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (InvalidConfigurationException exception) {
			exception.printStackTrace();
		}
		for (String kitName : kitConfig.getKeys(false)) {
			kits.put(kitName, (Kit) kitConfig.get(kitName));
		}
		this.registerListeners(new PlayerInteractListener(), new SignChangeListener());
	}
	
	@Override
	public void onDisable() {
		File warpFile = new File(this.getDataFolder() + File.separator + "warps.yml");
		YamlConfiguration warpConfig = new YamlConfiguration();
		for (String warpName : warps.keySet()) {
			warpConfig.set(warpName, new SerialisableLocation(warps.get(warpName)));
		}
		try {
			warpConfig.save(warpFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		File kitFile = new File(this.getDataFolder() + File.separator + "kits.yml");
		YamlConfiguration kitConfig = new YamlConfiguration();
		for (String kitName : kits.keySet()) {
			kitConfig.set(kitName, kits.get(kitName));
		}
		try {
			kitConfig.save(kitFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	private void registerListeners(Listener... listeners) {
		for (Listener listener : listeners) {
			this.getServer().getPluginManager().registerEvents(listener, this);
		}
	}

	@Override
	public Map<String, Location> getWarps() {
		return warps;
	}

	@Override
	public Location getWarp(String name) {
		return warps.get(name);
	}

	@Override
	public Map<String, Kit> getKits() {
		return kits;
	}

	@Override
	public Kit getKit(String name) {
		return kits.get(name);
	}

}
