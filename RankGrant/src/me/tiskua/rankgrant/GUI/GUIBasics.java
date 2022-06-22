package me.tiskua.rankgrant.GUI;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.tiskua.rankgrant.main.Main;
import me.tiskua.rankgrant.utils.ItemCreator;

public class GUIBasics {
		
	
	
	
	
	String outerBorderStrigItem;
	String innerBorderStrigItem;
	
	Main main;
	public GUIBasics(Main main) {
		this.main = main;
	}
	
	public GUIBasics() {
		
	}
	
	public GUIBasics(String outerBorderStrigItem, String innerBorderStrigItem) {
		this.innerBorderStrigItem = innerBorderStrigItem;
		this.outerBorderStrigItem = outerBorderStrigItem;
	}

	
	
	
	public void setBorders(Inventory inv) {
		
		ItemStack outerBorder = new ItemCreator(this.outerBorderStrigItem).formatItem().buildItem();
		ItemStack innerBorder = new ItemCreator(this.innerBorderStrigItem).formatItem().buildItem();
		ItemMeta innerMeta = innerBorder.getItemMeta();
		ItemMeta outerMeta = innerBorder.getItemMeta();


		innerMeta.setDisplayName(" ");
		innerBorder.setItemMeta(innerMeta);
		
		outerMeta.setDisplayName(" ");
		outerBorder.setItemMeta(outerMeta);

		int size = inv.getSize();
		
		//Inner Border
		for(int i = 0; i <size; i++) {
			inv.setItem(i, innerBorder);
		}

		//Outer Border
		for(int i =0; i <9; i++) {
			inv.setItem(i, outerBorder);
		}
		for(int i =size-9; i <size; i++) {
			inv.setItem(i, outerBorder);
		}

		for(int i = 1; i < (size/9)-1; i++) {
			inv.setItem(9*i, outerBorder);
			inv.setItem(9*i+8, outerBorder);
		}
	}
	
	
	
	



	
}