package me.tiskua.rankgrant.utils;

import me.tiskua.rankgrant.Debug.Debugger;
import me.tiskua.rankgrant.Grant.GrantManager;
import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.main.Main;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Util {


	public static String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static List<String> formatList(List<String> list) {
		return list.stream().map(line->ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
	}

	public static boolean isLegacy() {
		String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		int numVersion = Integer.parseInt(serverVersion.split("_")[1]);

		return numVersion <= 12;

	}

	public static String replaceMessageValues(String msg) {
		if(GrantManager.getGrantType() == GrantManager.GrantType.RANK)
			return msg.replace("{target}", GrantManager.getTarget())
				.replace("{granter}", GrantManager.getGranter().getName())
				.replace("{rank}", GrantManager.getTargetRank())
				.replace("{reason}", GrantManager.getGrantReason())
				.replace("{duration}", buildTimeMeasurements(GrantManager.getGrantDuration()));
		else
			return msg.replace("{target}", GrantManager.getTarget())
				.replace("{granter}", GrantManager.getGranter().getName())
				.replace("{permission}", GrantManager.getTargetPermission())
				.replace("{trueorfalse}", GrantManager.getPermissionBoolean())
				.replace("{reason}", GrantManager.getGrantReason())
				.replace("{duration}", buildTimeMeasurements(GrantManager.getGrantDuration()));
		
	}



	public static String buildTimeMeasurements(int time) {
		String string;
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
		}
		if(time/divider == 1) string = StringUtils.chop(string);
		return (time/divider) + string;
	}

	public static int durationToSeconds(String time)  {
		String[] arr = time.split(" ");
		int duration = Integer.parseInt(arr[0]);
		int multiplier = 0;

		if(time.contains("year") || time.contains("y")) multiplier = 31536000;
		else if(time.contains("month") || time.contains("M")) multiplier = 2628000;
		else if(time.contains("week") || time.contains("w")) multiplier = 604800;
		else if(time.contains("day") || time.contains("d") ) multiplier = 86400;
		else if(time.contains("hour") || time.contains("h") ) multiplier = 3600;
		else if(time.contains("minute") || time.contains("m") ) multiplier = 60;
		else if(time.contains("second") || time.contains("s")) multiplier = 1;

		return duration * multiplier;
	}

	public static boolean isInteger(String msg) {
		boolean isValidInteger = false;
		try{
			Integer.parseInt(msg);
			isValidInteger = true;
		}
		catch (NumberFormatException ignored){}

		return isValidInteger;
	}

	
	public static Date convertToDate(String d) {
		Debugger debugger = Main.getMainInstance().debug_gui;
		Date date;
		try {
			date = getRegularDateFormat().parse(d);
			return date;
		} catch (ParseException e) {
			debugger.addError("String To Date", new ArrayList<>(Collections.singleton(d)));
			e.printStackTrace();
		}
		return null;
	}

//	public static SimpleDateFormat getDateFormat() {
//		return new SimpleDateFormat(Files.config.getString("Logs.format"));
//	}
	public static SimpleDateFormat getRegularDateFormat() {
	return new SimpleDateFormat("E MMM dd kk:mm:ss z yyyy");
}
	public static SimpleDateFormat getConfigDateFormat() {
		return new SimpleDateFormat(Files.config.getString("Logs.format"));
	}


}

