package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
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

public class Promote implements CommandExecutor {

	private PokeTeamsAPI role, roleOther;
	private User promoPlayer;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		promoPlayer = args.<User>getOne(Text.of("player")).get();

		if(!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

		role = new PokeTeamsAPI(src);
		roleOther = new PokeTeamsAPI(promoPlayer.getName(), false);

		if((!role.inTeam() || !roleOther.inTeam()) || (!role.getTeam().equals(roleOther.getTeam())))
			return ErrorCheck.test(src, ErrorMessages.BOTH_NOT_IN_TEAM);
		if(!role.canPromote())
			return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);
		if(roleOther.getPlace() + 1 >= 5)
			return ErrorCheck.test(src, ErrorMessages.CANT_PROMOTE);
		if(role.getPlace() <= (roleOther.getPlace() + 1))
			if(role.getPlace() != 4)
				return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);

		if(roleOther.getPlace() == 3 && role.getPlace() == 4) {

			roleOther.setRole(roleOther.getPlace() + 1);
			role.setRole(role.getPlace() - 1);

			src.sendMessage(SuccessMessages.PROMOTE_SEND.getText(src));
			src.sendMessage(SuccessMessages.DEMOTE_SEND.getText(src));
			promoPlayer.getPlayer().ifPresent(p -> p.sendMessage(SuccessMessages.PROMOTED.getText((p))));

		} else {
			roleOther.setRole(roleOther.getPlace() + 1);
			promoPlayer.getPlayer().ifPresent(p -> p.sendMessage(SuccessMessages.PROMOTED.getText((p))));
			src.sendMessage(SuccessMessages.PROMOTE_SEND.getText(src));
		}
		return CommandResult.success();
	}

	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.PROMOTE)
				.arguments(GenericArguments.onlyOne(GenericArguments.user(Text.of("player"))))
				.executor(new Promote())
				.build();
	}
}
