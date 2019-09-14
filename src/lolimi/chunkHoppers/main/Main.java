package lolimi.chunkHoppers.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import lolimi.chunkHoppers.chunkHoppers.ChunkHopper;
import lolimi.chunkHoppers.chunkHoppers.LevelOne;
import lolimi.chunkHoppers.chunkHoppers.LevelThree;
import lolimi.chunkHoppers.chunkHoppers.LevelTwo;
import lolimi.chunkHoppers.commands.CommandTab;
import lolimi.chunkHoppers.commands.GetChunkHopperCmd;
import lolimi.chunkHoppers.commands.GetUpgradeOneCmd;
import lolimi.chunkHoppers.commands.GetUpgradeTwoCmd;
import lolimi.chunkHoppers.commands.HelpCmd;
import lolimi.chunkHoppers.commands.RmvFailedChunkHopperCmd;
import lolimi.chunkHoppers.commands.StaticVariableDebug;
import lolimi.chunkHoppers.listeners.BreakChunkHopperListener;
import lolimi.chunkHoppers.listeners.ItemDropListenerCh;
import lolimi.chunkHoppers.listeners.ItemDropListenerChShopGUI;
import lolimi.chunkHoppers.listeners.NewPlaceChunkHopperListener;
import lolimi.chunkHoppers.listeners.NewPlaceChunkHopperListener1_13;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	private static FileConfiguration sellConf;
	
	public static String prefix;

	private static Economy econ = null;
	private static boolean economySetup;

	private static Main plugin;
	private PluginManager pluginManager;
	public static Logger log;

	public static String cHDisplayName;
	public static List<String> cHLore;
	
	public static String 	chSettingsInvName,
							normalFilterInvName,
							sellingFilterInvName,
							
							toUpgradeItemName,
							toFilterItemName,
							toSellingFilterItemName,
							toBreakItemName,
							soldItemName,
							explainBookItemNameNormal,
							explainBookItemNameSelling,
							explainEmeraldItemName,
							explainRedstoneItemName;
							
	public static List<String> 	explainBookItemLoreNormal,
								explainBookItemLoreSelling,
								explainEmeraldItemLore,
								explainRedstoneItemLore,
								whiteListItemLoreNormal,
								blackListItemLoreNormal,
								whiteListItemLoreSelling,
								blackListItemLoreSelling;
	
	public static ItemStack[] 	defaultFilterNormal = new ItemStack[45],
								defaultFilterSelling = new ItemStack[45];
	
	
	public static String 	upgradeName1,
							upgradeName2;;
	public static List<String> 	upgrade1Lore,
								upgrade2Lore;
	
	public static ItemStack upgrade1,
							upgrade2;
	
	public static boolean useLevel;		
	public static boolean useSell;
	
	public static String rchHelp;

	public static ArrayList<ChunkHopper> chunkHopper = new ArrayList<ChunkHopper>();
	
	public static boolean hasShopGuiPlus;
	public static String version;
	public static boolean legacy;

	@Override
	public void onEnable() {
		plugin = this;
		if(setupEconomy())
			economySetup = true;
		else
			economySetup = false;
		getVersion();
		initConfig();
		init();
		initCommands();
		if(useLevel)
			initItems();
	}
	
	public static void initConfig() throws NullPointerException{
		File f = new File(plugin.getDataFolder() + File.separator + "config.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
				OutputStream writer = new FileOutputStream(f);
				InputStream out = plugin.getResource("config.yml");
				byte[] linebuffer = new byte[out.available()];
				out.read(linebuffer);
				writer.write(linebuffer);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);
		
		prefix = conf.getString("Prefix");
		prefix += new String(" ");
		
		rchHelp = conf.getString("rchHelp");
		
		cHDisplayName = conf.getString("ChunkHopper.Name");
		cHLore = Arrays.asList(conf.getString("ChunkHopper.Lore").split(","));
		
		useLevel = conf.getBoolean("LevelSystem");
		if(economySetup)
			useSell = conf.getBoolean("UseSellSystem");
		else
			useSell = false;
		
		if(useLevel) {
			upgradeName1 = conf.getString("Upgrade.1.Name");
			upgradeName2 = conf.getString("Upgrade.2.Name");
			upgrade1Lore = Arrays.asList(conf.getString("Upgrade.1.Lore").split(","));
			upgrade2Lore = Arrays.asList(conf.getString("Upgrade.2.Lore").split(","));
		}
		
		chSettingsInvName = conf.getString("SettingsGUI");
		normalFilterInvName = conf.getString("NormalFilterGUI");
		sellingFilterInvName = conf.getString("SellingFilterGUI");
		
		toUpgradeItemName = conf.getString("UpgradeHopperItemName");
		toFilterItemName = conf.getString("OpenNormalFilterItemName");
		toSellingFilterItemName = conf.getString("OpenSellingFilterItemName");
		toBreakItemName = conf.getString("BreakHopperItemName");
		soldItemName = conf.getString("SoldItemName");
		
		explainBookItemNameNormal = conf.getString("Explain.Book.NormalFilter.Name");
		explainBookItemLoreNormal = Arrays.asList(conf.getString("Explain.Book.NormalFilter.Lore").split(","));
		explainBookItemNameSelling = conf.getString("Explain.Book.SellingFilter.Name");
		explainBookItemLoreSelling = Arrays.asList(conf.getString("Explain.Book.SellingFilter.Lore").split(","));
		
		explainEmeraldItemName = conf.getString("Explain.Emerald.Name");
		explainEmeraldItemLore = Arrays.asList(conf.getString("Explain.Emerald.Lore").split(","));
		explainRedstoneItemName = conf.getString("Explain.Redstone.Name");
		explainRedstoneItemLore = Arrays.asList(conf.getString("Explain.Redstone.Lore").split(","));
		
		blackListItemLoreNormal = Arrays.asList(conf.getString("BlacklistLore.NormalFilter").split(","));
		blackListItemLoreSelling = Arrays.asList(conf.getString("BlacklistLore.SellingFilter").split(","));
		whiteListItemLoreNormal = Arrays.asList(conf.getString("WhitelistLore.NormalFilter").split(","));
		whiteListItemLoreSelling = Arrays.asList(conf.getString("WhitelistLore.SellingFilter").split(","));
		
		if(conf.getBoolean("UseDefaultFilters")) {
			for(int i = 1; i<= 9*5; i++) {
				try {
					defaultFilterNormal[i-1] = new ItemStack(Material.getMaterial(conf.getString("DefaultFilters.Normal."+i)));
				}catch(Exception e) {
					defaultFilterNormal[i-1] = new ItemStack(Material.AIR);
				}
				try {
					defaultFilterSelling[i-1] = new ItemStack(Material.getMaterial(conf.getString("DefaultFilters.Selling."+i)));
				}catch(Exception e) {
					defaultFilterSelling[i-1] = new ItemStack(Material.AIR);
				}
			}
		}
		if(useSell)
			initSellConf();
	}
	
	private static void initSellConf(){
		File f = new File(plugin.getDataFolder().getPath()+ File.separator + "worth.yml");
		
		if (!f.exists()) {
			try {
				f.createNewFile();
				OutputStream writer = new FileOutputStream(f);
				InputStream out = plugin.getResource("worth.yml");
				byte[] linebuffer = new byte[out.available()];
				out.read(linebuffer);
				writer.write(linebuffer);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		sellConf = YamlConfiguration.loadConfiguration(f);
	}

	private void init() {
		pluginManager = Bukkit.getPluginManager();
		log = Bukkit.getLogger();
		
		if(!economySetup) {
			log.info("Selling option turned off, Vault is not installed!");
		}
		
		if(useSell) {
			if(getServer().getPluginManager().getPlugin("ShopGUIPlus") != null)
				hasShopGuiPlus = true;
			else {
				hasShopGuiPlus = false;
				Bukkit.getConsoleSender().sendMessage("§4ShopGUIPlus is not installed! Please put your prices in the worth file!");
			}
			
		}else {
			log.info("Not using sell option!");
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
									Double.parseDouble(split[3].replace(".yml", ""))), false, null));
						}else if(chFileNames[i].contains("level3") && useSell) {
							chunkHopper.add(new LevelThree(new Location(Bukkit.getWorld(split[0]),
									Double.parseDouble(split[1]), Double.parseDouble(split[2]),
									Double.parseDouble(split[3].replace(".yml", ""))), false, null));
						}
					}
						
					Bukkit.broadcastMessage("§4Chunk Hoppers have been loaded");
					Bukkit.getConsoleSender().sendMessage("§bMinecraft version: §3"+version);

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
		getCommand("rch").setTabCompleter(new CommandTab());
		getCommand("gch").setExecutor(new GetChunkHopperCmd());
		getCommand("rmvch").setExecutor(new RmvFailedChunkHopperCmd());
		getCommand("rchdebug").setExecutor(new StaticVariableDebug());
		if(useLevel) {
			getCommand("gu1").setExecutor(new GetUpgradeOneCmd());
			getCommand("gu2").setExecutor(new GetUpgradeTwoCmd());
		}
		pluginManager.registerEvents(new BreakChunkHopperListener(), plugin);
		if(!hasShopGuiPlus)
			pluginManager.registerEvents(new ItemDropListenerCh(), plugin);
		else
			pluginManager.registerEvents(new ItemDropListenerChShopGUI(), this);
		if(version.equals("v1_13_R2"))
			pluginManager.registerEvents(new NewPlaceChunkHopperListener1_13(), plugin);
		else
			pluginManager.registerEvents(new NewPlaceChunkHopperListener(), this);
	}
	
	private void initItems() {
		upgrade1 = new ItemStack(Material.IRON_INGOT);
		upgrade2 = new ItemStack(Material.IRON_INGOT);
		
		ItemMeta 	m1 = upgrade1.getItemMeta(),
					m2 = upgrade1.getItemMeta();
		m1.setDisplayName(upgradeName1);
		m2.setDisplayName(upgradeName2);
		m1.setLore(upgrade1Lore);
		m2.setLore(upgrade2Lore);
		
		upgrade1.setItemMeta(m1);
		upgrade2.setItemMeta(m2);
	}
	
	private void getVersion() {
		String a = this.getServer().getClass().getPackage().getName();
		version = a.substring(a.lastIndexOf('.') + 1);
		
		Bukkit.broadcastMessage(version);
		
		
		if(version.contains("1_13")|| version.contains("1_14")) {
			legacy = false;
		} else {
			legacy = true;
		}
		
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
	
	
	public static FileConfiguration getSellConf() {
		if(hasShopGuiPlus) 
			return null;
		return sellConf;
	}

}
