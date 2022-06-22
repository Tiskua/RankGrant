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
			if(player.hasPermission("rankgrant.grant")) {
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
						
					GrantManager.setGranter(player);
					gui.createMainGUI();
					player.openInventory(GUIManager.getMainGUI());
				} else player.sendMessage(ChatColor.RED + "Usage: /grant <player>");
			} else player.sendMessage(ChatColor.RED + "You do not have permission to do this command!");

		} 

		if(label.equalsIgnoreCase("grantadmin")) {
			if(!(sender instanceof Player)) return false;
			Player player = (Player) sender;
//			Set<Group> groups = main.luckperms.getGroupManager().getLoadedGroups();
			if(args.length != 1)  return false;
			if(args[0].equalsIgnoreCase("reload")) {
				if(!player.hasPermission("rankgrant.reload")) return false;
				Files.reloadConfig(main);
				
				main.createGUIS();
				player.sendMessage(ChatColor.GREEN + "Successfully reloaded the config!");
			} else if(args[0].equalsIgnoreCase("help")) {
				if(!player.hasPermission("rankgrant.help")) return false;
				helpMessage(player);
			}
			
		}
		return false;
	}

	private void helpMessage(Player player) {
		player.sendMessage(Util.format("&6&l-------------------------"));
		player.sendMessage(Util.format("&e/grant<Player>: Grants a player a rank or a permission"));
		player.sendMessage(Util.format("&e/grantadmin reload: Reloads the config file"));
		player.sendMessage(Util.format("&e/grantadmin help: Displays this message"));
		player.sendMessage(Util.format("&6&l-------------------------"));
	}
	
}