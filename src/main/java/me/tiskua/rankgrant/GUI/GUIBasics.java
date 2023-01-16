package me.tiskua.rankgrant.GUI;

import me.tiskua.rankgrant.main.*;
import me.tiskua.rankgrant.utils.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

import java.util.*;

public class GUIBasics {

	
	public void setBorders(Inventory inv) {
		ItemStack innerBorder = new ItemCreator(Files.config.getString("Border.Inner.item")).setDisplayname(" ").formatItem().buildItem();
		ItemStack outerBorder = new ItemCreator(Files.config.getString("Border.Outer.item")).setDisplayname(" ").formatItem().buildItem();

		int size = inv.getSize();
		
		//Inner Border
		for(int i = 0; i <size; i++) 
			inv.setItem(i, innerBorder);
		
		//Outer Border
		for(int i =0; i <9; i++) 
			inv.setItem(i, outerBorder);
		
		for(int i =size-9; i <size; i++) 
			inv.setItem(i, outerBorder);
		
		for(int i = 1; i < (size/9)-1; i++) {
			inv.setItem(9*i, outerBorder);
			inv.setItem(9*i+8, outerBorder);
		}
	}
	

	public void addBackButtons() {
		for(Map.Entry<Inventory, Inventory> entry : GUIManager.getBackButtonInvs().entrySet()) {
			Inventory invs = entry.getKey();
			invs.setItem(invs.getSize()-9, new ItemCreator(Material.ARROW).setDisplayname("&c&lBack").buildItem());
		}
		
	}
}
