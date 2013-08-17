package com.gildorymrp.essentials;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.gildorymrp.api.Gildorym;
import com.gildorymrp.api.plugin.essentials.GildorymEssentialsPlugin;
import com.gildorymrp.api.plugin.essentials.Kit;

public class GildorymEssentials extends JavaPlugin implements GildorymEssentialsPlugin {
	
	private WarpManager warpManager = new WarpManager(this);
	private KitManager kitManager = new KitManager(this);
	private BookshelfManager bookshelfManager = new BookshelfManager(this);
	
	@Override
	public void onEnable() {
		Gildorym.registerEssentialsPlugin(this);
		ConfigurationSerialization.registerClass(SerialisableLocation.class);
		this.registerListeners(new PlayerInteractListener(this), new SignChangeListener(), new BlockBreakListener(this));
		warpManager.load();
		kitManager.load();
		bookshelfManager.load();
	}
	
	@Override
	public void onDisable() {
		warpManager.save();
		kitManager.save();
		bookshelfManager.save();
	}
	
	private void registerListeners(Listener... listeners) {
		for (Listener listener : listeners) {
			this.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	
	@Override
	public Map<String, Location> getWarps() {
		return warpManager.getWarps();
	}
	
	@Override
	public Location getWarp(String name) {
		return warpManager.getWarp(name);
	}
	
	@Override
	public Map<String, Kit> getKits() {
		return kitManager.getKits();
	}
	
	@Override
	public Kit getKit(String name) {
		return kitManager.getKit(name);
	}
	
	public Inventory getBookshelfInventory(Block block) {
		return bookshelfManager.getBookshelfInventory(block);
	}
	
	public void createBookshelfInventory(Block block) {
		bookshelfManager.createBookshelfInventory(block);
	}
	
	public Map<Block, Inventory> getBookshelfInventories() {
		return bookshelfManager.getBookshelfInventories();
	}
	
}
