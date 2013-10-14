package io.github.vayleryn.vaylerynessentials.portcullis;

import io.github.vayleryn.vaylerynessentials.VaylerynEssentials;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static org.bukkit.block.BlockFace.*;

import org.bukkit.event.block.BlockRedstoneEvent;

/**
 * @author pepijn
 */
public class PortcullisBlockRedstoneListener implements Listener {
	public PortcullisBlockRedstoneListener(VaylerynEssentials plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		Block block = event.getBlock();
		if (!((event.getOldCurrent() == 0) || (event.getNewCurrent() == 0))) {
			return;
		}
		if (!CONDUCTIVE.contains(block.getType())) {
			return;
		}
		boolean powerOn = event.getOldCurrent() == 0;
		for (BlockFace direction : CARDINAL_DIRECTIONS) {
			Portcullis portCullis = findPortcullisInDirection(block, direction);
			if (portCullis != null) {
				portCullis = normalisePortcullis(portCullis);
				if (powerOn) {
					hoistPortcullis(portCullis);
				} else {
					dropPortcullis(portCullis);
				}
			}
		}
	}
	
	private Portcullis findPortcullisInDirection(Block block, BlockFace direction) {
		Block powerBlock = block.getRelative(direction);
		Material powerBlockType = powerBlock.getType();
		if (isPotentialPowerBlock(powerBlockType)) {
			Block firstPortcullisBlock = powerBlock.getRelative(direction);
			if (isPotentialPortcullisBlock(firstPortcullisBlock)) {
				Material portcullisType = firstPortcullisBlock.getType();
				if ((portcullisType == powerBlockType)) {
					return null;
				}
				Block lastPortCullisBlock = firstPortcullisBlock.getRelative(direction);
				if (isPortcullisBlock(portcullisType, lastPortCullisBlock)) {
					int width = 2;
					Block nextBlock = lastPortCullisBlock.getRelative(direction);
					while (isPortcullisBlock(portcullisType, nextBlock)) {
						width++;
						lastPortCullisBlock = nextBlock;
						nextBlock = lastPortCullisBlock.getRelative(direction);
					}
					int highestY = firstPortcullisBlock.getLocation().getBlockY();
					Block nextBlockUp = firstPortcullisBlock.getRelative(UP);
					while (isPortcullisBlock(portcullisType, nextBlockUp)) {
						highestY++;
						nextBlockUp = nextBlockUp.getRelative(UP);
					}
					int lowestY = firstPortcullisBlock.getLocation().getBlockY();
					Block nextBlockDown = firstPortcullisBlock.getRelative(DOWN);
					while (isPortcullisBlock(portcullisType, nextBlockDown)) {
						lowestY--;
						nextBlockDown = nextBlockDown.getRelative(DOWN);
					}
					int height = highestY - lowestY + 1;
					if (height >= 2) {
						int x = firstPortcullisBlock.getX();
						int y = lowestY;
						int z = firstPortcullisBlock.getZ();
						World world = firstPortcullisBlock.getWorld();
						for (int i = -1; i <= width; i++) {
							for (int dy = -1; dy <= height; dy++) {
								if ((((i == -1) || (i == width)) && (dy != -1) && (dy != height)) || (((dy == -1) || (dy == height)) && (i != -1) && (i != width))) {
									Block frameBlock = world.getBlockAt(x + i * direction.getModX(), y + dy, z + i * direction.getModZ());
									if (isPortcullisBlock(portcullisType, frameBlock)) {
										return null;
									}
								} else if ((i >= 0) && (i < width) && (dy >= 0) && (dy < height)) {
									Block portcullisBlock = world.getBlockAt(x + i * direction.getModX(), y + dy, z + i * direction.getModZ());
									if (!isPortcullisBlock(portcullisType, portcullisBlock)) {
										return null;
									}
								}
							}
						}
						return new Portcullis(world.getName(), x, z, y, width, height, direction, portcullisType);
					}
				}
			}
		}
		return null;
	}
	
	private boolean isPotentialPowerBlock(Material powerBlockType) {
		return plugin.getPortcullisManager().isAllPowerBlocksAllowed() ? WALL_MATERIALS.contains(powerBlockType) : plugin.getPortcullisManager().getPowerBlocks().contains(powerBlockType);
	}
	
