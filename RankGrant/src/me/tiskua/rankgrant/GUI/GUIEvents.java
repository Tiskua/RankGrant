package me.tiskua.rankgrant.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.main.Main;
import me.tiskua.rankgrant.utils.GrantManager;
import me.tiskua.rankgrant.utils.ItemCreator;
import me.tiskua.rankgrant.utils.Util;

public class GUIEvents implements Listener{

	GUIS gui;
	Main main;
	public GUIEvents(GUIS gui, Main main) {
		this.gui = gui;
		this.main = main;
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
		} else if(slot == 14) {
			player.openInventory(GUIManager.getPermGui());
			GrantManager.setGrantOption("Permission");
		}
	}


	@EventHandler
	public void chooseRank(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getRankGui())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();

		Player player = (Player) event.getWhoClicked();
		if(slot == event.getInventory().getSize()-9 && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Util.format("&c&lBack"))) {
			player.openInventory(GUIManager.getMainGUI());
			return;
		}
		if(!gui.ranks.containsKey(slot)) return;

		if(Files.config.getBoolean("ranks-need-permission")) {
			if(player.hasPermission("rankgrant.grant." + gui.ranks.get(slot)) || player.hasPermission("rankgrant.grant.*")) {
				GrantManager.setTargetRank(gui.ranks.get(slot));
				player.openInventory(GUIManager.getDurationGUI());
			} else player.sendMessage(ChatColor.RED + "You do not have permission to grant people this rank!");
		} else {
			GrantManager.setTargetRank(gui.ranks.get(slot));
			player.openInventory(GUIManager.getDurationGUI());
		}

	}

	@EventHandler
	public void choosePermission(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getPermGui())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();

		Player player = (Player) event.getWhoClicked();
		if(slot == 8) {
			if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Util.format("&a&lGive"))) {
				if(Util.isLegacy()) event.setCurrentItem(new ItemCreator(Material.INK_SACK).setDisplayname("&c&lRemove").setDurability((short) 1).buildItem());
				else event.setCurrentItem(new ItemCreator(Material.valueOf("RED_DYE")).setDisplayname("&c&lRemove").buildItem());
				GrantManager.setPermBoolean("False");
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Util.format("&c&lRemove"))){
				if(Util.isLegacy()) event.setCurrentItem(new ItemCreator(Material.INK_SACK).setDisplayname("&a&lGive").setDurability((short) 10).buildItem());
				else event.setCurrentItem(new ItemCreator(Material.valueOf("LIME_DYE")).setDisplayname("&a&lGive").buildItem());
				GrantManager.setPermBoolean("True");
			}
		}
		if(slot == event.getInventory().getSize()-9 && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Util.format("&c&lBack"))) {
			player.openInventory(GUIManager.getMainGUI());
			return;
		}
		if(!gui.permissions.containsKey(slot)) return;

		if(Files.config.getBoolean("permissions-need-permission")) {
			if(player.hasPermission("rankgrant.grant." + gui.permissions.get(slot)) || player.hasPermission("rankgrant.grant.*")) {
				GrantManager.setPermission(gui.permissions.get(slot));
				player.openInventory(GUIManager.getDurationGUI());
			} else player.sendMessage(ChatColor.RED + "You do not have permission to grant people this permission!");
		} else {
			GrantManager.setPermission(gui.permissions.get(slot));
			player.openInventory(GUIManager.getDurationGUI());
		}
	}




	@EventHandler
	public void chooseDuration(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getDurationGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();

		Player player = (Player) event.getWhoClicked();

		if(slot == event.getInventory().getSize()-9 && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Util.format("&c&lBack"))) {
			player.openInventory(GrantManager.getGrantOption().equals("Rank") ? GUIManager.getRankGui() : GUIManager.getPermGui());
			return;
		}
		if(!gui.durations.containsKey(slot)) return;
		if(gui.durations.get(slot) == -2) {
			gui.customDuration.add(player);
			player.closeInventory();
			player.sendMessage(Util.format("&eType what you want the Duration to be:"));
			return;
		}
		GrantManager.setGrantDuration(gui.durations.get(slot));
		player.openInventory(GUIManager.getReasonGUI());
	}


	@EventHandler
	public void chooseReason(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getReasonGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();

		Player player = (Player) event.getWhoClicked();
		if(slot == event.getInventory().getSize()-9 && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Util.format("&c&lBack"))) {			
			player.openInventory(GUIManager.getDurationGUI());
			return;
		}
		if(!gui.reasons.containsKey(slot)) return;
		if(gui.reasons.get(slot).equals("custom-reason")) {
			gui.customReason.add(player);
			player.closeInventory();
			player.sendMessage(Util.format("&eType what you want the reason to be:"));
			return;
		}
		GrantManager.setGrantReason(gui.reasons.get(slot));
		player.closeInventory();
		player.openInventory(GUIManager.getConfirmGui());

		addConfirmCompass();



	}

	private void addConfirmCompass() {
		List<String> lore = new ArrayList<>();
		String permission = "&e&lPermission&7: " + GrantManager.getPermission() + " : " + GrantManager.getPermBoolean();
		String rank = "&e&lRank&7: " + GrantManager.getTargetRank();

		lore.add("&e&lTarget&7: " + GrantManager.getTarget());
		lore.add((GrantManager.getGrantOption() == "Rank") ? rank : permission);
		lore.add("&e&lDuration&7: " + Util.buildTimeMeasurements(GrantManager.getGrantDuration()));
		lore.add("&e&lReason&7: " + GrantManager.getGrantReason());

		GUIManager.getConfirmGui().setItem(22, new ItemCreator(Material.COMPASS).setDisplayname("&6&lInfo").lore(Util.formatList(lore)).buildItem());
	}


	@EventHandler
	public void confirm(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getConfirmGui())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		if(event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirm")) {
			gui.grantAction((Player) event.getWhoClicked());
			player.closeInventory();
		}

		else if(event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().contains("Cancel")) {
			player.sendMessage(Util.format("&c&lYou cancelled the grant!"));
			player.closeInventory();
		}
		gui.resetPermBool();
	}

	@EventHandler
	public void logpage(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(!event.getInventory().equals(GUIManager.getLogGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();


		if(slot == GUIManager.getLogGUI().getSize()-1 && event.getCurrentItem().getType() == Material.ARROW) {
			gui.page++;
			gui.updateLogsInv();
		}
		if(slot == GUIManager.getLogGUI().getSize()-9 && event.getCurrentItem().getType() == Material.ARROW) {
			gui.page--;
			gui.updateLogsInv();
		}
		player.updateInventory();
	}


//	@EventHandler
//	public void closeInventory(InventoryCloseEvent event) {
//		if(event.getInventory().equals(GUIManager.getLogGUI())) {
//			gui.page = 0;
//			gui.updateLogsInv();
//		}
//
//		Player player = (Player) event.getPlayer();
//		Inventory inv = event.getInventory();
//
//		if(!inv.equals(GUIManager.getMainGUI()) 
//				&& !inv.equals(GUIManager.getRankGui())
//				&& !inv.equals(GUIManager.getPermGui())
//				&& !inv.equals(GUIManager.getDurationGUI())
//				&& !inv.equals(GUIManager.getReasonGUI())) {
//			return;
//		}
//		if(inv.equals(GUIManager.getConfirmGui())) 
//			return;
//		if(inv.equals(GUIManager.getLogGUI())) 
//			return;
//
//		if(gui.customDuration.contains(player) || gui.customReason.contains(player))
//			return;
//
//		player.sendMessage("INB" + event.getInventory().getName());
//
//	}

	@EventHandler 
	public void custom(AsyncPlayerChatEvent event) {
		if(!gui.customDuration.contains(event.getPlayer()) && !gui.customReason.contains(event.getPlayer())) 
			return;

		event.setCancelled(true);
		Player player = event.getPlayer();
		if(gui.customDuration.contains(player)) {
			String[] arr = event.getMessage().split(" ");
			if(Util.isInteger(arr[0])) {
				if(Integer.parseInt(arr[0]) > 0) {
					if(arr[1].contains("year") || arr[1].contains("month") || arr[1].contains("week") || arr[1].contains("day") || arr[1].contains("hour") || arr[1].contains("second")){
						GrantManager.setGrantDuration(Util.durationToSeconds(event.getMessage()));
						player.sendMessage(Util.format("&bYou set the duration to&e: " + event.getMessage()));
						gui.customDuration.remove(player);					
						syncOpenInv(player, GUIManager.getReasonGUI());
					} else {
						player.sendMessage(Util.format("&cThis is not a valid duration! Try again."));
						player.sendMessage(Util.format("&cExample: 1 month"));
						return;
					}
				} else {
					player.sendMessage(Util.format("&cDuration must be greater than 0! Try again."));
					return;
				}
			}
			else {
				player.sendMessage(Util.format("&cThis is not a valid duration! Try again."));
				player.sendMessage(Util.format("&cExample: 1 month"));
				return;
			}

		} else if(gui.customReason.contains(player)) {
			player.sendMessage(Util.format("&bYou set the reason to&e: " + event.getMessage()));
			GrantManager.setGrantReason(event.getMessage());
			gui.customReason.remove(player);
			addConfirmCompass();
			syncOpenInv(player, GUIManager.getConfirmGui());

		}
	}

	private void syncOpenInv(Player player, Inventory inv) {
		Bukkit.getScheduler().runTask(main, () -> {
			player.openInventory(inv);
		});

	}
}
