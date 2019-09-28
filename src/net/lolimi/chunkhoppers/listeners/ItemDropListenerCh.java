package net.lolimi.chunkhoppers.listeners;

import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import net.lolimi.chunkhoppers.chunkhoppers.AbstractChunkHopper;
import net.lolimi.chunkhoppers.main.ChDataHandler;
import net.lolimi.chunkhoppers.main.ChunkHopperUtil;
import net.lolimi.chunkhoppers.main.Main;

public class ItemDropListenerCh implements Listener {

	@EventHandler
	public void onItemDrop(ItemSpawnEvent event) throws Exception {
		try {
			event.getEntity().getItemStack().isSimilar(Main.upgrade1);
		} catch (NullPointerException e) {
			return;
		}
		if (event.getEntity().getItemStack().isSimilar(Main.upgrade1)
				|| event.getEntity().getItemStack().isSimilar(Main.upgrade2))
			return;
		AbstractChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(event.getLocation());
		if (ch != null) {
			ItemStack item = event.getEntity().getItemStack();
			if (!ch.exists())
				return;
			if (ch.getLocation().getBlock().isBlockPowered() || ch.getLocation().getBlock().isBlockIndirectlyPowered())
				return;
			if (this.putInCh(ch, item))
				event.setCancelled(true);
		}
	}

	public boolean putInCh(AbstractChunkHopper ch, ItemStack item) {
		if (ch.getLevel() < 1 || ch.getLevel() > 3) {
			Bukkit.getConsoleSender().sendMessage("§4There is a §6ChunkHopper §4 in the data file at the location §2"
					+ ch.getLocation() + "§4, that has an invalid level!");
			return false;
		}
		Hopper h;
		try {
			h = (Hopper) ch.getLocation().getBlock().getState();
		} catch (ClassCastException e) {
			Bukkit.getConsoleSender().sendMessage("§4There is a §6ChunkHopper §4 in the data file at the location §2"
					+ ch.getLocation() + "§4, but it doesn't exist in world!");
			return false;
		}

//level 1
		if (ch.getLevel() == 1) {
			if (h.getInventory().firstEmpty() != -1) {
				h.getInventory().addItem(item);
				return true;
			} else {
				return false;
			}

//level 2
		} else if (ch.getLevel() == 2) {
			boolean inFilter = false;
			for (int i = 0; i < 44; i++) {
				if (ch.getNormalFilter()[i].getType().equals(item.getType())) {
					inFilter = true;
					break;
				}
			}

			if ((inFilter && ch.isNormalWhitelist()) || (!inFilter && !ch.isNormalWhitelist())) {
				if (h.getInventory().firstEmpty() != -1) {
					h.getInventory().addItem(item);
					return true;
				}
			}
			return false;

//level 3
		} else {
			if (!Main.useSell)
				return false;
			OfflinePlayer player = Bukkit.getOfflinePlayer(ch.getOwnerUUID());
			if (!player.isOnline()) {
				boolean inFilter = false;
				for (int i = 0; i < 44; i++) {
					if (ch.getNormalFilter()[i].getType().equals(item.getType())) {
						inFilter = true;
						break;
					}
				}

				if (ch.isNormalWhitelist() && inFilter) {
					ChunkHopperUtil hopper = new ChunkHopperUtil(h);
					Location loc = hopper.getFacing();
					if (!loc.getBlock().isEmpty()) {
						Material m = loc.getBlock().getType();
						if (m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST)) {
							Chest chest = (Chest) loc.getBlock().getState();
							if (chest.getInventory().firstEmpty() != -1) {
								chest.getInventory().addItem(item);
								return true;
							}
						} else if (m.equals(Material.HOPPER)) {
							Hopper ho = (Hopper) loc.getBlock().getState();
							if (ho.getInventory().firstEmpty() != -1) {
								ho.getInventory().addItem(item);
								return true;
							}
						}
					}
					if (h.getInventory().firstEmpty() != -1) {
						h.getInventory().addItem(item);
						return true;
					} else {
						return false;
					}

				} else if (!ch.isNormalWhitelist() && !inFilter) {
					ChunkHopperUtil hopper = new ChunkHopperUtil(h);
					Location loc = hopper.getFacing();
					if (!loc.getBlock().isEmpty()) {
						Material m = loc.getBlock().getType();

						if (m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST)) {
							Chest chest = (Chest) loc.getBlock().getState();
							if (chest.getInventory().firstEmpty() != -1) {
								chest.getInventory().addItem(item);
								return true;
							}
						} else if (m.equals(Material.HOPPER)) {
							Hopper ho = (Hopper) loc.getBlock().getState();
							if (ho.getInventory().firstEmpty() != -1) {
								ho.getInventory().addItem(item);
								return true;
							}
						}
					}
					if (h.getInventory().firstEmpty() != -1) {
						h.getInventory().addItem(item);
						return true;
					} else {
						return false;
					}
				} else
					return false;

			}
			boolean inFilter = false;
			boolean inSFilter = false;

