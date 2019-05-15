package io.github.tsecho.poketeams.commands.admin;

import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AdminBase implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		PaginationList.builder()
		.title(Text.of(TextColors.YELLOW, "PokeTeams admin"))
		.contents(
				Text.of(TextColors.YELLOW, "/teams admin set <player> <team>"),
				Text.of(TextColors.YELLOW, "/teams admin promote <player>"),
				Text.of(TextColors.YELLOW, "/teams admin demote <player>"),
				Text.of(TextColors.YELLOW, "/teams admin create <team>"),
				Text.of(TextColors.YELLOW, "/teams admin delete <team>"),
				Text.of(TextColors.YELLOW, "/teams admin kick <team> <name>"),
				Text.of(TextColors.YELLOW, "/teams admin rename <team>"),
				Text.of(TextColors.YELLOW, "/teams admin tag <tag>"),
				Text.of(TextColors.YELLOW, "/teams admin reset <team>"))
		.padding(Text.of(TextColors.RED, "="))
		.sendTo(src);
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.executor(new AdminBase())
				.permission(Permissions.BASE)
				.child(Set.build(), "set")
				.child(Promote.build(), "promote")
				.child(Demote.build(), "demote")
				.child(Help.build(), "help")
				.child(Delete.build(), "delete")
				.child(Rename.build(), "rename")
				.child(Kick.build(), "kick")
				.child(Tag.build(), "tag")
				.child(Create.build(), "create")
				.child(Reset.build(), "reset")
				.build();
	}
}
	
