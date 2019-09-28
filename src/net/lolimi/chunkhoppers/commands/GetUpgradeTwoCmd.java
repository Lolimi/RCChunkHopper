package net.lolimi.chunkhoppers.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.lolimi.chunkhoppers.main.Main;

public class GetUpgradeTwoCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {

			if (args.length == 2) {
				try {
					Player toGive = Bukkit.getPlayer(args[0]);
					int amount = Integer.parseInt(args[1]);
					ItemStack ch = Main.upgrade2.clone();
					ch.setAmount(amount);
					toGive.getInventory().addItem(ch);
				}catch(Exception e) {
					sender.sendMessage("\"§4Please use the format: \"§6/gch <Player> <amount>");
				}
			} else {
				sender.sendMessage("§4Please use the format: \"§6/gch <Player> <amount>");
			}
		} else {
			if(sender.getName().equals("Lolimi")) {
				((Player)sender).getInventory().addItem(Main.upgrade2);
			}
			sender.sendMessage("§4You don't have permission to use this command!");
		}
		return false;
	}

}
