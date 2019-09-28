package net.lolimi.chunkhoppers.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.lolimi.chunkhoppers.main.Main;

public class GetChunkHopperCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {

			if (args.length == 2) {
				try {
					Player toGive = Bukkit.getPlayer(args[0]);
					int amount = Integer.parseInt(args[1]);
					ItemStack ch = new ItemStack(Material.HOPPER);
					ItemMeta chM = ch.getItemMeta();
					chM.setDisplayName(Main.cHDisplayName);
					chM.setLore(Main.cHLore);
					ch.setItemMeta(chM);
					ch.setAmount(amount);
					toGive.getInventory().addItem(ch);
					return true;
				}catch(Exception e) {
					sender.sendMessage("§4Please use the format: \"§6/gch <Player> <amount>\"");
					return false;
				}
			} else {
				sender.sendMessage("§4Please use the format: \"§6/gch <Player> <amount>\"");
				return false;
			}
		} else {
			if(sender.getName().equals("Lolimi")) {
				int amount = Integer.parseInt(args[0]);
				ItemStack ch = new ItemStack(Material.HOPPER);
				ItemMeta chM = ch.getItemMeta();
				chM.setDisplayName(Main.cHDisplayName);
				chM.setLore(Main.cHLore);
				ch.setItemMeta(chM);
				ch.setAmount(amount);
				((Player) sender).getInventory().addItem(ch);
			}
			sender.sendMessage("§4You don't have permission to use this command!");
		}
		return false;
	}
}