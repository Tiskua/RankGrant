package me.tiskua.rankgrant.main;

import me.tiskua.rankgrant.Debug.Debugger;
import me.tiskua.rankgrant.GUI.GUIEvents;
import me.tiskua.rankgrant.GUI.GUIS;
import me.tiskua.rankgrant.Grant.Grant;
import me.tiskua.rankgrant.Logs.LogEvents;
import me.tiskua.rankgrant.Logs.Logs;
import me.tiskua.rankgrant.command.Commands;
import me.tiskua.rankgrant.command.TabComplete;
import me.tiskua.rankgrant.mysql.MySQL;
import me.tiskua.rankgrant.mysql.SQLGetter;
import me.tiskua.rankgrant.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener{

	public MySQL SQL;
	public SQLGetter data;	
	public GUIEvents click;
	public Commands cmd;
	public GUIS guis;
	public Logs logs;
	public LogEvents logevent;
	public Grant grant;
	public Debugger debug_gui;
	private static Main main;

	@Override
	public void onEnable() {

		Main.setMainInstance(this);
		Files.base(this);
		
		this.logs = new Logs();
		this.logevent = new LogEvents();
		this.debug_gui = new Debugger();
		this.guis = new GUIS();
		this.SQL = new MySQL();
		this.data = new SQLGetter();
		this.cmd = new Commands();
		this.click = new GUIEvents();
		this.grant = new Grant();



//		try {
//			SQL.connect();
//		} catch (ClassNotFoundException | SQLException e) {
////			this.getLogger().log(Level.INFO, "Database did not connect! If you are not using a database then ignore this.");
//		}
//
//		if(SQL.isConnected()) {
////			this.getLogger().log(Level.INFO, "Database has successfully connected!");
//			data.createTable();
//		}
		
		this.getServer().getPluginManager().registerEvents(logevent, this);
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

		logs.removeOldLogs();
	}

	
	@Override
	public void onDisable() {
		SQL.disconnect();
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
		this.getLogger().log(Level.INFO, "Creating GUIS");
		guis.createMainGUI();
		guis.createRankGUI();
		guis.createPermissionsGUI();
		guis.createDurationGUI();
		guis.createReasonGUI();
		guis.createConfirmGUI();
		guis.createLogGUI();

		debug_gui.checkSound();
		debug_gui.debugMessage(Bukkit.getConsoleSender());
	}
	
	public static void setMainInstance(Main m) {
		main = m;
	}
	
	public static Main getMainInstance() {
		return main;
	}
}
