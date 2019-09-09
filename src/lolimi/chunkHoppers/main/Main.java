package lolimi.chunkHoppers.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import lolimi.chunkHoppers.chunkHoppers.ChunkHopper;
import lolimi.chunkHoppers.chunkHoppers.LevelOne;
import lolimi.chunkHoppers.chunkHoppers.LevelThree;
import lolimi.chunkHoppers.chunkHoppers.LevelTwo;
import lolimi.chunkHoppers.commands.GetChunkHopperCmd;
import lolimi.chunkHoppers.commands.GetUpgradeOneCmd;
import lolimi.chunkHoppers.commands.GetUpgradeTwoCmd;
import lolimi.chunkHoppers.commands.HelpCmd;
import lolimi.chunkHoppers.commands.RmvFailedChunkHopperCmd;
import lolimi.chunkHoppers.commands.StaticVariableDebug;
import lolimi.chunkHoppers.listeners.BreakChunkHopperListener;
import lolimi.chunkHoppers.listeners.ItemDropListenerCh;
import lolimi.chunkHoppers.listeners.NewPlaceChunkHopperListener;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static Economy econ = null;

	private static Main plugin;
	private PluginManager pluginManager;
	public static Logger log;

	public static final String cHDisplayName = "§6§lChunk Hopper";
	public static final List<String> cHLore = Arrays.asList("§bThis is a Chunk Hopper",
			"§bIt picks up all the items in a chunk!");
	
	private final String 	upgradeName = "§aChunk Hopper upgrade";
	private final List<String> 	upgrade1Lore = Arrays.asList("§6Tier 1 §a➜ §6Tier 2"),
								upgrade2Lore = Arrays.asList("§6Tier 2 §a➜ §6Tier 3");
	
	public static ItemStack upgrade1,
							upgrade2;
								

	public static ArrayList<ChunkHopper> chunkHopper = new ArrayList<ChunkHopper>();

	@Override
	public void onEnable() {
		plugin = this;
		init();
		initCommands();
		initItems();
	}

	private void init() {
		pluginManager = Bukkit.getPluginManager();
		log = Bukkit.getLogger();

		if (!setupEconomy() || getServer().getPluginManager().getPlugin("Essentials") == null) {
			log.info("Shutting down: plugins Vault and/or Essentials not installed!");
			//pluginManager.disablePlugin(this);
			return;
		}

		File pluginFile = this.getDataFolder();
		pluginFile.mkdir();
		File directory = new File(this.getDataFolder().getPath() + File.separator + "ChunkHoppers");
		directory.mkdir();

		String[] chFileNames = directory.list();

		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			@Override
			public void run() {
				if (chunkHopper.isEmpty()) {
					try {
						if (chFileNames.length == 0)
							return;
					} catch (NullPointerException e) {
						return;
					}
					String[] split;
					int i;
					for (i = 0; i < chFileNames.length; i++) {
						split = chFileNames[i].split(";");
						if(chFileNames[i].contains("level1")) {
							chunkHopper.add(new LevelOne(new Location(Bukkit.getWorld(split[0]),
									Double.parseDouble(split[1]), Double.parseDouble(split[2]),
									Double.parseDouble(split[3].replace(".yml", "")))));
						}else if(chFileNames[i].contains("level2")) {
							chunkHopper.add(new LevelTwo(new Location(Bukkit.getWorld(split[0]),
									Double.parseDouble(split[1]), Double.parseDouble(split[2]),
									Double.parseDouble(split[3].replace(".yml", ""))), false));
						}else if(chFileNames[i].contains("level3")) {
							chunkHopper.add(new LevelThree(new Location(Bukkit.getWorld(split[0]),
									Double.parseDouble(split[1]), Double.parseDouble(split[2]),
									Double.parseDouble(split[3].replace(".yml", ""))), false));
						}
					}
						
					Bukkit.broadcastMessage("§4Chunk Hoppers have been loaded");

				}
			}
		}, 60);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				saveChunkHoppers(false);
			}
		}, 20 * 60 * 10, 20 * 60 * 10);
	}

	private void initCommands() {
		String[] rchAliases = { "rcchunkhopper", "rchopper", "chunkhopper", "ch" };

		getCommand("rch").setExecutor(new HelpCmd());
		getCommand("rch").setAliases(Arrays.asList(rchAliases));
		getCommand("gch").setExecutor(new GetChunkHopperCmd());
		getCommand("rmvch").setExecutor(new RmvFailedChunkHopperCmd());
		getCommand("rchdebug").setExecutor(new StaticVariableDebug());
		getCommand("gu1").setExecutor(new GetUpgradeOneCmd());
		getCommand("gu2").setExecutor(new GetUpgradeTwoCmd());

		pluginManager.registerEvents(new BreakChunkHopperListener(), plugin);
		pluginManager.registerEvents(new ItemDropListenerCh(), plugin);
		pluginManager.registerEvents(new NewPlaceChunkHopperListener(), plugin);
	}
	
	private void initItems() {
		upgrade1 = new ItemStack(Material.IRON_INGOT);
		upgrade2 = new ItemStack(Material.IRON_INGOT);
		
		ItemMeta 	m1 = upgrade1.getItemMeta(),
					m2 = upgrade1.getItemMeta();
		m1.setDisplayName(upgradeName);
		m2.setDisplayName(upgradeName);
		m1.setLore(upgrade1Lore);
		m2.setLore(upgrade2Lore);
		
		upgrade1.setItemMeta(m1);
		upgrade2.setItemMeta(m2);
	}

	public void onDisable() {
		saveChunkHoppers(true);
	}

	public void saveChunkHoppers(boolean stopping) {
		ArrayList<ChunkHopper> removed = new ArrayList<ChunkHopper>();
		for (int i = 0; i < chunkHopper.size(); i++) {
			if (!chunkHopper.get(i).save()) {
				removed.add(chunkHopper.get(i));
			}
		}
		if (stopping) {

			return;
		}
		for (int i = 0; i < removed.size(); i++) {
			try {
				chunkHopper.remove(removed.get(i));
			} catch (Exception e) {
			}
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public static Economy getEconomy() {
		return econ;
	}

	public static Main getPlugin() {
		return plugin;
	}

	public static ArrayList<ChunkHopper> getChunkHopper() {
		return chunkHopper;
	}

	public boolean isChunkHopper(Location loc) {
		if (chunkHopper == null || chunkHopper.isEmpty())
			return false;
		for (int i = 0; i < chunkHopper.size(); i++) {
			if (chunkHopper.get(i).getLocation().equals(loc))
				return true;
		}
		return false;
	}

	public ChunkHopper isInChunkHopperChunk(Location loc) {
		if (chunkHopper == null || chunkHopper.isEmpty()) {
			return null;
		}
		for (int i = 0; i < chunkHopper.size(); i++) {
			if(loc.equals(chunkHopper.get(i).getLocation())) {
				return chunkHopper.get(i);
			}
			if (chunkHopper.get(i).getChunkX() == loc.getChunk().getX()
					&& chunkHopper.get(i).getChunkZ() == loc.getChunk().getZ()
					&& chunkHopper.get(i).getLocation().getWorld().equals(loc.getWorld())) {
				return chunkHopper.get(i);
			}
		}
		return null;
	}

	public void addChunkHopper(ChunkHopper ch) {
		chunkHopper.add(ch);
	}

	public void rmvChunkHopper(Location loc) {
		chunkHopper.remove(isInChunkHopperChunk(loc));
	}

	public void rmvChunkHopper(ChunkHopper ch) {
		chunkHopper.remove(ch);
	}
	
	

}
