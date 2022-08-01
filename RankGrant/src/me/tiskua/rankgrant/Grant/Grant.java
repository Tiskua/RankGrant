package me.tiskua.rankgrant.Grant;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.main.Main;

public class Grant {


	private Main main = Main.getMain();
	private Messages messages = main.messages; 

	
	public void grantAction(Player player) {
		//sound
		try {
			float volume = (float) Files.config.getDouble("Sound_settings.volume");
			float pitch = (float) Files.config.getDouble("Sound_settings.pitch");
			player.playSound(player.getLocation(), Sound.valueOf(Files.config.getString("Sound_settings.sound")), volume, pitch);
		} catch(IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + "There was an error playing the sound! "
					+ "\n Sound: " + Files.config.getString("Sound_settings.sound") 
					+ "\n Volume: " + Files.config.getDouble("Sound_settings.pitch")
					+ "\n Pitch: " + Files.config.getDouble("Sound_settings.pitch")
					+ "\n Check the sound, volume, and pitch (Some sounds are different in newer versions)");
		}
		

		//Message
		messages.sendToGranter();
		messages.sendGrantStaffMessage(player);
		
		Player target = Bukkit.getPlayer(GrantManager.getTarget());
		if(target != null) messages.sendToTarget(target);
		
		//Save to log
		saveLog();
		//Command
		buildCommand(player);
	}
	
	
	
	public void buildCommand(Player player) {
		if(GrantManager.getGrantOption() == "Rank") {
			String finalTimedRankCommand = replaceRankCommand(Files.config.getString("Commands.Ranks.timed"));			
			String finalPermRankCommand = replaceRankCommand(Files.config.getString("Commands.Ranks.forever"));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), GrantManager.getGrantDuration() == -1 ? finalPermRankCommand : finalTimedRankCommand );
			
		}else if(GrantManager.getGrantOption() == "Permission") {
			String finalTimedPermissionCommand = replacePermCommand
					(Files.config.getString(GrantManager.getPermBoolean().equalsIgnoreCase("True") ? "Commands.Permissions.Give.timed" : "Commands.Permissions.Remove.timed"));
			String finalPermPermissionCommand = replacePermCommand
					(Files.config.getString(GrantManager.getPermBoolean().equalsIgnoreCase("True") ? "Commands.Permissions.Give.forever" : "Commands.Permissions.Remove.forever")); 
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (GrantManager.getGrantDuration() == -1) ? finalPermPermissionCommand : finalTimedPermissionCommand);
		}
	}

	
	private String replaceRankCommand(String command) {
		String replaced = command 
				.replace("{rank}", GrantManager.getTargetRank())
				.replace("{target}", GrantManager.getTarget())
				.replace("{duration}", String.valueOf(GrantManager.getGrantDuration()));
		return replaced;
	}
	
	private String replacePermCommand(String command) {
		String replaced = command 
				.replace("{permission}", GrantManager.getPermission())
				.replace("{target}", GrantManager.getTarget())
				.replace("{trueorfalse}", GrantManager.getPermBoolean())
				.replace("{duration}", String.valueOf(GrantManager.getGrantDuration()));
		return replaced;
	}
	
	
	public void saveLog() {
		Date date = new Date();
		
		Files.log.set("Logs." + date + ".granter", GrantManager.getGranter().getName());
		
		if(GrantManager.getGrantOption().equals("Rank")) Files.log.set("Logs." + date + ".rank", GrantManager.getTargetRank());
		else  Files.log.set("Logs." + date + ".permission", GrantManager.getPermission() + " | " + GrantManager.getPermBoolean());
		
		Files.log.set("Logs." + date + ".duration", GrantManager.getGrantDuration());
		Files.log.set("Logs." + date + ".reason", GrantManager.getGrantReason());
		Files.log.set("Logs." + date + ".target", GrantManager.getTarget());
		Files.savelog();
		
		main.gui.createLogGUI();
	}
}
