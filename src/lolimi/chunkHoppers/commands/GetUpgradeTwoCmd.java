package lolimi.chunkHoppers.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lolimi.chunkHoppers.main.Main;

public class GetUpgradeTwoCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {

			if (args.length == 2) {
				Player toGive = Bukkit.getPlayer(args[0]);
				int amount = Integer.parseInt(args[1]);
				ItemStack ch = Main.upgrade2.clone();
				ch.setAmount(amount);
				toGive.getInventory().addItem(ch);

			} else {
				sender.sendMessage("§4Please use the format: \"§6/gch <Player> <amount>\"");
			}

		} else {
			sender.sendMessage("§4This command is not meant to be used by players!");
		}
		return false;
	}

}