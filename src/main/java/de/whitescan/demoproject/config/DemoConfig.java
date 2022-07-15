package de.whitescan.demoproject.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import de.whitescan.api.bukkit.BukkitConfigHandler;
import lombok.Getter;

/**
 * 
 * @author Whitescan
 *
 */
public class DemoConfig extends BukkitConfigHandler {

	// Config Section

	@Getter
	private boolean setupModeEnabled;

	// SQL Section

	@Getter
	private String dbHost;

	@Getter
	private int dbPort;

	@Getter
	private String dbName;

	@Getter
	private String dbUser;

	@Getter
	private String dbPassword;

	@Getter
	private String dbTablePrefix;

	// Warn Section

	@Getter
	private Map<Integer, String> warnTriggers;

	public DemoConfig(Plugin plugin) {
		super(plugin, new File(plugin.getDataFolder(), "config.yml"));
	}

	@Override
	protected void read() {

		// Config Section

		final ConfigurationSection config = super.config.getConfigurationSection("config");

		this.setupModeEnabled = config.getBoolean("setup", true);
		if (isSetupModeEnabled()) {
			disableSetupMode();
			return;
		}

		// SQL Section

		final ConfigurationSection sql = config.getConfigurationSection("sql");

		this.dbHost = sql.getString("host", "localhost");
		this.dbPort = sql.getInt("port", 3306);
		this.dbName = sql.getString("name");
		this.dbUser = sql.getString("user", "root");
		this.dbPassword = sql.getString("password", "");

		String tablePrefix = sql.getString("table-prefix");
		if (!tablePrefix.isBlank() && !tablePrefix.endsWith("_"))
			tablePrefix = tablePrefix + "_";
		this.dbTablePrefix = tablePrefix;

		// Warn Section

		final ConfigurationSection warns = config.getConfigurationSection("warns");

		this.warnTriggers = new HashMap<>();
		for (String key : warns.getKeys(false)) {
			Integer count = Integer.valueOf(key);
			String command = warns.getString(key);
			getWarnTriggers().put(count, command);
		}

	}

	private void disableSetupMode() {
		config.set("config.setup", false);
		saveConfig();
	}

}
