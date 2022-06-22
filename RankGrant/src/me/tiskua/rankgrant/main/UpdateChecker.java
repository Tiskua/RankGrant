package me.tiskua.rankgrant.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class UpdateChecker implements Listener{

	private Main main;
	private int resourceID;
	
	public UpdateChecker(Main main, int resourceID) {
		this.main = main;
		this.resourceID = resourceID;
		
	}
	
	public void getLatestVersion(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
            try (InputStream inputstream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceID).openStream();
            Scanner s = new Scanner(inputstream)) {
            if (s.hasNext()) consumer.accept(s.next());
        } catch (IOException e) {
                main.getLogger().info("Update checker is broken, can't find an update!" + e.getMessage());
            }
        });
    }
	
	
}
