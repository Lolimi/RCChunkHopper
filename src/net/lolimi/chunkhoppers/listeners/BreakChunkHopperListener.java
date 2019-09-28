package net.lolimi.chunkhoppers.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.lolimi.chunkhoppers.main.Main;

public class BreakChunkHopperListener implements Listener {

	@EventHandler
	public boolean onChBreak(BlockBreakEvent event) {
		
		Player blockBreaker = event.getPlayer();
		Block brokenBlock = event.getBlock();
		if (brokenBlock.getType().equals(Material.HOPPER)) {
			if(Main.getPlugin().isChunkHopper(brokenBlock.getLocation())) {
				blockBreaker.sendMessage(Main.prefix + "§cPlease use the hopper settings to break your §6Chunk hopper §c(shift right click the hopper)");
				event.setCancelled(true);
			}
		}
		return false;
	}
	
}
