package me.tiskua.rankgrant.GUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.tiskua.rankgrant.Grant.GrantManager;
import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.main.Main;
import me.tiskua.rankgrant.utils.ItemCreator;
import me.tiskua.rankgrant.utils.Util;


public class GUIS implements Listener{

	private Main main = Main.getMain();
	private GUIBasics basics = new GUIBasics();
	public ArrayList<String> logs = new ArrayList<>();
	
	
	public HashMap<Integer, Integer> durations = new HashMap<>();
	public HashMap<Integer, String> reasons= new HashMap<>();
	public HashMap<Integer, String> ranks = new HashMap<>();
	public HashMap<Integer, String> permissions = new HashMap<>();
	
	public ArrayList<Player> customReason = new ArrayList<>();
	public ArrayList<Player> customDuration = new ArrayList<>();
	public int page = 0;

	
	public static String error_GUI;
	
	public void createMainGUI() {
		error_GUI = "Main GUI";
		GUIManager.setMainGUI(Bukkit.createInventory(null, 27, "Grant: " + GrantManager.getTarget()));
		basics.setBorders(GUIManager.getMainGUI());	
		GUIManager.getMainGUI().setItem(12, new ItemCreator(Material.DIAMOND).setDisplayname("&a&lRanks").buildItem());
		GUIManager.getMainGUI().setItem(14, new ItemCreator(Material.EMERALD).setDisplayname("&a&lPermissions").buildItem());
		GUIManager.addBackButtonInvs(GUIManager.getRankGui(), GUIManager.getMainGUI());
		GUIManager.addBackButtonInvs(GUIManager.getPermGui(), GUIManager.getMainGUI());
	}

	public void createRankGui() {
		error_GUI = "Rank GUI";
		main.getLogger().info("Loading Rank GUI");
		GUIManager.setRankGui(Bukkit.createInventory(null, 36, "Rank"));
		basics.setBorders(GUIManager.getRankGui());
		setRanks();
		main.getLogger().info("Created Rank GUI");
		
		System.out.println(GUIManager.getMainGUI());
	}

	public void createPermissionsGui() {
		error_GUI = "Permission GUI";
		main.getLogger().info("Loading Permission GUI");
		GUIManager.setPermGui(Bukkit.createInventory(null, 36, "Permission"));
		basics.setBorders(GUIManager.getPermGui());
		setPermissions();
		if(Util.isLegacy()) GUIManager.getPermGui().setItem(8, new ItemCreator(Material.INK_SACK).setDisplayname("&a&lGive").setDurability((short) 10).buildItem());
		else GUIManager.getPermGui().setItem(8, new ItemCreator(Material.valueOf("LIME_DYE")).setDisplayname("&a&lGive").buildItem());
		GrantManager.setPermBoolean("True");
		main.getLogger().info("Created Permission GUI");
		
	}
	
	public void resetPermBool() {
		if(Util.isLegacy()) GUIManager.getPermGui().setItem(8, new ItemCreator(Material.INK_SACK).setDisplayname("&a&lGive").setDurability((short) 10).buildItem());
		else GUIManager.getPermGui().setItem(8, new ItemCreator(Material.valueOf("LIME_DYE")).setDisplayname("&a&lGive").buildItem());
		GrantManager.setPermBoolean("True");
	}

	
	public void createDurationGUI() {
		error_GUI = "Duration GUI";
		main.getLogger().info("Loading Duration GUI");
		GUIManager.setDurationGUI(Bukkit.createInventory(null, 36, "Duration"));
		basics.setBorders(GUIManager.getDurationGUI());		
		GUIManager.addBackButtonInvs(GUIManager.getDurationGUI(), GUIManager.getRankGui());
		for(String duration : Files.config.getConfigurationSection("Duration").getKeys(false)) {
			int slot = Files.config.getInt("Duration." + duration + ".slot");
			String displayName = Util.format(Files.config.getString("Duration." + duration + ".displayname"));
			int item_duration = Files.config.getInt("Duration." + duration + ".duration");
			Boolean glow = Files.config.getBoolean("Duration." + duration + ".glow");
			GUIManager.getDurationGUI().setItem(slot, new ItemCreator(Util.isLegacy() ? Material.WATCH : Material.valueOf("CLOCK")).setDisplayname(displayName).glow(glow).buildItem()); 
			durations.put(slot, item_duration);
		}
		main.getLogger().info("Created Duration GUI");
	}
	
