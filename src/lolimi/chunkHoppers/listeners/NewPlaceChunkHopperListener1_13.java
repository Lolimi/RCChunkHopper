package lolimi.chunkHoppers.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import lolimi.chunkHoppers.chunkHoppers.ChunkHopper;
import lolimi.chunkHoppers.chunkHoppers.LevelOne;
import lolimi.chunkHoppers.chunkHoppers.LevelThree;
import lolimi.chunkHoppers.chunkHoppers.LevelTwo;
import lolimi.chunkHoppers.main.Gui;
import lolimi.chunkHoppers.main.Main;

public class NewPlaceChunkHopperListener1_13 implements Listener {
	private static Location[] locCh = new Location[50];
	private static boolean set = false;
	private static Location[] settingsLoc = new Location[50];
	private static Location[] filterLoc = new Location[50];
	private static boolean stillSettingUpFilter = false;

	@EventHandler
	public boolean onPlaceChunkHopper(BlockPlaceEvent e) {

		ItemStack placedBlock = new ItemStack(e.getItemInHand());
		ItemMeta placedBlockMeta = placedBlock.getItemMeta();
		Player blockPlacer = e.getPlayer();

		if (placedBlock.getType() == Material.HOPPER) {
			if (placedBlockMeta.getDisplayName().equals(Main.cHDisplayName)
					&& placedBlockMeta.getLore().equals(Main.cHLore)) {
				ChunkHopper chInChunk = Main.getPlugin().isInChunkHopperChunk(e.getBlock().getLocation());
				if (chInChunk != null) {
					blockPlacer.sendMessage(Main.prefix
							+ "§cThere is already a §6Chunk Hopper §cin this Chunk, you can't place another one here!");
					e.setCancelled(true);
					return false;
				}

				e.getPlayer().sendMessage(Main.prefix + "§bYou have placed down a §6Chunk Hopper§b!");
				if (Main.useLevel)
					Main.chunkHopper.add(new LevelOne(e.getBlock().getLocation(), e.getPlayer().getUniqueId()));
				else if(Main.useSell)
					Main.chunkHopper.add(new LevelThree(e.getBlock().getLocation(), true, e.getPlayer().getUniqueId()));
				else
					Main.chunkHopper.add(new LevelTwo(e.getBlock().getLocation(), true, e.getPlayer().getUniqueId()));
				return true;

			}
		}
		return false;
	}

	public static Location[] getLocCh() {
		return locCh;
	}

	public static Location[] getSettingsLoc() {
		return settingsLoc;
	}

	public static Location[] getFilterLoc() {
		return filterLoc;
	}

