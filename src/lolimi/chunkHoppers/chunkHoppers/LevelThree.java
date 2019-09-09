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

public class LevelThree extends ChunkHopper {
	
	public LevelThree(Location loc, boolean isNew) {
		removed = false;
		changed = false;
		level = 3;
		location = loc;
		
		if (!isNew) {
			file = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "ChunkHoppers"
					+ File.separator + loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
					+ loc.getBlockZ() + ";level3.yml");

			if (!file.exists())
				return;

			conf = YamlConfiguration.loadConfiguration(file);

			
//			this.location = loc;
//					new Location(Bukkit.getWorld(location[0]), Double.parseDouble(location[1]),
//					Double.parseDouble(location[2]), Double.parseDouble(location[3]));
			chunkX = this.location.getChunk().getX();
			chunkZ = this.location.getChunk().getZ();
			ownerName = conf.getString("Owner.Name");
			ownerUUID = UUID.fromString(conf.getString("Owner.UUID"));
			sold = conf.getDouble("Sold");

			if (conf.getString("NormalWhitelist").equals("true"))
				normalWhitelist = true;
			else
				normalWhitelist = false;
			if (conf.getString("SellingWhitelist").equals("true"))
				sellingWhitelist = true;
			else
				sellingWhitelist = false;

			for (int i = 0; i < 45; i++) {
				this.normalFilter[i] = new ItemStack(Material.getMaterial(conf.getString("NormalFilter." + i)));
			}
			for (int i = 0; i < 45; i++) {
				this.sellingFilter[i] = new ItemStack(Material.getMaterial(conf.getString("SellingFilter." + i)));
			}
			
		}else {
			if(Main.getPlugin().isInChunkHopperChunk(loc).getLevel() != 2) return;
			LevelTwo old = (LevelTwo) Main.getPlugin().isInChunkHopperChunk(loc);
			file = old.getFile();
			file = new File(file.getPath().replace("level2.yml", "level3.yml"));
			conf = YamlConfiguration.loadConfiguration(file);
			
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			location = old.getLocation();
			chunkX = old.getChunkX();
			chunkZ = old.getChunkZ();
			ownerName = old.getOwnerName();
			ownerUUID = old.getOwnerUUID();
			normalWhitelist = false;
			sellingWhitelist = true;
			sold = 0;
			location = old.getLocation();
			old.remove();
			
			for (int i = 0; i < 45; i++) {
				this.normalFilter[i] = new ItemStack(Material.AIR);
			}
			for (int i = 0; i < 45; i++) {
				this.sellingFilter[i] = new ItemStack(Material.AIR);
			}
			
			conf.set("Owner.Name", Bukkit.getOfflinePlayer(ownerUUID).getName());
			conf.set("Owner.UUID", ownerUUID.toString());

			conf.set("NormalWhitelist", String.valueOf(normalWhitelist));
			conf.set("SellingWhitelist", String.valueOf(sellingWhitelist));
			conf.set("Sold", sold);

			for (int i = 0; i < 9 * 5; i++) {
				conf.set("NormalFilter." + i, "AIR");
				this.normalFilter[i] = new ItemStack(Material.AIR);
			}
			for (int i = 0; i < 9 * 5; i++) {
				conf.set("SellingFilter." + i, "AIR");
				this.sellingFilter[i] = new ItemStack(Material.AIR);
			}
			try {
				conf.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		level = 3;
	}
	
	

	@Override
	public boolean exists() {
		if (removed) {
			return false;
		}
		if (location != null) {
			return true;
		}
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
		if (!changed)
			return true;

		conf.set("NormalWhitelist", normalWhitelist);
		conf.set("SellingWhitelist", sellingWhitelist);

		for (int i = 0; i < normalFilter.length; i++) {
			conf.set("NormalFilter." + i, normalFilter[i].getType().toString().toUpperCase());
		}
		for (int i = 0; i < sellingFilter.length; i++) {
			conf.set("SellingFilter." + i, sellingFilter[i].getType().toString().toUpperCase());
		}
		
		try {
			conf.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

}
