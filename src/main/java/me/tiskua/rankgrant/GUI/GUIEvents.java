package me.tiskua.rankgrant.GUI;

import me.tiskua.rankgrant.Grant.Grant;
import me.tiskua.rankgrant.Grant.GrantManager;
import me.tiskua.rankgrant.anvil.AnvilManager;
import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.main.Main;
import me.tiskua.rankgrant.utils.ItemCreator;
import me.tiskua.rankgrant.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GUIEvents implements Listener{


	Main main = Main.getMainInstance();
	GUIS gui = main.guis;
	Grant grant = new Grant();
	AnvilManager anvilInterface = new AnvilManager();


	@EventHandler
	public void chooseGrantOption(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(!event.getInventory().equals(GUIManager.getMainGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);

		int slot = event.getSlot();
		if(slot == Files.config.getInt("Main.Ranks.slot")) {
			player.openInventory(GUIManager.getRankGUI());
			GrantManager.setGrantType(GrantManager.GrantType.RANK);
			GUIManager.addBackButtonInvs(GUIManager.getDurationGUI(), GUIManager.getRankGUI());
			GUIManager.addBackButtonInvs(GUIManager.getRankGUI(), GUIManager.getMainGUI());
			new GUIBasics().addBackButtons();
		} else if(slot == Files.config.getInt("Main.Permissions.slot")) {
			player.openInventory(GUIManager.getPermissionGUI());
			GrantManager.setGrantType(GrantManager.GrantType.PERMISSION);
			GUIManager.addBackButtonInvs(GUIManager.getDurationGUI(), GUIManager.getPermissionGUI());
			GUIManager.addBackButtonInvs(GUIManager.getPermissionGUI(), GUIManager.getMainGUI());
			new GUIBasics().addBackButtons();

		}
	}


	@EventHandler
	public void chooseRank(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getRankGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();

		Player player = (Player) event.getWhoClicked();
		if(previousInv(player, event.getCurrentItem(), slot, event.getInventory(), GUIManager.getBackButtonInvs().get(event.getInventory()))) return;
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
		if(!event.getInventory().equals(GUIManager.getPermissionGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();

		Player player = (Player) event.getWhoClicked();
		if(previousInv(player, event.getCurrentItem(), slot, event.getInventory(), GUIManager.getBackButtonInvs().get(event.getInventory()))) return;
		if(slot == 8) {
			if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Util.format("&a&lGive"))) {
				if(Util.isLegacy()) event.setCurrentItem(new ItemCreator(Material.INK_SACK).setDisplayname("&c&lRemove").setDurability((short) 1).buildItem());
				else event.setCurrentItem(new ItemCreator(Material.valueOf("RED_DYE")).setDisplayname("&c&lRemove").buildItem());
				GrantManager.setPermissionBoolean("False");
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Util.format("&c&lRemove"))){
				if(Util.isLegacy()) event.setCurrentItem(new ItemCreator(Material.INK_SACK).setDisplayname("&a&lGive").setDurability((short) 10).buildItem());
				else event.setCurrentItem(new ItemCreator(Material.valueOf("LIME_DYE")).setDisplayname("&a&lGive").buildItem());
				GrantManager.setPermissionBoolean("True");
			}
		}
		if(previousInv(player, event.getCurrentItem(), slot, event.getInventory(), GUIManager.getBackButtonInvs().get(event.getInventory()))) return;
		if(!gui.permissions.containsKey(slot)) return;

		if(Files.config.getBoolean("permissions-need-permission")) {
			if(player.hasPermission("rankgrant.grant." + gui.permissions.get(slot)) || player.hasPermission("rankgrant.grant.*")) {
				GrantManager.setTargetPermission(gui.permissions.get(slot));
				player.openInventory(GUIManager.getDurationGUI());
			} else player.sendMessage(ChatColor.RED + "You do not have permission to grant people this permission!");
		} else {
			GrantManager.setTargetPermission(gui.permissions.get(slot));
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

		if(previousInv(player, event.getCurrentItem(), slot, event.getInventory(), GrantManager.getGrantType() == GrantManager.GrantType.RANK? GUIManager.getRankGUI() : GUIManager.getPermissionGUI())) return;

		if(!gui.durations.containsKey(slot)) return;
		if(gui.durations.get(slot) == -2) {
			gui.customDuration.add(player);
			anvilInterface.customDuration(player);
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
		if(previousInv(player, event.getCurrentItem(), slot, event.getInventory(), GUIManager.getBackButtonInvs().get(event.getInventory()))) return;
		if(!gui.reasons.containsKey(slot)) return;
		if(gui.reasons.get(slot).equals("custom-reason")) {
			gui.customReason.add(player);
			anvilInterface.customReason(player);
			return;
		}
		GrantManager.setGrantReason(gui.reasons.get(slot));
		player.closeInventory();
		player.openInventory(GUIManager.getConfirmGUI());
		addConfirmCompass();
	}

	private void addConfirmCompass() {
		List<String> lore = new ArrayList<>();
		String permission = "&e&lPermission&7: " + (GrantManager.getPermissionBoolean().equals("True") ? "Giving" : "Removing")
				+ " | " + GrantManager.getTargetPermission();
		String rank = "&e&lRank&7: " + GrantManager.getTargetRank();

		lore.add("&e&lTarget&7: " + GrantManager.getTarget());
		lore.add(GrantManager.getGrantType() == GrantManager.GrantType.RANK ? rank : permission);
		lore.add("&e&lDuration&7: " + Util.buildTimeMeasurements(GrantManager.getGrantDuration()));
		lore.add("&e&lReason&7: " + GrantManager.getGrantReason());

		GUIManager.getConfirmGUI().setItem(22, new ItemCreator(Material.COMPASS).setDisplayname("&6&lInfo").lore(Util.formatList(lore)).buildItem());
	}

	@EventHandler
	public void confirm(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getConfirmGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		if(event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirm")) {
			grant.grantAction(player);
			player.closeInventory();
		}

		else if(event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().contains("Cancel")) {
			player.sendMessage(Util.format("&c&lYou cancelled the grant!"));
			player.closeInventory();
		}
		gui.resetPermBool();
	}




	@EventHandler
	public void closeInventory(InventoryCloseEvent event) {

		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();

		if(gui.customDuration.contains(player) || gui.customReason.contains(player))
			return;
		if(!inv.equals(GUIManager.getMainGUI())
				&& !inv.equals(GUIManager.getRankGUI()) && !inv.equals(GUIManager.getPermissionGUI())
				&& !inv.equals(GUIManager.getDurationGUI()) && !inv.equals(GUIManager.getReasonGUI())) {
			return;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				InventoryType newInvType = player.getOpenInventory().getTopInventory().getType();

				if (newInvType != InventoryType.CHEST) {
					player.sendMessage(Util.format("&c&lCANCELLED GRANT!"));

				}
			}
		}.runTaskLater(main, 5);
	}

//	@EventHandler
//	public void custom(AsyncPlayerChatEvent event) {
//		if(!gui.customDuration.contains(event.getPlayer()) && !gui.customReason.contains(event.getPlayer()))
//			return;
//
//		event.setCancelled(true);
//		Player player = event.getPlayer();
//		if(gui.customDuration.contains(player)) {
//			String[] arr = event.getMessage().split(" ");
//			if(Util.isInteger(arr[0])) {
//				if(Integer.parseInt(arr[0]) > 0) {
//					if(arr[1].contains("year") || arr[1].contains("month") || arr[1].contains("week") || arr[1].contains("day") || arr[1].contains("hour") || arr[1].contains("second")){
//						GrantManager.setGrantDuration(Util.durationToSeconds(event.getMessage()));
//						player.sendMessage(Util.format("&bYou set the duration to&e: " + event.getMessage()));
//						gui.customDuration.remove(player);
//						syncOpenInv(player, GUIManager.getReasonGUI());
//					} else {
//						player.sendMessage(Util.format("&cThis is not a valid duration! Try again."));
//						player.sendMessage(Util.format("&cExample: 1 month"));
//					}
//				} else player.sendMessage(Util.format("&cDuration must be greater than 0! Try again."));
//
//			}
//			else {
//				player.sendMessage(Util.format("&cThis is not a valid duration! Try again."));
//				player.sendMessage(Util.format("&cExample: 1 month"));
//			}
//
//		} else if(gui.customReason.contains(player)) {
//			player.sendMessage(Util.format("&bYou set the reason to&e: " + event.getMessage()));
//			GrantManager.setGrantReason(event.getMessage());
//			gui.customReason.remove(player);
//			addConfirmCompass();
//			syncOpenInv(player, GUIManager.getConfirmGUI());
//		}
//	}


	private void syncOpenInv(Player player, Inventory inv) {
		Bukkit.getScheduler().runTask(main, () -> player.openInventory(inv));

	}
	private void delayInventoryOpen(Player player, Inventory inv) {
		Bukkit.getScheduler().runTaskLater(Main.getMainInstance(), () -> player.openInventory(inv), 1L);
	}

	private boolean previousInv(Player player, ItemStack item, int slot, Inventory from, Inventory to) {
		if(slot == from.getSize()-9 && item.getItemMeta().getDisplayName().equalsIgnoreCase(Util.format("&c&lBack"))) {
			player.closeInventory();
			delayInventoryOpen(player, to);
			return true;
		}
		return false;
	}
}
