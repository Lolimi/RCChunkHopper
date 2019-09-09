package lolimi.chunkHoppers.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lolimi.chunkHoppers.chunkHoppers.ChunkHopper;
import lolimi.chunkHoppers.main.Main;

public class RmvFailedChunkHopperCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arcmdg1, String label, String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;
			ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(p.getLocation());
			if(ch!=null) {
				if (p.hasPermission("chunkHopper.removeFromData")||p.hasPermission("rchoppers.admin")) {
					Main.getPlugin().rmvChunkHopper(ch.getLocation());
					ch.remove();
					p.sendMessage("§3[§6RCHoppers§3]"+ChatColor.DARK_GREEN+"You have successfully removed the §6Chunk Hopper §2at §b"+ch.getLocation()+"§2!");
						
				} else {
					p.sendMessage(ChatColor.DARK_RED + "You don't have permission to execute this command!");
				}
			} else {
				p.sendMessage("§3[§6RCHoppers§3] §cThere is no Chunk Hopper in this chunk");
			}
		}
		return false;
	}
}
