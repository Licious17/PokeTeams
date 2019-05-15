package io.github.tsecho.poketeams.commands;

import java.util.Map;

import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Delete implements CommandExecutor {

	private PokeTeamsAPI role;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if (!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

		role = new PokeTeamsAPI(src);

		if (!role.inTeam())
			return ErrorCheck.test(src, ErrorMessages.NOT_IN_TEAM);
		if (!role.canDelete())
			return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);

		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigManager
				.getStorNode("Teams", role.getTeam(), "Members").getChildrenMap().entrySet()) {

			ChatUtils.removeChat(teams.getKey().toString());

			Sponge.getServer().getPlayer(teams.getKey().toString()).ifPresent(p ->
					p.sendMessage(SuccessMessages.DISBANDED.getText(src)));
	    }

		role.deleteTeam();

		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.DELETE)
				.executor(new Delete())
				.build();
	}
}
