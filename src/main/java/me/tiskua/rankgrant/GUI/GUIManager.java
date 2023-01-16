package me.tiskua.rankgrant.GUI;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class GUIManager {
	private static Inventory mainGUI;
	private static Inventory durationGUI;
	private static Inventory reasonGUI;
	private static Inventory rankGUI;
	private static Inventory permissionGUI;
	private static Inventory confirmGUI;
	private static Inventory logGUI;
	private static HashMap<Inventory, Inventory> backButtonInvs = new HashMap<>();

	public static Inventory getMainGUI() {
		return GUIManager.mainGUI;
	}

	public static Inventory getDurationGUI() {
		return GUIManager.durationGUI;
	}

	public static Inventory getReasonGUI() {
		return GUIManager.reasonGUI;
	}

	public static Inventory getRankGUI() {
		return GUIManager.rankGUI;
	}

	public static Inventory getPermissionGUI() {
		return GUIManager.permissionGUI;
	}

	public static Inventory getConfirmGUI() {
		return GUIManager.confirmGUI;
	}

	public static Inventory getLogGUI() {
		return GUIManager.logGUI;
	}

	public static HashMap<Inventory, Inventory> getBackButtonInvs() {
		return GUIManager.backButtonInvs;
	}

	public static void setMainGUI(Inventory mainGUI) {
		GUIManager.mainGUI = mainGUI;
	}

	public static void setDurationGUI(Inventory durationGUI) {
		GUIManager.durationGUI = durationGUI;
	}

	public static void setReasonGUI(Inventory reasonGUI) {
		GUIManager.reasonGUI = reasonGUI;
	}

	public static void setRankGUI(Inventory rankGUI) {
		GUIManager.rankGUI = rankGUI;
	}

	public static void setPermissionGUI(Inventory permissionGUI) {
		GUIManager.permissionGUI = permissionGUI;
	}

	public static void setConfirmGUI(Inventory confirmGUI) {
		GUIManager.confirmGUI = confirmGUI;
	}

	public static void setLogGUI(Inventory logGUI) {
		GUIManager.logGUI = logGUI;
	}

	public static void addBackButtonInvs(Inventory to, Inventory from) {
		backButtonInvs.put(to, from);
	}
}
