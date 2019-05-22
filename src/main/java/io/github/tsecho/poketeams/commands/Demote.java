package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.enums.messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.messages.SuccessMessages;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.language.Texts;
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

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;

public class Demote implements CommandExecutor {

	private PokeTeamsAPI role, roleOther;
	private User demoPlayer;
	private CommandSource src;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		demoPlayer = args.<User>getOne(Text.of("player")).get();
		this.src = src;
		roleOther = new PokeTeamsAPI(demoPlayer.getName(), false);

		if (!(src instanceof Player)) {
			doAction();
			return CommandResult.success();
		}

		role = new PokeTeamsAPI(src);

		if(!role.inTeam() || !roleOther.inTeam() || !role.getTeam().equals(roleOther.getTeam()))
			return ErrorCheck.test(src, ErrorMessages.BOTH_NOT_IN_TEAM);
		if(!role.canDemote())
			return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);

		if(roleOther.getPlace() - 1 < 0)
			return ErrorCheck.test(src, ErrorMessages.CANT_DEMOTE);
		if(role.getPlace() <= roleOther.getPlace())
			return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);

		doAction();

		return CommandResult.success();
	}

	private void doAction() {
		roleOther.setRole(roleOther.getPlace() - 1);
		src.sendMessage(Texts.of(SuccessMessages.DEMOTED_ADMIN.getString(), demoPlayer.getName()));
		demoPlayer.getPlayer().ifPresent(p -> p.sendMessage(SuccessMessages.DEMOTED.getText(src)));
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.DEMOTE)
				.arguments(GenericArguments.onlyOne(GenericArguments.user(Text.of("player"))))
				.executor(new Demote())
				.build();
	}
}
