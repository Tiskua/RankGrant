package me.tiskua.rankgrant.GUI;

import org.bukkit.inventory.Inventory;

public class GUIManager {

	
	private static Inventory main;
	private static Inventory duration;
	private static Inventory reason;
	private static Inventory rank;
	private static Inventory perm;
	private static Inventory truefalse;
	private static Inventory confirm;
	
	
	public static Inventory getMainGUI() {
		return main;
	}

	public static void setMainGUI(Inventory mainGUI) {
		main = mainGUI;
	}

	public static Inventory getDurationGUI() {
		return duration;
	}

	public static void setDurationGUI(Inventory durationGUI) {
		duration = durationGUI;
	}

	public static Inventory getReasonGUI() {
		return reason;
	}

	public static void setReasonGUI(Inventory reasonGUI) {
		reason = reasonGUI;
	}

	public static Inventory getRankGui() {
		return rank;
	}

	public static void setRankGui(Inventory rankGui) {
		rank = rankGui;
	}

	public static Inventory getPermGui() {
		return perm;
	}

	public static void setPermGui(Inventory permGui) {
		perm = permGui;
	}

	public static Inventory getTruefalseGui() {
		return truefalse;
	}

	public static void setTruefalseGui(Inventory truefalseGui) {
		truefalse = truefalseGui;
	}

	public static Inventory getConfirmGui() {
		return confirm;
	}

	public static void setConfirmGui(Inventory confirmGui) {
		confirm = confirmGui;
	}
}
