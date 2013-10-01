package io.github.vayleryn.vaylerynessentials;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * @author pepijn
 */
public class PortcullisMover implements Runnable {
	public PortcullisMover(VaylerynEssentials plugin, Portcullis portcullis) {
		this.plugin = plugin;
		this.portcullis = portcullis;
	}
	
	public Portcullis getPortcullis() {
		return portcullis;
	}
	
	void setPortcullis(Portcullis portcullis) {
		if (!portcullis.equals(this.portcullis)) {
			throw new IllegalArgumentException();
		}
		this.portcullis = portcullis;
	}
	
	public void hoist() {
		if (status == Status.HOISTING) {
			return;
		}
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		if (status == Status.DROPPING) {
			scheduler.cancelTask(taskId);
		}
		int hoistingDelay = plugin.getPortcullisManager().getHoistingDelay();
		taskId = scheduler.scheduleSyncRepeatingTask(plugin, this, hoistingDelay / 2, hoistingDelay);
		status = Status.HOISTING;
	}
	
	public void drop() {
		if (status == Status.DROPPING) {
			return;
		}
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		if (status == Status.HOISTING) {
			scheduler.cancelTask(taskId);
		}
		int droppingDelay = plugin.getPortcullisManager().getDroppingDelay();
		taskId = scheduler.scheduleSyncRepeatingTask(plugin, this, droppingDelay, droppingDelay);
		status = Status.DROPPING;
	}
	
	@Override
	public void run() {
		if ((status == Status.HOISTING) && (!movePortcullisUp(portcullis))) {
			plugin.getServer().getScheduler().cancelTask(taskId);
			taskId = 0;
			status = Status.IDLE;
		} else if ((status == Status.DROPPING) && (!movePortcullisDown(portcullis))) {
			plugin.getServer().getScheduler().cancelTask(taskId);
			taskId = 0;
			status = Status.IDLE;
		}
	}
	
	private boolean movePortcullisUp(Portcullis portcullis) {
		World world = plugin.getServer().getWorld(portcullis.getWorldName());
		if (world == null) {
			return false;
		}
		int x = portcullis.getX();
		int z = portcullis.getZ();
		int y = portcullis.getY();
		int width = portcullis.getWidth();
		int height = portcullis.getHeight();
		BlockFace direction = portcullis.getDirection();
		Set<Point> chunkCoords = getChunkCoords(x, z, direction, width);
		if (!areChunksLoaded(world, chunkCoords)) {
			return false;
		}
		if (!isPortcullisWhole(world)) {
			return false;
		}
		if (y + height >= world.getMaxHeight()) {
			explodePortcullis(world);
			return false;
		}
		int dx = direction.getModX(), dz = direction.getModZ();
		if (!plugin.getPortcullisManager().isAllowFloating()) {
			boolean solidBlockFound = false;
			for (int yy = y + 1; yy <= y + height; yy++) {
				Material blockType = world.getBlockAt(x - dx, yy, z - dz).getType();
				if (BlockRedstoneListener.WALL_MATERIALS.contains(blockType) || SUPPORTING_MATERIALS.contains(blockType)) {
					solidBlockFound = true;
					break;
				}
				blockType = world.getBlockAt(x + width * dx, yy, z + width * dz).getType();
				if (BlockRedstoneListener.WALL_MATERIALS.contains(blockType) || SUPPORTING_MATERIALS.contains(blockType)) {
					solidBlockFound = true;
					break;
				}
			}
			if (!solidBlockFound) {
				return false;
			}
		}
		for (int i = 0; i < width; i++) {
			Block block = world.getBlockAt(x + i * dx, y + height, z + i * dz);
			if (!AIR_MATERIALS.contains(block.getType())) {
				return false;
			}
		}
		Material portcullisType = portcullis.getType();
		for (int i = 0; i < width; i++) {
			Block block = world.getBlockAt(x + i * dx, y + height, z + i * dz);
			block.setType(portcullisType);
			block = world.getBlockAt(x + i * dx, y, z + i * dz);
			block.setType(Material.AIR);
		}
		if (plugin.getPortcullisManager().isEntityMovingEnabled()) {
			moveEntitiesUp(world, chunkCoords, portcullis);
		}
		portcullis.setY(y + 1);
		return true;
	}
	
	private boolean movePortcullisDown(Portcullis portcullis) {
		World world = plugin.getServer().getWorld(portcullis.getWorldName());
		if (world == null) {
			return false;
		}
		int x = portcullis.getX();
		int z = portcullis.getZ();
		int y = portcullis.getY();
		int width = portcullis.getWidth();
		int height = portcullis.getHeight();
		BlockFace direction = portcullis.getDirection();
		if (!areChunksLoaded(world, getChunkCoords(x, z, direction, width))) {
			return false;
		}
		if (!isPortcullisWhole(world)) {
			return false;
		}
		if (y <= 0) {
			return false;
		}
		int dx = direction.getModX(), dz = direction.getModZ();
		for (int i = 0; i < width; i++) {
			Block block = world.getBlockAt(x + i * dx, y - 1, z + i * dz);
			if (!AIR_MATERIALS.contains(block.getType())) {
				return false;
			}
		}
		Material portcullisType = portcullis.getType();
		y--;
		for (int i = 0; i < width; i++) {
			Block block = world.getBlockAt(x + i * dx, y + height, z + i * dz);
			block.setType(Material.AIR);
			block = world.getBlockAt(x + i * dx, y, z + i * dz);
			block.setType(portcullisType);
		}
		portcullis.setY(y);
		return true;
	}
	
