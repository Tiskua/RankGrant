package me.tiskua.rankgrant.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.tiskua.rankgrant.GUI.GUIManager;
import me.tiskua.rankgrant.GUI.GUIS;
import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.main.Main;

public class Commands implements CommandExecutor {

	Main main;
	GUIS gui;
	public Commands(Main main, GUIS gui) {
		this.main = main;
		this.gui = gui;
	}

	


	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("grant")) {
			if(!(sender instanceof Player)) return false;
			Player player = (Player) sender;
			if(!hasPermission(player, "rankgrant.grant")) return false;
			if(args.length == 1) {
				if(Bukkit.getPlayer(args[0]) == null) {
					OfflinePlayer oplayer = Bukkit.getOfflinePlayer(args[0]);
					if(oplayer.hasPlayedBefore()) 
						GrantManager.setTarget(oplayer.getName());
					else {
						player.sendMessage(ChatColor.RED + "That player has not played before!");
						return false;
					}
				}
				else {
					Player target = Bukkit.getPlayer(args[0]);
					GrantManager.setTarget(target.getName());
				}
				if(gui.customDuration.contains(player)) gui.customDuration.remove(player);
				if(gui.customReason.contains(player)) gui.customReason.remove(player);
				GrantManager.setGranter(player);
				gui.createMainGUI();
				player.openInventory(GUIManager.getMainGUI());
			} else player.sendMessage(ChatColor.RED + "Usage: /grant <player>");

		} 

		else if(label.equalsIgnoreCase("grantadmin")) {
			if(!(sender instanceof Player)) return false;
			Player player = (Player) sender;
			if(args[0].equalsIgnoreCase("reload")) {
				if(!hasPermission(player, "rankgrant.reload")) return false;
				Files.reloadConfig(main);
				main.createGUIS();
				player.sendMessage(ChatColor.GREEN + "Successfully reloaded the config!");
			} else if(args[0].equalsIgnoreCase("help")) {
				if(!hasPermission(player, "rankgrant.help")) return false;
				helpMessage(player);
			}
			else if(args[0].equalsIgnoreCase("logs")) {
				if(!hasPermission(player, "rankgrant.logs")) return false;
				if(Files.log.getConfigurationSection("Logs") == null || Files.log.getConfigurationSection("Logs").getKeys(false).isEmpty()) {
					player.sendMessage(Util.format("&cThere are no logs to display!"));
					return false;
				}
				player.openInventory(GUIManager.getLogGUI());
			}
			else if(args[0].equalsIgnoreCase("fixconfig")) {
				if(!hasPermission(player, "rankgrant.fixconfig")) return false;
				Files.fixConfig(main, player);
			}
				
			else player.sendMessage(Util.format("&7Use &e/grantadmin help &7 to see commands"));
		}
		return false;
	}

	private void helpMessage(Player player) {
		player.sendMessage(Util.format("&6&l-------------------------"));
		player.sendMessage(Util.format("&e/grant <Player>&7: Grants a player a rank or a permission"));
		player.sendMessage(Util.format("&e/grantadmin logs&7: Displays the latest number of logs"));
		player.sendMessage(Util.format("&e/grantadmin reload&7: Reloads the config file"));
		player.sendMessage(Util.format("&e/grantadmin help&7: Displays this message"));
		player.sendMessage(Util.format("&e/grantadmin fixconfig&7: Updates the config file (To the versions config file), and creates a backup config(So you can replace everything in the new config)"));
		player.sendMessage(Util.format("&6&l-------------------------"));
	}

	private boolean hasPermission(Player player, String permission) {
		if(!player.hasPermission(permission)) {
			player.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
			return false;
		}
		return true;
	}
}