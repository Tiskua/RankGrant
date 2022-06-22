package me.tiskua.rankgrant.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Util {

	public static String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static boolean isLegacy() {
		String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		int numVersion = Integer.valueOf(serverVersion.split("_")[1]);

		if(numVersion > 12) return false;
		else if(numVersion <= 12) return true;
		
		return false;
	}
	
	public static String replaceRankValues(String msg) {
		String replacedMessage = msg.replace("{target}", GrantManager.getTarget())
				.replace("{granter}", GrantManager.getGranter().getName())
				.replace("{rank}", GrantManager.getTargetRank())
				.replace("{reason}", GrantManager.getGrantReason())
				.replace("{duration}", (GrantManager.getGrantDuration() == -1) 
						? "Forever" : buildTimeMeasurements(GrantManager.getGrantDuration()));
		return replacedMessage;
	}
	
	public static String replacePermissionValues(String msg) {
		String replacedMessage = msg.replace("{target}", GrantManager.getTarget())
				.replace("{granter}", GrantManager.getGranter().getName())
				.replace("{permission}", GrantManager.getPermission())
				.replace("{trueorfalse}", GrantManager.getPermBoolean())
				.replace("{reason}", GrantManager.getGrantReason())
				.replace("{duration}", (GrantManager.getGrantDuration() == -1) 
				? "Forever" : buildTimeMeasurements(GrantManager.getGrantDuration()));
		return replacedMessage;
	}
	
	
	
	public static String buildTimeMeasurements(int time) {
		String string = "N/A";
		int divider = 1;
		if(time >= 31536000) {
			string = (" Years");
			divider = 31536000;
		}
		else if(time >= 2628000) {
			string =(" Months");
			divider = 2628000;
		}
		else if(time >= 604800) {
			string = (" Weeks");
			divider = 604800;
		}
		else if(time >= 86400) {
			string = (" Days");
			divider = 86400;
		}
		else if(time >= 3600) {
			string = (" Hours");
			divider = 3600;
		}
		else if(time >= 60) {
			string = (" Minutes");
			divider = 60;
		}
		else {
			string = (" Seconds");
			divider = 1;
		}

		return (time/divider) + string;

	}
}