	public void createReasonGUI() {
		error_GUI = "Reason GUI";
		main.getLogger().info("Loading Reason GUI");
		GUIManager.setReasonGUI(Bukkit.createInventory(null, 36, "Reason"));
		basics.setBorders(GUIManager.getReasonGUI());
		GUIManager.addBackButtonInvs(GUIManager.getReasonGUI(), GUIManager.getDurationGUI());
		for(String reason : Files.config.getConfigurationSection("Reason").getKeys(false)) {
			int slot = Files.config.getInt("Reason." + reason + ".slot");
			String displayName = ChatColor.translateAlternateColorCodes('&', Files.config.getString("Reason." + reason + ".displayname"));
			String item_reason = Files.config.getString("Reason." + reason + ".reason");
			Boolean glow = Files.config.getBoolean("Reason." + reason + ".glow");
			GUIManager.getReasonGUI().setItem(slot, new ItemCreator(Material.NAME_TAG).setDisplayname(displayName).glow(glow).buildItem());
			reasons.put(slot, item_reason);
		}
		
		main.getLogger().info("Created Reason GUI");
	}
	
	public void createConfirmGUI() {
		main.getLogger().info("Loading Confirm GUI");
		GUIManager.setConfirmGui(Bukkit.createInventory(null, 45, "Confirm"));
		ItemStack cancel = null;
		ItemStack confirm = null;
		if(Util.isLegacy()) {
			 cancel = new ItemCreator(Material.WOOL).setDisplayname("&c&lCancel").setDurability((short)14).buildItem();
			 confirm = new ItemCreator(Material.WOOL).setDisplayname("&2&lConfirm").setDurability((short) 13).buildItem();	
		} else {
			 cancel = new ItemCreator(Material.valueOf("RED_WOOL")).setDisplayname("&c&lCancel").buildItem();
			 confirm = new ItemCreator(Material.valueOf("GREEN_WOOL")).setDisplayname("&2&lConfirm").buildItem();	
		}
		int s = 10;
		basics.setBorders(GUIManager.getConfirmGui());
		
		for(int i = 0; i<3; i++) 
			for(int j = 0; j<3; j++) 
				GUIManager.getConfirmGui().setItem(s + j + (i*9), confirm);
		
		int s2 = 14;
		for(int i = 0; i<3; i++) 
			for(int j = 0; j<3; j++) 
				GUIManager.getConfirmGui().setItem(s2 + j + (i*9), cancel);
		
		main.getLogger().info("Created Confirm GUI");
	}
	
	
	
	public void createLogGUI() {
		if(Files.log.getConfigurationSection("Logs") == null || Files.log.getConfigurationSection("Logs").getKeys(false).isEmpty()) {
			main.getLogger().log(Level.INFO, "There are no logs to display! Once you execute a grant you can see it");
			return;
		}
			
		GUIManager.setLogGui(Bukkit.createInventory(null, 54, "Logs"));
		basics.setBorders(GUIManager.getLogGUI());
	
		Set<String> data = Files.log.getConfigurationSection("Logs").getKeys(false);
		logs.clear();
		logs.addAll(data);
		
		updateLogsInv();
		main.getLogger().info("Created Log GUI");
	}



	public void setRanks() {
		for(String rank : Files.config.getConfigurationSection("Ranks").getKeys(false)) {
			String displayName = Util.format(Files.config.getString("Ranks." + rank + ".displayname"));
			String rankname = Files.config.getString("Ranks." + rank + ".rank");
			int slot = Files.config.getInt("Ranks." + rank + ".slot");
			List<String> lore = Files.config.getStringList("Ranks." + rank + ".lore");
			
			GUIManager.getRankGui().setItem(slot, new ItemCreator(Files.config.getString("Ranks." + rank + ".item_type")).formatItem().setDisplayname(displayName).lore(Util.formatList(lore)).buildItem());
			ranks.put(slot, rankname);
		}
	}

