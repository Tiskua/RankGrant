package me.tiskua.rankgrant.main;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.tiskua.rankgrant.GUI.GUIEvents;
import me.tiskua.rankgrant.GUI.GUIS;
import me.tiskua.rankgrant.utils.Commands;
import me.tiskua.rankgrant.utils.TabComplete;
import me.tiskua.rankgrant.utils.Util;

public class Main extends JavaPlugin implements Listener{

	GUIS gui = new GUIS(this);
//	public MySQL SQL;
	
	GUIEvents click = new GUIEvents(gui, this);
	Commands cmd = new Commands(this, gui);

	@Override
	public void onEnable() {
		Files.base(this);
//		
//		this.SQL = new MySQL();
//		
//		try {
//			SQL.connect();
//		} catch (ClassNotFoundException | SQLException e) {
//			this.getLogger().log(Level.INFO, "Database did not connect! If you are not using a database then ignore this.");
//		}
//		
//		if(SQL.isConnected()) {
//			this.getLogger().log(Level.INFO, "Database has successfully connected!");
//		}
		
		this.getServer().getPluginManager().registerEvents(click, this);
		this.getServer().getPluginManager().registerEvents(this, this);
		
		this.getCommand("Grant").setExecutor(cmd);
		this.getCommand("Grantadmin").setExecutor(cmd);
		this.getCommand("Grantadmin").setTabCompleter(new TabComplete());
		(new UpdateChecker(this, 97146)).getLatestVersion((version) -> {
			if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
				this.getLogger().log(Level.INFO, "You are running the latest version of RankGrant!");
			} else {
				this.getLogger().log(Level.WARNING, " ");
				this.getLogger().log(Level.WARNING, "An update is available for RankGrant!");
				this.getLogger().log(Level.WARNING, "Download it here: https://www.spigotmc.org/resources/rank-grant.97146");
				this.getLogger().log(Level.WARNING, " ");
			}

		});
		createGUIS();
	}
	
	@Override
	public void onDisable() {
//		SQL.disconnect();
	}
	
	@EventHandler
	public void checkLatestVersion(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission("rankgrant.update")) return;
		(new UpdateChecker(this, 97146)).getLatestVersion((version) -> {
			if (!(this.getDescription().getVersion().equalsIgnoreCase(version))) {
				player.sendMessage(Util.format("&7------------------------------------------"
						+ "\n &7"
						+ "\n &3&lAn update is available for RankGrant!"
						+ "\n &bDownload it here: &7https://www.spigotmc.org/resources/rank-grant.97146"
						+ "\n &7"
						+ "\n &7------------------------------------------"));
			}
		});
	}

	public void createGUIS() {
		gui.createRankGui();
		gui.createPermissionsGui();
		gui.createDurationGUI();
		gui.createReasonGUI();
		gui.createConfirmGUI();
		gui.createLogGUI();
	}
}
