package me.tiskua.rankgrant.GUI;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

	
	public void createMainGUI() {
		GUIManager.setMainGUI(Bukkit.createInventory(null, 27, "Grant: " + GrantManager.getTarget()));
		new GUIBasics(getOuterBorder(), getInnerBorder()).setBorders(GUIManager.getMainGUI());
		GUIManager.getMainGUI().setItem(12, new ItemCreator(Material.DIAMOND).setDisplayname("&a&lRanks").buildItem());
		GUIManager.getMainGUI().setItem(14, new ItemCreator(Material.EMERALD).setDisplayname("&a&lPermissions").buildItem());
	}

	public void createRankGui() {
		GUIManager.setRankGui(Bukkit.createInventory(null, 36, "Rank"));
		new GUIBasics(getOuterBorder(), getInnerBorder()).setBorders(GUIManager.getRankGui());
		setRanks();
	}

	public void createPermissionsGui() {
		GUIManager.setPermGui(Bukkit.createInventory(null, 36, "Permission"));
		new GUIBasics(getOuterBorder(), getInnerBorder()).setBorders(GUIManager.getPermGui());
		setPermissions();
	}


	public void createTrueFalseGui() {
		GUIManager.setTruefalseGui(Bukkit.createInventory(null, 27, "Give/Remove"));
		new GUIBasics(getOuterBorder(), getInnerBorder()).setBorders(GUIManager.getTruefalseGui());
		
		GUIManager.getTruefalseGui().setItem(12, new ItemCreator(isLegacy() ? Material.SAPLING : Material.valueOf("OAK_SAPLING")).setDisplayname("&a&lGive").buildItem());

		GUIManager.getTruefalseGui().setItem(14, new ItemCreator(Material.BARRIER).setDisplayname("&c&lRemove").buildItem());
	}
	public void createDurationGUI() {
		GUIManager.setDurationGUI(Bukkit.createInventory(null, 36, "Duration"));
		new GUIBasics(getOuterBorder(), getInnerBorder()).setBorders(GUIManager.getDurationGUI());
		

		for(String duration : Files.config.getConfigurationSection("Duration").getKeys(false)) {
			int slot = Files.config.getInt("Duration." + duration + ".slot");
			String displayName = ChatColor.translateAlternateColorCodes('&', Files.config.getString("Duration." + duration + ".displayname"));
			
			GUIManager.getDurationGUI().setItem(slot, new ItemCreator(isLegacy() ? Material.WATCH : Material.valueOf("CLOCK")).setDisplayname(displayName).buildItem()); 

		}
	}
	
	public void createConfirmGUI() {
		GUIManager.setConfirmGui(Bukkit.createInventory(null, 45, "Confirm"));
		
			
		ItemStack confirm = new ItemCreator(Material.WOOL).setDisplayname("&2&lConfirm").setDurability((short) 13).buildItem();
		
		ItemStack cancel = new ItemCreator(Material.WOOL).setDisplayname("&c&lCancel").setDurability((short)14).buildItem();
		
		int s = 10;
		new GUIBasics(getOuterBorder(), getInnerBorder()).setBorders(GUIManager.getConfirmGui());
		for(int i = 0; i<3; i++) 
			for(int j = 0; j<3; j++) 
				GUIManager.getConfirmGui().setItem(s + j + (i*9), confirm);
		
		int s2 = 14;
		for(int i = 0; i<3; i++) 
			for(int j = 0; j<3; j++) 
				GUIManager.getConfirmGui().setItem(s2 + j + (i*9), cancel);
		
		
	}
	
	
	public void createReasonGUI() {
		GUIManager.setReasonGUI(Bukkit.createInventory(null, 36, "Reason"));
		new GUIBasics(getOuterBorder(), getInnerBorder()).setBorders(GUIManager.getReasonGUI());

		for(String reason : Files.config.getConfigurationSection("Reason").getKeys(false)) {

			int slot = Files.config.getInt("Reason." + reason + ".slot");
			String displayName = ChatColor.translateAlternateColorCodes('&', Files.config.getString("Reason." + reason + ".displayname"));

			GUIManager.getReasonGUI().setItem(slot, new ItemCreator(Material.NAME_TAG).setDisplayname(displayName).buildItem());
		}
	}



	public void setRanks() {
		String displayName;
		int slot;

		for(String rank : Files.config.getConfigurationSection("Ranks").getKeys(false)) {

			displayName =ChatColor.translateAlternateColorCodes('&',  Files.config.getString("Ranks." + rank + ".displayname"));
			slot = Files.config.getInt("Ranks." + rank + ".slot");
			List<String> lore = Files.config.getStringList("Ranks." + rank + ".lore");

			GUIManager.getRankGui().setItem(slot, new ItemCreator(Files.config.getString("Ranks." + rank + ".item_type")).formatItem().setDisplayname(displayName).lore(lore).buildItem());
		}
	}

	public void setPermissions() {
		ItemStack displayItem;
		ItemMeta meta;
		String displayName;
		int slot;

		for(String perm : Files.config.getConfigurationSection("Permissions").getKeys(false)) {
			displayItem = new ItemCreator(Files.config.getString("Permissions." + perm + ".item_type")).formatItem().buildItem();
			displayName = ChatColor.translateAlternateColorCodes('&',  Files.config.getString("Permissions." + perm + ".displayname"));
			slot = Files.config.getInt("Permissions." + perm + ".slot");
			List<String> lore = Files.config.getStringList("Permissions." + perm + ".lore");

			meta = displayItem.getItemMeta();
			meta.setDisplayName(displayName);
			meta.setLore(lore);
			displayItem.setItemMeta(meta);

			GUIManager.getPermGui().setItem(slot, new ItemCreator(Files.config.getString("Permissions." + perm + ".item_type")).formatItem().setDisplayname(displayName).buildItem());
		}
	}



	public void grantAction(Player player) {
		//sound
		try {
			player.playSound(player.getLocation(), Sound.valueOf(Files.config.getString("Sound_settings.sound")), 10, 10);
		} catch(Exception e) {
			player.sendMessage(ChatColor.RED + Files.config.getString("Sound_settings.sound") + " sound does not exist (It might be different in other versions)");
		}

		//Message
		if(GrantManager.getGrantOption() == "Rank") {
			StringBuilder finalmessage = new StringBuilder();
			for (String msg : Files.config.getStringList("Messages.Grant.Rank.Granter")) {
				finalmessage.append(Util.replaceRankValues(msg) + "\n");
			}
			player.sendMessage(Util.format(finalmessage.toString()));
			
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(online.hasPermission("grant.notify") && online != player) {
					StringBuilder finalStaffmessage = new StringBuilder();
					for (String msg : Files.config.getStringList("Messages.Grant.Rank.Staff")) 
						finalStaffmessage.append(Util.replaceRankValues(msg) + "\n");
			
					online.sendMessage(Util.format(finalStaffmessage.toString()));
				}
			}
			
		} else if(GrantManager.getGrantOption() == "Permission") {
			StringBuilder finalGrantermessage = new StringBuilder();
			for (String msg : Files.config.getStringList("Messages.Grant.Permission.Granter")) {
				finalGrantermessage.append(Util.replacePermissionValues(msg) + "\n");
			}
			player.sendMessage(Util.format(finalGrantermessage.toString()));
			
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(online.hasPermission("grant.notify") && online != player) {
					StringBuilder finalStaffmessage = new StringBuilder();
					for (String msg : Files.config.getStringList("Messages.Grant.Permission.Staff")) 
						finalStaffmessage.append(Util.replacePermissionValues(msg) + "\n");
					
					online.sendMessage(Util.format(finalStaffmessage.toString()));
				}
			}
		}
		//Save to log
		saveLog();

		//Command
		buildCommand(player);
	}


	private void buildCommand(Player player) {
		String timedRankCommand	= Files.config.getString("Commands.Ranks.timed");
		String permRankCommand = Files.config.getString("Commands.Ranks.forever");
		String timedPermissionCommand = "";
		String permPermissionCommand = "";
		if(GrantManager.getGrantOption().equalsIgnoreCase("Permission")) {
			timedPermissionCommand= (GrantManager.getPermBoolean().equals("True")) ? 
				Files.config.getString("Commands.Permissions.Give.timed") 
				: Files.config.getString("Commands.Permissions.Remove.timed");
			permPermissionCommand = (GrantManager.getPermBoolean().equalsIgnoreCase("true")) ? 
				Files.config.getString("Commands.Permissions.Give.forever") 
				: Files.config.getString("Commands.Permissions.Remove.forever");
		}
		if(GrantManager.getGrantOption() == "Rank") {
			String finalTimedRankCommand = timedRankCommand
					.replace("{rank}", GrantManager.getTargetRank())
					.replace("{target}", GrantManager.getTarget())
					.replace("{duration}", String.valueOf(GrantManager.getGrantDuration()));
			
			String finalPermRankCommand = permRankCommand
					.replace("{rank}", GrantManager.getTargetRank())
					.replace("{target}", GrantManager.getTarget())
					.replace("{duration}", String.valueOf(GrantManager.getGrantDuration()));
			
			if(GrantManager.getGrantDuration() == -1) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalPermRankCommand);
			else Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalTimedRankCommand);

			
			
		}else if(GrantManager.getGrantOption() == "Permission") {
			String finalTimedPermissionCommand = timedPermissionCommand
					.replace("{permission}", GrantManager.getPermission())
					.replace("{target}", GrantManager.getTarget())
					.replace("{trueorfalse}", GrantManager.getPermBoolean())
					.replace("{duration}", String.valueOf(GrantManager.getGrantDuration()));
			
			String finalPermPermissionCommand = permPermissionCommand
					.replace("{permission}", GrantManager.getPermission())
					.replace("{target}", GrantManager.getTarget())
					.replace("{trueorfalse}", GrantManager.getPermBoolean())
					.replace("{duration}", String.valueOf(GrantManager.getGrantDuration()));
			if(GrantManager.getGrantDuration() == -1)Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalPermPermissionCommand);
			else Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalTimedPermissionCommand);
		}
	}



	public void saveLog() {
		Date date = new Date();
		Files.log.set("Logs." + date, GrantManager.getTarget() + " was granted " + GrantManager.getTargetRank() + " for " + Util.buildTimeMeasurements(GrantManager.getGrantDuration()) 
		+ " because " + GrantManager.getGrantReason() + " by " + GrantManager.getGranter().getName());
		Files.savelog();
	}

	
	public String getInnerBorder() {
		return Files.config.getString("Border.Inner.item");
	}
	
	public String getOuterBorder() {
		return Files.config.getString("Border.Outer.item");
	}
	
	
	public boolean isLegacy() {
		String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		int numVersion = Integer.valueOf(serverVersion.split("_")[1]);

		if(numVersion > 12) return false;
		else if(numVersion <= 12) return true;
		
		return false;
	}
	
} 




