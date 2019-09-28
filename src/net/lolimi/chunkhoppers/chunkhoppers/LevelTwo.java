package net.lolimi.chunkhoppers.chunkhoppers;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import net.lolimi.chunkhoppers.main.Main;

public class LevelTwo extends AbstractChunkHopper {

	public LevelTwo(Location loc, boolean isNew, UUID uid) {
		removed = false;
		changed = false;
		level = 2;
		
		if (!isNew) {
			file = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "ChunkHoppers"
					+ File.separator + loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
					+ loc.getBlockZ() + ";level2.yml");

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

			for (int i = 0; i < 45; i++) {
				this.normalFilter[i] = new ItemStack(Material.getMaterial(conf.getString("NormalFilter." + i)));
			}
		}else {
			if(uid == null) {
				if(Main.getPlugin().isInChunkHopperChunk(loc).getLevel() != 1) return;
				LevelOne old = (LevelOne) Main.getPlugin().isInChunkHopperChunk(loc);
				file = old.getFile();
				file = new File(file.getPath().replace("level1.yml", "level2.yml"));
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
				boolean useDefaultFilters = Main.getPlugin().getConfig().getBoolean("UseDefaultFilters");
				if(!useDefaultFilters)
					normalWhitelist = false;
				else
					normalWhitelist = Main.getPlugin().getConfig().getBoolean("DefaultFilters.Normal.Whitelist");
				
				conf.set("Owner.Name", Bukkit.getOfflinePlayer(ownerUUID).getName());
				conf.set("Owner.UUID", ownerUUID.toString());

				conf.set("NormalWhitelist", String.valueOf(normalWhitelist));
				
				if(useDefaultFilters) {
					this.normalFilter = Main.defaultFilterNormal;
					for (int i = 0; i < 9 * 5; i++) {
						conf.set("NormalFilter."+i, normalFilter[i].getType().name());
					}
				}else {
					for (int i = 0; i < 9 * 5; i++) {
						conf.set("NormalFilter." + i, "AIR");
						this.normalFilter[i] = new ItemStack(Material.AIR);
					}
				}
				try {
					conf.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				old.remove();
			}else {
				file = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "ChunkHoppers"
						+ File.separator + loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
						+ loc.getBlockZ() + ";level2.yml");

				if (file.exists())
					return;
				try {
					file.createNewFile();
				} catch (IOException e) {
				}
				conf = YamlConfiguration.loadConfiguration(file);
				location = loc.clone();
				chunkX = this.location.getChunk().getX();
				chunkZ = this.location.getChunk().getZ();
				
				ownerName = Bukkit.getOfflinePlayer(uid).getName();
				ownerUUID = uid;
				
				boolean useDefaultFilters = Main.getPlugin().getConfig().getBoolean("UseDefaultFilters");
				if(!useDefaultFilters)
					normalWhitelist = false;
				else
					normalWhitelist = Main.getPlugin().getConfig().getBoolean("DefaultFilters.Normal.Whitelist");
				
				conf.set("NormalWhitelist", String.valueOf(normalWhitelist));
				if(useDefaultFilters) {
					this.normalFilter = Main.defaultFilterNormal;
					for (int i = 0; i < 9 * 5; i++) {
						conf.set("NormalFilter."+i, normalFilter[i].getType().name());
					}
				}else {
					for (int i = 0; i < 9 * 5; i++) {
						conf.set("NormalFilter." + i, "AIR");
						this.normalFilter[i] = new ItemStack(Material.AIR);
					}
				}
				
				conf.set("Owner.Name", Bukkit.getOfflinePlayer(ownerUUID).getName());
				conf.set("Owner.UUID", ownerUUID.toString());
				
				try {
					conf.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
		if (!changed)
			return true;

		conf.set("NormalWhitelist", normalWhitelist);

		for (int i = 0; i < normalFilter.length; i++) {
			conf.set("NormalFilter." + i, normalFilter[i].getType().toString().toUpperCase());
		}
		
		try {
			conf.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
	

}
