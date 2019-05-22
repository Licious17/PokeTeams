package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.InfoBuilderAPI;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessage;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import io.github.tsecho.poketeams.utilities.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class Info implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		Optional<String> teamOpt = args.<String>getOne(Text.of("team"));

		if (teamOpt.isPresent()) {

			String teamName = teamOpt.get();

			if(!Utils.teamExists(teamName))
				ErrorCheck.test(src, ErrorMessage.NOT_EXISTS);

			InfoBuilderAPI.builder(teamName, src)
					.addName()
					.addRating()
					.addBase()
					.addWinLoss()
					.addKills()
					.addCaught()
					.addLegendCaught()
					.addBank()
					.addPlayerList()
					.send();

		} else if(src instanceof Player) {

			PokeTeamsAPI role = new PokeTeamsAPI(src);

			if(role.teamExists()) {
				InfoBuilderAPI.builder((Player) src, (Player) src)
						.addName()
						.addRating()
						.addBase()
						.addWinLoss()
						.addKills()
						.addCaught()
						.addLegendCaught()
						.addBank()
						.addRank()
						.addPlayerList()
						.sendWithFeedback();
			} else {
				src.sendMessage(ErrorMessage.NOT_IN_TEAM.getText(src));
			}

		} else {
			src.sendMessage(TechnicalMessage.NOT_PLAYER.getText(src));
		}

		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.INFO)
				.arguments(GenericArguments.optionalWeak(GenericArguments.remainingJoinedStrings(Text.of("team"))))
				.executor(new Info())
				.build();
	}
}
