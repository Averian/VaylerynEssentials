package io.github.vayleryn.vaylerynessentials;

import io.github.vayleryn.vaylerynessentials.boat.BoatPlayerInteractListener;
import io.github.vayleryn.vaylerynessentials.boat.BoatSignChangeListener;
import io.github.vayleryn.vaylerynessentials.bookshelf.BookshelfBlockBreakListener;
import io.github.vayleryn.vaylerynessentials.bookshelf.BookshelfManager;
import io.github.vayleryn.vaylerynessentials.bookshelf.BookshelfPlayerInteractListener;
import io.github.vayleryn.vaylerynessentials.command.EnchantCommand;
import io.github.vayleryn.vaylerynessentials.command.FeedCommand;
import io.github.vayleryn.vaylerynessentials.command.FlyCommand;
import io.github.vayleryn.vaylerynessentials.command.HealCommand;
import io.github.vayleryn.vaylerynessentials.command.InventoryCommand;
import io.github.vayleryn.vaylerynessentials.command.ItemCommand;
import io.github.vayleryn.vaylerynessentials.command.ItemMetaCommand;
import io.github.vayleryn.vaylerynessentials.command.JumpCommand;
import io.github.vayleryn.vaylerynessentials.command.KitCommand;
import io.github.vayleryn.vaylerynessentials.command.MsgCommand;
import io.github.vayleryn.vaylerynessentials.command.RepairCommand;
import io.github.vayleryn.vaylerynessentials.command.RunAsCommand;
import io.github.vayleryn.vaylerynessentials.command.SetSpawnCommand;
import io.github.vayleryn.vaylerynessentials.command.SetWarpCommand;
import io.github.vayleryn.vaylerynessentials.command.SpawnCommand;
import io.github.vayleryn.vaylerynessentials.command.SpawnMobCommand;
import io.github.vayleryn.vaylerynessentials.command.SpawnerCommand;
import io.github.vayleryn.vaylerynessentials.command.SpeedCommand;
import io.github.vayleryn.vaylerynessentials.command.SudoCommand;
import io.github.vayleryn.vaylerynessentials.command.UnsignCommand;
import io.github.vayleryn.vaylerynessentials.command.WarpCommand;
import io.github.vayleryn.vaylerynessentials.kit.Kit;
import io.github.vayleryn.vaylerynessentials.kit.KitManager;
import io.github.vayleryn.vaylerynessentials.portcullis.PortcullisBlockRedstoneListener;
import io.github.vayleryn.vaylerynessentials.portcullis.PortcullisPlayerInteractListener;
import io.github.vayleryn.vaylerynessentials.portcullis.PortcullisSignChangeListener;
import io.github.vayleryn.vaylerynessentials.warp.WarpManager;
import io.github.vayleryn.vaylerynlib.util.serialisation.SerialisableLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class VaylerynEssentials extends JavaPlugin {
	
	private WarpManager warpManager;
	private KitManager kitManager;
	private BookshelfManager bookshelfManager;
	
	@Override
	public void onEnable() {
		ConfigurationSerialization.registerClass(Kit.class);
		ConfigurationSerialization.registerClass(SerialisableLocation.class);
		this.registerListeners(new BoatPlayerInteractListener(this), new BoatSignChangeListener(this),
				new BookshelfBlockBreakListener(this), new BookshelfPlayerInteractListener(this),
				new PlayerJoinListener(this),
				new PortcullisBlockRedstoneListener(), new PortcullisPlayerInteractListener(), new PortcullisSignChangeListener(this));
		this.registerCommands();
		warpManager = new WarpManager(this);
		kitManager = new KitManager(this);
		bookshelfManager = new BookshelfManager(this);
		loadState();
	}
	
	@Override
	public void onDisable() {
		saveState();
	}
	
	private void registerListeners(Listener... listeners) {
		for (Listener listener : listeners) {
			this.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	
	private void registerCommands() {
		this.getCommand("enchant").setExecutor(new EnchantCommand(this));
		this.getCommand("feed").setExecutor(new FeedCommand(this));
		this.getCommand("fly").setExecutor(new FlyCommand(this));
		this.getCommand("heal").setExecutor(new HealCommand(this));
		this.getCommand("inventory").setExecutor(new InventoryCommand(this));
		this.getCommand("item").setExecutor(new ItemCommand(this));
		this.getCommand("itemmeta").setExecutor(new ItemMetaCommand(this));
		this.getCommand("jump").setExecutor(new JumpCommand(this));
		this.getCommand("kit").setExecutor(new KitCommand(this));
		this.getCommand("msg").setExecutor(new MsgCommand(this));
		this.getCommand("repair").setExecutor(new RepairCommand(this));
		this.getCommand("runas").setExecutor(new RunAsCommand(this));
		this.getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
		this.getCommand("setwarp").setExecutor(new SetWarpCommand(this));
		this.getCommand("spawn").setExecutor(new SpawnCommand(this));
		this.getCommand("spawner").setExecutor(new SpawnerCommand(this));
		this.getCommand("spawnmob").setExecutor(new SpawnMobCommand(this));
		this.getCommand("speed").setExecutor(new SpeedCommand(this));
		this.getCommand("sudo").setExecutor(new SudoCommand(this));
		this.getCommand("unsign").setExecutor(new UnsignCommand(this));
		this.getCommand("warp").setExecutor(new WarpCommand(this));
	}
	
	public String getPrefix() {
		return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + getDescription().getName() + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
	}
	
	public void loadState() {
		warpManager.load();
		kitManager.load();
		bookshelfManager.load();
	}
	
	public void saveState() {
		warpManager.save();
		kitManager.save();
		bookshelfManager.save();
	}
	
	public Map<String, Location> getWarps() {
		return warpManager.getWarps();
	}
	
	public Location getWarp(String name) {
		return warpManager.getWarp(name);
	}
	
	public Map<String, Kit> getKits() {
		return kitManager.getKits();
	}
	
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

	public FileConfiguration getPortcullisConfig() {
		YamlConfiguration portcullisConfig = new YamlConfiguration();
		try {
			portcullisConfig.load(getDataFolder() + File.separator + "portcullis.yml");
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (InvalidConfigurationException exception) {
			exception.printStackTrace();
		}
		return portcullisConfig;
	}
	
    public void saveDefaultPortcullisConfig() {
    	saveResource("portcullis.yml", false);
    }
	
}
