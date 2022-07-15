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
public class MsgCommand extends BukkitCommand {

	private final DemoProject demoProject;

	public MsgCommand(DemoProject demoProject) {
		super("demoproject.command.msg", new TextComponent("§cKeine Rechte!"));
		this.demoProject = demoProject;
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
			
			demoProject.msg(actor, target, message);
			return;

			
		}

		sender.spigot().sendMessage(new TextComponent("Versuche /msg <player> <message>"));

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
