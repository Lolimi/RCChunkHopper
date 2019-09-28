package net.lolimi.chunkhoppers.main;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.lolimi.chunkhoppers.chunkhoppers.AbstractChunkHopper;

public class Gui {

	public static Inventory getSettingsTabGui(Location loc) {
		
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, Main.chSettingsInvName);
		AbstractChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(loc);
		
		if(ch.getLevel() == 1) {
			ItemStack upgrade = new ItemStack(Material.GOLD_INGOT);
			ItemStack reset = new ItemStack(Material.BARRIER);
			
			ItemMeta upgradeM = upgrade.getItemMeta();
			ItemMeta resetM = reset.getItemMeta();
			
			upgradeM.setDisplayName(Main.toUpgradeItemName);
			resetM.setDisplayName(Main.toBreakItemName);
			upgrade.setItemMeta(upgradeM);
			reset.setItemMeta(resetM);
			
			inv.setItem(1, upgrade);
			inv.setItem(3, reset);
			return inv;
		}else if(ch.getLevel() == 2 && Main.useSell) {
			ItemStack upgrade = new ItemStack(Material.GOLD_INGOT);
			ItemStack filter = new ItemStack(Material.BOOK);
			ItemStack reset = new ItemStack(Material.BARRIER);
			
			ItemMeta upgradeM = upgrade.getItemMeta();
			ItemMeta filterM = filter.getItemMeta();
			ItemMeta resetM = reset.getItemMeta();
			
			upgradeM.setDisplayName(Main.toUpgradeItemName);
			filterM.setDisplayName(Main.toFilterItemName);
			resetM.setDisplayName(Main.toBreakItemName);
			upgrade.setItemMeta(upgradeM);
			filter.setItemMeta(filterM);
			reset.setItemMeta(resetM);
			
			inv.setItem(0, upgrade);
			inv.setItem(2, filter);
			inv.setItem(4, reset);
			return inv;
		}else if(ch.getLevel() == 2) {
			ItemStack filter = new ItemStack(Material.BOOK);
			ItemStack reset = new ItemStack(Material.BARRIER);
			
			ItemMeta filterM = filter.getItemMeta();
			ItemMeta resetM = reset.getItemMeta();
			
			filterM.setDisplayName(Main.toFilterItemName);
			resetM.setDisplayName(Main.toBreakItemName);
			filter.setItemMeta(filterM);
			reset.setItemMeta(resetM);
			
			inv.setItem(1, filter);
			inv.setItem(3, reset);
			return inv;
		}
		ItemStack filter = new ItemStack(Material.BOOK);
		ItemStack sellingFilter = new ItemStack(Material.DIAMOND);
		ItemStack reset = new ItemStack(Material.BARRIER);
		ItemStack sold = new ItemStack(Material.GOLD_INGOT);

		ItemMeta filterM = filter.getItemMeta();
		ItemMeta sFilterM = sellingFilter.getItemMeta();
		ItemMeta resetM = reset.getItemMeta();
		ItemMeta soldM = sold.getItemMeta();

		List<String> filterLore = Arrays.asList(ChatColor.DARK_AQUA + "The selling filter overwrites this");
		List<String> soldLore = Arrays.asList(ChatColor.GOLD + "" + Main.getPlugin().isInChunkHopperChunk(loc).getSold());

		filterM.setDisplayName(Main.toFilterItemName);
		sFilterM.setDisplayName(Main.toSellingFilterItemName);
		resetM.setDisplayName(Main.toBreakItemName);
		soldM.setDisplayName(Main.soldItemName);

		filterM.setLore(filterLore);
		soldM.setLore(soldLore);

		filter.setItemMeta(filterM);
		sellingFilter.setItemMeta(sFilterM);
		reset.setItemMeta(resetM);
		sold.setItemMeta(soldM);

		inv.setItem(0, filter);
		inv.setItem(1, sellingFilter);
		inv.setItem(4, reset);
		inv.setItem(2, sold);

