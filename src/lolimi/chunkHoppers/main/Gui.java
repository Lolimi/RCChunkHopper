package lolimi.chunkHoppers.main;

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

import lolimi.chunkHoppers.chunkHoppers.ChunkHopper;

public class Gui {

	public static Inventory getSettingsTabGui(Location loc) {
		
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§3Your Chunk Hopper Settings");
		ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(loc);
		
		if(ch.getLevel() == 1) {
			ItemStack upgrade = new ItemStack(Material.GOLD_INGOT);
			ItemStack reset = new ItemStack(Material.BARRIER);
			
			ItemMeta upgradeM = upgrade.getItemMeta();
			ItemMeta resetM = reset.getItemMeta();
			
			upgradeM.setDisplayName("§aClick here to upgrade this §6RCChunkHopper");
			resetM.setDisplayName("§4Click here to break this hopper");
			upgrade.setItemMeta(upgradeM);
			reset.setItemMeta(resetM);
			
			inv.setItem(1, upgrade);
			inv.setItem(3, reset);
			return inv;
		}else if(ch.getLevel() == 2) {
			ItemStack upgrade = new ItemStack(Material.GOLD_INGOT);
			ItemStack filter = new ItemStack(Material.BOOK);
			ItemStack reset = new ItemStack(Material.BARRIER);
			
			ItemMeta upgradeM = upgrade.getItemMeta();
			ItemMeta filterM = filter.getItemMeta();
			ItemMeta resetM = reset.getItemMeta();
			
			upgradeM.setDisplayName("§aClick here to upgrade this §6RCChunkHopper");
			filterM.setDisplayName("§3Click here to set up a filter for what items should be picked up");
			resetM.setDisplayName("§4Click here to break this hopper");
			upgrade.setItemMeta(upgradeM);
			filter.setItemMeta(filterM);
			reset.setItemMeta(resetM);
			
			inv.setItem(0, upgrade);
			inv.setItem(2, filter);
			inv.setItem(4, reset);
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

		filterM.setDisplayName(ChatColor.DARK_AQUA + "Click here to set up a filter for what items should be picked up");
		sFilterM.setDisplayName(ChatColor.DARK_AQUA+"Click here to set up a filter for what items should be sold");
		resetM.setDisplayName(ChatColor.DARK_RED + "Click here to break this hopper");
		soldM.setDisplayName(ChatColor.GOLD + "This Chunk Hopper has sold:");

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

	public static Inventory getNormalFilterGui(Location loc){
		Inventory inv = Bukkit.createInventory(null, 9 * 6, ChatColor.DARK_AQUA + "Set up your filter here");
		ItemStack i1 = new ItemStack(Material.BOOK);
		ItemStack i2 = new ItemStack(Material.EMERALD_BLOCK);
		ItemStack i3 = new ItemStack(Material.REDSTONE_BLOCK);
		ItemStack i4 = new ItemStack(Material.STAINED_CLAY, 1, (short) 0);
		ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(loc);
		if(!ch.isNormalWhitelist())
			i4.setDurability((short) 15);
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		ItemMeta m3 = i3.getItemMeta();
		ItemMeta m4 = i4.getItemMeta();
		
		m1.setDisplayName(ChatColor.DARK_AQUA+"In here you can set a whitelist or a blacklist of max 45 item types.");
		m2.setDisplayName(ChatColor.DARK_GREEN+"To add an item, click it in your inventory");
		m3.setDisplayName(ChatColor.DARK_RED+"To remove an item, click it in the filter inventory");
		if(i4.getDurability() == (short) 0)
			m4.setDisplayName(ChatColor.GRAY+"Whitelist");
		else
			m4.setDisplayName(ChatColor.DARK_GRAY+"Blacklist");
		
		List<String> l1 = Arrays.asList(ChatColor.DARK_AQUA+"Blacklisted items will not get picked up.", ChatColor.DARK_AQUA+"In whitelist mode only set items will get picked up");
		List<String> l4 = Arrays.asList(ChatColor.GRAY+"Only picks up items set up in this filter", ChatColor.GRAY+"Click to change");
		if(i4.getDurability() == (short) 15)
			l4 = Arrays.asList(ChatColor.DARK_GRAY+"Picks up all items but the ones set up in this filter", ChatColor.DARK_GRAY+"Click to change");
		m1.setLore(l1);
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
	
	public static Inventory getShSettingsGui() {
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§4Set sorter by placing an item");
		ItemStack sorterExplain = new ItemStack(Material.HOPPER);
		ItemMeta explain = sorterExplain.getItemMeta();
		explain.setDisplayName("§6How to use the sorter:");
		List<String> lore = Arrays.asList("§bPlace the item you want to sort out to the right of this one",
				"§bThen take it back out to set the sorter",
				"§bThat item will always goes to the inventory below the hopper",
				"§bThe other items go to the inventory the hopper is facing.");
		explain.setLore(lore);
		sorterExplain.setItemMeta(explain);

		inv.setItem(0, sorterExplain);
		
		return inv;
	}

	public static Inventory getChAdminGui(Location loc) {
		ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(loc);
		Inventory inv = Bukkit.createInventory(null, 9, "§4Admin Chunk Hopper settings: " + ch.getOwnerName());
		ItemStack whitelist;
		boolean isWhitelist = ch.isNormalWhitelist();
		if(isWhitelist)
			whitelist = new ItemStack(Material.STAINED_CLAY, 1, (short) 0);
		else
			whitelist = new ItemStack(Material.STAINED_CLAY, 1, (short) 15);
		ItemMeta wm = whitelist.getItemMeta();
		if(isWhitelist)
			wm.setDisplayName("§fWhitelist: click to change");
		else
			wm.setDisplayName("§7Blacklist: click to change");
		whitelist.setItemMeta(wm);
		inv.setItem(0, whitelist);
		
		ItemStack rmv = new ItemStack(Material.BARRIER);
		ItemMeta rmvm = rmv.getItemMeta();
		rmvm.setDisplayName("§4Click here to rmv and break this hopper");
		rmv.setItemMeta(rmvm);
		inv.setItem(2, rmv);
		
		ItemStack clearFilter = new ItemStack(Material.GLASS);
		ItemMeta cfm = clearFilter.getItemMeta();
		cfm.setDisplayName("§cClick here to clear the filter");
		clearFilter.setItemMeta(cfm);
		inv.setItem(6, clearFilter);
		
		ItemStack seeInv = new ItemStack(Material.BOOK);
		ItemMeta sim = seeInv.getItemMeta();
		sim.setDisplayName("§3Click here to open the hoppers inventory");
		seeInv.setItemMeta(sim);
		inv.setItem(8, seeInv);
		
		return inv;
	}
	
	public static Inventory getSellingFillterGui(Location loc) {
		Inventory inv = Bukkit.createInventory(null, 9*6, ChatColor.DARK_AQUA+ "Setup your Selling Filter here");
		
		ItemStack i1 = new ItemStack(Material.BOOK);
		ItemStack i2 = new ItemStack(Material.EMERALD_BLOCK);
		ItemStack i3 = new ItemStack(Material.REDSTONE_BLOCK);
		ItemStack i4 = new ItemStack(Material.STAINED_CLAY, 1, (short) 0);
		ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(loc);
		if(!ch.isSellingWhitelist())
			i4.setDurability((short) 15);
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		ItemMeta m3 = i3.getItemMeta();
		ItemMeta m4 = i4.getItemMeta();
		
		m1.setDisplayName(ChatColor.DARK_AQUA+"In here you can set a whitelist or a blacklist for selling of max 45 item types.");
		m2.setDisplayName(ChatColor.DARK_GREEN+"To add an item, click it in your inventory");
		m3.setDisplayName(ChatColor.DARK_RED+"To remove an item, click it in the filter inventory");
		if(i4.getDurability() == (short) 0)
			m4.setDisplayName(ChatColor.GRAY+"Whitelist");
		else
			m4.setDisplayName(ChatColor.DARK_GRAY+"Blacklist");
		
		List<String> l1 = Arrays.asList(ChatColor.DARK_AQUA+"Blacklisted items will not get sold.", ChatColor.DARK_AQUA+"In whitelist mode only set items will get sold");
		List<String> l4 = Arrays.asList(ChatColor.GRAY+"Only sells items set up in this filter", ChatColor.GRAY+"Click to change");
		if(i4.getDurability() == (short) 15)
			l4 = Arrays.asList(ChatColor.DARK_GRAY+"Sells all items but the ones set up in this filter", ChatColor.DARK_GRAY+"Click to change");
		m1.setLore(l1);
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
