package com.phondeux.phongates;

import org.bukkit.Material;
import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class PGBlockListener implements Listener {
	public static PhonGates plugin;

	public PGBlockListener(PhonGates phonGates) {
		plugin = phonGates;
	}

	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		if (event.getBlock().getType() != Material.PORTAL) {
			return;
		}
		
		if (isBlockInPortal(event.getBlock())) {
			event.setCancelled(true);
		}
	}

	private boolean isBlockInPortal(Block block) {
/*		if (block.getRelative(BlockFace.UP).getType() == Material.AIR) {
			return false;
		}
		
		if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
			return false;
		}
		
		if ( block.getRelative(BlockFace.NORTH).getType() != Material.AIR && block.getRelative(BlockFace.SOUTH).getType() != Material.AIR ) {
			return true;
		}
		
		if ( block.getRelative(BlockFace.WEST).getType() != Material.AIR && block.getRelative(BlockFace.EAST).getType() != Material.AIR ) {
			return true;
		}
		
		return false;
*/
		return true;
	}
}