		return inv;
	}

	@SuppressWarnings("deprecation")
	public static Inventory getNormalFilterGui(Location loc){
		Inventory inv = Bukkit.createInventory(null, 9 * 6, Main.normalFilterInvName);
		ItemStack i1 = new ItemStack(Material.BOOK);
		ItemStack i2 = new ItemStack(Material.EMERALD_BLOCK);
		ItemStack i3 = new ItemStack(Material.REDSTONE_BLOCK);
		ItemStack i4;
		if(Main.legacy)
			i4 = new ItemStack(Material.getMaterial("STAINED_CLAY"), 1, (short) 0);
		else 
			i4 = new ItemStack(Material.WHITE_TERRACOTTA);
		AbstractChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(loc);
		
		if(!ch.isNormalWhitelist() && Main.legacy)
			i4.setDurability((short) 15);
		else if(!ch.isNormalWhitelist() && !Main.legacy)
			i4.setType(Material.BLACK_TERRACOTTA);
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		ItemMeta m3 = i3.getItemMeta();
		ItemMeta m4 = i4.getItemMeta();
		
		m1.setDisplayName(Main.explainBookItemNameNormal);
		m2.setDisplayName(Main.explainEmeraldItemName);
		m3.setDisplayName(Main.explainRedstoneItemName);
		if(Main.legacy)
			if(i4.getDurability() == (short) 0)
				m4.setDisplayName(ChatColor.GRAY+"Whitelist");
			else
				m4.setDisplayName(ChatColor.DARK_GRAY+"Blacklist");
		else
			if(i4.getType().equals(Material.WHITE_TERRACOTTA))
				m4.setDisplayName(ChatColor.GRAY+"Whitelist");
			else
				m4.setDisplayName(ChatColor.DARK_GRAY+"Blacklist");
		List<String> l1 = Main.explainBookItemLoreNormal;
		List<String> l2 = Main.explainEmeraldItemLore;
		List<String> l3 = Main.explainRedstoneItemLore;
		List<String> l4 = Main.whiteListItemLoreNormal;
		if(Main.legacy)
			if(i4.getDurability() == (short) 15)
				l4 = Main.blackListItemLoreNormal;
			else {}
		else
			if(i4.getType().equals(Material.BLACK_TERRACOTTA))
				l4 = Main.blackListItemLoreNormal;
		m1.setLore(l1);
		m2.setLore(l2);
		m3.setLore(l3);
		m4.setLore(l4);
		i1.setItemMeta(m1);
		i2.setItemMeta(m2);
		i3.setItemMeta(m3);
		i4.setItemMeta(m4);
		
		inv.setItem(1, i1);
		inv.setItem(3, i2);
		inv.setItem(5, i3);
		inv.setItem(7, i4);
		
		ItemStack[] filter = ch.getNormalFilter();
		for(int i = 0; i<9*5; i++) {
			try {
				inv.setItem(i+9, filter[i]);
			}catch(NullPointerException e) {
			}
		}
		return inv;
	}

