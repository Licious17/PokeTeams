package io.github.tsecho.poketeams.commands.admin;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class Rename implements CommandExecutor {

	private PokeTeamsAPI role;
	private String team, name;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		team = args.<String>getOne(Text.of("team")).get();
		name = args.<String>getOne(Text.of("name")).get();
		role = new PokeTeamsAPI(team, true);

		if(!role.teamExists())
			src.sendMessage(ErrorMessages.NOT_EXISTS.getText(src));

		role.setTeamName(name);
		src.sendMessage(SuccessMessages.RENAMED.getText(src));
		
		return CommandResult.success();
	}


	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_RENAME)
				.arguments(GenericArguments.seq(
							GenericArguments.string(Text.of("team"))),
							GenericArguments.remainingJoinedStrings(Text.of("name")))
				.executor(new Rename())
				.build();
	}
}
