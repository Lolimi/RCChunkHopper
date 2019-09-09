package lolimi.chunkHoppers.chunkHoppers;

import java.io.File;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import lolimi.chunkHoppers.main.Main;

public abstract class ChunkHopper {
	
	protected boolean changed;
	protected boolean removed;

	protected Location location;
	protected int chunkX;
	protected int chunkZ;
	protected String ownerName;
	protected UUID ownerUUID;
	protected boolean normalWhitelist;
	protected boolean sellingWhitelist;
	protected double sold;
	protected ItemStack[] normalFilter = new ItemStack[45];
	protected ItemStack[] sellingFilter = new ItemStack[45];
	
	protected File file;
	protected YamlConfiguration conf;
	
	protected int level;
	

	public abstract boolean exists();
	public abstract void remove();
	public abstract boolean save();
	
	
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public int getChunkX() {
		return chunkX;
	}
	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}
	public int getChunkZ() {
		return chunkZ;
	}
	public void setChunkZ(int chunkZ) {
		this.chunkZ = chunkZ;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public UUID getOwnerUUID() {
		return ownerUUID;
	}
	public void setOwnerUUID(UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
	}
	public boolean isNormalWhitelist() {
		return normalWhitelist;
	}
	public void setNormalWhitelist(boolean normalWhitelist) {
		this.normalWhitelist = normalWhitelist;
	}
	public double getSold() {
		return sold;
	}
	public void setSold(double sold) {
		this.sold = sold;
	}
	public ItemStack[] getNormalFilter() {
		return normalFilter;
	}
	public void setNormalFilter(ItemStack[] normalFilter) {
		this.normalFilter = normalFilter;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isSellingWhitelist() {
		return sellingWhitelist;
	}
	public void setSellingWhitelist(boolean sellingWhitelist) {
		this.sellingWhitelist = sellingWhitelist;
	}
	public ItemStack[] getSellingFilter() {
		return sellingFilter;
	}
	public void setSellingFilter(ItemStack[] sellingFilter) {
		this.sellingFilter = sellingFilter;
	}
	
	
	public void changeNWhitelist() {
		if (normalWhitelist) {
			normalWhitelist = false;
		} else {
			normalWhitelist = true;
		}
		changed = true;
	}
	
	public void addToNFilter(ItemStack item) {
		for (int i = 0; i < 44; i++) {
			if (normalFilter[i].getType().equals(Material.AIR)) {
				normalFilter[i].setType(item.getType());
				break;
			}
		}
		changed = true;
	}
	
	public void rmvFromNFilter(ItemStack item, int slot) {
		if (normalFilter[slot - 9].getType().equals(item.getType())) {
			normalFilter[slot - 9].setType(Material.AIR);
		}
		changed = true;
	}
	
	public void changeSWhitelist() {
		if (sellingWhitelist) {
			sellingWhitelist = false;
		} else {
			sellingWhitelist = true;
		}
		changed = true;
	}
	
	public void addToSFilter(ItemStack item) {
		for (int i = 0; i < 44; i++) {
			if (sellingFilter[i].getType().equals(Material.AIR)) {
				sellingFilter[i].setType(item.getType());
				break;
			}
		}
		changed = true;
	}
	
	public void rmvFromSFilter(ItemStack item, int slot) {
		if (sellingFilter[slot - 9].getType().equals(item.getType())) {
			sellingFilter[slot - 9].setType(Material.AIR);
		}
		changed = true;
	}
	
	public void addToSold(double amount) {
		sold += amount;
	}
	
	public void upgrade() {
		if(level == 1) {
			LevelTwo two = new LevelTwo(location, true);
			two.save();
			Main.getPlugin().addChunkHopper(two);
		}else if(level == 2) {
			LevelThree three = new LevelThree(location, true);
			three.save();
			Main.getPlugin().addChunkHopper(three);
		}
	}

}
