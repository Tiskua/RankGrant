package me.tiskua.rankgrant.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.main.Main;
import me.tiskua.rankgrant.utils.GrantManager;
import me.tiskua.rankgrant.utils.ItemCreator;
import me.tiskua.rankgrant.utils.Util;

public class ClickEvent implements Listener{

	GUIS gui;
	Main main;
	public ClickEvent(GUIS gui, Main main) {
		this.gui = gui;
		this.main = main;
	}
	
	
	@EventHandler
	public void chooseDuration(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getDurationGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();

		for(String duration : Files.config.getConfigurationSection("Duration").getKeys(false)) {
			int configSlot = Files.config.getInt("Duration." + duration + ".slot");
			int durationTime = Files.config.getInt("Duration." + duration + ".duration");

			if(configSlot == slot) {
				GrantManager.setGrantDuration(durationTime);
				event.getWhoClicked().openInventory(GUIManager.getReasonGUI());
				break;

			} else continue;
		}
	}


	@EventHandler
	public void chooseReason(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getReasonGUI())) return;
		if(event.getCurrentItem() == null) return;
		
		event.setCancelled(true);
		int slot = event.getSlot();

		Player player = (Player) event.getWhoClicked();
		for(String reason : Files.config.getConfigurationSection("Reason").getKeys(false)) {
			int configSlot = Files.config.getInt("Reason." + reason + ".slot");
			String rankReason = Files.config.getString("Reason." + reason + ".reason");

			if(configSlot == slot) {
				GrantManager.setGrantReason(rankReason);
				event.getWhoClicked().closeInventory();
				player.openInventory(GUIManager.getConfirmGui());
				
				break;

			} else continue;
		}
		
		List<String> lore = new ArrayList<>();
		
		String permission = Util.format("&e&lPermission&7: " + GrantManager.getPermission() + " : " + GrantManager.getPermBoolean());
		String rank = Util.format("&e&lRank&7: " + GrantManager.getTargetRank());
		
		lore.add((GrantManager.getGrantOption() == "Rank") ? rank : permission);
		lore.add(Util.format("&e&lDuration&7: " + Util.buildTimeMeasurements(GrantManager.getGrantDuration())));
		lore.add(Util.format("&e&lReason&7: " + GrantManager.getGrantReason()));

		GUIManager.getConfirmGui().setItem(22, new ItemCreator(Material.COMPASS).setDisplayname("&6&lInfo").lore(lore).buildItem());
		
	}
	
	
	@EventHandler
	public void confirm(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getConfirmGui())) return;
		if(event.getCurrentItem() == null) return;
		
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirm")) 
			gui.grantAction((Player) event.getWhoClicked());
		
		else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Cancel")) 
			player.sendMessage(format("&c&lYou cancelled the grant!"));
		
		player.closeInventory();
			
	}
	
	
	
	
	@EventHandler
	public void chooseRank(InventoryClickEvent event) {

		Player player = (Player) event.getWhoClicked();
		if(!event.getInventory().equals(GUIManager.getRankGui())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();


		for(String rank : Files.config.getConfigurationSection("Ranks").getKeys(false)) {
			int configSlot = Files.config.getInt("Ranks." + rank + ".slot");
			String rankName = Files.config.getString("Ranks." + rank + ".rank");

			if(configSlot == slot) {
				if(player.hasPermission("rankgrant.grant." + rank)) {
					GrantManager.setTargetRank(rankName);
					player.openInventory(GUIManager.getDurationGUI());
				} else player.sendMessage(ChatColor.RED + "You do not have permission to grant people this rank!");
				
				break;

			} else continue;
		}

	}
	
	@EventHandler
	public void choosePermission(InventoryClickEvent event) {

		Player player = (Player) event.getWhoClicked();
		if(!event.getInventory().equals(GUIManager.getPermGui())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();


		for(String perm : Files.config.getConfigurationSection("Permissions").getKeys(false)) {
			int configSlot = Files.config.getInt("Permissions." + perm + ".slot");
			String permission = Files.config.getString("Permissions." + perm + ".permission");

			if(configSlot == slot) {
				if(player.hasPermission("Permissions.permissions." + perm)) {
					GrantManager.setPermission(permission);
					player.openInventory(GUIManager.getDurationGUI());
				} else player.sendMessage(ChatColor.RED + "You do not have permission to grant people this permission!");
				
				break;

			} else continue;
		}

	}
	
	@EventHandler
	public void chooseGrantOption(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(!event.getInventory().equals(GUIManager.getMainGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();
		
		if(slot == 12) {
			player.openInventory(GUIManager.getRankGui());
			GrantManager.setGrantOption("Rank");
			
		}
		
		if(slot == 14) {
			player.openInventory(GUIManager.getTruefalseGui());
			GrantManager.setGrantOption("Permission");
			
		}
		
	}
	
	@EventHandler
	public void choosetruefalse(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(!event.getInventory().equals(GUIManager.getTruefalseGui())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();
		
		if(slot == 12) {
			GrantManager.setPermBoolean("True");
		}
		
		if(slot == 14) {
			GrantManager.setPermBoolean("False");
			
		}
		player.openInventory(GUIManager.getPermGui());
	}
	
	
	public String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
