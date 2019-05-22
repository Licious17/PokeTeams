package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.Ranks;
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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

public class Invite implements CommandExecutor {

	private CommandSource receiver, sender;
	private PokeTeamsAPI role, roleOther;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
			sender = src;
			receiver = args.<Player>getOne(Text.of("player")).get();
			role = new PokeTeamsAPI(src);
			roleOther = new PokeTeamsAPI(receiver);

			if(!(src instanceof Player))
				return ErrorCheck.test(src, TechnicalMessage.NOT_PLAYER);
			if(!role.inTeam())
				return ErrorCheck.test(src, ErrorMessage.NOT_IN_TEAM);
			if(!role.canInvite())
				return ErrorCheck.test(src, ErrorMessage.INSUFFICIENT_RANK);
			if(role.getMemberTotal() >= role.getMaxPlayers())
				return ErrorCheck.test(src, ErrorMessage.MAX_MEMBERS);
			if(roleOther.inTeam())
				return ErrorCheck.test(src, ErrorMessage.OTHER_IN_TEAM);

			receiver.sendMessage(Text.builder()
					    .append(SuccessMessage.INVITED.getText(sender))
						.append(Texts.of("\n"))
					    .append(SuccessMessage.INVITE_CLICK.getText(sender))
						.onClick(TextActions.executeCallback(callback -> joinTeam()))
						.build());

			sender.sendMessage(SuccessMessage.SEND_INVITE.getText(src));

		return CommandResult.success();
	}
	
	private void joinTeam() {
		role.addTeamMember(receiver, Ranks.GRUNT.getName());
		receiver.sendMessage(SuccessMessage.JOINED_TEAM.getText(sender));
		sender.sendMessage(SuccessMessage.INVITE_ACCEPTED.getText(receiver));
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.INVITE)
				.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
				.executor(new Invite())
				.build();
	}
}

