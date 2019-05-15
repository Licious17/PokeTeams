package io.github.tsecho.poketeams.commands.admin;

import java.util.Map;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Delete implements CommandExecutor {

	private String team;
	private PokeTeamsAPI role;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
				
		team = args.<String>getOne(Text.of("team")).get();
		role = new PokeTeamsAPI(team, true);

		if(!role.teamExists())
			return ErrorCheck.test(src, ErrorMessages.NOT_EXISTS);

		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigManager
				.getStorNode("Teams", team, "Members").getChildrenMap().entrySet()) {

			String member = teams.getKey().toString();
			ChatUtils.removeChat(member);

			Sponge.getServer().getPlayer(member).ifPresent(p -> p.sendMessage(SuccessMessages.DISBANDED.getText(p)));
		}

		role.deleteTeam();
		src.sendMessage(SuccessMessages.DELETED_TEAM.getText(src));

		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_DELETE)
				.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("team"))))
				.executor(new Delete())
				.build();
	}
	
}
