package io.github.tsecho.poketeams.commands.admin;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.ChatTypes;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.SuccessMessage;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import io.github.tsecho.poketeams.utilities.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class Kick implements CommandExecutor {

	private PokeTeamsAPI role;
	private String team;
	private CommandSource src;
	private User user;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		team = args.<String>getOne(Text.of("team")).get();
		user = args.<User>getOne(Text.of("player")).get();
		this.src = src;

		if(!Utils.teamExists(team))
			ErrorCheck.test(src, ErrorMessage.NOT_EXISTS);
		
		role = new PokeTeamsAPI(user.getName(), false);
		
		if(!role.inTeam())
			ErrorCheck.test(src, ErrorMessage.NOT_IN_TEAM);
		if(!role.getTeam().equals(team))
			ErrorCheck.test(src, ErrorMessage.OTHER_IN_TEAM);
		
		kickPlayer();

		return CommandResult.success();
	}
	
	private void kickPlayer() {
		if(role.getMemberTotal() == 1 && ConfigManager.getConfNode("Team-Settings", "Delete-When-Empty").getBoolean()) {
			messagePlayer();
			role.deleteTeam();
			ChatUtils.setChat(user.getName(), ChatTypes.PUBLIC);
		} else {
			messagePlayer();
			role.kickPlayer(user.getName());
		}
	}
	
	private void messagePlayer() {
		Sponge.getServer().getPlayer(user.getName()).ifPresent(p -> p.sendMessage(SuccessMessage.KICK_RECEIVE.getText(p)));
		src.sendMessage(Texts.of(SuccessMessage.KICK_SEND.getString(), user.getName()));
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_KICK)
				.arguments(GenericArguments.seq
						(GenericArguments.string(Text.of("team"))),
						GenericArguments.onlyOne(GenericArguments.user(Text.of("player"))))
				.executor(new Kick())
				.build();
	}
}