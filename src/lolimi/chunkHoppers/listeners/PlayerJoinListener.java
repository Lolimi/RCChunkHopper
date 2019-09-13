package lolimi.chunkHoppers.listeners;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import lolimi.chunkHoppers.main.ChDataHandler;
import lolimi.chunkHoppers.main.Main;

public class PlayerJoinListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		String[] message = Main.getPlugin().getConfig().getString("JoinMessage.Message").split(",");
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				for(String m : message) {
					e.getPlayer().sendMessage(m);
				}
			}
		}, Main.getPlugin().getConfig().getInt("JoinMessage.Delay"));

		double sold = ChDataHandler.getOfflineSold(e.getPlayer().getUniqueId() + "");
		if (sold == -1) {
			ChDataHandler.setupOfflineSold(e.getPlayer().getUniqueId().toString());
		} else {

			Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
			int i = 0;
			while (i <= plugins.length) {
				if (plugins[i].getName().equalsIgnoreCase("essentials")) {
					OfflinePlayer player = Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId());
					if (sold != -1)
						Main.getEconomy().depositPlayer(player, sold);
					if (sold > 0) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							@Override
							public void run() {
								DecimalFormat df = new DecimalFormat("#.###");
								e.getPlayer().sendMessage(Main.prefix + "§bYour §6Chunk Hoppers §bhave sold §2$§6"
										+ df.format(sold) + " §bwhile you were offline.");
							}
						}, Main.getPlugin().getConfig().getInt("JoinMessage.Delay"));
						ChDataHandler.setOfflineSold(e.getPlayer().getUniqueId().toString(), 0, true);
					}
					break;
				}
				i++;
			}
		}
	}

}
