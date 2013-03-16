package com.phondeux.phongates;

//import org.bukkit.Location;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
//import org.bukkit.block.BlockFace;
//import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class PGPlayerListener implements Listener {
  public static PhonGates plugin;
  Material matSource = null;
  Material matTarget = null;
  String destWorld = "";

  public PGPlayerListener(PhonGates instance) {
    plugin = instance;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
	  if (event.isCancelled()) {
		  return;
	  }
	  
	  Block block = event.getClickedBlock();
	  if (event.getPlayer().getWorld().getName().matches("world")) {
		  matSource = Material.LAPIS_BLOCK;
	  } else if (event.getPlayer().getWorld().getName().matches("world_nether")) {
		  matSource = Material.OBSIDIAN;
	  } else if (event.getPlayer().getWorld().getName().matches("world_sandv3")) {
		  matSource = Material.SAND;
	  }
	  if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
		  if (event.getPlayer().getItemInHand().getType() == Material.REDSTONE) {
			  if (block.getType() == Material.DIAMOND_BLOCK) {
				  if (block.getRelative(0, -1, 0).getType() == Material.LAPIS_BLOCK ) {
					  matTarget = Material.LAPIS_BLOCK;
					  destWorld = "world";
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.OBSIDIAN ) {
					  matTarget = Material.OBSIDIAN;
					  destWorld = "world_nether";
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.NETHER_BRICK ) {
					  matTarget = Material.NETHER_BRICK;
					  destWorld = "spawn";
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.SAND ) {
					  matTarget = Material.SAND;
					  destWorld = "world_sandv3";
				  }
				  if (matTarget != null) {
					  if (isSafeBlock(block.getLocation(), event.getPlayer())) {
						  createPortal(matTarget, block.getLocation());
					  }
					  if (!destWorld.matches("spawn")) {
						  if (isSafeBlock(new Location(event.getPlayer().getServer().getWorld(destWorld), block.getX(), block.getY(), block.getZ()), event.getPlayer())) {
							  createPortal(matSource, new Location(event.getPlayer().getServer().getWorld(destWorld), block.getX(), block.getY(), block.getZ()));
						  }
					  }
				  }
			  }
		  }
	  }
  }
  
private boolean isSafeBlock(Location bLoc, Player player) {
	  // TODO: Create Return portal
	  // 1. Can player create a portal at location?
	  //    - Check the four corner blocks
	  // Towny
	  // boolean TownyUniverse.isWildernessBlock(Block)
	  // WorldCoord WorldCoord.parseWorldCoord(Block)
	  // TownyUniverse.getTownBlock(Location)
	  TownBlock tBlock = TownyUniverse.getTownBlock(bLoc);
	  try {
		if (TownyUniverse.isWilderness(bLoc.getBlock()) || tBlock.getTown().hasResident(player.getName())) {
			  return true;
		  }
		else {
			  return false;
		}
	} catch (NotRegisteredException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return true;
	}
}

