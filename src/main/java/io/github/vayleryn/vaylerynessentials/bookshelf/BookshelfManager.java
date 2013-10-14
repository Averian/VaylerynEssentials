package io.github.vayleryn.vaylerynessentials.bookshelf;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BookshelfManager {
	
	private VaylerynEssentials plugin;
	private Map<Block, Inventory> bookshelfInventories = new HashMap<Block, Inventory>();
	
	public BookshelfManager(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	public Map<Block, Inventory> getBookshelfInventories() {
		return bookshelfInventories;
	}
	
	public Inventory getBookshelfInventory(Block block) {
		return bookshelfInventories.get(block);
	}
	
	public void createBookshelfInventory(Block block) {
		bookshelfInventories.put(block, plugin.getServer().createInventory(null, 9, "Bookshelf"));
	}
	
	public void save() {
		File bookshelfDirectory = new File(plugin.getDataFolder().getPath() + File.separator + "bookshelves");
		if (!bookshelfDirectory.exists()) {
			bookshelfDirectory.mkdir();
		}
		for (Block block : bookshelfInventories.keySet()) {
			bookshelfDirectory = new File(plugin.getDataFolder().getPath() + File.separator + "bookshelves" + File.separator + block.getWorld().getName() + File.separator + block.getX() + File.separator + block.getY() + File.separator + block.getZ());
			if (!bookshelfDirectory.exists()) {
				bookshelfDirectory.mkdir();
			}
			YamlConfiguration bookshelfConfig = new YamlConfiguration();
			if (block.getType() == Material.BOOKSHELF) {
				bookshelfConfig.set("contents", Arrays.asList(plugin.getBookshelfInventory(block).getContents()));
			} else {
				bookshelfConfig.set("contents", null);
			}
			try {
				bookshelfConfig.save(new File(plugin.getDataFolder().getPath() + File.separator + "bookshelves" + File.separator + block.getWorld().getName() + File.separator + block.getX() + File.separator + block.getY() + File.separator + block.getZ() + File.separator + "bookshelf.yml"));
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		File bookshelfDirectory = new File(plugin.getDataFolder().getPath() + File.separator + "bookshelves");
		if (bookshelfDirectory.exists()) {
			for (File worldDirectory : bookshelfDirectory.listFiles()) {
				for (File xDirectory : worldDirectory.listFiles()) {
					for (File yDirectory : xDirectory.listFiles()) {
						for (File zDirectory : yDirectory.listFiles()) {
							File bookshelfFile = new File(zDirectory.getPath() + File.separator + "bookshelf.yml");
							if (bookshelfFile.exists()) {
								try {
									YamlConfiguration bookshelfConfig = new YamlConfiguration();
									bookshelfConfig.load(bookshelfFile);
									Inventory inventory = plugin.getServer().createInventory(null, 9, "Bookshelf");
									for (ItemStack itemStack : (List<ItemStack>) bookshelfConfig.get("contents")) {
										if (itemStack != null) {
											inventory.addItem(itemStack);
										}
									}
									bookshelfInventories.put(plugin.getServer().getWorld(worldDirectory.getName()).getBlockAt(Integer.parseInt(xDirectory.getName()), Integer.parseInt(yDirectory.getName()), Integer.parseInt(zDirectory.getName())), inventory);
								} catch (FileNotFoundException exception) {
									exception.printStackTrace();
								} catch (IOException exception) {
									exception.printStackTrace();
								} catch (InvalidConfigurationException exception) {
									exception.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
	
}
