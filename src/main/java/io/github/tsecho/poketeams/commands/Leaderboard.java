package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.LeaderboardAPI;
import io.github.tsecho.poketeams.enums.LeaderboardTypes;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class Leaderboard implements CommandExecutor {

    @Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
    	Optional<String> opt = args.getOne(Text.of("type"));

    	if(opt.isPresent()) {
    		new LeaderboardAPI(LeaderboardTypes.valueOf(opt.get().toUpperCase())).sendTo((Player) src);
		} else {
			PaginationList.builder()
					.title(Texts.of("&6Leaderboard Types"))
					.padding(Texts.of("&7="))
					.contents(Texts.of("&6/teams leaderboard rating"),
							  Texts.of("&6/teams leaderboard record"),
							  Texts.of("&6/teams leaderboard caught"),
							  Texts.of("&6/teams leaderboard legends"),
							  Texts.of("&6/teams leaderboard kills"),
							  Texts.of("&6/teams leaderboard bal"))
					.sendTo(src);
		}

		return CommandResult.success();
	}

	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.LEADERBOARD)
				.arguments(GenericArguments.optional(
						GenericArguments.choices(Text.of("type"), LeaderboardTypes.getChoices())))
				.executor(new Leaderboard())
				.build();
	}

}
