package me.tiskua.rankgrant.main;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.tiskua.rankgrant.GUI.ClickEvent;
import me.tiskua.rankgrant.GUI.GUIS;
import me.tiskua.rankgrant.utils.Commands;
import me.tiskua.rankgrant.utils.Util;

public class Main extends JavaPlugin implements Listener{

	GUIS gui = new GUIS(this);
	
	ClickEvent click = new ClickEvent(gui, this);
	Commands cmd = new Commands(this, gui);
//	public LuckPerms luckperms;

	@Override
	public void onEnable() {
		Files.base(this);
		this.getServer().getPluginManager().registerEvents(click, this);
		this.getServer().getPluginManager().registerEvents(this, this);
		
		
		this.getCommand("Grant").setExecutor(cmd);
		this.getCommand("Grantadmin").setExecutor(cmd);
		
		createGUIS();
		(new UpdateChecker(this, 97146)).getLatestVersion((version) -> {
			if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
				this.getLogger().log(Level.INFO, "You are running the latest version of RankGrant!");
			} else {
				this.getLogger().log(Level.WARNING, "------------------------------------------");
				this.getLogger().log(Level.WARNING, " ");
				this.getLogger().log(Level.WARNING, "An update is available for RankGrant!");
				this.getLogger().log(Level.WARNING, "Download it here: https://www.spigotmc.org/resources/rank-grant.97146");
				this.getLogger().log(Level.WARNING, " ");
				this.getLogger().log(Level.WARNING, "------------------------------------------");
			}

		});
		
//		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
//		if (provider != null) {
//		    provider.getProvider();
//		    luckperms = provider.getProvider();
//		} 
		
		
	}
	
	
	
	@EventHandler
	public void checkLatestVersion(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission("rankgrant.update")) return;
		
		(new UpdateChecker(this, 97146)).getLatestVersion((version) -> {
			if (!(this.getDescription().getVersion().equalsIgnoreCase(version))) {
				player.sendMessage(Util.format("&7------------------------------------------"));
				player.sendMessage(" ");
				player.sendMessage(Util.format("&3&lAn update is available for RankGrant!"));
				player.sendMessage(Util.format("&bDownload it here: &7https://www.spigotmc.org/resources/rank-grant.97146"));
				player.sendMessage(" ");
				player.sendMessage(Util.format("&7------------------------------------------"));
			}

		});
	}

	public void createGUIS() {
		gui.createDurationGUI();
		gui.createReasonGUI();
		gui.createRankGui();
		gui.createPermissionsGui();
		gui.createTrueFalseGui();
		gui.createConfirmGUI();
	}
}
