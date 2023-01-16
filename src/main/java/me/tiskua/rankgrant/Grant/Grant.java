package me.tiskua.rankgrant.Grant;

import me.tiskua.rankgrant.Debug.*;
import me.tiskua.rankgrant.main.*;
import org.bukkit.*;
import org.bukkit.entity.Player;

import me.tiskua.rankgrant.Logs.Logs;

public class Grant {


	private final Messages messages = new Messages();
	Logs logs = new Logs();
	Debugger debugger = Main.getMainInstance().debug_gui;

	public void grantAction(Player player) {
		playSound(player);
		//Message
		messages.sendToGranter();
		messages.sendGrantStaffMessage(player);
		
		Player target = Bukkit.getPlayer(GrantManager.getTarget());
		if(target != null) messages.sendToTarget(target);
		
		//Save to log
		logs.saveLog();
		//Command
		buildCommand();
	}

	private void playSound(Player player) {
		if(debugger.checkSound()) {
			float volume = (float) Files.config.getDouble("Sound_settings.volume");
			float pitch = (float) Files.config.getDouble("Sound_settings.pitch");
			player.playSound(player.getLocation(), Sound.valueOf(Files.config.getString("Sound_settings.sound"))
					, volume, pitch);
		} else {
			player.sendMessage(ChatColor.RED + "There was an error playing the sound! "
					+ "\n Sound: " + Files.config.getString("Sound_settings.sound")
					+ "\n Volume: " + Files.config.getDouble("Sound_settings.pitch")
					+ "\n Pitch: " + Files.config.getDouble("Sound_settings.pitch")
					+ "\n Check the sound, volume, and pitch (Some sounds are different in newer versions)");
		}
	}
	

	private void buildCommand() {
		if(GrantManager.getGrantType() == GrantManager.GrantType.RANK) {
			String finalTimedRankCommand = replaceRankCommand(Files.config.getString("Commands.Ranks.timed"));			
			String finalPermRankCommand = replaceRankCommand(Files.config.getString("Commands.Ranks.forever"));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), GrantManager.getGrantDuration() == -1 ? finalPermRankCommand : finalTimedRankCommand );
			
		}else if(GrantManager.getGrantType() == GrantManager.GrantType.PERMISSION) {
			String finalTimedPermissionCommand = replacePermCommand
					(Files.config.getString(GrantManager.getPermissionBoolean().equalsIgnoreCase("True") ? "Commands.Permissions.Give.timed" : "Commands.Permissions.Remove.timed"));
			String finalPermPermissionCommand = replacePermCommand
					(Files.config.getString(GrantManager.getPermissionBoolean().equalsIgnoreCase("True") ? "Commands.Permissions.Give.forever" : "Commands.Permissions.Remove.forever"));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (GrantManager.getGrantDuration() == -1) ? finalPermPermissionCommand : finalTimedPermissionCommand);
		}
	}

	
	private String replaceRankCommand(String command) {
		return command
				.replace("{rank}", GrantManager.getTargetRank())
				.replace("{target}", GrantManager.getTarget())
				.replace("{duration}", String.valueOf(GrantManager.getGrantDuration()));
	}
	
	private String replacePermCommand(String command) {
		return command
				.replace("{permission}", GrantManager.getTargetPermission())
				.replace("{target}", GrantManager.getTarget())
				.replace("{trueorfalse}", GrantManager.getPermissionBoolean())
				.replace("{duration}", String.valueOf(GrantManager.getGrantDuration()));
	}
}
