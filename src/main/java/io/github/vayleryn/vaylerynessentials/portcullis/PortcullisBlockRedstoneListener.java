package io.github.vayleryn.vaylerynessentials.portcullis;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class PortcullisBlockRedstoneListener implements Listener {
	
	private Set<Material> portcullisMaterials = new HashSet<Material>();
	
	public PortcullisBlockRedstoneListener() {
		portcullisMaterials.add(Material.FENCE);
		portcullisMaterials.add(Material.IRON_FENCE);
		portcullisMaterials.add(Material.NETHER_FENCE);
	}
	
	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event) {
		for (BlockFace face : BlockFace.values()) {
			if (portcullisMaterials.contains(event.getBlock().getRelative(face).getType())) {
				if (event.getNewCurrent() > event.getOldCurrent()) {
					movePortcullis(event.getBlock().getRelative(face), BlockFace.UP);
				} else {
					movePortcullis(event.getBlock().getRelative(face), BlockFace.DOWN);
				}
			}
		}
		
	}

	private void movePortcullis(Block block, BlockFace face) {
		for (Block portcullis : findBlocksInPortcullis(block)) {
			int i = 1;
			while ((portcullis.getRelative(face, i).getType() == Material.AIR || portcullisMaterials.contains(portcullis.getRelative(face, i).getType())) && i < 64) {
				i++;
			}
			portcullis.getRelative(face, i).setType(portcullis.getType());
		}
	}

	private Set<Block> findBlocksInPortcullis(Block block) {
		Set<Block> blocks = new HashSet<Block>();
		for (BlockFace face : BlockFace.values()) {
			if (block.getRelative(face).getType() == block.getType()) {
				if (!blocks.contains(block.getRelative(face))) {
					blocks.add(block.getRelative(face));
					blocks.addAll(findBlocksInPortcullis(block.getRelative(face)));
				}
			}
		}
		return blocks;
	}
	
}
