package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessage;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

public class Reload implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		ConfigManager.load();
		src.sendMessage(TechnicalMessage.RELOADED.getText(src));
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.RELOAD)
				.executor(new Reload())
				.build();
	}
}
