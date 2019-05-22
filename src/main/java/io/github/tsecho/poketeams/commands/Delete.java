package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.AllianceAPI;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.AllyRanks;
import io.github.tsecho.poketeams.enums.ChatTypes;
import io.github.tsecho.poketeams.enums.messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessages;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getStorNode;

public class Delete implements CommandExecutor {

	private PokeTeamsAPI role;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if (!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

		role = new PokeTeamsAPI(src);

		if(!role.inTeam())
			return ErrorCheck.test(src, ErrorMessages.NOT_IN_TEAM);
		if(!role.canDelete())
			return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);

		AllianceAPI alliance = new AllianceAPI(role);

		if(alliance.inAlliance() && alliance.getRank().equals(AllyRanks.OWNER.getName()))
			return ErrorCheck.test(src, ErrorMessages.ALLY_NEEDS_LEADER);

		getStorNode("Teams", role.getTeam(), "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.forEach(member -> {
					ChatUtils.setChat(member, ChatTypes.PUBLIC);
					Sponge.getServer().getPlayer(member).ifPresent(p -> p.sendMessage(SuccessMessages.DISBANDED.getText(src)));
				});

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
