package me.tiskua.rankgrant.Logs;

import me.tiskua.rankgrant.GUI.GUIManager;
import me.tiskua.rankgrant.anvil.AnvilManager;
import me.tiskua.rankgrant.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class LogEvents implements Listener{

	Logs logs =Main.getMainInstance().logs;
	LogsManager logsmanager = logs.logsmanager;
	AnvilManager anvil = new AnvilManager();


	@EventHandler
	public void logPage(InventoryClickEvent event) {
		if(!event.getInventory().equals(GUIManager.getLogGUI())) return;
		if(event.getCurrentItem() == null) return;

		event.setCancelled(true);
		int slot = event.getSlot();
		Player player = (Player) event.getWhoClicked();


		if(slot == GUIManager.getLogGUI().getSize()-1 && event.getCurrentItem().getType() == Material.ARROW) {
			logsmanager.setPage(logsmanager.getPage() + 1);
			logs.updateLogsInv();
		}
		else if(slot == GUIManager.getLogGUI().getSize()-9 && event.getCurrentItem().getType() == Material.ARROW) {
			logsmanager.setPage(logsmanager.getPage() - 1);
			logs.updateLogsInv();
		}
		else if(slot == 47) {
			logsmanager.setShowExpired(!logsmanager.isShowExpired());

			logs.updateLogsInv();
		}
		else if(slot==48) {
			logsmanager.setShowOld(!logsmanager.isShowOld());

			logs.updateLogsInv();
		}
		else if(slot==49) {
			if(event.getClick() == ClickType.SHIFT_RIGHT) {
				logsmanager.setFilterTargetName("No Current Filter");
				logs.updateLogsInv();
				return;
			}
			logsmanager.setFilteringGranterName(true);
			anvil.logsFilterAnvil(player);

		}
		else if(slot==50) {
			if(event.getClick() == ClickType.SHIFT_RIGHT) {
				logsmanager.setFilterGranterName("No Current Filter");
				logs.updateLogsInv();
				return;
			}
			logsmanager.setFilteringGranterName(true);
			anvil.logsFilterAnvil(player);

		}
		else if(slot==51) {
			logsmanager.setSort(logsmanager.getSort()+ 1);
			if(logsmanager.getSort() > 4) logsmanager.setSort(0);
			logs.updateLogsInv();

		}
		player.updateInventory();
	}

	@EventHandler
	public void close(InventoryCloseEvent event) {
		if(!event.getInventory().equals(GUIManager.getLogGUI())) return;
		logsmanager.setPage(0);
		logsmanager.setFilterTargetName("No Current Filter");
		logsmanager.setFilterGranterName("No Current Filter");
		logsmanager.setShowExpired(true);
		logsmanager.setShowOld(true);
		logsmanager.setSort(0);
		logs.updateLogsInv();
	}
}