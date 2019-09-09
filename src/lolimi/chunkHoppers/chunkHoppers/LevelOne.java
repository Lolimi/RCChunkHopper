package lolimi.chunkHoppers.chunkHoppers;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import lolimi.chunkHoppers.main.Main;

public class LevelOne extends ChunkHopper {
	
	public LevelOne(Location loc, UUID uid) {
		removed = false;
		changed = false;
		level = 1;
		
		file = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "ChunkHoppers"
				+ File.separator + loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
				+ loc.getBlockZ() + ";level1.yml");
		
		if (!file.exists()) {
			try {
				location = loc;
				chunkX = loc.getChunk().getX();
				chunkZ = loc.getChunk().getZ();
				ownerName = Bukkit.getOfflinePlayer(uid).getName();
				ownerUUID = uid;
				normalWhitelist = false;
				sold = 0;

				file.createNewFile();
				conf = YamlConfiguration.loadConfiguration(file);
				
				conf.set("Owner.Name", Bukkit.getOfflinePlayer(uid).getName());
				conf.set("Owner.UUID", uid.toString());

				conf.set("NormalWhitelist", String.valueOf(normalWhitelist));
				conf.set("Sold", sold);

				for (int i = 0; i < 9 * 5; i++) {
					conf.set("NormalFilter." + i, "AIR");
					this.normalFilter[i] = new ItemStack(Material.AIR);
				}
				conf.save(file);
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public LevelOne(Location loc) {
		removed = false;
		changed = false;
		level = 1;
		
		file = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "ChunkHoppers"
				+ File.separator + loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
				+ loc.getBlockZ() + ";level1.yml");

		if (!file.exists())
			return;

		conf = YamlConfiguration.loadConfiguration(file);
		

		String[] location = file.getName().split(";");
		this.location = new Location(Bukkit.getWorld(location[0]), Double.parseDouble(location[1]),
				Double.parseDouble(location[2]), Double.parseDouble(location[3].replace(".yml", "")));
		chunkX = this.location.getChunk().getX();
		chunkZ = this.location.getChunk().getZ();
		ownerName = conf.getString("Owner.Name");
		ownerUUID = UUID.fromString(conf.getString("Owner.UUID"));
		
		if (conf.getString("NormalWhitelist").equals("true"))
			normalWhitelist = true;
		else
			normalWhitelist = false;
		
		this.sold = conf.getDouble("Sold");
		for (int i = 0; i < 45; i++) {
			this.normalFilter[i] = new ItemStack(Material.getMaterial(conf.getString("NormalFilter." + i)));
		}
	}
	
	@Override
	public boolean exists() {
		if (removed)
			return false;
		if (location != null)
			return true;
		return false;
	}

	@Override
	public void remove() {
		try{
			file.delete();
		}catch(Exception e) {}
		Main.getPlugin().rmvChunkHopper(this);
		removed = true;
		location = null;
	}
	
	@Override
	public boolean save() {
		if (removed)
			return false;
		return true;
	}

}
