package me.tiskua.rankgrant.Logs;

import me.tiskua.rankgrant.GUI.*;
import me.tiskua.rankgrant.Grant.*;
import me.tiskua.rankgrant.main.*;
import me.tiskua.rankgrant.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.concurrent.*;

public class Logs {

	Main main = Main.getMainInstance();
	public LogsManager logsmanager = new LogsManager();
	
	public ArrayList<String> logs = new ArrayList<>();
	

	public void updateLogsInv() {
		if(Files.log.getConfigurationSection("Logs") == null) {
			Files.log.createSection("Logs");
			Files.savelog();
		}
		Set<String> data = Files.log.getConfigurationSection("Logs").getKeys(false);
		logs.clear();
		logs.addAll(data);

		int xoffset = 0;
		int yoffset = 0;

		Date now = new Date();

		GUIManager.getLogGUI().clear();
		new GUIBasics().setBorders(GUIManager.getLogGUI());

		updateItem("&e&lShow Expired", 47, logsmanager.isShowExpired(), "Grants that are Expired");
		updateItem("&e&lShow Old", 48, logsmanager.isShowOld(), "Grants that are Older than a Month");

		GUIManager.getLogGUI().setItem(49, new ItemCreator(Material.NAME_TAG).setDisplayname("&e&lTarget&7: " + logsmanager.getFilterTargetName()).glow(true).lore(" ", "&bShift-Right-Click to remove the filter").buildItem());
		GUIManager.getLogGUI().setItem(50, new ItemCreator(Material.NAME_TAG).setDisplayname("&e&lWho Granted&7: " + logsmanager.getFilterGranterName()).glow(true).lore(" ", "&bShift-Right-Click to remove the filter").buildItem());
		GUIManager.getLogGUI().setItem(51, new ItemCreator(Material.COMPASS).setDisplayname("&6&lSort By&7: ").lore("&7Newest to Oldest", "&7Oldest to Newest"
				, "&7Duration (Shortest)", "&7Duration (Longest)", "&7Expired").buildItem());
		applyLogFilters();
		updateSort();

		if(logs.size() > (logsmanager.getPage() * 28) + 28) GUIManager.getLogGUI().setItem(GUIManager.getLogGUI().getSize()-1, new ItemCreator(Material.ARROW).setDisplayname("&a&lNext Page").buildItem()); 
		if(logsmanager.getPage() > 0) GUIManager.getLogGUI().setItem(GUIManager.getLogGUI().getSize()-9, new ItemCreator(Material.ARROW).setDisplayname("&a&lPrevious Page").buildItem());

		if(logs.isEmpty()) {
			for(int i = 10; i < 17; i++)
				GUIManager.getLogGUI().setItem(i, new ItemCreator(Util.isLegacy() ? Material.STAINED_GLASS_PANE : Material.valueOf("RED_STAINED_GLASS_PANE"))
						.setDurability(14).setDisplayname("&c&lThere are no logs to display!").buildItem());
			return;
		}
		for(int i = logsmanager.getPage() * 28; i < logsmanager.getPage() * 28 + 28; i++) {
			String log = logs.get(i);
			Date past = Util.convertToDate(log);
			assert past != null;
			List<String> lore = setLogLore(now, past, i);
			String prefix = "Logs." + log;
			String log_date = "&6&l" + Util.getConfigDateFormat().format(past);
			int offsetslot = 10 + xoffset + (yoffset*9);
			if(logsmanager.getSort() == 0 || logsmanager.getSort() == 1) {
				if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) <=24) GUIManager.getLogGUI().setItem(offsetslot, 
						new ItemCreator("WOOL").setDisplayname(log_date).setDurability(5).lore(lore).convertColoredBlocksToNew().buildItem());
				else if(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) <= 7) GUIManager.getLogGUI().setItem(offsetslot, 
						new ItemCreator("WOOL").setDisplayname(log_date).setDurability(13).lore(lore).convertColoredBlocksToNew().buildItem());
				else if(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) <= 30) GUIManager.getLogGUI().setItem(offsetslot, 
						new ItemCreator("WOOL").setDisplayname(log_date).setDurability(4).lore(lore).convertColoredBlocksToNew().buildItem());
				else GUIManager.getLogGUI().setItem(offsetslot, new ItemCreator(Material.WOOL).setDisplayname(log_date).setDurability(1).lore(lore).convertColoredBlocksToNew().buildItem());
			}

			else if(logsmanager.getSort() == 2 || logsmanager.getSort() == 3) {
				int duration = Files.log.getInt(prefix + ".duration");
				
				if (duration == -1)  //Forever
					GUIManager.getLogGUI().setItem(offsetslot, new ItemCreator("WOOL").setDisplayname(log_date).setDurability(9).lore(lore)
							.convertColoredBlocksToNew().buildItem()); 
				else if(duration <= 3600)  //1 hour
					GUIManager.getLogGUI().setItem(offsetslot, new ItemCreator("WOOL").setDisplayname(log_date).setDurability(3).lore(lore)
							.convertColoredBlocksToNew().buildItem()); 
				else if(duration <= 86400)  //1 day
					GUIManager.getLogGUI().setItem(offsetslot, new ItemCreator("WOOL").setDisplayname(log_date).setDurability(1).lore(lore)
							.convertColoredBlocksToNew().buildItem()); 
				else if(duration <= 604800)  //1 week
					GUIManager.getLogGUI().setItem(offsetslot, new ItemCreator("WOOL").setDisplayname(log_date).setDurability(2).lore(lore)
							.convertColoredBlocksToNew().buildItem()); 
				else if(duration <= 2628000)  //1 month
					GUIManager.getLogGUI().setItem(offsetslot, new ItemCreator("WOOL").setDisplayname(log_date).setDurability(10).lore(lore)
							.convertColoredBlocksToNew().buildItem()); 
				else  //1 year+
					GUIManager.getLogGUI().setItem(offsetslot, new ItemCreator("WOOL").setDisplayname(log_date).setDurability(14).lore(lore)
							.convertColoredBlocksToNew().buildItem()); 				
			}
			else if(logsmanager.getSort() == 4) {
				GUIManager.getLogGUI().setItem(offsetslot, new ItemCreator("WOOL").setDisplayname(log_date).setDurability(8).lore(lore)
						.convertColoredBlocksToNew().buildItem()); 
			}
			xoffset++;
			if(xoffset == 7) {yoffset++; xoffset = 0;}
			if(i == logs.size()-1) break;
		}
	}

	public void updateSort() {
		String activeColor = "&e";
		ItemMeta meta = GUIManager.getLogGUI().getItem(51).getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(logsmanager.getSort(), activeColor + lore.get(logsmanager.getSort()).substring(2));
		meta.setLore(Util.formatList(lore));
		GUIManager.getLogGUI().getItem(51).setItemMeta(meta);
		sortLogs();
	}

	private void sortLogs() {
		if(logsmanager.getSort() == 0) Collections.sort(logs); //newest 
		else if(logsmanager.getSort() == 1) Collections.reverse(logs); //oldest
		else if(logsmanager.getSort() == 2) { //duration
			sortByDurations();
			Collections.reverse(logs);
		}
		else if(logsmanager.getSort() == 3) sortByDurations();
		else if(logsmanager.getSort() == 4) {
			Date now = new Date();
			List<String> temp = new ArrayList<>();
			for(String log : logs) {
				Date past = Util.convertToDate(log);
				assert past != null;
				if(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) >= Files.log.getInt("Logs." + log + ".duration"))
					temp.add(log);
			}
			logs.clear();
			logs.addAll(temp);
		}
	}
	
	public void sortByDurations() {
		HashMap<String, Integer> durations = new HashMap<>();
		for(String log : logs) {
			String prefix = "Logs." + log;
			durations.put(log, Files.log.getInt(prefix + ".duration"));
		}
		logs.clear();
		List<Map.Entry<String, Integer>> list = new LinkedList<>(durations.entrySet());

		list.sort(Map.Entry.comparingByValue());

		for (Map.Entry<String, Integer> aa : list) {
			logs.add(aa.getKey());
		}
		Collections.reverse(logs);
	}


	private void applyLogFilters() {
		Date now = new Date();
		
		List<String> logsToRemove = new ArrayList<>();
		for(String log : logs) {
			Date past = Util.convertToDate(log);
			String prefix = "Logs." + log;
			assert past != null;
			boolean isExpired = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) >= Files.log.getInt(prefix + ".duration");
			boolean isOld = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > 30;

			if(isExpired && !logsmanager.isShowExpired()) logsToRemove.add(log);
			if(isOld && !logsmanager.isShowOld()) logsToRemove.add(log);
			if(!(logsmanager.getFilterTargetName().equalsIgnoreCase("No Current Filter")))
				if(!(Files.log.getString(prefix + ".target").contains(logsmanager.getFilterTargetName())))
					logsToRemove.add(log);
			if(!(logsmanager.getFilterGranterName().equalsIgnoreCase("No Current Filter")))
				if(!(Files.log.getString(prefix + ".granter").contains(logsmanager.getFilterGranterName())))
					logsToRemove.add(log);
			
		}

		for(String log : logsToRemove)
			logs.remove(log);
	}


	private List<String> setLogLore(Date now, Date past, int i) {
		List<String> lore = new ArrayList<>();
		String prefix = "Logs." + logs.get(i);
		boolean isExpired = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) >= Files.log.getInt(prefix + ".duration");

		lore.add("&7---------------------------");
		if(Files.log.getString(prefix + ".rank") != null) lore.add("&eRank&8: " + Files.log.getString(prefix + ".rank"));
		else lore.add("&ePermission&8: " + Files.log.getString(prefix + ".permission"));
		lore.add("&eGranted To&7: " +  Files.log.getString(prefix + ".target"));
		lore.add("&eDuration&7: " +  Util.buildTimeMeasurements(Files.log.getInt(prefix + ".duration")));
		lore.add("&eReason&8: " + Files.log.getString(prefix + ".reason"));
		lore.add("&eGranted by&7: " +  Files.log.getString(prefix + ".granter"));
		lore.add("&eExpired&7: " + ((isExpired) ? "&c&lYes" : "&a&lNo"));
		lore.add("&7---------------------------");
		return lore;
	}

	@SuppressWarnings("deprecation")
	public void saveLog() {
		Date date = new Date();

		Files.log.set("Logs." + date + ".granter", GrantManager.getGranter().getName());

		if(GrantManager.getGrantType() == GrantManager.GrantType.RANK) Files.log.set("Logs." + date + ".rank", GrantManager.getTargetRank());
		else  Files.log.set("Logs." + date + ".permission", GrantManager.getTargetPermission() + " | " + GrantManager.getPermissionBoolean());

		Files.log.set("Logs." + date + ".duration", GrantManager.getGrantDuration());
		Files.log.set("Logs." + date + ".reason", GrantManager.getGrantReason());
		Files.log.set("Logs." + date + ".target", GrantManager.getTarget());
		Files.savelog();

		updateLogsInv();

		Player player = Bukkit.getPlayer(GrantManager.getTarget());

		if(player == null) main.data.createMYSQLLog(Bukkit.getOfflinePlayer(GrantManager.getTarget()).getPlayer());		
		else main.data.createMYSQLLog(player);
	}

	public void updateItem(String name, int slot, boolean bol,String l) {
		if(!bol) {
			if(Util.isLegacy()) GUIManager.getLogGUI().setItem(slot, new ItemCreator(Material.INK_SACK).lore("&7You will not see " + l).setDisplayname(name).setDurability(8).buildItem());
			else GUIManager.getLogGUI().setItem(slot, new ItemCreator(Material.valueOf("GRAY_DYE")).setDisplayname(name).lore("&7You will not see " + l).buildItem());
		} else {
			if(Util.isLegacy()) GUIManager.getLogGUI().setItem(slot, new ItemCreator(Material.INK_SACK).setDisplayname(name).lore("&7You will see " + l)
					.setDurability(10).glow(true).buildItem());
			else GUIManager.getLogGUI().setItem(slot, new ItemCreator(Material.valueOf("LIME_DYE")).setDisplayname(name).lore("&7You will see" + l)
					.glow(true).buildItem());
		}
	}

	public void removeOldLogs() {
		ArrayList<String> removeLogs = new ArrayList<>();
		Set<String> data = Files.log.getConfigurationSection("Logs").getKeys(false);
		ArrayList<String> allLogs = new ArrayList<>(data);
		for(String log : allLogs) {
			Date now_date = new Date();
			Date log_date = Util.convertToDate(log);
			assert log_date != null;
			if(TimeUnit.MILLISECONDS.toDays(now_date.getTime() - log_date.getTime()) > Files.log.getInt("Logs.keep")) {
				removeLogs.add(log);
			}
		}

		for(String log : removeLogs) {
			Files.log.set("Logs." + log, null);
		}
		Files.savelog();

	}
}
