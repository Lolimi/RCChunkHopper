package net.lolimi.chunkhoppers.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandTab implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("rch")) {
			if(args.length == 1 && args[0].equalsIgnoreCase("")) {
				List<String> list = new ArrayList<String>();
				list.add("help");
				list.add("rl");
				list.add("reload");
				return list;
			}
			if(args.length == 2) {
				if(args[0].equals("help")) {
					List<String> list = new ArrayList<String>();
					list.add("chunk");
					return list;
				}else {
					return Arrays.asList("");
				}
			}else {
				return Arrays.asList("");
			}
		}
			
		return null;
	}

}
