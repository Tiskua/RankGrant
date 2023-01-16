package me.tiskua.rankgrant.GUI;

import me.tiskua.rankgrant.Debug.*;
import me.tiskua.rankgrant.Grant.*;
import me.tiskua.rankgrant.Logs.*;
import me.tiskua.rankgrant.main.*;
import me.tiskua.rankgrant.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;

import java.util.*;


public class GUIS implements Listener{

	Main main = Main.getMainInstance();
	Debugger debugger = main.debug_gui;
	Logs logs = main.logs;

	public HashMap<Integer, Integer> durations = new HashMap<>();
	public HashMap<Integer, String> reasons= new HashMap<>();
	public HashMap<Integer, String> ranks = new HashMap<>();
	public HashMap<Integer, String> permissions = new HashMap<>();

	public ArrayList<Player> customReason = new ArrayList<>();
	public ArrayList<Player> customDuration = new ArrayList<>();


	GUIBasics basics = new GUIBasics();
	
	public void createMainGUI() {
		debugger.setCheckingGUI("Main");
		GUIManager.setMainGUI(Bukkit.createInventory(null, 27, "Grant: " + GrantManager.getTarget()));
		basics.setBorders(GUIManager.getMainGUI());
		try {
			GUIManager.getMainGUI().setItem(Files.config.getInt("Main.Ranks.slot"), new ItemCreator(Files.config.getString("Main.Ranks.item"))
					.setDisplayname(Files.config.getString("Main.Ranks.displayname")).lore(Files.config.getStringList("Main.Ranks.lore"))
					.formatItem().buildItem());
		} catch(ArrayIndexOutOfBoundsException e) {
			debugger.addError("Invalid Slot", new ArrayList<>(Arrays.asList("Main", Files.config.getInt("Main.Ranks.slot"))));
		}
		try {
			GUIManager.getMainGUI().setItem(Files.config.getInt("Main.Permissions.slot"), new ItemCreator(Files.config.getString("Main.Permissions.item"))
					.setDisplayname(Files.config.getString("Main.Permissions.displayname")).lore(Files.config.getStringList("Main.Permissions.lore"))
					.formatItem().buildItem());
		} catch(ArrayIndexOutOfBoundsException e) {
			debugger.addError("Invalid Slot", new ArrayList<>(Arrays.asList("Main", Files.config.getInt("Main.Permissionss.slot"))));
		}

	}

	public void createRankGUI() {
		debugger.setCheckingGUI("Rank");
		GUIManager.setRankGUI(Bukkit.createInventory(null, 36, "Rank"));
		basics.setBorders(GUIManager.getRankGUI());
		setRanks();

	}

	public void createPermissionsGUI() {
		debugger.setCheckingGUI("Permission");
		GUIManager.setPermissionGUI(Bukkit.createInventory(null, 36, "Permission"));
		basics.setBorders(GUIManager.getPermissionGUI());
		setPermissions();
		if(Util.isLegacy()) GUIManager.getPermissionGUI().setItem(8, new ItemCreator(Material.INK_SACK).setDisplayname("&a&lGive").setDurability(10).buildItem());
		else GUIManager.getPermissionGUI().setItem(8, new ItemCreator(Material.valueOf("LIME_DYE")).setDisplayname("&a&lGive").buildItem());
		GrantManager.setPermissionBoolean("True");
	}

	public void resetPermBool() {
		if(Util.isLegacy()) GUIManager.getPermissionGUI().setItem(8, new ItemCreator(Material.INK_SACK).setDisplayname("&a&lGive").setDurability(10).buildItem());
		else GUIManager.getPermissionGUI().setItem(8, new ItemCreator(Material.valueOf("LIME_DYE")).setDisplayname("&a&lGive").buildItem());
		GrantManager.setPermissionBoolean("True");
	}


	public void createDurationGUI() {
		debugger.setCheckingGUI("Duration");
		GUIManager.setDurationGUI(Bukkit.createInventory(null, 36, "Duration"));
		basics.setBorders(GUIManager.getDurationGUI());
		
		for(String duration : Files.config.getConfigurationSection("Duration").getKeys(false)) {
			int slot = Files.config.getInt("Duration." + duration + ".slot");
			String displayName = Util.format(Files.config.getString("Duration." + duration + ".displayname"));
			int item_duration = Files.config.getInt("Duration." + duration + ".duration");
			Boolean glow = Files.config.getBoolean("Duration." + duration + ".glow");
			try {
				GUIManager.getDurationGUI().setItem(slot, new ItemCreator(Files.config.getString("Duration_Item")).setDisplayname(displayName).glow(glow).formatItem().buildItem()); 

			} catch(ArrayIndexOutOfBoundsException e) {
				debugger.addError("Invalid Slot", new ArrayList<>(Arrays.asList("Main", slot)));

			}
			durations.put(slot, item_duration);
		}
	}