	private boolean isPotentialPortcullisBlock(Block block) {
		return plugin.getPortcullisManager().getPortcullisMaterials().contains(block.getType());
	}
	
	private boolean isPortcullisBlock(Material portcullisType, Block block) {
		return (block.getType() == portcullisType);
	}
	
	private Portcullis normalisePortcullis(Portcullis portcullis) {
		if (portcullis.getDirection() == NORTH) {
			return new Portcullis(portcullis.getWorldName(), portcullis.getX() - portcullis.getWidth() + 1, portcullis.getZ(), portcullis.getY(), portcullis.getWidth(), portcullis.getHeight(), SOUTH, portcullis.getType());
		} else if (portcullis.getDirection() == EAST) {
			return new Portcullis(portcullis.getWorldName(), portcullis.getX(), portcullis.getZ() - portcullis.getWidth() + 1, portcullis.getY(), portcullis.getWidth(), portcullis.getHeight(), WEST, portcullis.getType());
		} else {
			return portcullis;
		}
	}
	
	private void hoistPortcullis(final Portcullis portcullis) {
		for (PortcullisMover mover : portcullisMovers) {
			if (mover.getPortcullis().equals(portcullis)) {
				mover.setPortcullis(portcullis);
				mover.hoist();
				return;
			}
		}
		PortcullisMover mover = new PortcullisMover(plugin, portcullis);
		portcullisMovers.add(mover);
		mover.hoist();
	}
	
	private void dropPortcullis(final Portcullis portcullis) {
		for (PortcullisMover mover : portcullisMovers) {
			if (mover.getPortcullis().equals(portcullis)) {
				mover.setPortcullis(portcullis);
				mover.drop();
				return;
			}
		}
		PortcullisMover mover = new PortcullisMover(plugin, portcullis);
		portcullisMovers.add(mover);
		mover.drop();
	}
	
	private final VaylerynEssentials plugin;
	private Set<PortcullisMover> portcullisMovers = new HashSet<PortcullisMover>();
	static final Set<Material> WALL_MATERIALS = new HashSet<Material>(Arrays.asList(Material.STONE, Material.GRASS, Material.DIRT, Material.COBBLESTONE, Material.WOOD, Material.BEDROCK, Material.GOLD_ORE, Material.IRON_ORE, Material.COAL, Material.WOOD, Material.SPONGE, Material.GLASS, Material.LAPIS_ORE, Material.LAPIS_BLOCK, Material.SANDSTONE, Material.WOOL, Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.DOUBLE_STEP, Material.BRICK, Material.BOOKSHELF, Material.MOSSY_COBBLESTONE, Material.OBSIDIAN, Material.CHEST, Material.DIAMOND_ORE, Material.DIAMOND_BLOCK, Material.WORKBENCH, Material.SOIL, Material.FURNACE, Material.BURNING_FURNACE, Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE, Material.ICE, Material.SNOW_BLOCK, Material.CLAY, Material.JUKEBOX, Material.NETHERRACK, Material.SOUL_SAND, Material.GLOWSTONE, Material.MONSTER_EGGS, Material.SMOOTH_BRICK, Material.MYCEL, Material.NETHER_BRICK, Material.ENDER_PORTAL_FRAME, Material.ENDER_STONE, Material.REDSTONE_LAMP_OFF, Material.REDSTONE_LAMP_ON, Material.DOUBLE_STEP, Material.EMERALD_BLOCK, Material.EMERALD_ORE, Material.ENDER_CHEST));
	private static final BlockFace[] CARDINAL_DIRECTIONS = { NORTH, EAST, SOUTH, WEST };
	private static final Set<Material> CONDUCTIVE = new HashSet<Material>(Arrays.asList(Material.REDSTONE_WIRE, Material.REDSTONE_TORCH_ON, Material.REDSTONE_TORCH_OFF, Material.DIODE_BLOCK_ON, Material.DIODE_BLOCK_OFF, Material.STONE_BUTTON, Material.LEVER, Material.STONE_PLATE, Material.WOOD_PLATE, Material.TRIPWIRE_HOOK, Material.WOOD_BUTTON));
}
