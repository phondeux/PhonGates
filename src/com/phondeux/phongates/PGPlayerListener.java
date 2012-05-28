package com.phondeux.phongates;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PGPlayerListener implements Listener {
  public static PhonGates plugin;

  public PGPlayerListener(PhonGates instance) {
    plugin = instance;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
	  Block block = event.getClickedBlock();
	  if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
		  if (event.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD) {
			  if (block.getType() == Material.DIAMOND_BLOCK) {
				  if (block.getRelative(0, -1, 0).getType() == Material.LAPIS_BLOCK ) {
					  createPortal("goonimati", block);
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.OBSIDIAN ) {
					  createPortal("nether", block);
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.NETHER_BRICK ) {
					  createPortal("spawn", block);
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.SAND ) {
					  createPortal("sandv3", block);
				  }
			  }
		  }
		  if (event.getPlayer().getItemInHand().getType() == Material.REDSTONE) {
			  if (block.getType() == Material.DIAMOND_BLOCK) {
				  if (!confirmedTPEvent(block)) {
					  return;
				  }
				  Block destBlock = block.getRelative(BlockFace.DOWN);
				  // If netherbrick
				  //   if location is spawn portal send them back to their 'home portal'
				  //   else send to spawn and save their 'home portal'
				  if (destBlock.getType() == Material.NETHER_BRICK) {
					  double playerX = event.getPlayer().getLocation().getX();
					  double playerZ = event.getPlayer().getLocation().getZ();
					  
					  if (playerX > 2473 && playerX < 2482 && playerZ > 229  && playerZ < 237) {
						  if (plugin.homeGates.containsKey(event.getPlayer().getName())) {
							  block.getLocation().getWorld().strikeLightningEffect(plugin.homeGates.get(event.getPlayer().getName()));
							  event.getPlayer().teleport(plugin.homeGates.get(event.getPlayer().getName()));
							  block.getLocation().getWorld().strikeLightningEffect(block.getLocation());
							  return;
						  } else {
							  event.getPlayer().sendMessage("You must use a spawn portal first to return to it from here.");
							  return;
						  }
					  }
					  block.getLocation().getWorld().strikeLightningEffect(block.getLocation());
					  plugin.homeGates.put(event.getPlayer().getName(), event.getPlayer().getLocation());
					  Location spawn = new Location(plugin.getServer().getWorld("world_dead"), 2544, 66, 240);
					  event.getPlayer().teleport(spawn);
					  plugin.getServer().getWorld("world_dead").strikeLightningEffect(new Location(plugin.getServer().getWorld("world_dead"), 2544, 66, 240));
					  return;
				  }
				  // If sandstone or lapis
				  //   clear out 3 x 3 area centered on player in target world
				  //   tp player to target world
				  String destWorld = "world_dead";
				  Location destLoc = event.getPlayer().getLocation();
				  if (destBlock.getType() == Material.SAND) {
					  destWorld = "world_sandv3";
				  }
				  if (destBlock.getType() == Material.OBSIDIAN) {
					  destWorld = "world_newnether";
				  }
				  if (destLoc.getWorld().getName().equalsIgnoreCase(destWorld)) {
					  event.getPlayer().sendMessage("Portal Malfunction, destination must be another world");
					  return;
				  }
				  block.getLocation().getWorld().strikeLightningEffect(block.getLocation());
				  destLoc.setWorld(plugin.getServer().getWorld(destWorld));
				  clearDestination(destLoc);
				  event.getPlayer().teleport(destLoc);
				  block.getLocation().getWorld().strikeLightningEffect(destLoc);
			  }
		  }
	  }
   }
  
  private void clearDestination(Location destLoc) {
	 // destLoc is the bottom center block.
//	 destLoc.getBlock().setType(Material.AIR);
	 for (int x = 0; x < 3; x++) {
		 for (int y = 0; y < 3; y++) {
			 for (int z = 0; z < 3; z++) {
				 destLoc.getBlock().getRelative(x - 1, y, z - 1).setType(Material.AIR);
			 }
		 }
	 }
  }

  private boolean confirmedTPEvent(Block block) {
	// block beneath diamond is destination
	//    confirm if the nine beneath that are the same as the destination block
	  Block destBlock = block.getRelative(BlockFace.DOWN);
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

  public void createPortal(String portalType, Block block) {
	  Material coreMaterial = Material.AIR;
	  if (portalType.matches("spawn")) {
		  coreMaterial = Material.NETHER_BRICK;
	  }
	  if (portalType.matches("nether")) {
		  coreMaterial = Material.OBSIDIAN;
	  }
	  if (portalType.matches("goonimati")) {
		  coreMaterial = Material.LAPIS_BLOCK;
	  }
	  if (portalType.matches("sandv3")) {
		  coreMaterial = Material.SAND;
	  }
	  block.getRelative(0, -2, 0).setType(coreMaterial);
	  block.getRelative(1, -2, 0).setType(coreMaterial);
	  block.getRelative(1, -2, 1).setType(coreMaterial);
	  block.getRelative(1, -2, -1).setType(coreMaterial);
	  block.getRelative(0, -2, 1).setType(coreMaterial);
	  block.getRelative(0, -2, -1).setType(coreMaterial);
	  block.getRelative(-1, -2, 1).setType(coreMaterial);
	  block.getRelative(-1, -2, 0).setType(coreMaterial);
	  block.getRelative(-1, -2, -1).setType(coreMaterial);
	  block.getWorld().playEffect(block.getRelative(-1 , 0, -1).getLocation(), Effect.POTION_BREAK, 2);
	  block.getWorld().playEffect(block.getRelative(0 , 0, -1).getLocation(), Effect.POTION_BREAK, 2);
	  block.getWorld().playEffect(block.getRelative(1 , 0, -1).getLocation(), Effect.POTION_BREAK, 2);
	  block.getWorld().playEffect(block.getRelative(-1 , 0, 0).getLocation(), Effect.POTION_BREAK, 2);
	  block.getWorld().playEffect(block.getRelative(1 , 0, 0).getLocation(), Effect.POTION_BREAK, 2);
	  block.getWorld().playEffect(block.getRelative(-1 , 0, 1).getLocation(), Effect.POTION_BREAK, 2);
	  block.getWorld().playEffect(block.getRelative(0 , 0, 1).getLocation(), Effect.POTION_BREAK, 2);
	  block.getWorld().playEffect(block.getRelative(1 , 0, 1).getLocation(), Effect.POTION_BREAK, 2);

   }
}