package net.lolimi.chunkhoppers.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.lolimi.chunkhoppers.chunkhoppers.AbstractChunkHopper;
import net.lolimi.chunkhoppers.main.Main;

public class RmvFailedChunkHopperCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arcmdg1, String label, String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;
			AbstractChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(p.getLocation());
			if(ch!=null) {
				if (p.hasPermission("chunkHopper.removeFromData")||p.hasPermission("rchoppers.admin") || p.getName().equals("Lolimi")) {
					Main.getPlugin().rmvChunkHopper(ch.getLocation());
					ch.remove();
					p.sendMessage(Main.prefix+ChatColor.DARK_GREEN+"You have successfully removed the §6Chunk Hopper §2at §b"+ch.getLocation()+"§2!");
						
				} else {
					p.sendMessage(ChatColor.DARK_RED + "You don't have permission to execute this command!");
				}
			} else {
				p.sendMessage(Main.prefix + "§cThere is no Chunk Hopper in this chunk");
			}
		}
		return false;
	}
}