	public void setPermissions() {
		for(String perm : Files.config.getConfigurationSection("Permissions").getKeys(false)) {
			String displayName = ChatColor.translateAlternateColorCodes('&',  Files.config.getString("Permissions." + perm + ".displayname"));
			String permission = Files.config.getString("Permissions." + perm + ".permission");
			int slot = Files.config.getInt("Permissions." + perm + ".slot");
			List<String> lore = Files.config.getStringList("Permissions." + perm + ".lore");

			GUIManager.getPermGui().setItem(slot, new ItemCreator(Files.config.getString("Permissions." + perm + ".item_type")).formatItem().setDisplayname(displayName).lore(Util.formatList(lore)).buildItem());
			permissions.put(slot, permission);
		}
	}


	
	public void updateLogsInv() {
		int xoffset = 0;
		int yoffset = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy"); 
		Date now = new Date();
		
		GUIManager.getLogGUI().clear();
		basics.setBorders(GUIManager.getLogGUI());
		
		if(page != (int) (logs.size()/28)) GUIManager.getLogGUI().setItem(GUIManager.getLogGUI().getSize()-1, new ItemCreator(Material.ARROW).setDisplayname("&a&lNext Page").buildItem()); 
		if(page > 0) GUIManager.getLogGUI().setItem(GUIManager.getLogGUI().getSize()-9, new ItemCreator(Material.ARROW).setDisplayname("&a&lPrevious Page").buildItem());
		

		for(int i = (logs.size() - (page*28)-1); i > (logs.size() -(page*28)-29); i--) {
			Date past = null;
			try {
				past = format.parse(logs.get(i));
			} catch (ParseException e) {
				System.out.println("CANNOT CONVERT THE STRING TO A DATE");
				e.printStackTrace();
			}
			List<String> lore = new ArrayList<>();
			String prefix = "Logs." + logs.get(i);
			lore.add("&7---------------------------");
			if(Files.log.getString(prefix + ".rank") != null) lore.add("&eRank&8: " + Files.log.getString(prefix + ".rank"));
			else lore.add("&ePermission&8: " + Files.log.getString(prefix + ".permission"));
			lore.add("&eGranted To&7: " +  Files.log.getString(prefix + ".target"));
			lore.add("&eDuration&7: " +  Util.buildTimeMeasurements(Files.log.getInt(prefix + ".duration")));
			lore.add("&eReason&8: " + Files.log.getString(prefix + ".reason"));
			lore.add("&eGranted by&7: " +  Files.log.getString(prefix + ".granter"));
			lore.add("&eExpired&7: " + (TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) >= Files.log.getInt(prefix + ".duration") ? "&c&lYes" : "&a&lNo"));
			lore.add("&7---------------------------");
			if(Util.isLegacy()) {
				if(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) >= Files.log.getInt(prefix + ".duration")) GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.WOOL).setDisplayname("&6&l" + logs.get(i))
						.setDurability((short) 8).lore(lore).buildItem()); 
				else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) <=2) GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.WOOL).setDisplayname("&6&l" + logs.get(i))
						.setDurability((short) 5).lore(lore).buildItem()); 
				else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) <= 24) GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.WOOL).setDisplayname("&6&l" + logs.get(i))
						.setDurability((short) 13).lore(lore).buildItem()); 
				else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) <= 168) GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.WOOL).setDisplayname("&6&l" + logs.get(i))
						.setDurability((short) 4).lore(lore).buildItem()); 
				else GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.WOOL).setDisplayname("&6&l" + logs.get(i))
						.setDurability((short) 1).lore(lore).buildItem()); 
				
			} else {
				if(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) >= Files.log.getInt(prefix + ".duration")) 
					GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.valueOf("GRAY_WOOL")).setDisplayname("&6&l" + logs.get(i)).lore(lore).buildItem()); 
				else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) <=2) 
					GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.valueOf("LIME_WOOL")).setDisplayname("&6&l" + logs.get(i)).lore(lore).buildItem()); 
				else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) <= 24) 
					GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.valueOf("GREEN_WOOL")).setDisplayname("&6&l" + logs.get(i)).lore(lore).buildItem()); 
				else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) <= 168)
					GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.valueOf("YELLOW_WOOL")).setDisplayname("&6&l" + logs.get(i)).lore(lore).buildItem()); 
				else 
					GUIManager.getLogGUI().setItem(10 + xoffset + (yoffset*9), new ItemCreator(Material.valueOf("ORANGE_WOOL")).setDisplayname("&6&l" + logs.get(i)).lore(lore).buildItem()); 
			}
			xoffset++;
			if(xoffset == 7) {
				yoffset++;
				xoffset = 0;
			}
			if(i <= 0) break;
		}
	}
} 