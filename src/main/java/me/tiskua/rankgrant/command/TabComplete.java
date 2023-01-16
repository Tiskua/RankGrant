package me.tiskua.rankgrant.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;


public class TabComplete implements TabCompleter {
	
	
	
	List<String> arguments = new ArrayList<>();
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(arguments.isEmpty()) {
			arguments.add("logs");
			arguments.add("help");
			arguments.add("reload");
			arguments.add("fixconfig");
			arguments.add("debug");
		}
		
		List<String> result = new ArrayList<>();
		if(args.length == 1) {
			for(String a : arguments) {
				if(a.toLowerCase().startsWith(args[0].toLowerCase()))
					result.add(a);
			}
			return result;	
		}
		return null;
	}
}
