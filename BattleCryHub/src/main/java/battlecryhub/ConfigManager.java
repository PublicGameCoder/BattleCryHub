package battlecryhub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

class ConfigManager {

	private static ConfigManager instance;
	public static ConfigManager getManager() {
		if (instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}
	
	private File configFile;
	
	private ConfigManager() {
		
		if (!BattleCryHub.getInstance().getDataFolder().exists()) {
			BattleCryHub.getInstance().getDataFolder().mkdirs();
		}
		
		configFile = new File(BattleCryHub.getInstance().getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setupDefaults();
		}
	}

	private void setupDefaults() {
		FileConfiguration config = getConfig();
		
		ConfigurationSection settings = config.createSection("Settings");
		settings.set("NewMessage", "&f[&6BattleCry&f] %PlayerName% has joined Battlecry for the first time!");
		saveConfig(config);
		
		String[] joinMessage = new String[12];
		joinMessage[0] = "&7&m-----------------------------------------------------";
		joinMessage[1] = "";
		joinMessage[2] = "&rWelcome to the &6&lBattleCry Network";
		joinMessage[3] = "";
		joinMessage[4] = "&7&l•&6&l Website&r&7:&r www.battlecry.com";
		joinMessage[5] = "&7&l•&6&l Twitter&r&7:&r twitter.com/BattleCry";
		joinMessage[6] = "&7&l•&6&l TeamSpeak&r&7:&r ts.battlecry.com";
		joinMessage[7] = "&7&l•&6&l Store&r&7:&r store.battlecry.com";
		joinMessage[8] = "";
		joinMessage[9] = "&rThere is currently a &6&l90% off sale &ron our store.";
		joinMessage[10] = "";
		joinMessage[11] = "&7&m-----------------------------------------------------";
		
		settings.set("JoinMessage", joinMessage);
		saveConfig(config);
		
		settings.set("Navigator.DisplayName", "&6ServerSelector");
		settings.set("Navigator.Material", Material.NETHER_STAR.toString());
		settings.set("Navigator.Data", 0);
		settings.set("Navigator.Rows", 1);
		
		List<String> servers = new ArrayList<String>();
		
		servers.add("Test");
		
		config.set("Servers", servers);
		saveConfig(config);
		for (String serverName : servers) {
			ConfigurationSection serverSection = config.createSection(serverName);
			serverSection.set("Material", Material.STAINED_CLAY.toString());
			serverSection.set("Data", 0);
			saveConfig(config);
		}
		
		saveConfig(config);
	}
	
	public FileConfiguration getConfig() {
		return YamlConfiguration.loadConfiguration(configFile);
	}
	
	public void saveConfig(FileConfiguration config) {
		try {
			config.save(configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
