package io.github.tsecho.poketeams.commands.queue;

import io.github.tsecho.poketeams.enums.messages.QueueMessage;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessage;
import io.github.tsecho.poketeams.pixelmon.QueueManager;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

public class Leave implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessage.NOT_PLAYER);
		if(!QueueManager.getQueue().contains(src.getName()))
			return ErrorCheck.test(src, QueueMessage.NOT_IN_QUEUE);

		QueueManager.getQueue().remove(src.getName());
		src.sendMessage(QueueMessage.LEAVE_QUEUE.getText(src));

		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.QUEUE_LEAVE)
				.executor(new Leave())
				.build();
	}
}