@EventHandler
  public void onEntityPortalEnter(EntityPortalEnterEvent event) {
	  Block portLoc = event.getLocation().getBlock().getRelative(BlockFace.DOWN);
	  
/*	  if (event.getEntity() instanceof Player) {
		  plugin.getServer().broadcastMessage("Player entered portal");
	  }
*/	  
      if (!destPortal(portLoc).toString().matches("error")) {
    	  if (destPortal(portLoc).matches("LAPIS_BLOCK")) {
//    		  plugin.getServer().broadcastMessage("Player entered homeworld");
              Location destPlayer = new Location(plugin.getServer().getWorld("world"), event.getEntity().getLocation().getX(), event.getEntity().getLocation().getY(), event.getEntity().getLocation().getZ());
              event.getEntity().teleport(destPlayer.add(0.5, 0, 0.5));
    	  } else if (destPortal(portLoc).matches("OBSIDIAN")) {
//    		  plugin.getServer().broadcastMessage("Player entered nether");
              Location destPlayer = new Location(plugin.getServer().getWorld("world_nether"), event.getEntity().getLocation().getX(), event.getEntity().getLocation().getY(), event.getEntity().getLocation().getZ());
              event.getEntity().teleport(destPlayer.add(0.5, 0, 0.5));
    	  } else if (destPortal(portLoc).matches("SAND")) {
//    		  plugin.getServer().broadcastMessage("Player entered sand");
              Location destPlayer = new Location(plugin.getServer().getWorld("world_sandv3"), event.getEntity().getLocation().getX(), event.getEntity().getLocation().getY(), event.getEntity().getLocation().getZ());
              event.getEntity().teleport(destPlayer.add(0.5, 0, 0.5));
    	  } else if (destPortal(portLoc).matches("NETHER_BRICK")) {
//    		  plugin.getServer().broadcastMessage("Player entered spawn");
    	  }
      }
	  return;
  }

  private String destPortal(Block block) {
	// All eight blocks surrounding  the block must be of a portal destination material type
	// Spawn == NETHER_BRICK
	// Nether == OBSIDIAN
	// Goonimati == LAPIS_BLOCK
	// Sand == SAND
	Material portalMaterial = block.getRelative(1, 0, 0).getType();
	if ((block.getRelative(1, 0, 1).getType() == portalMaterial) &&
		(block.getRelative(1, 0, -1).getType() == portalMaterial) &&
		(block.getRelative(0, 0, 1).getType() == portalMaterial) &&
		(block.getRelative(0, 0, -1).getType() == portalMaterial) &&
		(block.getRelative(-1, 0, 0).getType() == portalMaterial) &&
		(block.getRelative(-1, 0, 1).getType() == portalMaterial) &&
		(block.getRelative(-1, 0, -1).getType() == portalMaterial)) {
		if ((portalMaterial == Material.NETHER_BRICK) ||
			(portalMaterial == Material.OBSIDIAN) ||	
			(portalMaterial == Material.LAPIS_BLOCK) ||	
			(portalMaterial == Material.SAND)) {
			return portalMaterial.toString();
		} else {
			return "error";
		}
	}
	return "error";
}

public void createPortal(Material portalType, Location portLoc) {
	  Material coreMaterial = portalType;

	  // TODO: Change this whole section to a loop ...
	  portLoc.getBlock().getRelative(0, -2, 0).setType(Material.GLASS);
	  portLoc.getBlock().getRelative(1, -2, 0).setType(coreMaterial);
	  portLoc.getBlock().getRelative(1, -2, 1).setType(coreMaterial);
	  portLoc.getBlock().getRelative(1, -2, -1).setType(coreMaterial);
	  portLoc.getBlock().getRelative(0, -2, 1).setType(coreMaterial);
	  portLoc.getBlock().getRelative(0, -2, -1).setType(coreMaterial);
	  portLoc.getBlock().getRelative(-1, -2, 1).setType(coreMaterial);
	  portLoc.getBlock().getRelative(-1, -2, 0).setType(coreMaterial);
	  portLoc.getBlock().getRelative(-1, -2, -1).setType(coreMaterial);
	  portLoc.getBlock().getRelative(0, -1, 0).setTypeId(Material.PORTAL.getId());
	  portLoc.getBlock().getRelative(0, 0, 0).setTypeId(Material.PORTAL.getId());
	  portLoc.getBlock().getRelative(-1, -1, 1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(0, -1, 1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(1, -1, 1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(-1, -1, 0).setType(Material.AIR);
	  portLoc.getBlock().getRelative(1, -1, 0).setType(Material.AIR);
	  portLoc.getBlock().getRelative(-1, -1, -1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(0, -1, -1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(1, -1, -1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(-1, 0, 1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(0, 0, 1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(1, 0, 1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(-1, 0, 0).setType(Material.AIR);
	  portLoc.getBlock().getRelative(1, 0, 0).setType(Material.AIR);
	  portLoc.getBlock().getRelative(-1, 0, -1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(0, 0, -1).setType(Material.AIR);
	  portLoc.getBlock().getRelative(1, 0, -1).setType(Material.AIR);
	  
	  Vector vector = portLoc.getBlock().getLocation().toVector();
	  plugin.getConfig().set("gates", vector);
   }
  
  
}