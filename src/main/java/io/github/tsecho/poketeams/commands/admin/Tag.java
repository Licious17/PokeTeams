package io.github.tsecho.poketeams.commands.admin;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.messages.SuccessMessages;
import io.github.tsecho.poketeams.language.CensorCheck;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class Tag implements CommandExecutor {

	private PokeTeamsAPI role;
	private String tag, team;
	private CensorCheck check;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		tag = args.<String>getOne(Text.of("tag")).get();
		team = args.<String>getOne(Text.of("team")).get();
		role = new PokeTeamsAPI(team, true);
		check = new CensorCheck(tag);

		if(!role.teamExists())
			return ErrorCheck.test(src, ErrorMessages.NOT_EXISTS);
		if(check.failsCensor(true))
			return ErrorCheck.test(src, ErrorMessages.INNAPROPRIATE);

		role.setTag(tag);
		src.sendMessage(SuccessMessages.TAG_CHANGE.getText(src));
				
		return CommandResult.success();
	}

	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_TAG)
				.arguments(GenericArguments.seq(
						GenericArguments.string(Text.of("team")),
						GenericArguments.string(Text.of("tag"))))
				.executor(new Tag())
				.build();
	}
}
