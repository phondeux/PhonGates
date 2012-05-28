package com.phondeux.phongates;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PGBlockListener implements Listener {
	public static PhonGates plugin;

	public PGBlockListener(PhonGates phonGates) {
		plugin = phonGates;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// Diamond, Sandstone, Lapis, Obsidian, or Netherbrick?
		//   Gate nearby?
		//     Destroy entire gate, drop one diamond block and one gate-type block
		//     Remove gate from list
		Block evtBlock = event.getBlock();
//plugin.getServer().broadcastMessage("Block Break Event");
		if (evtBlock.getType() == Material.DIAMOND_BLOCK) {
			if (!itsAPortal(evtBlock)) {
				return;
			} else {
				// It's a portal
				double playerX = event.getPlayer().getLocation().getX();
				double playerZ = event.getPlayer().getLocation().getZ();
				if (playerX > 2473 && playerX < 2482 && playerZ > 229  && playerZ < 237) {
					Location fatalSpawn = new Location(plugin.getServer().getWorld("world_dead"), 2436.5, 76, 240.5);
					plugin.getServer().broadcastMessage(event.getPlayer().getName() + " suffered a terrible portal accident!");
					event.getPlayer().teleport(fatalSpawn);
				} else {
					destroyPortal(evtBlock);
				}
			}
		}
		if (evtBlock.getType() == Material.NETHER_BRICK ||
			evtBlock.getType() == Material.OBSIDIAN ||
			evtBlock.getType() == Material.LAPIS_BLOCK ||
			evtBlock.getType() == Material.SAND) {
			// Is there a diamond block near it?
//plugin.getServer().broadcastMessage("Significant Block Break Event");
			if (!isDiamondNear(evtBlock)) {
				return;
			} else {
			//    if there is, is the block being broken part of a portal?
				if (itsAPortal(nearDiamondBlock(evtBlock))) {
					destroyPortal(nearDiamondBlock(evtBlock));
				}
			}
		}
	}

	private void destroyPortal(Block nearDiamondBlock) {
		// Passed the diamond block, destroy the portal and drop the diamond and portal type blocks naturally
	    Material destMaterial = nearDiamondBlock.getRelative(BlockFace.DOWN).getType();
		nearDiamondBlock.setType(Material.AIR);
		nearDiamondBlock.getWorld().dropItemNaturally(nearDiamondBlock.getLocation(), new ItemStack(Material.DIAMOND_BLOCK.getId(),1));
		nearDiamondBlock.getRelative(BlockFace.DOWN).setType(Material.AIR);
		nearDiamondBlock.getWorld().dropItemNaturally(nearDiamondBlock.getRelative(BlockFace.DOWN).getLocation(), new ItemStack(destMaterial.getId(),1));
		nearDiamondBlock.getRelative(-1,-2,-1).setType(Material.AIR);
		nearDiamondBlock.getRelative(0,-2,-1).setType(Material.AIR);
		nearDiamondBlock.getRelative(1,-2,-1).setType(Material.AIR);
		nearDiamondBlock.getRelative(-1,-2,0).setType(Material.AIR);
		nearDiamondBlock.getRelative(0,-2,0).setType(Material.AIR);
		nearDiamondBlock.getRelative(1,-2,0).setType(Material.AIR);
		nearDiamondBlock.getRelative(-1,-2,1).setType(Material.AIR);
		nearDiamondBlock.getRelative(0,-2,1).setType(Material.AIR);
		nearDiamondBlock.getRelative(1,-2,1).setType(Material.AIR);
	}

	private Block nearDiamondBlock(Block evtBlock) {
		if (evtBlock.getRelative(BlockFace.UP).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(BlockFace.UP);
		}
		if (evtBlock.getRelative(-1, 2, -1).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(-1, 2, -1);
		}
		if (evtBlock.getRelative(0, 2, -1).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(0, 2, -1);
		}
		if (evtBlock.getRelative(1, 2, -1).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(1, 2, -1);
		}
		if (evtBlock.getRelative(-1, 2, 0).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(-1, 2, 0);
		}
		if (evtBlock.getRelative(0, 2, 0).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(0, 2, 0);
		}
		if (evtBlock.getRelative(1, 2, 0).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(1, 2, 0);
		}
		if (evtBlock.getRelative(-1, 2, 1).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(-1, 2, 1);
		}
		if (evtBlock.getRelative(0, 2, 1).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(0, 2, 1);
		}
		if (evtBlock.getRelative(1, 2, 1).getType() == Material.DIAMOND_BLOCK) {
			return evtBlock.getRelative(1, 2, 1);
		}
		return null;
	}

	private boolean isDiamondNear(Block evtBlock) {
		// If part of a portal, there will be a diamond block either directly above it
		//    or in one of the nine squares above that
		if (evtBlock.getRelative(BlockFace.UP).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(-1, 2, -1).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(0, 2, -1).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(1, 2, -1).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(-1, 2, 0).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(0, 2, 0).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(1, 2, 0).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(-1, 2, 1).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(0, 2, 1).getType() == Material.DIAMOND_BLOCK ||
			evtBlock.getRelative(1, 2, 1).getType() == Material.DIAMOND_BLOCK) {
			return true;
		} else {
			return false;
		}
	}

	private boolean itsAPortal(Block evtBlock) {
		// Diamond block was passed
		// Confirm that the one beneath it and all that follow are portal blocks
		//   Rule is, one beneath diamond is world target, nine beneath that must be world target too
		Block destBlock = evtBlock.getRelative(BlockFace.DOWN);
	    Material destMaterial = destBlock.getType();
		if (destBlock.getRelative(-1, -1, -1).getType() == destMaterial &&
		    destBlock.getRelative(0, -1, -1).getType() == destMaterial &&
			destBlock.getRelative(1, -1, -1).getType() == destMaterial &&
			destBlock.getRelative(-1, -1, 0).getType() == destMaterial &&
			destBlock.getRelative(0, -1, 0).getType() == destMaterial &&
			destBlock.getRelative(1, -1, 0).getType() == destMaterial &&
			destBlock.getRelative(-1, -1, 1).getType() == destMaterial &&
			destBlock.getRelative(0, -1, 1).getType() == destMaterial &&
			destBlock.getRelative(1, -1, 1).getType() == destMaterial) {
			return true;
		} else {
			return false;
		}
	}
}
