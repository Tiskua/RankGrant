package me.tiskua.rankgrant.main;

import me.tiskua.rankgrant.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Files {
	
	public static File logfile;
	public static FileConfiguration log;

	public static File configfile;
	public static FileConfiguration config;


	public static void base(Main main) {
		if(main.getDataFolder().mkdirs())
			main.getLogger().log(Level.INFO, "Could not find a config file. Created a new configuration file!");
		else main.getLogger().log(Level.INFO, "A config file has been found and is being used!");

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
		} else {
			configfile = new File(main.getDataFolder(), "config-new.yml");
			if(!configfile.exists()) {
				main.saveResource("config-new.yml", false);
			}
		}
		config = YamlConfiguration.loadConfiguration(configfile);
	}
	
	public static void fixConfig(Main main, Player player) {
		player.sendMessage(Util.format("&7------------------------------------\n&3&lFIXING CONFIG:"));
		File backup  = new File(main.getDataFolder(), "backup.yml");
		if(backup.delete()) player.sendMessage(Util.format("&b&lDeleted the previous backup file"));
		try {
			java.nio.file.Files.copy(Files.configfile.toPath(), backup.toPath());
			player.sendMessage(Util.format("&a&lCreated a backup of config file!"));
		} catch (IOException e) {
			player.sendMessage(Util.format("&cThere was an error creating a backup!"));
			e.printStackTrace();
		}

		if(Files.configfile.delete()) player.sendMessage(Util.format("&e&lDeleted the config file (This is what is supposed to happen)!"));
		else player.sendMessage(Util.format("&cThere was an error deleting the config file (This should happen)"));

		Files.configfile = new File(main.getDataFolder(), "config.yml");
		main.saveResource("config.yml", false);
		Files.config = YamlConfiguration.loadConfiguration(Files.configfile);
		player.sendMessage(Util.format("&b&lCreated a new config file! \n&7------------------------------------"));
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
		if(Files.logfile == null) return;
		try {
			Files.log.save(Files.logfile);
		} catch(IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not save data to log.yml");
		}
	}
}