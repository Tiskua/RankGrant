package me.tiskua.rankgrant.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCreator {

	
	Material mat;
	short dur;
	String displayname;
	List<String> lore;
	String stringItem;
	Boolean glow = false;
	
	
	public ItemCreator(Material mat) {
		this.mat = mat;
		
		
	}
	
	public ItemCreator(String sitem) {
		this.stringItem = sitem;
	}
	
	public ItemCreator setDisplayname(String displayname) {
		this.displayname = displayname;
		return this;
	}
	
	public ItemCreator setDurability(short dur) {
		this.dur = dur;
		return this;
	}
	

	
	public ItemCreator lore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	

	
	public ItemStack buildItem() {
		ItemStack item = new ItemStack(this.mat);
		ItemMeta meta = item.getItemMeta();
		
		if(this.displayname != null) meta.setDisplayName(Util.format(this.displayname));
		if(this.lore != null) meta.setLore(lore);
		
		
		
		item.setDurability(dur);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemCreator formatItem() {
		if(this.stringItem.contains(":")) {
			String itemtype[] = this.stringItem.split(":", 0);
			this.mat = Material.valueOf(itemtype[0].toUpperCase());
			this.dur = Short.valueOf(itemtype[1]);

		} else {
			mat = Material.valueOf(stringItem.toUpperCase());
		}
		return this;
	}
	
	
}
