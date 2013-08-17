package com.gildorymrp.essentials;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SerialisableLocation implements ConfigurationSerializable, Serializable {
	
	private static final long serialVersionUID = 1270044049208072115L;
	private String worldName;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;
	
	public SerialisableLocation(Location location) {
		this.worldName = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}
	
	public SerialisableLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> serialised = new HashMap<String, Object>();
		serialised.put("world", worldName);
		serialised.put("x", x);
		serialised.put("y", y);
		serialised.put("z", z);
		serialised.put("yaw", yaw);
		serialised.put("pitch", pitch);
		return serialised;
	}
	
	public static SerialisableLocation deserialize(Map<String, Object> serialised) {
		SerialisableLocation deserialised = new SerialisableLocation((String) serialised.get("world"), (Double) serialised.get("x"), (Double) serialised.get("y"), (Double) serialised.get("z"), (Float) serialised.get("yaw"), (Float) serialised.get("pitch"));
		return deserialised;
	}
	
	public Location toLocation() {
		return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
	}

}
