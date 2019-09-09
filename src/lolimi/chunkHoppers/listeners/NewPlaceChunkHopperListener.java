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
import lolimi.chunkHoppers.main.Gui;
import lolimi.chunkHoppers.main.Main;

public class NewPlaceChunkHopperListener implements Listener {
	private static Location[] locCh = new Location[50];
	private static boolean set = false;
	private static Location[] settingsLoc = new Location[50];
	//private static boolean change = false;
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
				// boolean inChChunk = new DataHandler().inChChunk(e.getBlock().getLocation());
				if (chInChunk != null) {
					blockPlacer.sendMessage(
							"§3[§6RCHoppers§3] §cThere is already a §6Chunk Hopper §cin this Chunk, you can't place another one here!");
					e.setCancelled(true);
					return false;
				}
				
				e.getPlayer().sendMessage("§3[§6RCHoppers§3] §bYou have placed down a §6Chunk Hopper§b!");
//				Hopper h = (Hopper) e.getBlock().getState();
//				h.setCustomName("§4§lRC Chunk Hopper");
//				h.set
//				h.update();
				Main.chunkHopper.add(new LevelOne(e.getBlock().getLocation(), e.getPlayer().getUniqueId()));
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
//				if (h.getCustomName().contains(ChatColor.RED + "Chunk Hopper")
//						|| h.getCustomName().contains(ChatColor.DARK_RED + "§lRC Chunk Hopper")
//						|| h.getCustomName().contains(ChatColor.RED + "§lRC Chunk Hopper: ")) {
				{
					
					Inventory inv;
					ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(h.getLocation());
					try {
						if (ch.exists()) {
							e.setCancelled(true);
							inv = Gui.getSettingsTabGui(h.getLocation());
							String placer = Bukkit.getOfflinePlayer(ch.getOwnerUUID()).getName();
							if (!placer.equals(e.getPlayer().getName())) {
								if (!e.getPlayer().hasPermission("rchopper.admin")||e.getPlayer().hasPermission("rchopper.admin")) {
									e.getPlayer().sendMessage(
											"§3[§6RCHoppers§3] §cYou can only change §6Chunk Hoppers §cplaced by you!");

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
								//change = true;
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
							e.getPlayer()
									.sendMessage("§3[§6RCHoppers§3] §cBroken §6Chunk Hopper §cdetected, breaking it!");
//							new BlockBreakEvent(e.getClickedBlock(), e.getPlayer()).setDropItems(false);
							Main.getPlugin().rmvChunkHopper(e.getClickedBlock().getLocation());

							e.getClickedBlock().setType(Material.AIR);
							;
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gch " + e.getPlayer().getName() + " 1");
						}
					} catch (NullPointerException f) {
						e.getPlayer().sendMessage("§3[§6RCHoppers§3] §cBroken §6Chunk Hopper §cdetected, breaking it!");
//						new BlockBreakEvent(e.getClickedBlock(), e.getPlayer()).setDropItems(false);
						Main.getPlugin().rmvChunkHopper(e.getClickedBlock().getLocation());

						e.getClickedBlock().setType(Material.AIR);
						;
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gch " + e.getPlayer().getName() + " 1");
						f.printStackTrace();
					}
				}
			} catch (NullPointerException f) {
			}
		}
		return false;
	}

	public static boolean isSet() {
		return set;
	}

	//public static boolean isChange() {
		//return change;
	//}

	public static boolean isStillSettingUpFilter() {
		return stillSettingUpFilter;
	}

	//public static boolean isToFilter() {
		//return toFilter;
	//}

	//private static boolean toFilter = false;

	@EventHandler
	public boolean onSettingsTabClick(InventoryClickEvent e){
		if (e.getView().getTitle().equals("§3Your Chunk Hopper Settings")) {
			ItemStack i = e.getCurrentItem();
			try {
				String n = i.getItemMeta().getDisplayName();
				if (n.equals(
						ChatColor.DARK_AQUA + "Click here to set up a filter for what items should be picked up")) {
					e.setCancelled(true);
					for (int j = 0; j < settingsLoc.length; j++) {
						if (!(settingsLoc[j] == null)) {
							if (settingsLoc[j].getBlock().getMetadata("chunkhopper").get(0).asString()
									.equals(e.getWhoClicked().getName())) {
								e.setCancelled(true);
								//toFilter = true;
								Location loc = settingsLoc[j];
								e.getWhoClicked().openInventory(Gui.getNormalFilterGui(settingsLoc[j]));
								for (int k = 0; k < filterLoc.length; k++) {
									if (filterLoc[k] == null) {
										filterLoc[k] = loc;
										//change = true;
										break;
									}
								}
								settingsLoc[j] = null;
								return true;
							}
						}
					}
				} else if(n.equals("§aClick here to upgrade this §6RCChunkHopper")) {
					e.setCancelled(true);
					for (int j = 0; j < settingsLoc.length; j++) {
						if (!(settingsLoc[j] == null)) {
							if (settingsLoc[j].getBlock().getMetadata("chunkhopper").get(0).asString()
									.equals(e.getWhoClicked().getName())) {
								e.setCancelled(true);
								ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(settingsLoc[j]);
								
								if(ch.getLevel() == 1) {
									if(e.getCursor().isSimilar(Main.upgrade1)) {
//										for(int in = e.getCursor().getAmount() - 1; in > 0; in--) {
//											e.getWhoClicked().getInventory().addItem(Main.upgrade1);
//										}
										e.getCursor().setAmount(0);
										e.getWhoClicked().closeInventory();
										
										e.getWhoClicked().sendMessage("§3[§6RCHoppers§3] §aYou have upgraded your §6Chunk Hopper §ato §clevel " + (ch.getLevel() + 1));
										ch.upgrade();
										settingsLoc[j] = null;
										return true;
									}else {
										if(e.getCursor().isSimilar(Main.upgrade2))
											e.getWhoClicked().sendMessage("§3[§6RCHoppers§3] §cYou need the §6Tier 1 §a➜ §6Tier 2 §cupgrade here!");
										else
											e.getWhoClicked().sendMessage("§3[§6RCHoppers§3] §cYou have to drop a §aChunk Hopper upgrade §chere!");
										return false;
									}
								}else if(ch.getLevel() == 2) {
									if(e.getCursor().isSimilar(Main.upgrade2)) {
//										for(int in = e.getCursor().getAmount() - 1; in > 0; in--) {
//											e.getWhoClicked().getInventory().addItem(Main.upgrade2);
//										}
										e.getCursor().setAmount(0);
										e.getWhoClicked().closeInventory();
										
										
										
										e.getWhoClicked().sendMessage("§3[§6RCHoppers§3] §aYou have upgraded your §6Chunk Hopper §ato §clevel " + (ch.getLevel() + 1));
										ch.upgrade();
										settingsLoc[j] = null;
										return true;
									}else {
										if(e.getCursor().isSimilar(Main.upgrade1))
											e.getWhoClicked().sendMessage("§3[§6RCHoppers§3] §cYou need the §6Tier 2 §a➜ §6Tier 3 §cupgrade here!");
										else
											e.getWhoClicked().sendMessage("§3[§6RCHoppers§3] §cYou have to drop a §aChunk Hopper upgrade §chere!");
										return false;
									}
								}else {
									e.getWhoClicked().sendMessage("§3[§6RCHoppers§3] §4Some error occured (ERROR 211, please report!)");
								}
							}
						}
					}
				} else if (n.equals(ChatColor.DARK_RED + "Click here to break this hopper")) {
					for (int j = 0; j < settingsLoc.length; j++) {
						if (settingsLoc[j] != null) {
							if (settingsLoc[j].getBlock().getMetadata("chunkhopper").get(0).asString()
									.equals(e.getWhoClicked().getName())) {

								ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(settingsLoc[j]);

								ch.remove();
								
								settingsLoc[j].getBlock().setType(Material.AIR);

								e.setCancelled(true);
								e.getWhoClicked().closeInventory();
								e.getWhoClicked()
										.sendMessage("§3[§6RCHoppers§3] §cYou have broken the §6Chunk Hopper§c!");
								ItemStack item = new ItemStack(Material.HOPPER);
								ItemMeta m = item.getItemMeta();
								m.setDisplayName(Main.cHDisplayName);
								m.setLore(Main.cHLore);
								item.setItemMeta(m);
								e.getWhoClicked().getInventory().addItem(item);
								settingsLoc[j] = null;
								return true;
							}
						}
					}
				} else if (n
						.equals(ChatColor.DARK_AQUA + "Click here to set up a filter for what items should be sold")) {
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
										//change = true;
										break;
									}
								}
								settingsLoc[j] = null;
								return true;
							}
						}
					}
				}else if(n.equals("§aClick here to upgrade this §6RCChunkHopper")){
					e.setCancelled(true);
				}
			} catch (NullPointerException f) {
				return false;
			}
			
		}
		return false;
	}
	
	@EventHandler
	public void onSettingsTabClose(InventoryCloseEvent e) {
		if(!e.getView().getTitle().equals("§3Your Chunk Hopper Settings")) return;
		for(int i = 0; i<settingsLoc.length;i++) {
			if(settingsLoc[i]!=null&&e.getPlayer().getName().equals(settingsLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString())) {
				settingsLoc[i] = null;
				break;
			}
		}
	}

	@EventHandler
	public boolean onFilterTabClick(InventoryClickEvent e){
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return false;
		if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Set up your filter here")) {
			for (int i = 0; i < filterLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getWhoClicked().getName())) {
						ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(filterLoc[i]);
						if (e.getCurrentItem().getType().toString().equals("AIR"))
							return false;
						if (e.getRawSlot()<=53) {
								//&& e.getClickedInventory().equals(e.getView().getTopInventory())) {
							if (e.getSlot() > 8) {
								ch.rmvFromNFilter(e.getCurrentItem(), e.getSlot());
								stillSettingUpFilter = true;
								e.setCancelled(true);
								// e.getWhoClicked().closeInventory();
								e.getView().getTopInventory().remove(e.getCurrentItem().getType());
								e.getWhoClicked().openInventory(e.getInventory());
							} else if (e.getSlot() == 7) {
								ch.changeNWhitelist();
								
								e.setCancelled(true);
								if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY+ "Whitelist")) {
									ItemStack item = e.getCurrentItem().clone();
									ItemMeta m = item.getItemMeta();
									m.setDisplayName(ChatColor.DARK_GRAY + "Blacklist");
									List<String> l = Arrays.asList(
											ChatColor.DARK_GRAY + "Picks up all items but the ones set up in this filter",
											ChatColor.DARK_GRAY + "Click to change");
									m.setLore(l);
									item.setItemMeta(m);
									item.setDurability((short) 15); 
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
									item.setDurability((short) 0);
									e.setCurrentItem(item);
								}
								//e.getWhoClicked().openInventory(e.getView());
							} else
								e.setCancelled(true);
						} else if (e.getCurrentItem() != null && e.getClickedInventory() != null && e.getRawSlot()>53) {
								//&& e.getClickedInventory().equals(e.getView().getBottomInventory()
								
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
		if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Set up your filter here")) {
			for (int i = 0; i < settingsLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getPlayer().getName())) {
						if (!stillSettingUpFilter) {
							filterLoc[i] = null;
							//change = false;
							//toFilter = false;
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
		if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Setup your Selling Filter here")) {
			for (int i = 0; i < filterLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getWhoClicked().getName())) {
						ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(filterLoc[i]);
						if (e.getCurrentItem().getType().toString().equals("AIR"))
							return;
						if (e.getRawSlot()<=53) {
								//&& e.getClickedInventory().equals(e.getView().getTopInventory())) {
							if (e.getSlot() > 8) {
								ch.rmvFromSFilter(e.getCurrentItem(), e.getSlot());
								stillSettingUpFilter = true;
								e.setCancelled(true);
								// e.getWhoClicked().closeInventory();
								e.getView().getTopInventory().remove(e.getCurrentItem().getType());
								e.getWhoClicked().openInventory(e.getInventory());
							} else if (e.getSlot() == 7) {
								ch.changeSWhitelist();
								
								e.setCancelled(true);
								if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY+ "Whitelist")) {
									ItemStack item = e.getCurrentItem().clone();
									ItemMeta m = item.getItemMeta();
									m.setDisplayName(ChatColor.DARK_GRAY + "Blacklist");
									List<String> l = Arrays.asList(
											ChatColor.DARK_GRAY + "Picks up all items but the ones set up in this filter",
											ChatColor.DARK_GRAY + "Click to change");
									m.setLore(l);
									item.setItemMeta(m);
									item.setDurability((short) 15); 
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
									item.setDurability((short) 0);
									e.setCurrentItem(item);
								}
								//e.getWhoClicked().openInventory(e.getView());
							} else
								e.setCancelled(true);
						} else if (e.getCurrentItem() != null && e.getClickedInventory() != null && e.getRawSlot()>53) {
								//&& e.getClickedInventory().equals(e.getView().getBottomInventory()
								
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
		/*if(e.getCurrentItem() == null) return;
		if(e.getSlotType().equals(SlotType.OUTSIDE)) return;
		if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Set up your Selling Filter here")) {
			e.setCancelled(true);
			for (int i = 0; i < filterLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getWhoClicked().getName())) {
						ChunkHopper ch = Main.getPlugin().isInChunkHopperChunk(filterLoc[i]);
						if (e.getRawSlot() > 8) {
							if (e.getClickedInventory().equals(e.getView().getTopInventory())) {
								ch.rmvFromSFilter(e.getCurrentItem(), e.getRawSlot());
								stillSettingUpFilter = true;
								e.getClickedInventory().clear(e.getRawSlot());
								e.getWhoClicked().openInventory(e.getClickedInventory());
							}else {
								ch.addToSFilter(e.getCurrentItem());
								ItemStack[] filters = ch.getSFilter();
								int k = 0;
								while (filters.length >= k + 1) {
									if (filters[k] != null && filters[k].getType().equals(e.getCurrentItem().getType())) {
										e.setCancelled(true);
										return;
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
						} else if(e.getRawSlot() == 7) {
							ch.changeSWhitelist();
							if (e.getCurrentItem().getType().equals(Material.WHITE_CONCRETE)) {
								ItemStack item = new ItemStack(Material.BLACK_CONCRETE);
								ItemMeta m = item.getItemMeta();
								m.setDisplayName(ChatColor.DARK_GRAY + "Blacklist");
								List<String> l = Arrays.asList(
										ChatColor.DARK_GRAY + "Sells all items but the ones set up in this filter",
										ChatColor.DARK_GRAY + "Click to change");
								m.setLore(l);
								item.setItemMeta(m);
								e.setCurrentItem(item);
							} else {
								ItemStack item = new ItemStack(Material.WHITE_CONCRETE);
								ItemMeta m = item.getItemMeta();
								m.setDisplayName(ChatColor.GRAY + "Whitelist");
								List<String> l4 = Arrays.asList(
										ChatColor.GRAY + "Only sells items set up in this filter",
										ChatColor.GRAY + "Click to change");
								m.setLore(l4);
								item.setItemMeta(m);
								e.setCurrentItem(item);
							}
							e.getWhoClicked().openInventory(e.getView());
						} else {
							
						}
						break;
					}
				}
			}
		}
	}*/

	@EventHandler
	public boolean onSFilterGuiClose(InventoryCloseEvent e) {
		if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Set up your Selling Filter here")) {
			for (int i = 0; i < settingsLoc.length; i++) {
				if (!(filterLoc[i] == null)) {
					if (filterLoc[i].getBlock().getMetadata("chunkhopper").get(0).asString()
							.equals(e.getPlayer().getName())) {
						if (!stillSettingUpFilter) {
							filterLoc[i] = null;
							//change = false;
							//toFilter = false;
						} else {
							stillSettingUpFilter = false;
						}
					}
				}
			}
		}
		return false;
	}

	public static void resetChValues() {
		locCh = new Location[50];
		settingsLoc = new Location[50];
		filterLoc = new Location[50];
		//change = false;
		set = false;
		stillSettingUpFilter = false;
		//toFilter = false;
	}
	
}
