package lolimi.chunkHoppers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lolimi.chunkHoppers.main.Main;


public class HelpCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length == 0) {
			sender.sendMessage("");
			sender.sendMessage("");
			sender.sendMessage("");
			sender.sendMessage(Main.prefix + "§cv" +Main.getPlugin().getDescription().getVersion()+" §4Plugin author: §bLolimi");
			sender.sendMessage("§4§lGuides:");
			sender.sendMessage("§3You can buy all types of §6RCHoppers §3in the token shop");
			sender.sendMessage("§3How to use the §6Chunk Hoppers§3: §2/rch help chunk");
			sender.sendMessage("§3To access your §6Chunk Hopper§3 settings shift rightclick it");
		
	//help
		} else if (args.length == 2) {
			if (args[0].equals("help")) {
				
	//chunk
				} if (args[1].equalsIgnoreCase("chunk")) {
					sender.sendMessage("");
					sender.sendMessage("");
					sender.sendMessage("");
					sender.sendMessage(Main.prefix + "§4How to use the §6Chunk Hopper§4:");
					sender.sendMessage("§3For using a §6Chunk Hopper §3you simply place it down.");
					sender.sendMessage("§3Without changing any settings, it picks up all items inside the");
					sender.sendMessage("§3chunk it is placed in and transfers them like a normal hopper.");
					sender.sendMessage("§3To change your settings you sneak-rightclick the hopper:");
					sender.sendMessage("§3You will get a §6GUI §3in wich you can set up two types of filters.");
					sender.sendMessage("§3The §6pickup filter §3and the §6selling filter§3.");
					sender.sendMessage("§3Each of them have a §6whitelist §3and a §6blacklist §3function.");
					sender.sendMessage("§3The selling filter overrides the pickup filter!");
					sender.sendMessage("§3Also in the §6GUI §3it shows you how much money you have made.");
					sender.sendMessage("§3To remove a §6Chunk Hopper §3you always have to do it using the §6GUI§3.");
					sender.sendMessage("§6Chunk Hoppers §3can be turned off by powering them with redstone.");
					sender.sendMessage("§cNote that: §3Always put a normal container (e.g. hopper) between a ");
					sender.sendMessage("§6Chunk Hopper §3and any of the other §6RCHoppers §3(also §6AutoCrafters§3)!");
				}  else {
					sender.sendMessage("");
					sender.sendMessage("");
					sender.sendMessage("");
					sender.sendMessage(Main.prefix + "§cv" +Main.getPlugin().getDescription().getVersion()+" §4Plugin author: §bLolimi");
					sender.sendMessage("§4§lGuides:");
					sender.sendMessage("§3You can buy all types of §6RCHoppers §3in the token shop");
					sender.sendMessage("§3How to use the §6Chunk Hoppers§3: §2/rch help chunk");
					sender.sendMessage("§3To access your §6Chunk Hopper§3 settings shift rightclick it");
				}
			
		} else {
			sender.sendMessage("");
			sender.sendMessage("");
			sender.sendMessage("");
			sender.sendMessage(Main.prefix + "§cv" +Main.getPlugin().getDescription().getVersion()+" §4Plugin author: §bLolimi");
			sender.sendMessage("§4§lGuides:");
			sender.sendMessage("§3You can buy all types of §6RCHoppers §3in the token shop");
			sender.sendMessage("§3How to use the §6Chunk Hoppers§3: §2/rch help chunk");
			sender.sendMessage("§3To access your §6Chunk Hopper§3 settings shift rightclick it");
		}
		return false;
	}
}
