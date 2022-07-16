package me.tiskua.rankgrant.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.tiskua.rankgrant.GUI.GUIS;


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
		this.lore = Util.formatList(lore);
		return this;
	}

	public ItemCreator glow(Boolean glow) { 
		this.glow = glow;
		return this;
	}



	public ItemStack buildItem() {
		ItemStack item = new ItemStack(Material.BARRIER);
		try {
			item = new ItemStack(this.mat);

		} catch(Exception e) {
			this.displayname = "&c" + this.stringItem + " IS AN INVALID ITEM ID! CHECK THE CONFIG!";
			errorMessage(this.stringItem);
		}
		if(this.glow) item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		if(this.displayname != null) meta.setDisplayName(Util.format(this.displayname));
		if(this.lore != null) meta.setLore(lore);

		item.setDurability(dur);
		item.setItemMeta(meta);
		return item;
	}

	public ItemCreator formatItem() {
		if(this.stringItem.contains(":")) {
			String itemtype[] = this.stringItem.split(":", 0);
			try {
				this.mat = Material.valueOf(itemtype[0].toUpperCase());				

			} catch (Exception e) {
				errorMessage(itemtype[0].toUpperCase() + ":" + itemtype[1]);

			}
			this.dur = Short.valueOf(itemtype[1]);

		} else {
			try {
				this.mat = Material.valueOf(stringItem.toUpperCase());

			} catch(Exception e) {
				errorMessage(stringItem.toUpperCase());
			}
		}

		return this;
	}
	
	
	private void errorMessage(String msg) {
		Bukkit.getConsoleSender().sendMessage(Util.format("[RankGrant] &cAn error has occured while trying to create " + GUIS.error_GUI + "-> "
				+ msg + " is not a valid item id! Some item ids might have changed in newer versions!"));		
	}


}
