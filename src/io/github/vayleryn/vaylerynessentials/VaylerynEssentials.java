package io.github.vayleryn.vaylerynessentials;

import io.github.vayleryn.vaylerynessentials.command.EnchantCommand;
import io.github.vayleryn.vaylerynessentials.command.ItemMetaCommand;
import io.github.vayleryn.vaylerynlib.Vayleryn;
import io.github.vayleryn.vaylerynlib.plugin.essentials.EssentialsPlugin;
import io.github.vayleryn.vaylerynlib.plugin.essentials.Kit;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class VaylerynEssentials extends JavaPlugin implements EssentialsPlugin {
	
	private WarpManager warpManager = new WarpManager(this);
	private KitManager kitManager = new KitManager(this);
	private BookshelfManager bookshelfManager = new BookshelfManager(this);
	
	@Override
	public void onEnable() {
		Vayleryn.registerEssentialsPlugin(this);
		ConfigurationSerialization.registerClass(SerialisableLocation.class);
		this.registerListeners(new PlayerInteractListener(this), new SignChangeListener(this), new BlockBreakListener(this));
		this.registerCommands();
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
	
	private void registerCommands() {
		this.getCommand("enchant").setExecutor(new EnchantCommand(this));
		this.getCommand("itemmeta").setExecutor(new ItemMetaCommand(this));
	}
	
	@Override
	public String getPrefix() {
		return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "VaylerynEssentials" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
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
