package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.SuccessMessage;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessage;
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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class Kick implements CommandExecutor {

	private PokeTeamsAPI role, roleOther;
	private User kickUser;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		kickUser = args.<User>getOne(Text.of("player")).get();
		role = new PokeTeamsAPI(src);

		if(!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessage.NOT_PLAYER);

		roleOther = new PokeTeamsAPI(kickUser.getName(), false);

		if(!(role.inTeam() && roleOther.inTeam()) && (role.getTeam().equals(roleOther.getTeam())))
			return ErrorCheck.test(src, ErrorMessage.BOTH_NOT_IN_TEAM);
		if(!role.canKick())
			return ErrorCheck.test(src, ErrorMessage.INSUFFICIENT_RANK);
		if(src.getName().equals(kickUser.getName()))
			return ErrorCheck.test(src, ErrorMessage.KICK_YOURSELF);
		if(role.getPlace() <= roleOther.getPlace())
			return ErrorCheck.test(src, ErrorMessage.INSUFFICIENT_RANK);

		role.kickPlayer(kickUser.getName());
		src.sendMessage(Texts.of(SuccessMessage.KICK_SEND.getString(), kickUser.getName()));
		kickUser.getPlayer().ifPresent(p -> p.sendMessage(SuccessMessage.KICK_RECEIVE.getText(src)));

		return CommandResult.success();
	}

	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.KICK)
				.arguments(GenericArguments.onlyOne(GenericArguments.user(Text.of("player"))))
				.executor(new Kick())
				.build();
	}
}
