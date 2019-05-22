package io.github.tsecho.poketeams.commands.admin;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.ChatTypes;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.SuccessMessage;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
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

public class Delete implements CommandExecutor {

	private String team;
	private PokeTeamsAPI role;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
				
		team = args.<String>getOne(Text.of("team")).get();
		role = new PokeTeamsAPI(team, true);

		if(!role.teamExists())
			return ErrorCheck.test(src, ErrorMessage.NOT_EXISTS);


		ConfigManager.getStorNode("Teams", team, "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.forEach(user -> {
					ChatUtils.setChat(user, ChatTypes.PUBLIC);
					Sponge.getServer().getPlayer(user).ifPresent(p -> p.sendMessage(SuccessMessage.DISBANDED.getText(p)));
				});

		role.deleteTeam();
		src.sendMessage(SuccessMessage.DELETED_TEAM.getText(src));

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
