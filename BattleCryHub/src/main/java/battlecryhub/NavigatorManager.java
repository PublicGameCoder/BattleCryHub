package battlecryhub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class NavigatorManager implements Listener {

	private static NavigatorManager instance;
	public static NavigatorManager getManager() {
		if (instance == null) {
			instance = new NavigatorManager();
		}
		return instance;
	}
	
	private String navigationName;
	private ItemStack navigationItem;
	private Inventory serverMenu;
	private Map<ItemStack, String> servers;
	
	@SuppressWarnings("deprecation")
	private NavigatorManager() {
		
		servers = new HashMap<ItemStack, String>();
		
		BattleCryHub.getInstance().getServer().getPluginManager().registerEvents(this, BattleCryHub.getInstance());
		BattleCryHub.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(BattleCryHub.getInstance(), "BungeeCord");
		
		navigationName = ChatColor.translateAlternateColorCodes('&', ConfigManager.getManager().getConfig().getString("Settings.Navigator.DisplayName"));
		Material mat = Material.getMaterial(ConfigManager.getManager().getConfig().getString("Settings.Navigator.Material"));
		int data = ConfigManager.getManager().getConfig().getInt("Settings.Navigator.Data");
		
		navigationItem = new ItemStack(mat,1,(short) 0, (byte) data);
		ItemMeta meta = navigationItem.getItemMeta();
		meta.setDisplayName(navigationName);
		navigationItem.setItemMeta(meta);
		
		int rows = ConfigManager.getManager().getConfig().getInt("Settings.Navigator.Rows");
		if (rows < 1) {
			rows = 1;
		}
		if (rows > 6) {
			rows = 6;
		}
		serverMenu = Bukkit.createInventory(null, rows * 9, navigationName);
		
		List<String> servers = ConfigManager.getManager().getConfig().getStringList("Servers");
		
		FileConfiguration config = ConfigManager.getManager().getConfig();
		for (String serverName : servers) {
			Material material = Material.getMaterial(config.getString(serverName+".Material"));
			int dataValue = config.getInt(serverName+".Data");
			createServerItem(serverName,material,dataValue);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void createServerItem(String server, Material material, int data) {
		ItemStack serverItem = new ItemStack(material, 1, (short)0, (byte) data);
		
		ItemMeta meta = serverItem.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6"+server));
		serverItem.setItemMeta(meta);
		
		servers.put(serverItem, server);
		serverMenu.addItem(serverItem);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		player.getInventory().remove(navigationItem);
		player.getInventory().addItem(navigationItem);	
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		player.getInventory().remove(navigationItem);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		ItemStack item = e.getItemDrop().getItemStack();
		if (isNavigator(item)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		Action action = e.getAction();
		if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
		if (!isNavigator(e.getItem()))return;
		e.getPlayer().openInventory(serverMenu);
	}
	
	@EventHandler
	public void onServerSelect(InventoryClickEvent e) {
		ItemStack item = e.getCurrentItem();
		if (item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())return;
		if (e.getClickedInventory() != e.getInventory() || !e.getInventory().getTitle().equalsIgnoreCase(serverMenu.getTitle()))return;
		
		for (Entry<ItemStack, String> entry : servers.entrySet()) {
			if (entry.getKey().getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
				sendPlayer((Player) e.getWhoClicked(), entry.getValue());
				e.setCancelled(true);
				break;
			}
		}
	}

	public void sendPlayer(Player player, String server) {
		ChatUtil.sendMessage(player, "Connecting to server: &6"+server);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(BattleCryHub.getInstance(), "BungeeCord", out.toByteArray());
    }
	
	private boolean isNavigator(ItemStack item) {
		return (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(navigationName));
	}

}