			for (int i = 0; i < 44; i++) {
				if (ch.getNormalFilter()[i].getType().equals(item.getType())) {
					inFilter = true;
					break;
				}
			}
			for (int i = 0; i < 44; i++) {
				if (ch.getSellingFilter()[i].getType().equals(item.getType())) {
					inSFilter = true;
					break;
				}
			}

			if (ch.isSellingWhitelist() && inSFilter) {
				FileConfiguration conf = Main.getSellConf();
				try {

					double price = conf.getDouble(
							"worth." + item.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
							* item.getAmount();
					if (price <= 0) {
						if (h.getInventory().firstEmpty() != -1) {
							h.getInventory().addItem(item);
							return true;
						} else {
							return false;
						}
					}
					ch.addToSold(price);
					if (player.isOnline()) {
						Main.getEconomy().depositPlayer(player, price);
						return true;
					} else {
						ChDataHandler.setOfflineSold(ch.getOwnerUUID().toString(), price, false);
						return true;
					}
				} catch (NullPointerException e) {
				}

				if (h.getInventory().firstEmpty() != -1) {
					h.getInventory().addItem(item);
					return true;
				} else {
					return false;
				}

			} else if (!ch.isSellingWhitelist() && !inSFilter) {

				FileConfiguration conf = Main.getSellConf();
				try {

					double price = conf.getDouble(
							"worth." + item.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
							* item.getAmount();
					if (price <= 0) {
						if (h.getInventory().firstEmpty() != -1) {
							h.getInventory().addItem(item);
							return true;
						} else {
							return false;
						}
					}
					ch.addToSold(price);
					if (player.isOnline()) {
						Main.getEconomy().depositPlayer(player, price);
						return true;
					} else {
						ChDataHandler.setOfflineSold(ch.getOwnerUUID().toString(), price, false);
						return true;
					}
				} catch (NullPointerException e) {
				}
				if (h.getInventory().firstEmpty() != -1) {
					h.getInventory().addItem(item);
					return true;
				} else {
					return false;
				}

			} else if (ch.isNormalWhitelist() && inFilter) {
				ChunkHopperUtil hopper = new ChunkHopperUtil(h);
				Location loc = hopper.getFacing();
				if (!loc.getBlock().isEmpty()) {
					Material m = loc.getBlock().getType();
					if (m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST)) {
						Chest chest = (Chest) loc.getBlock().getState();
						if (chest.getInventory().firstEmpty() != -1) {
							chest.getInventory().addItem(item);
							return true;
						}
					} else if (m.equals(Material.HOPPER)) {
						Hopper ho = (Hopper) loc.getBlock().getState();
						if (ho.getInventory().firstEmpty() != -1) {
							ho.getInventory().addItem(item);
							return true;
						}
					}
				}
				if (h.getInventory().firstEmpty() != -1) {
					h.getInventory().addItem(item);
					return true;
				} else {
					return false;
				}

			} else if (!ch.isNormalWhitelist() && !inFilter) {
				ChunkHopperUtil hopper = new ChunkHopperUtil(h);
				Location loc = hopper.getFacing();
				if (!loc.getBlock().isEmpty()) {
					Material m = loc.getBlock().getType();

					if (m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST)) {
						Chest chest = (Chest) loc.getBlock().getState();
						if (chest.getInventory().firstEmpty() != -1) {
							chest.getInventory().addItem(item);
							return true;
						}
					} else if (m.equals(Material.HOPPER)) {
						Hopper ho = (Hopper) loc.getBlock().getState();
						if (ho.getInventory().firstEmpty() != -1) {
							ho.getInventory().addItem(item);
							return true;
						}
					}
				}
				if (h.getInventory().firstEmpty() != -1) {
					h.getInventory().addItem(item);
					return true;
				} else {
					return false;
				}
			} else
				return false;
		}
	}
}
