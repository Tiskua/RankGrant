package me.tiskua.rankgrant.Grant;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.utils.Util;

public class Messages {

	public void sendToGranter() {
		String messageSection = GrantManager.getGrantType() == GrantManager.GrantType.RANK ? "Messages.Grant.Rank.Granter" : "Messages.Grant.Permission.Granter";
		for (String msg : Files.config.getStringList(messageSection)) 
			GrantManager.getGranter().sendMessage(Util.format(Util.replaceMessageValues(msg)));

	}

	public void sendGrantStaffMessage(Player staff) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online.hasPermission("grant.notify") && online != staff) {
				StringBuilder finalStaffmessage = new StringBuilder();
				String messageSection =  GrantManager.getGrantType() == GrantManager.GrantType.RANK ? "Messages.Grant.Rank.Staff" : "Messages.Grant.Permission.Staff";
				for (String msg : Files.config.getStringList(messageSection)) 
					finalStaffmessage.append(Util.replaceMessageValues(msg)).append("\n");
				online.sendMessage(Util.format(finalStaffmessage.toString()));
			}
		}
	}

	public void sendToTarget(Player target) {
		String messageSection = GrantManager.getGrantType() == GrantManager.GrantType.RANK ? "Messages.Grant.Rank.Target" : "Messages.Grant.Permission.Target";
		for(String line : Files.config.getStringList(messageSection)) 
			target.sendMessage(Util.format(Util.replaceMessageValues(line)));
	}
}