	public void createReasonGUI() {
		debugger.setCheckingGUI("Reason");
		GUIManager.setReasonGUI(Bukkit.createInventory(null, 36, "Reason"));
		basics.setBorders(GUIManager.getReasonGUI());
		GUIManager.addBackButtonInvs(GUIManager.getReasonGUI(), GUIManager.getDurationGUI());
		
		for(String reason : Files.config.getConfigurationSection("Reason").getKeys(false)) {
			int slot = Files.config.getInt("Reason." + reason + ".slot");
			String displayName = ChatColor.translateAlternateColorCodes('&', Files.config.getString("Reason." + reason + ".displayname"));
			String item_reason = Files.config.getString("Reason." + reason + ".reason");
			Boolean glow = Files.config.getBoolean("Reason." + reason + ".glow");

			try {
				GUIManager.getReasonGUI().setItem(slot, new ItemCreator(Files.config.getString("Reason_Item")).setDisplayname(displayName).glow(glow).formatItem().buildItem());
				reasons.put(slot, item_reason);
			} catch(ArrayIndexOutOfBoundsException e) {
				debugger.addError("Invalid Slot", new ArrayList<>(Arrays.asList("Main", slot)));

			}
		}
	}

	public void createConfirmGUI() {
		GUIManager.setConfirmGUI(Bukkit.createInventory(null, 45, "Confirm"));
		ItemStack cancel;
		ItemStack confirm;
		if(Util.isLegacy()) {
			cancel = new ItemCreator(Material.WOOL).setDisplayname("&c&lCancel").setDurability(14).buildItem();
			confirm = new ItemCreator(Material.WOOL).setDisplayname("&2&lConfirm").setDurability(13).buildItem();
		} else {
			cancel = new ItemCreator(Material.valueOf("RED_WOOL")).setDisplayname("&c&lCancel").buildItem();
			confirm = new ItemCreator(Material.valueOf("GREEN_WOOL")).setDisplayname("&2&lConfirm").buildItem();	
		}
		int s = 10;
		basics.setBorders(GUIManager.getConfirmGUI());
		
		for(int i = 0; i<3; i++) 
			for(int j = 0; j<3; j++) 
				GUIManager.getConfirmGUI().setItem(s + j + (i*9), confirm);

		int s2 = 14;
		for(int i = 0; i<3; i++) 
			for(int j = 0; j<3; j++) 
				GUIManager.getConfirmGUI().setItem(s2 + j + (i*9), cancel);
		
		GUIManager.addBackButtonInvs(GUIManager.getConfirmGUI(), GUIManager.getReasonGUI());
	}



	public void createLogGUI() {
		GUIManager.setLogGUI(Bukkit.createInventory(null, 54, "Logs"));
		basics.setBorders(GUIManager.getLogGUI());

		logs.updateLogsInv();
	}


	public void setRanks() {
		for(String rank : Files.config.getConfigurationSection("Ranks").getKeys(false)) {
			String displayName = Util.format(Files.config.getString("Ranks." + rank + ".displayname"));
			String rankname = Files.config.getString("Ranks." + rank + ".rank");
			int slot = Files.config.getInt("Ranks." + rank + ".slot");
			List<String> lore = Files.config.getStringList("Ranks." + rank + ".lore");

			try {
				GUIManager.getRankGUI().setItem(slot, new ItemCreator(Files.config.getString("Ranks." + rank + ".item_type"))
						.formatItem().setDisplayname(displayName).lore(Util.formatList(lore)).buildItem());
			} catch(ArrayIndexOutOfBoundsException e) {
				debugger.addError("Invalid Slot", new ArrayList<>(Arrays.asList("Main", slot)));

			}
			ranks.put(slot, rankname);
		}
	}

	public void setPermissions() {
		for(String perm : Files.config.getConfigurationSection("Permissions").getKeys(false)) {
			String displayName = ChatColor.translateAlternateColorCodes('&',  Files.config.getString("Permissions." + perm + ".displayname"));
			String permission = Files.config.getString("Permissions." + perm + ".permission");
			int slot = Files.config.getInt("Permissions." + perm + ".slot");
			List<String> lore = Files.config.getStringList("Permissions." + perm + ".lore");
			try {
				GUIManager.getPermissionGUI().setItem(slot, new ItemCreator(Files.config.getString("Permissions." + perm + ".item_type")).formatItem().setDisplayname(displayName).lore(Util.formatList(lore)).buildItem());
			} catch(ArrayIndexOutOfBoundsException e) {
				debugger.addError("Invalid Slot", new ArrayList<>(Arrays.asList("Main", slot)));

			}
			permissions.put(slot, permission);			
		}
	}


} 