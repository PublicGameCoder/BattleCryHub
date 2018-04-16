package battlecryhub;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinLeaveManager implements Listener {
	
	List<String> joinMessage;
	
	public JoinLeaveManager() {
		joinMessage = ConfigManager.getManager().getConfig().getStringList("Settings.JoinMessage");
		for (int i = 0; i < joinMessage.size(); i++) {
			joinMessage.set(i, ChatColor.translateAlternateColorCodes('&', joinMessage.get(i)) );
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		if (!e.getPlayer().hasPlayedBefore()) {
			String text = ConfigManager.getManager().getConfig().getString("Settings.NewMessage");
			text = text.replaceAll("%PlayerName%", e.getPlayer().getName());
			String globalMessage = ChatColor.translateAlternateColorCodes('&', text);
			Bukkit.broadcastMessage(globalMessage);
		}
		for (String line : joinMessage) {
			e.getPlayer().sendMessage(line);
		}
	}
}
