package battlecryhub;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleCryHub extends JavaPlugin {

	private static BattleCryHub instance;
	
	@Override
	public void onEnable() {
		instance = this;
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new JoinLeaveManager(), this);
		
		ConfigManager.getManager();
		NavigatorManager.getManager();
	}
	
	static BattleCryHub getInstance() {
		return instance;
	}
}
