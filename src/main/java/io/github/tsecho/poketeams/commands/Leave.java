package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;

public class Leave implements CommandExecutor {

	private PokeTeamsAPI role;
	private Player player;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

		player = (Player) src;
		role = new PokeTeamsAPI(player);

		if(!role.inTeam())
			return ErrorCheck.test(src, ErrorMessages.NOT_IN_TEAM);
				
		if(canOwnerDelete()) {

			src.sendMessage(SuccessMessages.LEFT.getText(src));
			ConfigManager.getStorNode("Teams", role.getTeam(), "Members", src.getName()).setValue(null);
			ConfigManager.save();
			ChatUtils.removeChat(player.getName());

			if(ConfigManager.getStorNode("Teams", role.getTeam(), "Members").getChildrenMap().entrySet().isEmpty()) {
				if(ConfigManager.getConfNode("Team-Settings", "Delete-When-Empty").getBoolean()) {
					role.deleteTeam();
					src.sendMessage(SuccessMessages.DELETED_TEAM.getText(src));
				}
			}

		} else {

			if(role.getPlace() == 4)
				return ErrorCheck.test(src, ErrorMessages.NEEDS_LEADER);

			role.kickPlayer(player.getName());
			src.sendMessage(SuccessMessages.LEFT.getText(src));
		}

		return CommandResult.success();
	}

	private boolean canOwnerDelete() {
		return role.getRank().equals("Owner") && role.getMemberTotal() == 1;
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.LEAVE)
				.executor(new Leave())
				.build();
	}
}
