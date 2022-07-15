package de.whitescan.demoproject.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.whitescan.api.bukkit.BukkitCommand;
import de.whitescan.api.utils.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 
 * @author Whitescan
 *
 */
public class SudoCommand extends BukkitCommand {

	public SudoCommand() {
		super("demoproject.command.sudo", new TextComponent("§cKeine Rechte!"));
	}

	@Override
	protected void executeCommand(CommandSender sender, String label, String[] args) {

		if (!(sender instanceof Player actor)) {
			sender.spigot().sendMessage(new TextComponent("Ja ne geh weg!!!!"));
			return;
		}

		if (args.length >= 2) {

			Player target = Bukkit.getPlayer(args[0]);

			if (target == null || !actor.canSee(target)) {
				sender.spigot().sendMessage(new TextComponent("§cCould not find player..."));
				return;
			}

			// FIXME Test phase
//			if (target.equals(sender)) {
//				sender.spigot().sendMessage(new TextComponent("§cYou can interact with yourself!"));
//				return;
//			}

			final String message = StringUtils.join(args, " ", 1, args.length);

			if (message.isBlank()) {
				sender.spigot().sendMessage(new TextComponent("§cMessage must be provided!"));
				return;
			}

			if (message.startsWith("/")) {
				target.performCommand(message.substring(1));
			} else {
				target.chat(message);
			}

			actor.sendMessage("Voila!");
			return;

		}

		sender.spigot().sendMessage(new TextComponent("Versuche /sudo <player> <message>"));

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
