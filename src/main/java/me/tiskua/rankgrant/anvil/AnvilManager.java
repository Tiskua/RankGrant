package me.tiskua.rankgrant.anvil;

import me.tiskua.rankgrant.GUI.GUIManager;
import me.tiskua.rankgrant.GUI.GUIS;
import me.tiskua.rankgrant.Grant.GrantManager;
import me.tiskua.rankgrant.Logs.Logs;
import me.tiskua.rankgrant.Logs.LogsManager;
import me.tiskua.rankgrant.main.Main;
import me.tiskua.rankgrant.utils.ItemCreator;
import me.tiskua.rankgrant.utils.Util;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnvilManager {


	Logs logs =Main.getMainInstance().logs;
	GUIS gui = Main.getMainInstance().guis;
	LogsManager logsmanager = logs.logsmanager;
	public void logsFilterAnvil(Player p) {
		new AnvilGUI.Builder()
				.onComplete((completion) -> {
					if (logsmanager.isFilteringGranterName())
						logsmanager.setFilterGranterName(completion.getText());

					else if (logsmanager.isFilteringTargetName())
						logsmanager.setFilterTargetName(completion.getText());

					logs.updateLogsInv();
					return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(GUIManager.getLogGUI()));

				})
				.itemLeft(new ItemCreator(Material.PAPER).buildItem())
				.text("Player's Name")
				.title("Player Filer")
				.plugin(Main.getMainInstance())
				.open(p);
	}

	public void customReason(Player p) {
		new AnvilGUI.Builder()
				.onComplete((completion) -> {
					gui.customReason.remove(p);
					GrantManager.setGrantReason(completion.getText());
					return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(GUIManager.getConfirmGUI()));

				})
				.itemLeft(new ItemCreator(Material.PAPER).buildItem())
				.text("Reason")
				.title("Custom Grant Reason")
				.plugin(Main.getMainInstance())
				.open(p);
	}
	public void customDuration(Player p) {
		new AnvilGUI.Builder()
				.onComplete((completion) -> {
					List<String> timeOptions = new ArrayList<>(Arrays.asList("year", "month", "week", "day", "hour",
							"minute", "second", "y", "M", "w", "d", "h", "m", "s"));
					String[] parts = completion.getText().split(" ");
					if (Util.isInteger(parts[0]) && Integer.parseInt(parts[0]) > 0) {
						for (String time : timeOptions) {
							if (parts[1].contains(time)) {
								GrantManager.setGrantDuration(Util.durationToSeconds(completion.getText()));
								gui.customDuration.remove(p);
								return Collections.singletonList(AnvilGUI.ResponseAction
										.openInventory(GUIManager.getReasonGUI()));
							}
						}
					}
					return Collections.singletonList(AnvilGUI.ResponseAction.
							replaceInputText("Invalid Duration! (ex: 1 month)"));
				})
				.itemLeft(new ItemCreator(Material.PAPER).buildItem())
				.text("Duration")
				.title("Custom Grant Duration")
				.plugin(Main.getMainInstance())
				.open(p);
	}
}