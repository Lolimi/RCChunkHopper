package lolimi.chunkHoppers.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lolimi.chunkHoppers.listeners.NewPlaceChunkHopperListener;
import lolimi.chunkHoppers.listeners.NewPlaceChunkHopperListener1_13;
import lolimi.chunkHoppers.main.Main;

public class StaticVariableDebug implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!sender.hasPermission("rchopper.debug")&&!sender.hasPermission("rchoppers.admin")&&!sender.getName().equals("Lolimi")) {
			sender.sendMessage("§4You don't have permission to use this command!");
			return false;
		}
		sender.sendMessage("");
		sender.sendMessage("");
		sender.sendMessage("§6RCHoppers data debug:");
		Location[] locCh = NewPlaceChunkHopperListener.getLocCh();
		Location[] settingsLoc = NewPlaceChunkHopperListener.getSettingsLoc();
		Location[] filterLoc = NewPlaceChunkHopperListener.getFilterLoc();
		//boolean isChange = NewPlaceChunkHopperListener.isChange();
		boolean isSet = NewPlaceChunkHopperListener.isSet();
		boolean isSettingUp = NewPlaceChunkHopperListener.isStillSettingUpFilter();
		if(!Main.legacy) {
			locCh = NewPlaceChunkHopperListener1_13.getLocCh();
			settingsLoc = NewPlaceChunkHopperListener1_13.getSettingsLoc();
			filterLoc = NewPlaceChunkHopperListener1_13.getFilterLoc();
			//boolean isChange = NewPlaceChunkHopperListener.isChange();
			isSet = NewPlaceChunkHopperListener1_13.isSet();
			isSettingUp = NewPlaceChunkHopperListener1_13.isStillSettingUpFilter();
		}
		//boolean isToFilter = NewPlaceChunkHopperListener.isToFilter();
		sender.sendMessage("");
		for(int i = 0; i<locCh.length; i++) {
			if(locCh[i]!=null) {
				sender.sendMessage("§4locCh "+"§a"+i+"§4: §b"+ locCh[i]);
				if(locCh[i].getBlock().hasMetadata("chunkhopper"))
					sender.sendMessage("§aMetadata: " + locCh[i].getBlock().getMetadata("chunkhopper").get(0).asString());
			}
			if(settingsLoc[i]!=null) {
				sender.sendMessage("§b"+i+" §4"+"settingsLoc: §b"+ settingsLoc[i]);
				if(settingsLoc[i].getBlock().hasMetadata("chunkhopper"))
					sender.sendMessage("§aMetadata: " + settingsLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString());
			}
			if(filterLoc[i]!=null) {
				sender.sendMessage("§b"+i+" §4"+"filterLoc: §b"+ filterLoc[i]);
				if(filterLoc[i].getBlock().hasMetadata("chunkhopper"))
					sender.sendMessage("§aMetadata: " + filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString());
			}
		}
		//if(isChange)
			//sender.sendMessage("§4boolean change");
		if(isSet)
			sender.sendMessage("§4boolean set");
		if(isSettingUp)
			sender.sendMessage("§4boolean stillSettingUpFilter");
		//if(isToFilter)
			//sender.sendMessage("§4boolean toFilter");
		for(int i = 0; i<Main.chunkHopper.size(); i++) {
			sender.sendMessage("§6ChunkHopper at: §b"+ Main.chunkHopper.get(i).getLocation() +", §cLevel: "+ Main.chunkHopper.get(i).getLevel());
		}
		return false;
	}
}
