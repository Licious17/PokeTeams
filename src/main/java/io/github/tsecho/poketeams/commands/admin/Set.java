package io.github.tsecho.poketeams.commands.admin;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Ranks;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class Set implements CommandExecutor {

	private User player;
	private String rank;
	private PokeTeamsAPI role, newRole;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		player = args.<User>getOne(Text.of("player")).get();
		rank = args.<String>getOne(Text.of("rank")).get();
		role = new PokeTeamsAPI(player.getName(), false);
		newRole = new PokeTeamsAPI(args.<String>getOne(Text.of("team")).get(), true);

		if(role.inTeam())
			return ErrorCheck.test(src, ErrorMessages.OTHER_IN_TEAM);
		if(!newRole.teamExists())
			return ErrorCheck.test(src, ErrorMessages.NOT_EXISTS);

		newRole.addTeamMember(player.getName(), rank);
		player.getPlayer().ifPresent(p -> p.sendMessage(SuccessMessages.TEAM_CHANGE_RECEIVE.getText(p)));
		src.sendMessage(Texts.of(SuccessMessages.TEAM_CHANGE_SEND.getString(), player.getName()));

		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_SET)
				.arguments(GenericArguments.seq(
						   	GenericArguments.onlyOne(GenericArguments.user(Text.of("player"))),
						   	GenericArguments.string(Text.of("team")),
						   	GenericArguments.choices(Text.of("rank"), Ranks.getList())))
				.executor(new Set())
				.build();
	}
}
	