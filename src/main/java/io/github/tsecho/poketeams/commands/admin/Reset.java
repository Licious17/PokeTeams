package io.github.tsecho.poketeams.commands.admin;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.messages.SuccessMessages;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class Reset implements CommandExecutor {

	private String team;
	private PokeTeamsAPI role;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		team = args.<String>getOne(Text.of("team")).get();
		role = new PokeTeamsAPI(team, true);

		if(!role.teamExists())
			return ErrorCheck.test(src, ErrorMessages.NOT_EXISTS);

		ConfigManager.getStorNode("Teams", team, "Record", "Wins").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Record", "Losses").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Stats", "Kills").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Stats", "Caught").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Stats", "Legends").setValue(0);
		ConfigManager.save();

		src.sendMessage(SuccessMessages.RESET_TEAM.getText(src));

		return CommandResult.success();
	}

	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_RESET)
				.arguments(GenericArguments.remainingJoinedStrings(Text.of("team")))
				.executor(new Reset())
				.build();
	}
}
