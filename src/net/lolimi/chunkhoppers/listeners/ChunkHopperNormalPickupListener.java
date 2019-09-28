package net.lolimi.chunkhoppers.listeners;

import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import net.lolimi.chunkhoppers.chunkhoppers.AbstractChunkHopper;
import net.lolimi.chunkhoppers.main.Main;

public class ChunkHopperNormalPickupListener implements Listener {
	
	@EventHandler
	public void onNormalPickup(InventoryPickupItemEvent e) {
		if(e.getInventory().getHolder() instanceof Hopper) {
			if(Main.getPlugin().isChunkHopper(e.getInventory().getLocation())) {
				AbstractChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(e.getInventory().getLocation());
				ItemStack[] filter = ch.getNormalFilter();
				if(filter == null) return;
				boolean inFilter = false;
				for(ItemStack i : filter) {
					if(i.getType().equals(e.getItem().getItemStack().getType())) {
						inFilter = true;
						break;
					}
				}if(ch.isNormalWhitelist() && !inFilter) 
					e.setCancelled(true);
				else if(!ch.isNormalWhitelist() && inFilter)
					e.setCancelled(true);
			}
		}
	}

}