	@EventHandler
	public boolean onChShiftClick(PlayerInteractEvent e) {
		try {
			if (e.getItem().getItemMeta().getLore().equals(Main.cHLore))
				return false;
		} catch (NullPointerException f) {
		}
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getPlayer().isSneaking()
				&& e.getClickedBlock().getType().equals(Material.HOPPER)) {
			Hopper h = (Hopper) e.getClickedBlock().getState();
			try {
				Inventory inv;
				ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(h.getLocation());
				try {
					if (ch.exists()) {
						e.setCancelled(true);
						inv = Gui.getSettingsTabGui(h.getLocation());
						String placer = Bukkit.getOfflinePlayer(ch.getOwnerUUID()).getName();
						if (!placer.equals(e.getPlayer().getName())) {
							if (!e.getPlayer().hasPermission("rchopper.admin")
									|| e.getPlayer().hasPermission("rchopper.admin")) {
								e.getPlayer().sendMessage(
										Main.prefix + "§cYou can only change §6Chunk Hoppers §cplaced by you!");
							} else {
								for (int j = 0; j < settingsLoc.length; j++) {
									if (settingsLoc[j] == null) {
										settingsLoc[j] = e.getClickedBlock().getLocation();

										e.getClickedBlock().setMetadata("chunkhopper",
												new FixedMetadataValue(Main.getPlugin(), placer));
										break;
									}
								}
							}
						}
						if (placer.equals(e.getPlayer().getName())) {
							e.getPlayer().openInventory(inv);
							for (int j = 0; j < settingsLoc.length; j++) {
								if (settingsLoc[j] == null) {
									settingsLoc[j] = e.getClickedBlock().getLocation();

									e.getClickedBlock().setMetadata("chunkhopper",
											new FixedMetadataValue(Main.getPlugin(), e.getPlayer().getName()));
									break;
								}
							}
							return true;
						}
					} else {
						e.getPlayer().sendMessage(Main.prefix + "§cBroken §6Chunk Hopper §cdetected, breaking it!");
						Main.getPlugin().rmvChunkHopper(e.getClickedBlock().getLocation());

						e.getClickedBlock().setType(Material.AIR);
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gch " + e.getPlayer().getName() + " 1");
					}
				} catch (NullPointerException f) {
					e.getPlayer().sendMessage(Main.prefix + "§cBroken §6Chunk Hopper §cdetected, breaking it!");
					Main.getPlugin().rmvChunkHopper(e.getClickedBlock().getLocation());

					e.getClickedBlock().setType(Material.AIR);
					;
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gch " + e.getPlayer().getName() + " 1");

				}

			} catch (NullPointerException f) {
			}
		}
		return false;
	}

	public static boolean isSet() {
		return set;
	}

	public static boolean isStillSettingUpFilter() {
		return stillSettingUpFilter;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public boolean onSettingsTabClick(InventoryClickEvent e) {
		if (e.getView().getTitle().equals(Main.chSettingsInvName)) {
			ItemStack i = e.getCurrentItem();
			try {
				String n = i.getItemMeta().getDisplayName();
				if (n.equals(Main.toFilterItemName)) {
					e.setCancelled(true);
					for (int j = 0; j < settingsLoc.length; j++) {
						if (!(settingsLoc[j] == null)) {
							if (settingsLoc[j].getBlock().getMetadata("chunkhopper").get(0).asString()
									.equals(e.getWhoClicked().getName())) {
								e.setCancelled(true);
								Location loc = settingsLoc[j];
								e.getWhoClicked().openInventory(Gui.getNormalFilterGui(settingsLoc[j]));
								for (int k = 0; k < filterLoc.length; k++) {
									if (filterLoc[k] == null) {
										filterLoc[k] = loc;
										// change = true;
										break;
									}
								}
								settingsLoc[j] = null;
								return true;
							}
						}
					}
				} else if (n.equals(Main.toUpgradeItemName)) {
					e.setCancelled(true);
					for (int j = 0; j < settingsLoc.length; j++) {
						if (!(settingsLoc[j] == null)) {
							if (settingsLoc[j].getBlock().getMetadata("chunkhopper").get(0).asString()
									.equals(e.getWhoClicked().getName())) {
								e.setCancelled(true);
								ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(settingsLoc[j]);

								if (ch.getLevel() == 1) {
									if (e.getCursor().isSimilar(Main.upgrade1)) {
										for (int in = e.getCursor().getAmount() - 1; in > 0; in--) {
											e.getWhoClicked().getInventory().addItem(Main.upgrade1);
										}
										e.setCancelled(true);
										e.setCursor(null);
										e.getCursor().setType(Material.AIR);
										e.getCursor().setAmount(0);
										Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

											@Override
											public void run() {
												e.getWhoClicked().closeInventory();

											}
										}, 3);

										e.getWhoClicked()
												.sendMessage(Main.prefix
														+ "§aYou have upgraded your §6Chunk Hopper §ato §clevel "
														+ (ch.getLevel() + 1));
										ch.upgrade();
										settingsLoc[j] = null;
										return true;
									} else {
										if (e.getCursor().isSimilar(Main.upgrade2))
											e.getWhoClicked().sendMessage(Main.prefix
													+ "§cYou need the §6Tier 1 §a➜ §6Tier 2 §cupgrade here!");
										else
											e.getWhoClicked().sendMessage(Main.prefix
													+ "§cYou have to drop a §aChunk Hopper upgrade §chere!");
										return false;
									}
								} else if (ch.getLevel() == 2) {
									if (e.getCursor().isSimilar(Main.upgrade2)) {
										for (int in = e.getCursor().getAmount() - 1; in > 0; in--) {
											e.getWhoClicked().getInventory().addItem(Main.upgrade2);
										}
										e.setCancelled(true);
										e.setCursor(null);
										e.getCursor().setType(Material.AIR);
										e.getCursor().setAmount(0);

										Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

											@Override
											public void run() {
												e.getWhoClicked().closeInventory();

											}
										}, 3);

										e.getWhoClicked()
												.sendMessage(Main.prefix
														+ "§aYou have upgraded your §6Chunk Hopper §ato §clevel "
														+ (ch.getLevel() + 1));
										ch.upgrade();
										settingsLoc[j] = null;
										return true;
									} else {
										if (e.getCursor().isSimilar(Main.upgrade1))
											e.getWhoClicked().sendMessage(Main.prefix
													+ "§cYou need the §6Tier 2 §a➜ §6Tier 3 §cupgrade here!");
										else
											e.getWhoClicked().sendMessage(Main.prefix
													+ "§cYou have to drop a §aChunk Hopper upgrade §chere!");
										return false;
									}
								} else {
									e.getWhoClicked().sendMessage(
											Main.prefix + "§4Some error occured (ERROR 211, please report!)");
								}
							}
						}
					}
				} else if (n.equals(Main.toBreakItemName)) {
					for (int j = 0; j < settingsLoc.length; j++) {
						if (settingsLoc[j] != null) {
							if (settingsLoc[j].getBlock().getMetadata("chunkhopper").get(0).asString()
									.equals(e.getWhoClicked().getName())) {

								ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(settingsLoc[j]);

								ch.remove();

								settingsLoc[j].getBlock().setType(Material.AIR);

								e.setCancelled(true);
								e.getWhoClicked().closeInventory();
								if(Main.useLevel) {
									if(ch.getLevel() >= 2)
										e.getWhoClicked().getInventory().addItem(Main.upgrade1);
									if(ch.getLevel() == 3)
										e.getWhoClicked().getInventory().addItem(Main.upgrade2);
								}
								e.getWhoClicked().sendMessage(Main.prefix + "§cYou have broken the §6Chunk Hopper§c!");
								ItemStack item = new ItemStack(Material.HOPPER);
								ItemMeta m = item.getItemMeta();
								m.setDisplayName(Main.cHDisplayName);
								m.setLore(Main.cHLore);
								item.setItemMeta(m);
								e.getWhoClicked().getInventory().addItem(item);
								settingsLoc[j] = null;
								new BlockBreakEvent(ch.getLocation().getBlock(), (Player) e.getWhoClicked());
								return true;
							}
						}
					}
				} else if (n
						.equals(Main.toSellingFilterItemName)) {
					for (int j = 0; j < filterLoc.length; j++) {
						if (!(settingsLoc[j] == null)) {
							if (settingsLoc[j].getBlock().getMetadata("chunkhopper").get(0).asString()
									.equals(e.getWhoClicked().getName())) {
								e.setCancelled(true);
								Location loc = settingsLoc[j];
								e.getWhoClicked().openInventory(Gui.getSellingFillterGui(settingsLoc[j]));
								for (int k = 0; k < filterLoc.length; k++) {
									if (filterLoc[k] == null) {
										filterLoc[k] = loc;
										break;
									}
								}
								settingsLoc[j] = null;
								return true;
							}
						}
					}
				}
			} catch (NullPointerException f) {
				return false;
			}
		}
		return false;
	}

	@EventHandler
	public void onSettingsTabClose(InventoryCloseEvent e) {
		if (!e.getView().getTitle().equals(Main.chSettingsInvName))
			return;
		for (int i = 0; i < settingsLoc.length; i++) {
			if (settingsLoc[i] != null && e.getPlayer().getName()
					.equals(settingsLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString())) {
				settingsLoc[i] = null;
				break;
			}
		}
	}

	@EventHandler
	public boolean onFilterTabClick(InventoryClickEvent e) {

		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
			return false;
		if (e.getView().getTitle().equals(Main.normalFilterInvName)) {
			for (int i = 0; i < filterLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getWhoClicked().getName())) {
						ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(filterLoc[i]);
						if (e.getCurrentItem().getType().toString().equals("AIR"))
							return false;
						if (e.getRawSlot() <= 53) {
							if (e.getSlot() > 8) {
								ch.rmvFromNFilter(e.getCurrentItem(), e.getSlot());
								stillSettingUpFilter = true;
								e.setCancelled(true);
								e.getView().getTopInventory().remove(e.getCurrentItem().getType());
								e.getWhoClicked().openInventory(e.getInventory());
							} else if (e.getSlot() == 7) {
								ch.changeNWhitelist();

								e.setCancelled(true);
								if (e.getCurrentItem().getItemMeta().getDisplayName()
										.equals(ChatColor.GRAY + "Whitelist")) {
									ItemStack item = e.getCurrentItem().clone();
									ItemMeta m = item.getItemMeta();
									m.setDisplayName(ChatColor.DARK_GRAY + "Blacklist");
									List<String> l = Arrays.asList(
											ChatColor.DARK_GRAY
													+ "Picks up all items but the ones set up in this filter",
											ChatColor.DARK_GRAY + "Click to change");
									m.setLore(l);
									item.setItemMeta(m);
									item.setType(Material.BLACK_TERRACOTTA);
									e.setCurrentItem(item);
								} else {
									ItemStack item = e.getCurrentItem();
									ItemMeta m = item.getItemMeta();
									m.setDisplayName(ChatColor.GRAY + "Whitelist");
									List<String> l4 = Arrays.asList(
											ChatColor.GRAY + "Only picks up items set up in this filter",
											ChatColor.GRAY + "Click to change");
									m.setLore(l4);
									item.setItemMeta(m);
									item.setType(Material.WHITE_TERRACOTTA);
									e.setCurrentItem(item);
								}
							} else
								e.setCancelled(true);
						} else if (e.getCurrentItem() != null && e.getClickedInventory() != null
								&& e.getRawSlot() > 53) {

							ItemStack[] filters = ch.getNormalFilter();
							int k = 0;
							while (filters.length >= k + 1) {
								if (filters[k] != null && filters[k].getType().equals(e.getCurrentItem().getType())) {
									e.setCancelled(true);
									return false;
								}
								k++;
							}
							ch.addToNFilter(e.getCurrentItem());
							e.setCancelled(true);

							for (int j = 9; j <= 53; j++) {
								try {
									e.getView().getTopInventory().getItem(j).getItemMeta();
								} catch (Exception f) {
									stillSettingUpFilter = true;
									e.getInventory().setItem(j, new ItemStack(e.getCurrentItem().getType()));
									e.getWhoClicked().openInventory(e.getInventory());
									break;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	@EventHandler
	public boolean onFilterGuiClose(InventoryCloseEvent e) {
		if (e.getView().getTitle().equals(Main.normalFilterInvName)) {
			for (int i = 0; i < settingsLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getPlayer().getName())) {
						if (!stillSettingUpFilter) {
							filterLoc[i] = null;
						} else {
							stillSettingUpFilter = false;
						}
					}
				}
			}
		}
		return false;
	}

	@EventHandler
	public void onSFilterClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
			return;
		if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Setup your Selling Filter here")) {
			for (int i = 0; i < filterLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getWhoClicked().getName())) {
						ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(filterLoc[i]);
						if (e.getCurrentItem().getType().toString().equals("AIR"))
							return;
						if (e.getRawSlot() <= 53) {
							if (e.getSlot() > 8) {
								ch.rmvFromSFilter(e.getCurrentItem(), e.getSlot());
								stillSettingUpFilter = true;
								e.setCancelled(true);
								e.getWhoClicked().closeInventory();
								e.getView().getTopInventory().remove(e.getCurrentItem().getType());
								e.getWhoClicked().openInventory(e.getInventory());
							} else if (e.getSlot() == 7) {
								ch.changeSWhitelist();

								e.setCancelled(true);
								if (e.getCurrentItem().getItemMeta().getDisplayName()
										.equals(ChatColor.GRAY + "Whitelist")) {
									ItemStack item = e.getCurrentItem().clone();
									ItemMeta m = item.getItemMeta();
									m.setDisplayName(ChatColor.DARK_GRAY + "Blacklist");
									List<String> l = Arrays.asList(
											ChatColor.DARK_GRAY
													+ "Picks up all items but the ones set up in this filter",
											ChatColor.DARK_GRAY + "Click to change");
									m.setLore(l);
									item.setItemMeta(m);
									item.setType(Material.BLACK_TERRACOTTA);
									e.setCurrentItem(item);
								} else {
									ItemStack item = e.getCurrentItem();
									ItemMeta m = item.getItemMeta();
									m.setDisplayName(ChatColor.GRAY + "Whitelist");
									List<String> l4 = Arrays.asList(
											ChatColor.GRAY + "Only picks up items set up in this filter",
											ChatColor.GRAY + "Click to change");
									m.setLore(l4);
									item.setItemMeta(m);
									item.setType(Material.WHITE_TERRACOTTA);
									e.setCurrentItem(item);
								}
							} else
								e.setCancelled(true);
						} else if (e.getCurrentItem() != null && e.getClickedInventory() != null
								&& e.getRawSlot() > 53) {

							ItemStack[] filters = ch.getSellingFilter();
							int k = 0;
							while (filters.length >= k + 1) {
								if (filters[k] != null && filters[k].getType().equals(e.getCurrentItem().getType())) {
									e.setCancelled(true);
									return;
								}
								k++;
							}
							ch.addToSFilter(e.getCurrentItem());
							e.setCancelled(true);

							for (int j = 9; j <= 53; j++) {
								try {
									e.getView().getTopInventory().getItem(j).getItemMeta();
								} catch (Exception f) {
									f.printStackTrace();
									stillSettingUpFilter = true;
									e.getInventory().setItem(j, new ItemStack(e.getCurrentItem().getType()));
									e.getWhoClicked().openInventory(e.getInventory());
									break;
								}

							}
						}
					}
				}
			}
		}
		return;
	}

	@EventHandler
	public boolean onSFilterGuiClose(InventoryCloseEvent e) {
		if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Setup your Selling Filter here")) {
			for (int i = 0; i < settingsLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getPlayer().getName())) {
						if (!stillSettingUpFilter) {
							filterLoc[i] = null;
						} else {
							stillSettingUpFilter = false;
						}
					}
				}
			}
		}
		return false;
	}

}
