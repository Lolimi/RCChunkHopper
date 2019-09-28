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

public class LevelThree extends AbstractChunkHopper {

	public LevelThree(Location loc, boolean isNew, UUID uid) {
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

		} else {
			if (Main.useLevel) {
				if (Main.getPlugin().isInChunkHopperChunk(loc).getLevel() != 2) return;
				LevelTwo old = (LevelTwo) Main.getPlugin().isInChunkHopperChunk(loc);
				file = old.getFile();
				file = new File(file.getPath().replace("level2.yml", "level3.yml"));
				conf = YamlConfiguration.loadConfiguration(file);

				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				boolean useDefaultFilters = Main.getPlugin().getConfig().getBoolean("UseDefaultFilters");
				
				location = old.getLocation();
				chunkX = old.getChunkX();
				chunkZ = old.getChunkZ();
				ownerName = old.getOwnerName();
				ownerUUID = old.getOwnerUUID();
				normalWhitelist = old.normalWhitelist;
				if(useDefaultFilters)
					sellingWhitelist = Main.getPlugin().getConfig().getBoolean("DefaultFilters.Selling.Whitelist");
				else
					sellingWhitelist = true;
				sold = 0;
				location = old.getLocation();
				normalFilter = old.normalFilter;
				old.remove();

				
				if(useDefaultFilters) {
					sellingFilter = Main.defaultFilterSelling;
					for(int i = 0; i< 45; i++) {
						conf.set("NormalFilter." + i, normalFilter[i].getType().name().toUpperCase());
						conf.set("SellingFilter." + i, sellingFilter[i].getType().name().toUpperCase());
						conf.set("SellingWhitelist", String.valueOf(sellingWhitelist));
					}
				}else {
					for (int i = 0; i < 45; i++) {
						this.sellingFilter[i] = new ItemStack(Material.AIR);
						conf.set("SellingFilter." + i, "AIR");
						conf.set("NormalFilter." + i, normalFilter[i].getType().name().toUpperCase());
						conf.set("NormalWhitelist", "false");
						conf.set("SellingWhitelist", "true");
					}
				}
				conf.set("Owner.Name", Bukkit.getOfflinePlayer(ownerUUID).getName());
				conf.set("Owner.UUID", ownerUUID.toString());

				conf.set("Sold", sold);

				for (int i = 0; i < 9 * 5; i++) {
					
				}
				try {
					conf.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}else {
				file = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "ChunkHoppers"
						+ File.separator + loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
						+ loc.getBlockZ() + ";level3.yml");
				if(file.exists()) return;
				
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				conf = YamlConfiguration.loadConfiguration(file);
				
				boolean useDefaultFilters = Main.getPlugin().getConfig().getBoolean("UseDefaultFilters");
				
				location = loc;
				chunkX = this.location.getChunk().getX();
				chunkZ = this.location.getChunk().getZ();
				
				ownerName = Bukkit.getOfflinePlayer(uid).getName();
				ownerUUID = uid;
				
				if(useDefaultFilters) {
					normalWhitelist = Main.getPlugin().getConfig().getBoolean("DefaultFilters.Normal.Whitelist");
					sellingWhitelist = Main.getPlugin().getConfig().getBoolean("DefaultFilters.Selling.Whitelist");
					conf.set("NormalWhitelist", String.valueOf(normalWhitelist));
					conf.set("SellingWhitelist", String.valueOf(sellingWhitelist));
					normalFilter = Main.defaultFilterNormal;
					sellingFilter = Main.defaultFilterSelling;
					for(int i = 0; i< 45; i++) {
						conf.set("NormalFilter." + i, normalFilter[i].getType().name());
						conf.set("SellingFilter." + i, sellingFilter[i].getType().name());
					}
				}else {
					normalWhitelist = false;
					sellingWhitelist = true;
					conf.set("NormalWhitelist", "false");
					conf.set("SellingWhitelist", "true");
					for (int i = 0; i < 45; i++) {
						this.normalFilter[i] = new ItemStack(Material.AIR);
						this.sellingFilter[i] = new ItemStack(Material.AIR);
						conf.set("NormalFilter." + i, "AIR");
						conf.set("SellingFilter." + i, "AIR");
					}
				}
				sold = 0;
				
				conf.set("Owner.Name", ownerName);
				conf.set("Owner.UUID", uid.toString());

				conf.set("Sold", 0);

				try {
					conf.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		try {
			file.delete();
		} catch (Exception e) {
		}
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
