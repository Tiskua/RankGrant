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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.main.Main;
import me.tiskua.rankgrant.utils.GrantManager;
import me.tiskua.rankgrant.utils.ItemCreator;
import me.tiskua.rankgrant.utils.Util;


public class GUIS implements Listener{

	Main main;
	public GUIS(Main plugin) {
		main = plugin;
	}
	
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
		new GUIBasics(GUIManager.getMainGUI()).setBorders();	
		GUIManager.getMainGUI().setItem(12, new ItemCreator(Material.DIAMOND).setDisplayname("&a&lRanks").buildItem());
		GUIManager.getMainGUI().setItem(14, new ItemCreator(Material.EMERALD).setDisplayname("&a&lPermissions").buildItem());
	}

	public void createRankGui() {
		error_GUI = "Rank GUI";
		main.getLogger().info("Loading Rank GUI");
		GUIManager.setRankGui(Bukkit.createInventory(null, 36, "Rank"));
		new GUIBasics(GUIManager.getRankGui()).setBorders().addBackButton();
		setRanks();
		main.getLogger().info("Created Rank GUI");
	}

	public void createPermissionsGui() {
		error_GUI = "Permission GUI";
		main.getLogger().info("Loading Permission GUI");
		GUIManager.setPermGui(Bukkit.createInventory(null, 36, "Permission"));
		new GUIBasics(GUIManager.getPermGui()).setBorders().addBackButton();
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
		new GUIBasics(GUIManager.getDurationGUI()).setBorders().addBackButton();		

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
		new GUIBasics(GUIManager.getReasonGUI()).setBorders().addBackButton();
		
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
		new GUIBasics(GUIManager.getConfirmGui()).setBorders();
		
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
		new GUIBasics(GUIManager.getLogGUI()).setBorders();
	
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
		Player target = Bukkit.getPlayer(GrantManager.getTarget());
		StringBuilder finalmessage = new StringBuilder();
		
		if(GrantManager.getGrantOption() == "Rank") {
			for (String msg : Files.config.getStringList("Messages.Grant.Rank.Granter")) 
				finalmessage.append(Util.replaceRankValues(msg) + "\n");
			
			player.sendMessage(Util.format(finalmessage.toString()));
			for(String line : Files.config.getStringList("Messages.Grant.Rank.Target")) 
				if(target != null) target.sendMessage(Util.format(Util.replaceRankValues(line)));
			
			sendGrantStaffMessage(player);
			
		} else if(GrantManager.getGrantOption() == "Permission") {
			for (String msg : Files.config.getStringList("Messages.Grant.Permission.Granter")) 
				finalmessage.append(Util.replacePermissionValues(msg) + "\n");
			
			player.sendMessage(Util.format(finalmessage.toString()));
			for(String line : Files.config.getStringList("Messages.Grant.Permission.Target")) 
				if(target != null) target.sendMessage(Util.format(Util.replacePermissionValues(line)));
			
			sendGrantStaffMessage(player);
		}
		//Save to log
		saveLog();
		//Command
		buildCommand(player);
	}
	
	private void sendGrantStaffMessage(Player staff) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online.hasPermission("grant.notify") && online != staff) {
				StringBuilder finalStaffmessage = new StringBuilder();
				if(GrantManager.getGrantOption().equals("Permission")) {
					for (String msg : Files.config.getStringList("Messages.Grant.Permission.Staff")) 
						finalStaffmessage.append(Util.replacePermissionValues(msg) + "\n");					
				} else if(GrantManager.getGrantOption().equals("Rank")) {
					for (String msg : Files.config.getStringList("Messages.Grant.Rank.Staff")) 
						finalStaffmessage.append(Util.replaceRankValues(msg) + "\n");
				}
				
				online.sendMessage(Util.format(finalStaffmessage.toString()));
			}
		}
	}


	private void buildCommand(Player player) {
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
		
		createLogGUI();
	}
	
	public void updateLogsInv() {
		int xoffset = 0;
		int yoffset = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy"); 
		Date now = new Date();
		
		GUIManager.getLogGUI().clear();
		new GUIBasics(GUIManager.getLogGUI()).setBorders();
		
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