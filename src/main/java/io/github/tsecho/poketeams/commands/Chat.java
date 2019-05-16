package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.ChatTypes;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

public class Chat implements CommandExecutor {

	private PokeTeamsAPI role;
	private CommandSource src;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		this.src = src;

		if(!(src instanceof Player))
			src.sendMessage(TechnicalMessages.NOT_PLAYER.getText(src));

		role = new PokeTeamsAPI(src);

		if(!role.inTeam())
			src.sendMessage(ErrorMessages.NOT_IN_TEAM.getText(src));
		if(args.getOne(Text.of("message")).isPresent())
			return sendMessage(args);

		if(ChatUtils.getChatType(src.getName()) != ChatTypes.PUBLIC) {
			ChatUtils.setChat(src.getName(), ChatTypes.PUBLIC);
			src.sendMessage(SuccessMessages.REMOVED_CHAT.getText(src));
		} else {
			ChatUtils.setChat(src.getName(), ChatTypes.TEAM);
			src.sendMessage(SuccessMessages.ADDED_CHAT.getText(src));
		}

		return CommandResult.success();
	}
	
	private CommandResult sendMessage(CommandContext args) {

		String prefix = ConfigManager.getConfNode("Chat-Settings", "Prefix").getString();
		String chatColor = ConfigManager.getConfNode("Chat-Settings", "Chat-Color").getString();
		String message = args.<String>getOne(Text.of("message")).get();

		Text newMessage = Texts.of((prefix + chatColor + message), src);
		Text staffMessage = Texts.of(ConfigManager.getConfNode("Chat-Settings", "SocialSpy-Message").getString() + message, src);

		for(Player members : Sponge.getServer().getOnlinePlayers())
			if(inTeam(members))
				members.sendMessage(newMessage);
			else if(isStaff(members) && !ChatUtils.inSocialSpyOff(members.getName()))
				members.sendMessage(staffMessage);

		if(ConfigManager.getConfNode("Language-Settings", "Console-SocialSpy").getBoolean())
			MessageChannel.TO_CONSOLE.send(newMessage);

		return CommandResult.success();
	}
	
	private boolean inTeam(Player members) {
		return !ConfigManager.getStorNode("Teams", role.getTeam(), "Members", members.getUniqueId().toString()).isVirtual();
	}

	private boolean isStaff(Player members) {
		return ConfigManager.getConfNode("Chat-Settings", "Players-SocialSpy").getBoolean() && members.hasPermission(Permissions.SOCIAL_SPY);
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.CHAT)
				.arguments(GenericArguments.optionalWeak(GenericArguments.remainingJoinedStrings(Text.of("message"))))
				.executor(new Chat())
				.build();
	}
}
