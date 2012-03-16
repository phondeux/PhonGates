package com.phondeux.phongates;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PhonGates extends JavaPlugin
{
   final PGPlayerListener playerListener = new PGPlayerListener(this);
   final PGBlockListener blockListener = new PGBlockListener(this);
   
   public static final Logger log = Logger.getLogger("Minecraft");
   // List of gates to spawn that players most recently took
   public HashMap<String, Location> homeGates = new HashMap<String, Location>();
   
  public void onEnable() {
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(playerListener, this);
    pm.registerEvents(blockListener, this);

    log.info("[PhonGates] Enabled.");
  }

  public void onDisable() {
    log.info("[PhonGates] Disabled.");
  }
}