//	@SuppressWarnings("deprecation")
//	public static Inventory getChAdminGui(Location loc) {
//		ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(loc);
//		Inventory inv = Bukkit.createInventory(null, 9, "§4Admin Chunk Hopper settings: " + ch.getOwnerName());
//		ItemStack whitelist;
//		boolean isWhitelist = ch.isNormalWhitelist();
//		if(isWhitelist)
//			if(Main.legacy)
//				whitelist = new ItemStack(Material.getMaterial("STAINED_CLAY"), 1, (short) 0);
//			else
//				whitelist = new ItemStack(Material.WHITE_TERRACOTTA);
//		else
//			if(Main.legacy)
//				whitelist = new ItemStack(Material.getMaterial("STAINED_CLAY"), 1, (short) 15);
//			else 
//				whitelist = new ItemStack(Material.BLACK_TERRACOTTA);
//		ItemMeta wm = whitelist.getItemMeta();
//		if(isWhitelist)
//			wm.setDisplayName("§fWhitelist: click to change");
//		else
//			wm.setDisplayName("§7Blacklist: click to change");
//		whitelist.setItemMeta(wm);
//		inv.setItem(0, whitelist);
//		
//		ItemStack rmv = new ItemStack(Material.BARRIER);
//		ItemMeta rmvm = rmv.getItemMeta();
//		rmvm.setDisplayName("§4Click here to rmv and break this hopper");
//		rmv.setItemMeta(rmvm);
//		inv.setItem(2, rmv);
//		
//		ItemStack clearFilter = new ItemStack(Material.GLASS);
//		ItemMeta cfm = clearFilter.getItemMeta();
//		cfm.setDisplayName("§cClick here to clear the filter");
//		clearFilter.setItemMeta(cfm);
//		inv.setItem(6, clearFilter);
//		
//		ItemStack seeInv = new ItemStack(Material.BOOK);
//		ItemMeta sim = seeInv.getItemMeta();
//		sim.setDisplayName("§3Click here to open the hoppers inventory");
//		seeInv.setItemMeta(sim);
//		inv.setItem(8, seeInv);
//		
//		return inv;
//	}
	
	@SuppressWarnings("deprecation")
	public static Inventory getSellingFillterGui(Location loc) {
		Inventory inv = Bukkit.createInventory(null, 9*6, Main.sellingFilterInvName);
		
		ItemStack i1 = new ItemStack(Material.BOOK);
		ItemStack i2 = new ItemStack(Material.EMERALD_BLOCK);
		ItemStack i3 = new ItemStack(Material.REDSTONE_BLOCK);
		ItemStack i4;
		if(Main.legacy)
			i4 = new ItemStack(Material.getMaterial("STAINED_CLAY"), 1, (short) 0);
		else {
			i4 = new ItemStack(Material.WHITE_TERRACOTTA);
		}
		AbstractChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(loc);
		if(!ch.isSellingWhitelist())
			if(Main.legacy)
				i4.setDurability((short) 15);
			else
				i4.setType(Material.BLACK_TERRACOTTA);
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		ItemMeta m3 = i3.getItemMeta();
		ItemMeta m4 = i4.getItemMeta();
		
		m1.setDisplayName(Main.explainBookItemNameSelling);
		m2.setDisplayName(Main.explainEmeraldItemName);
		m3.setDisplayName(Main.explainRedstoneItemName);
		if(Main.legacy)
			if(i4.getDurability() == (short) 0)
				m4.setDisplayName(ChatColor.GRAY+"Whitelist");
			else 
				m4.setDisplayName(ChatColor.DARK_GRAY+"Blacklist");
		else
			if(i4.getType().equals(Material.WHITE_TERRACOTTA))
				m4.setDisplayName(ChatColor.GRAY+"Whitelist");
			else
				m4.setDisplayName(ChatColor.DARK_GRAY+"Blacklist");
		List<String> l1 = Main.explainBookItemLoreSelling;
		List<String> l2 = Main.explainEmeraldItemLore;
		List<String> l3 = Main.explainRedstoneItemLore;
		List<String> l4 = Main.whiteListItemLoreSelling;
		if(Main.legacy)
			if(i4.getDurability() == (short) 15)
				l4 = Main.blackListItemLoreSelling;
			else {}
		else
			if(i4.getType().equals(Material.BLACK_TERRACOTTA))
				l4 = Main.blackListItemLoreSelling;
		m1.setLore(l1);
		m2.setLore(l2);
		m3.setLore(l3);
		m4.setLore(l4);
		i1.setItemMeta(m1);
		i2.setItemMeta(m2);
		i3.setItemMeta(m3);
		i4.setItemMeta(m4);
		
		inv.setItem(1, i1);
		inv.setItem(3, i2);
		inv.setItem(5, i3);
		inv.setItem(7, i4);
		
		ItemStack[] filter = ch.getSellingFilter();
		for(int i = 0; i<9*5; i++) {
			try {
				inv.setItem(i+9, filter[i]);
			}catch(NullPointerException e) {
			}
		}
		
		return inv;
	}
	
}
