package io.github.vayleryn.vaylerynessentials.kit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit implements ConfigurationSerializable {
	
	private Collection<ItemStack> items = new ArrayList<ItemStack>();
	
	public void give(Player player) {
		for (ItemStack item : items) {
			player.getInventory().addItem(item);
		}
	}

	public Collection<ItemStack> getItems() {
		return items;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> serialised = new HashMap<String, Object>();
		serialised.put("items", items);
		return serialised;
	}
	
	@SuppressWarnings("unchecked")
	public static Kit deserialize(Map<String, Object> serialised) {
		Kit deserialised = new Kit();
		deserialised.items = (Collection<ItemStack>) serialised.get("items");
		return deserialised;
	}
	
}
