package lolimi.chunkHoppers.listeners;


public class OldChunkHopper {

//	private boolean changed = false;
//	private boolean removed = false;
//
//	private Location location;
//	private int chunkX;
//	private int chunkZ;
//	private String ownerName;
//	private UUID ownerUUID;
//	private boolean nWhitelist;
//	private boolean sWhilelist;
//	private double sold;
//	private ItemStack[] normalFilter = new ItemStack[45];
//	private ItemStack[] sellingFilter = new ItemStack[45];
//
//	private File file;
//	private YamlConfiguration conf;
//
//	public OldChunkHopper(Location loc, UUID uid) {
//		File file = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "ChunkHoppers"
//				+ File.separator + loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
//				+ loc.getBlockZ() + ".yml");
//
//		if (!file.exists()) {
//			try {
//				this.location = loc;
//				this.chunkX = loc.getChunk().getX();
//				this.chunkZ = loc.getChunk().getZ();
//				this.ownerName = Bukkit.getOfflinePlayer(uid).getName();
//				this.ownerUUID = uid;
//				this.nWhitelist = false;
//				this.sWhilelist = true;
//				this.sold = 0;
//				this.file = file;
//
//				file.createNewFile();
//				conf = YamlConfiguration.loadConfiguration(file);
//
//				conf.set("Owner.Name", Bukkit.getOfflinePlayer(uid).getName());
//				conf.set("Owner.UUID", uid.toString());
//
//				// conf.set("Selling", String.valueOf(selling));
//				conf.set("NormalWhitelist", String.valueOf(nWhitelist));
//				conf.set("SellingWhitelist", String.valueOf(sWhilelist));
//				conf.set("Sold", sold);
//
//				for (int i = 0; i < 9 * 5; i++) {
//					conf.set("NormalFilter." + i, "AIR");
//					this.normalFilter[i] = new ItemStack(Material.AIR);
//					conf.set("SellingFilter." + i, "AIR");
//					this.sellingFilter[i] = new ItemStack(Material.AIR);
//				}
//				conf.save(file);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public OldChunkHopper(Location loc) {
//		File file = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "ChunkHoppers"
//				+ File.separator + loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";"
//				+ loc.getBlockZ() + ".yml");
//
//		if (!file.exists())
//			return;
//
//		conf = YamlConfiguration.loadConfiguration(file);
//		try {
//			if (!conf.getString("Whitelist").equals("OLD")) {
//				if (conf.getString("Selling").equals("true")) {
//					for (int i = 0; i < 9 * 5; i++) {
//						conf.set("SellingFilter." + i, conf.getString("Filter." + i));
//						conf.set("NormalFilter." + i, "AIR");
//					}
//					if (conf.getString("Whitelist").equals("true")) {
//						conf.set("NormalWhitelist", "false");
//						conf.set("SellingWhitelist", "true");
//					} else {
//						conf.set("NormalWhitelist", "false");
//						conf.set("SellinWhitelist", "false");
//					}
//				} else {
//					for (int i = 0; i < 9 * 5; i++) {
//						conf.set("SellingFilter." + i, conf.getString("Filter." + i));
//						conf.set("NormalFilter." + i, "AIR");
//					}
//					if (conf.getString("Whitelist").equals("true")) {
//						conf.set("SellingWhitelist", "true");
//						conf.set("NormalWhitelist", "true");
//					} else {
//						conf.set("SellingWhitelist", "true");
//						conf.set("NormalWhitelist", "false");
//					}
//				}
//				conf.set("Selling", "OLD");
//				conf.set("Whitelist", "OLD");
//				for (int i = 0; i < 9 * 5; i++) {
//					conf.set("Filter." + i, "OLD");
//				}
//				try {
//					conf.save(file);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//		} catch (NullPointerException e) {
//		}
//
//		String[] location = file.getName().split(";");
//		this.location = new Location(Bukkit.getWorld(location[0]), Double.parseDouble(location[1]),
//				Double.parseDouble(location[2]), Double.parseDouble(location[3].replace(".yml", "")));
//		this.chunkX = this.location.getChunk().getX();
//		this.chunkZ = this.location.getChunk().getZ();
//		this.ownerName = conf.getString("Owner.Name");
//		this.ownerUUID = UUID.fromString(conf.getString("Owner.UUID"));
//		// if(conf.getString("Selling").equals("true"))this.selling = true;
//		// else this.selling = false;
//		if (conf.getString("NormalWhitelist").equals("true"))
//			this.nWhitelist = true;
//		else
//			this.nWhitelist = false;
//		if (conf.getString("SellingWhitelist").equals("true"))
//			this.sWhilelist = true;
//		else
//			this.sWhilelist = false;
//		this.sold = conf.getDouble("Sold");
//		for (int i = 0; i < 45; i++) {
//			this.normalFilter[i] = new ItemStack(Material.getMaterial(conf.getString("NormalFilter." + i)));
//			this.sellingFilter[i] = new ItemStack(Material.getMaterial(conf.getString("SellingFilter." + i)));
//		}
//		this.file = file;
//	}
//
//	public void remove() {
//		file.delete();
//		Main.getPlugin().rmvChunkHopper(this);
//		removed = true;
//		location = null;
//	}
//
//	public void addToSold(double amount) {
//		sold = sold + amount;
//		/*
//		 * conf.set("Sold", (Double) conf.get("Sold") + amount); try { conf.save(file);
//		 * } catch (IOException e) { e.printStackTrace(); }
//		 */
//		changed = true;
//	}
//
//	public void changeNWhitelist() {
//		if (nWhitelist) {
//			nWhitelist = false;
//			// conf.set("Whitelist", String.valueOf(whitelist));
//		} else {
//			nWhitelist = true;
//			// conf.set("Whitelist", String.valueOf(whitelist));
//		}
//		/*
//		 * try { conf.save(file); } catch (IOException e) { e.printStackTrace(); }
//		 */
//		changed = true;
//	}
//
//	public void changeSWhitelist() {
//		if (sWhilelist)
//			sWhilelist = false;
//		else
//			sWhilelist = true;
//		changed = true;
//	}
//
//	public void rmvFromNFilter(ItemStack item, int slot) {
//		if (normalFilter[slot - 9].getType().equals(item.getType())) {
//			normalFilter[slot - 9].setType(Material.AIR);
//			// conf.set("Filter."+ (slot-9), "AIR");
//			/*
//			 * try { conf.save(file); } catch (IOException e) { e.printStackTrace(); }
//			 */
//		}
//		changed = true;
//	}
//
//	public void rmvFromSFilter(ItemStack item, int slot) {
//		if (sellingFilter[slot - 9].getType().equals(item.getType())) {
//			sellingFilter[slot - 9].setType(Material.AIR);
//		}
//		changed = true;
//	}
//
//	public void addToNFilter(ItemStack item) {
//		for (int i = 0; i < 44; i++) {
//			if (normalFilter[i].getType().equals(Material.AIR)) {
//				normalFilter[i].setType(item.getType());
//				/*
//				 * conf.set("Filter."+i, item.getType().toString().toUpperCase()); try {
//				 * conf.save(file); } catch (IOException e) { e.printStackTrace(); }
//				 */
//				break;
//			}
//		}
//		changed = true;
//	}
//
//	public void addToSFilter(ItemStack item) {
//		for (int i = 0; i < 44; i++) {
//			if (sellingFilter[i].getType().equals(Material.AIR)) {
//				sellingFilter[i].setType(item.getType());
//				break;
//			}
//		}
//		changed = true;
//	}
//
//	public boolean save() {
//		if (removed)
//			return false;
//		if (!changed)
//			return true;
//
//		conf.set("Sold", sold);
//		conf.set("NormalWhitelist", nWhitelist);
//		conf.set("SellingWhitelist", sWhilelist);
//
//		for (int i = 0; i < normalFilter.length; i++) {
//			conf.set("NormalFilter." + i, normalFilter[i].getType().toString().toUpperCase());
//			conf.set("SellingFilter."+i, sellingFilter[i].getType().toString().toUpperCase());
//		}
//		
//		try {
//			conf.save(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return true;
//	}
//
//	public Location getLocation() {
//		return location;
//	}
//
//	public int getChunkX() {
//		return chunkX;
//	}
//
//	public int getChunkZ() {
//		return chunkZ;
//	}
//
//	public String getOwnerName() {
//		return ownerName;
//	}
//
//	public UUID getOwnerUUID() {
//		return ownerUUID;
//	}
//
//	/*
//	 * public boolean isSelling() { return selling; }
//	 */
//
//	public boolean isNWhitelist() {
//		return nWhitelist;
//	}
//
//	public boolean isSWhitelist() {
//		return sWhilelist;
//	}
//
//	public double getSold() {
//		return sold;
//	}
//
//	public ItemStack[] getNFilter() {
//		return normalFilter;
//	}
//
//	public ItemStack[] getSFilter() {
//		return sellingFilter;
//	}
//
//	public ItemStack getFromNFilter(int i) {
//		return normalFilter[i];
//	}
//
//	public ItemStack getFromSFilter(int i) {
//		return sellingFilter[i];
//	}
//
//	public File getFile() {
//		return file;
//	}
//
//	public boolean exists() {
//		if (location != null)
//			return true;
//		if (removed)
//			return false;
//		return false;
//	}

}
