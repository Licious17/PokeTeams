package io.github.tsecho.poketeams.commands.queue;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.economy.EconManager;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.QueueMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
import io.github.tsecho.poketeams.PokeTeams;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.pixelmon.QueueManager;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import org.spongepowered.api.service.economy.transaction.ResultType;

public class Join implements CommandExecutor{

	private PokeTeamsAPI role;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

		role = new PokeTeamsAPI(src);

		if(!role.inTeam())
			return ErrorCheck.test(src, ErrorMessages.NOT_IN_TEAM);
		if(!role.canJoinQueue())
			return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);
		if(QueueManager.queue.contains(src.getName()))
			return ErrorCheck.test(src, QueueMessages.ALREADY_IN_QUEUE);

		if(ConfigManager.getConfNode("Battle-Settings", "queue-Fee", "Enabled").getBoolean()) {

			EconManager econ = new EconManager((Player) src);

			if(econ.isEnabled()) {

				if(QueueManager.queue.contains(src.getName()))
					return ErrorCheck.test(src, QueueMessages.ALREADY_IN_QUEUE);

				if(econ.enterQueue().getResult() != ResultType.SUCCESS)
					return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_FUNDS);

				src.sendMessage(Texts.of(QueueMessages.ADDED_QUEUE_COST.getString()
						.replaceAll("%price%", String.valueOf(ConfigManager.getConfNode("Battle-Settings", "queue-Fee", "Price").getInt())), src));

				QueueManager.queue.add(src.getName());

			} else {
				src.sendMessage(Texts.of("&cEconomy plugin is not installed! Please contact an admin with this message"));
				PokeTeams.getInstance().getLogger().error("Economy plugin not installed! Economy features will not work correctly!");
			}

		} else {
			QueueManager.queue.add(src.getName());
			src.sendMessage(QueueMessages.ADDED_QUEUE.getText(src));
		}
		
		return CommandResult.success();
	}

	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.QUEUE_JOIN)
				.executor(new Join())
				.build();
	}
}