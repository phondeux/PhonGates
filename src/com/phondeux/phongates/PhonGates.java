package com.phondeux.phongates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PhonGates extends JavaPlugin
{
   final PGPlayerListener playerListener = new PGPlayerListener(this);
   public static final Logger log = Logger.getLogger("Minecraft");
   // Complete gate list
   public List<Location> phonGates = new ArrayList<Location>();
   // List of gates to spawn that players most recently took to spawn
   public HashMap<Location, String> homeGates = new HashMap<Location, String>();
   
  public void onEnable() {
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(playerListener, this);

    log.info("[PhonGates] Enabled.");
  }

  public void onDisable() {
    log.info("[PhonGates] Disabled.");
  }
}