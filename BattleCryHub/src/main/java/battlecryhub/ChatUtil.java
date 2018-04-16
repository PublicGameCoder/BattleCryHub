package battlecryhub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {

	private static String prefix = "&8[&6&lBattleCry&r&8] &7";
	
	public static void sendMessage(Player player, String text) {
		String message = ChatColor.translateAlternateColorCodes('&', prefix + text);
		player.sendMessage(message);
	}
	
}
