package me.tiskua.rankgrant.Grant;

import org.bukkit.entity.Player;

public class GrantManager {
	
	
	
	private static Player granter;
	private static String target;

	private static String grantOption;

	private static String targetRank;
	private static int grantDuration;

	private static String grantReason;

	private static String targetPerm;
	private static String permBoolean;
	
	public static void setTarget(String tar) {
		target = tar;
	}
	public static String getTarget() {
		return target;
	}
	
	public static void setGranter(Player player) {
		granter = player;
	}
	public static Player getGranter() {
		return granter;
	}
	
	public static void setTargetRank(String rank) {
		targetRank = rank;
	}
	public static String getTargetRank() {
		return targetRank;
	}
	
	public static  void setGrantDuration(int dur) {
		grantDuration = dur;
	}
	public static Integer getGrantDuration() {
		return grantDuration;
	}
	
	public static void setGrantReason(String reason) {
		grantReason = reason;
	}
	public static String getGrantReason() {
		return grantReason;
	}
	
	public static void setPermission(String permission) {
		targetPerm = permission;
	}
	public static String getPermission() {
		return targetPerm;
	}
	
	public static void setPermBoolean(String bool) {
		permBoolean = bool;
	}
	public static String getPermBoolean() {
		return permBoolean;
	}
	
	public static void setGrantOption(String option) {
		grantOption = option;
	}
	public static String getGrantOption() {
		return grantOption;
	}
	
}
