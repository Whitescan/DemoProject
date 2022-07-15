package de.whitescan.demoproject;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.whitescan.api.database.MySQLDatabase;
import de.whitescan.demoproject.command.MsgCommand;
import de.whitescan.demoproject.command.SudoCommand;
import de.whitescan.demoproject.command.WarnCommand;
import de.whitescan.demoproject.config.DemoConfig;
import de.whitescan.demoproject.database.DemoDAO;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 
 * @author Whitescan
 *
 */
public class DemoProject extends JavaPlugin {

	// Setup

	@Getter
	private DemoConfig demoConfig;

	@Getter
	private MySQLDatabase mysqlDatabase;

	@Getter
	private DemoDAO demoDAO;

	@Override
	public void onEnable() {
		loadConfigs();
		getCommand("warn").setExecutor(new WarnCommand(this));
		getCommand("msg").setExecutor(new MsgCommand(this));
		getCommand("sudo").setExecutor(new SudoCommand());
		getLogger().info("Enabled!");
	}

	private void loadConfigs() {
		this.demoConfig = new DemoConfig(this);

		if (getDemoConfig().isSetupModeEnabled()) {
			getLogger().warning("Setup Mode detected. Config has been created. Please finish the setup and restart!");
			Bukkit.getPluginManager().disablePlugin(this);
		}

		this.mysqlDatabase = new MySQLDatabase(getLogger(), getDemoConfig().getDbHost(), getDemoConfig().getDbPort(),
				getDemoConfig().getDbName(), getDemoConfig().getDbUser(), getDemoConfig().getDbPassword(),
				getDemoConfig().getDbTablePrefix());
		this.demoDAO = new DemoDAO(this);
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled!");
	}

	public void warn(CommandSender sender, Player target, String reason) {

		final String authorId;
		if (sender instanceof Player actor) {
			authorId = actor.getUniqueId().toString();
		} else {
			authorId = sender.getName();
		}

		Bukkit.spigot().broadcast(new TextComponent("§8[§cWarn§8] §9" + sender.getName() + " §ewarned player §c"
				+ target.getName() + " §efor: §7" + reason));

		getDemoDAO().createWarn(authorId, target.getUniqueId(), reason);

		int warncount = getDemoDAO().getWarnCount(target.getUniqueId());

		String command = getDemoConfig().getWarnTriggers().get(warncount);

		if (command != null) {
			command = command.replace("{PLAYER}", target.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		}

	}

	public void msg(Player actor, Player target, String message) {

		actor.sendMessage("§8[§2MSG§8] §b" + actor.getName() + " §e-> §b" + target.getName() + "§e: §7" + message);
		target.sendMessage("§8[§2MSG§8] §b" + actor.getName() + " §e-> §b" + target.getName() + "§e: §7" + message);

		for (Player player : Bukkit.getOnlinePlayers())
			if (player.hasPermission("demoproject.command.msg.socialspy")
					&& !(player.equals(actor) || player.equals(target)))
				player.sendMessage(
						"§8[§2SPY§8] §b" + actor.getName() + " §e-> §b" + target.getName() + "§e: §7" + message);

	}

}
