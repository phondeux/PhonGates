package com.phondeux.phongates;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
					  plugin.phonGates.add(block.getLocation());
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.SANDSTONE ) {
					  createPortal("sand", block);
					  plugin.phonGates.add(block.getLocation());
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.OBSIDIAN ) {
					  createPortal("nether", block);
					  plugin.phonGates.add(block.getLocation());
				  }
				  if (block.getRelative(0, -1, 0).getType() == Material.NETHER_BRICK ) {
					  createPortal("spawn", block);
					  plugin.phonGates.add(block.getLocation());
					  plugin.homeGates.put(block.getLocation(), event.getPlayer().getName());
				  }
			  }
		  }
	  }
	  if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
		  if (event.getPlayer().getItemInHand().getType() == Material.REDSTONE) {
			  plugin.getServer().broadcastMessage("Teleport Event");
			  event.getPlayer().setItemInHand(null);
		  }
	  }
   }
  
  public void createPortal(String portalType, Block block) {
	  Material coreMaterial = Material.AIR;
	  if (portalType.matches("sand")) {
		  coreMaterial = Material.SANDSTONE;
	  }
	  if (portalType.matches("spawn")) {
		  coreMaterial = Material.NETHER_BRICK;
	  }
	  if (portalType.matches("nether")) {
		  coreMaterial = Material.OBSIDIAN;
	  }
	  if (portalType.matches("goonimati")) {
		  coreMaterial = Material.LAPIS_BLOCK;
	  }
	  block.setType(Material.AIR);
	  block.getRelative(0, -1, 0).setType(Material.DIAMOND_BLOCK);
	  block.getRelative(1, -1, 0).setType(coreMaterial);
	  block.getRelative(1, -1, 1).setType(coreMaterial);
	  block.getRelative(1, -1, -1).setType(coreMaterial);
	  block.getRelative(0, -1, 1).setType(coreMaterial);
	  block.getRelative(0, -1, -1).setType(coreMaterial);
	  block.getRelative(-1, -1, 1).setType(coreMaterial);
	  block.getRelative(-1, -1, 0).setType(coreMaterial);
	  block.getRelative(-1, -1, -1).setType(coreMaterial);
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