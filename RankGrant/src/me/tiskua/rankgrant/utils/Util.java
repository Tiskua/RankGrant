package me.tiskua.rankgrant.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.tiskua.rankgrant.Grant.GrantManager;

public class Util {

	public static String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static List<String> formatList(List<String> list) {
		return list.stream().map(line->ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
	}

	public static boolean isLegacy() {
		String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		int numVersion = Integer.valueOf(serverVersion.split("_")[1]);

		if(numVersion > 12) return false;
		else if(numVersion <= 12) return true;

		return false;
	}

	public static String replaceMessageValues(String msg) {
		String replacedMessage = "N/A";
		if(GrantManager.getGrantOption() == "Rank") 
			replacedMessage = msg.replace("{target}", GrantManager.getTarget())
				.replace("{granter}", GrantManager.getGranter().getName())
				.replace("{rank}", GrantManager.getTargetRank())
				.replace("{reason}", GrantManager.getGrantReason())
				.replace("{duration}", buildTimeMeasurements(GrantManager.getGrantDuration()));
		
		else if(GrantManager.getGrantOption() == "Permission")
			replacedMessage = msg.replace("{target}", GrantManager.getTarget())
				.replace("{granter}", GrantManager.getGranter().getName())
				.replace("{permission}", GrantManager.getPermission())
				.replace("{trueorfalse}", GrantManager.getPermBoolean())
				.replace("{reason}", GrantManager.getGrantReason())
				.replace("{duration}", buildTimeMeasurements(GrantManager.getGrantDuration()));
		
		return replacedMessage;
	}



	public static String buildTimeMeasurements(int time) {
		String string = "N/A";
		int divider = 1;
		if(time == -1) return "Forever";

		else if(time >= 31536000) {
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
		if(time/divider == 1) string = StringUtils.chop(string);
		return (time/divider) + string;

	}

	public static int durationToSeconds(String time)  {
		String[] arr = time.split(" ");
		int duration = Integer.parseInt(arr[0]);
		int multiplier = 0;

		if(time.contains("year")) 
			multiplier = 31536000;
		else if(time.contains("month")) 
			multiplier = 2628000;
		else if(time.contains("week")) 
			multiplier = 604800;
		else if(time.contains("day")) 
			multiplier = 86400;
		else if(time.contains("hour")) 
			multiplier = 3600;
		else if(time.contains("minute")) 
			multiplier = 60;
		else if(time.contains("second")) 
			multiplier = 1;



		return duration * multiplier;
	}

	public static boolean isInteger(String msg) {
		boolean isValidInteger = false;
		try{
			Integer.parseInt(msg);
			isValidInteger = true;
		}
		catch (NumberFormatException e){}

		return isValidInteger;
	}
}
