package me.tiskua.rankgrant.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.tiskua.rankgrant.utils.Util;

public class Files {
	
	public static File logfile;
	public static FileConfiguration log;

	public static File configfile;
	public static FileConfiguration config;


	public static void base(Main main) {
		if(!main.getDataFolder().exists()) {
			main.getDataFolder().mkdirs();
		}
		//log.yml
		logfile = new File(main.getDataFolder(), "log.yml");
		if(!logfile.exists()) {
			main.saveResource("log.yml", false);
		}
		log = YamlConfiguration.loadConfiguration(logfile);

		//config.yml
		if(Util.isLegacy()) {
			configfile = new File(main.getDataFolder(), "config.yml");
			if(!configfile.exists()) {
				main.saveResource("config.yml", false);
			}
			config = YamlConfiguration.loadConfiguration(configfile);
		} else {
			configfile = new File(main.getDataFolder(), "config-new.yml");
			if(!configfile.exists()) {
				main.saveResource("config-new.yml", false);
			}
			config = YamlConfiguration.loadConfiguration(configfile);			
		}
	}
	
	public static void fixConfig(Main main, Player player) {
		player.sendMessage(Util.format("&7------------------------------------\n&3&lFIXING CONFIG:"));
		File backup  = new File(main.getDataFolder(), "backup.yml");
		if(backup.exists()) 
			backup.delete();
		try {
			java.nio.file.Files.copy(Files.configfile.toPath(), backup.toPath());
			player.sendMessage(Util.format("&a&lCreated a backup of config file!"));
		} catch (IOException e) {
			player.sendMessage(Util.format("&c&lThere was an error creating a backup!"));
			e.printStackTrace();
		}
		
		Files.configfile.delete();
		player.sendMessage(Util.format("&e&lDelted the config file (This is what is supposed to happen)!"));
		
		Files.configfile = new File(main.getDataFolder(), "config.yml");
		main.saveResource("config.yml", false);
		Files.config = YamlConfiguration.loadConfiguration(Files.configfile);
		player.sendMessage(Util.format("&b&lCreated a new config file! \n&7------------------------------------"));
	}



	public static void reloadLog(Main main) {
		
		//log
		if(Files.logfile == null) {
			Files.logfile = new File(main.getDataFolder(), "log.yml");
		}

		Files.log = YamlConfiguration.loadConfiguration(Files.logfile);

		InputStream defaultStream = main.getResource("log.yml");
		System.out.println(defaultStream);
		if(defaultStream !=  null) {
			YamlConfiguration defaultlog = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			Files.log.setDefaults(defaultlog);
		}
	

	}
	
	
	
	public static void saveConfig() {
		if(Files.configfile == null)
			return;

		try {
			Files.config.save(Files.configfile);
		} catch(IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not save data to config.yml");
		}
	}
	
	public static void reloadConfig(Main main) {
		if(Files.configfile == null) {
			Files.configfile = new File(main.getDataFolder(), "config.yml");
		}
 
		Files.config = YamlConfiguration.loadConfiguration(Files.configfile);

		InputStream defaultStream2 = main.getResource("config.yml");
		if(defaultStream2 !=  null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream2));
			Files.config.setDefaults(defaultConfig);
		}
	}
	


	public static void savelog() {
		if(Files.logfile == null)
			return;

		try {
			Files.log.save(Files.logfile);
		} catch(IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not save data to log.yml");
		}
	}

}