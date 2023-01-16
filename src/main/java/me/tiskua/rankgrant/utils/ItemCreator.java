package me.tiskua.rankgrant.utils;

import me.tiskua.rankgrant.Debug.*;
import me.tiskua.rankgrant.main.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;


public class ItemCreator {

	Material mat;
	int dur;
	String displayname;
	List<String> lore;
	String stringItem;
	Boolean glow = false;
	Debugger debugger = Main.getMainInstance().debug_gui;
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

	public ItemCreator setDurability(int dur) {
		this.dur = dur;
		return this;
	}


	public ItemCreator lore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	public ItemCreator lore(String... s) {
		lore = new ArrayList<>();
		Collections.addAll(lore, s);
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
			if(Util.isLegacy()) item.setDurability((short)dur);
		} catch(Exception e) {
			this.displayname = "&c" + this.stringItem + " IS AN INVALID ITEM ID! CHECK THE CONFIG FILE!";
			debugger.addError("Invalid Item", new ArrayList<>(Arrays.asList(debugger.getCheckingGUI(), this.stringItem)));
		}
		if(this.glow) item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		if(this.displayname != null) meta.setDisplayName(Util.format(this.displayname));
		if(this.lore != null) meta.setLore(Util.formatList(lore));


		item.setItemMeta(meta);
		return item;
	}

	public ItemCreator formatItem() {
		if(this.stringItem.contains(":")) {
			String[] itemtype = this.stringItem.split(":", 0);
			try {
				this.mat = Material.valueOf(itemtype[0].toUpperCase());
				this.dur = Short.parseShort(itemtype[1]);
			} catch (Exception e) {
				debugger.addError("Invalid Item", new ArrayList<>(Arrays.asList(debugger.getCheckingGUI(), this.stringItem)));
			}

		} else {
			try {
				this.mat = Material.valueOf(stringItem.toUpperCase());
			} catch(Exception e) {
				debugger.addError("Invalid Item", new ArrayList<>(Arrays.asList(debugger.getCheckingGUI(), this.stringItem)));
			}
		}

		return this;
	}
	
	public ItemCreator convertColoredBlocksToNew() {
		if(Util.isLegacy()) {
			mat = Material.WOOL;
			return this;
		}
		if(!this.stringItem.equalsIgnoreCase("WOOL")) return this;
		
		if(dur == 0) mat = Material.valueOf("WHITE_WOOL"); 
		if(dur==1) mat = Material.valueOf("ORANGE_WOOL");
		else if(dur==2) mat = Material.valueOf("MAGENTA_WOOL");
		else if(dur==3) mat = Material.valueOf("LIGHT_BLUE_WOOL");
		else if(dur==4) mat = Material.valueOf("YELLOW_WOOL");
		else if(dur==5) mat = Material.valueOf("LIME_WOOL");
		else if(dur==6) mat = Material.valueOf("PINK_WOOL");
		else if(dur==7) mat = Material.valueOf("GRAY_WOOL");
		else if(dur==8) mat = Material.valueOf("LIGHT_GRAY_WOOL");
		else if(dur==9) mat = Material.valueOf("CYAN_WOOL");
		else if(dur==10) mat = Material.valueOf("PURPLE_WOOL");
		else if(dur==11) mat = Material.valueOf("BLUE_WOOL");
		else if(dur==12) mat = Material.valueOf("BROWN_WOOL");
		else if(dur==13) mat = Material.valueOf("GREEN_WOOL");
		else if(dur==14) mat = Material.valueOf("RED_WOOL");
		else if(dur==15) mat = Material.valueOf("BLACK_WOOL");
		else mat = Material.valueOf("WHITE_WOOL");
		return this;
	}
}