	private void moveEntitiesUp(World world, Set<Point> chunkCoords, Portcullis portcullis) {
		for (Point chunkCoord : chunkCoords) {
			Chunk chunk = world.getChunkAt(chunkCoord.x, chunkCoord.y);
			for (Entity entity : chunk.getEntities()) {
				Location location = entity.getLocation();
				if (isOnPortcullis(location, portcullis)) {
					location.setY(location.getY() + 1);
					entity.teleport(location);
				}
			}
		}
	}
	
	private boolean isOnPortcullis(Location location, Portcullis portcullis) {
		int x = portcullis.getX(), y = portcullis.getY(), z = portcullis.getZ(), width = portcullis.getWidth(), height = portcullis.getHeight();
		BlockFace direction = portcullis.getDirection();
		int x2 = x + direction.getModX() * width;
		int z2 = z + direction.getModZ() * width;
		if (x > x2) {
			int tmp = x;
			x = x2;
			x2 = tmp;
		}
		if (z > z2) {
			int tmp = z;
			z = z2;
			z2 = tmp;
		}
		int locX = location.getBlockX(), locY = location.getBlockY(), locZ = location.getBlockZ();
		return (locX >= x) && (locX <= x2) && (locZ >= z) && (locZ <= z2) && (locY == (y + height));
	}
	
	private boolean areChunksLoaded(World world, Set<Point> chunkCoords) {
		for (Point point : chunkCoords) {
			if (!world.isChunkLoaded(point.x, point.y)) {
				return false;
			}
		}
		return true;
	}
	
	private Set<Point> getChunkCoords(int x, int z, BlockFace direction, int width) {
		Set<Point> chunkCoords = new HashSet<Point>();
		int firstChunkX = x >> 4;
		int firstChunkZ = z >> 4;
		chunkCoords.add(new Point(firstChunkX, firstChunkZ));
		int secondChunkX = (x + direction.getModX() * (width - 1)) >> 4;
		int secondChunkZ = (z + direction.getModZ() * (width - 1)) >> 4;
		if ((secondChunkX != firstChunkX) || (secondChunkZ != firstChunkZ)) {
			chunkCoords.add(new Point(secondChunkX, secondChunkZ));
		}
		return chunkCoords;
	}
	
	private boolean isPortcullisWhole(World world) {
		int portcullisX = portcullis.getX();
		int portcullisY1 = portcullis.getY(), portcullisY2 = portcullisY1 + portcullis.getHeight();
		int portcullisZ = portcullis.getZ();
		int portcullisWidth = portcullis.getWidth();
		BlockFace portcullisDirection = portcullis.getDirection();
		int dx = portcullisDirection.getModX(), dz = portcullisDirection.getModZ();
		Material portcullisType = portcullis.getType();
		for (int y = portcullisY1; y < portcullisY2; y++) {
			int x = portcullisX;
			int z = portcullisZ;
			for (int i = 0; i < portcullisWidth; i++) {
				Block block = world.getBlockAt(x, y, z);
				if (block.getType() != portcullisType) {
					return false;
				}
				x += dx;
				z += dz;
			}
		}
		return true;
	}
	
	private void explodePortcullis(World world) {
		int portcullisX = portcullis.getX();
		int portcullisY1 = portcullis.getY(), portcullisY2 = portcullisY1 + portcullis.getHeight();
		int portcullisZ = portcullis.getZ();
		BlockFace portcullisDirection = portcullis.getDirection();
		Material portcullisType = portcullis.getType();
		ItemStack itemStack = new ItemStack(portcullisType, 1, (short) 0);
		for (int y = portcullisY1; y < portcullisY2; y++) {
			int x = portcullisX;
			int z = portcullisZ;
			for (int i = 0; i < portcullis.getWidth(); i++) {
				Block block = world.getBlockAt(x, y, z);
				block.setType(Material.AIR);
				world.dropItemNaturally(block.getLocation(), itemStack);
				x += portcullisDirection.getModX();
				z += portcullisDirection.getModZ();
			}
		}
	}
	
	private final VaylerynEssentials plugin;
	private Portcullis portcullis;
	private int taskId;
	private Status status = Status.IDLE;
	private static final Set<Material> AIR_MATERIALS = new HashSet<Material>(Arrays.asList(Material.AIR, Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA, Material.SUGAR_CANE, Material.SNOW, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.FIRE, Material.WHEAT, Material.LONG_GRASS, Material.DEAD_BUSH, Material.WEB, Material.PUMPKIN_STEM, Material.MELON_STEM, Material.VINE));
	private static final Set<Material> SUPPORTING_MATERIALS = new HashSet<Material>(Arrays.asList(Material.FENCE, Material.FENCE_GATE, Material.IRON_FENCE, Material.NETHER_FENCE, Material.WOOD_STEP, Material.STEP, Material.WOOD_STAIRS, Material.COBBLESTONE_STAIRS, Material.NETHER_BRICK_STAIRS, Material.SANDSTONE_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.JUNGLE_WOOD_STAIRS));
	
	static enum Status {
		IDLE, HOISTING, DROPPING
	}
}