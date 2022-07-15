package de.whitescan.demoproject.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.whitescan.api.bukkit.BukkitCommand;
import de.whitescan.api.utils.StringUtils;
import de.whitescan.demoproject.DemoProject;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 
 * @author Whitescan
 *
 */
public class WarnCommand extends BukkitCommand {

	private final DemoProject demoProject;

	public WarnCommand(DemoProject demoProject) {
		super("demoproject.command.warn", new TextComponent("§cYou don't have permission to do that!"));
		this.demoProject = demoProject;
	}

	@Override
	protected void executeCommand(CommandSender sender, String label, String[] args) {

		if (args.length >= 2) {

			final Player target = Bukkit.getPlayer(args[0]);

			if (target == null || (sender instanceof Player actor && !actor.canSee(target))) {
				sender.spigot().sendMessage(new TextComponent("§cCould not find player..."));
				return;
			}

			// FIXME Test phase
//			if (target.equals(sender)) {
//				sender.spigot().sendMessage(new TextComponent("§cYou can interact with yourself!"));
//				return;
//			}

			final String reason = StringUtils.join(args, " ", 1, args.length);

			if (reason.isBlank()) {
				sender.spigot().sendMessage(new TextComponent("§cReason must be provided!"));
				return;
			}
			
			demoProject.warn(sender, target, reason);
			return;

		}

		sender.spigot().sendMessage(new TextComponent("§cWrong argument! §aTry -> /warn <player> <reason>"));

	}

	@Override
	protected List<String> tabComplete(Player actor, String label, String[] args) {

		if (args.length == 1) {

			List<String> complete = new ArrayList<>();

			for (Player player : Bukkit.getOnlinePlayers())
				if (actor.canSee(player))
					complete.add(player.getName());

			return complete;

		}

		return new ArrayList<>();

	}

}
