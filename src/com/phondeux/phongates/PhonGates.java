package com.phondeux.phongates;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class PhonGates extends JavaPlugin
{
   final PGPlayerListener playerListener = new PGPlayerListener(this);
   final PGBlockListener blockListener = new PGBlockListener(this);
   
   public static final Logger log = Logger.getLogger("Minecraft");
   // List of gates to spawn that players most recently took
   //    Should be saved to config as vectors
   public List<Location> playerGates = new ArrayList<Location>();
   
   private FileConfiguration customConfig = null;
   private File configFile = null;
   
   Towny towny;
   TownyUniverse tUniverse;
   
   public void onEnable() {
      PluginManager pm = getServer().getPluginManager();
      pm.registerEvents(playerListener, this);
      pm.registerEvents(blockListener, this);
      Plugin pTowny = pm.getPlugin("Towny");
      
      this.towny = (Towny) pTowny;
      this.tUniverse = towny.getTownyUniverse();

      //Load gates from config
      reloadCustomConfig();
      
      log.info("[PhonGates] Enabled.");
   }

   public void onDisable() {
      log.info("[PhonGates] Disabled.");
      // Save gates to config file
      saveCustomConfig();
   }
   
   public void reloadCustomConfig() {
	    if (configFile == null) {
	    configFile = new File(getDataFolder(), "customConfig.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = this.getResource("customConfig.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	}
   
   public FileConfiguration getCustomConfig() {
	    if (customConfig == null) {
	        this.reloadCustomConfig();
	    }
	    return customConfig;
	}
   
   public void saveCustomConfig() {
	    if (customConfig == null || configFile == null) {
	    return;
	    }
	    try {
	        getCustomConfig().save(configFile);
	    } catch (IOException ex) {
	        this.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
	    }
	}
}