package io.github.tsecho.poketeams.commands.admin;

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

public class AdminBase implements CommandExecutor {

	private ArrayList<Text> help;
	private CommandSource src;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		this.src = src;
		this.help = new ArrayList<>();

		addIfPermissible(Permissions.ADMIN_SET, Texts.of("&e/teams admin set <player> <team>"));
		addIfPermissible(Permissions.ADMIN_PROMOTE, Texts.of("&e/teams admin promote <player>"));
		addIfPermissible(Permissions.ADMIN_DEMOTE, Texts.of("&e/teams admin demote <player>"));
		addIfPermissible(Permissions.ADMIN_CREATE, Texts.of("&e/teams admin create <team>"));
		addIfPermissible(Permissions.ADMIN_DELETE, Texts.of("&e/teams admin delete <team>"));
		addIfPermissible(Permissions.ADMIN_KICK, Texts.of("&e/teams admin kick <team> <name>"));
		addIfPermissible(Permissions.ADMIN_RENAME, Texts.of("&e/teams admin rename <team>"));
		addIfPermissible(Permissions.ADMIN_TAG, Texts.of("&e/teams admin tag <tag>"));
		addIfPermissible(Permissions.ADMIN_RESET, Texts.of("&e/teams admin reset <team>"));

		PaginationList.builder()
			.title(Texts.of("&ePokeTeams Admin"))
			.contents(help)
			.padding(Texts.of("&c="))
			.sendTo(src);
		
		return CommandResult.success();
	}

	private void addIfPermissible(String permission, Text line) {
        if(src.hasPermission(permission)) {
            Text newLine = line.toBuilder()
                    .onHover(TextActions.showText(Texts.of("&b&oClick me to execute this command!")))
                    .build();

            if(line.toPlain().contains("<") || line.toPlain().contains("["))
                help.add(newLine.toBuilder().onClick(TextActions.suggestCommand(line.toPlain())).build());
            else
                help.add(newLine.toBuilder().onClick(TextActions.runCommand(line.toPlain())).build());
        }
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.executor(new AdminBase())
				.permission(Permissions.BASE)
				.child(AdminBase.help(), "help")
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

	private static CommandSpec help() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_BASE)
				.executor(new AdminBase())
				.build();
	}
}
	
