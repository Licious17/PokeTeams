package io.github.tsecho.poketeams.commands.queue;

import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;

public class QueueBase implements CommandExecutor {

	private ArrayList<Text> help;
	private CommandSource src;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		this.src = src;
		this.help = new ArrayList<>();

		addIfPermissible(Permissions.QUEUE_BASE, Texts.of("&e/teams queue help"));
		addIfPermissible(Permissions.QUEUE_JOIN, Texts.of("&e/teams queue join"));
		addIfPermissible(Permissions.QUEUE_LEAVE, Texts.of("&e/teams queue leave"));
		addIfPermissible(Permissions.QUEUE_LIST, Texts.of("&e/teams queue list"));

		PaginationList.builder()
		.title(Texts.of("&ePokeTeams queue"))
		.contents(help)
		.padding(Texts.of("&a="))
		.sendTo(src);
		
		return CommandResult.success();
	}

	private void addIfPermissible(String permission, Text line) {
		if(src.hasPermission(permission))
			help.add(line.toBuilder()
					.onHover(TextActions.showText(Texts.of("&b&oClick me to execute this command!")))
					.onClick(TextActions.suggestCommand(line.toPlain()))
					.build());
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.executor(new QueueBase())
				.permission(Permissions.QUEUE_BASE)
				.child(QueueBase.help(), "help")
				.child(Join.build(), "join")
				.child(Leave.build(), "leave")
				.child(List.build(), "list")
				.build();
	}

	private static CommandSpec help() {
		return CommandSpec.builder()
				.permission(Permissions.QUEUE_BASE)
				.executor(new QueueBase())
				.build();
	}
}