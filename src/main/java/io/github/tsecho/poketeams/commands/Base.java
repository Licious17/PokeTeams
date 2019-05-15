package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.commands.admin.AdminBase;
import io.github.tsecho.poketeams.commands.banks.BankBase;
import io.github.tsecho.poketeams.commands.bases.BasesBase;
import io.github.tsecho.poketeams.commands.queue.QueueBase;
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

public class Base implements CommandExecutor{

	private CommandSource src;
	private ArrayList<Text> help;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		this.src = src;
		this.help = new ArrayList();
		
		addIfPermissable(Permissions.LEADERBOARD, Texts.of("&c/teams leaderboard"));
		addIfPermissable(Permissions.LIST, Texts.of("&c/teams list"));
		addIfPermissable(Permissions.CREATE, Texts.of("&c/teams create <team>"));
		addIfPermissable(Permissions.DELETE, Texts.of("&c/teams delete"));
		addIfPermissable(Permissions.INFO, Texts.of("&c/teams info [<team>]"));
		addIfPermissable(Permissions.INVITE, Texts.of("&c/teams invite"));
		addIfPermissable(Permissions.KICK, Texts.of("&c/teams kick <player>"));
		addIfPermissable(Permissions.TAG, Texts.of("&c/teams tag <tag>"));
		addIfPermissable(Permissions.CHAT, Texts.of("&c/teams chat [<message>]"));
		addIfPermissable(Permissions.BASE_TELEPORT, Texts.of("&c/teams base"));
		addIfPermissable(Permissions.BASE_TELEPORT, Texts.of("&c/teams base set"));
		addIfPermissable(Permissions.QUEUE_BASE, Texts.of("&c/teams queue"));
		addIfPermissable(Permissions.BANK_BASE, Texts.of("&c/teams bank"));
		addIfPermissable(Permissions.TRANSFEROWNER, Texts.of("&c/teams transferowner <player>"));
		addIfPermissable(Permissions.ADMIN_BASE, Texts.of("&c/teams admin"));
		addIfPermissable(Permissions.SOCIAL_SPY, Texts.of("&c/teams socialspy"));

		PaginationList.builder()
			.title(Texts.of("&cPokeTeams"))
			.contents(help)
			.padding(Texts.of("&b="))
			.sendTo(src);
		
		return CommandResult.success();
	}

	private void addIfPermissable(String permission, Text line) {
		if(src.hasPermission(permission))
			help.add(line.toBuilder()
						.onHover(TextActions.showText(Texts.of("&b&oClick me to execute this command!")))
						.onClick(TextActions.suggestCommand(line.toPlain()))
						.build());
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.executor(new Base())
				.permission(Permissions.BASE)
				.child(help(), "help")
				.child(Reload.build(), "reload")
				.child(Create.build(), "create")
				.child(Delete.build(), "delete")
				.child(Leave.build(), "leave", "quit")
				.child(Invite.build(), "invite")
				.child(Info.build(), "info")
				.child(Chat.build(), "chat")
				.child(Promote.build(), "promote")
				.child(Demote.build(), "demote")
				.child(Kick.build(), "kick")
				.child(List.build(), "list")
				.child(Leaderboard.build(), "leaderboard", "top")
				.child(Tag.build(), "tag")
				.child(BasesBase.build(), "base", "bases", "home")
				.child(AdminBase.build(), "admin", "staff")
				.child(QueueBase.build(), "queue")
				.child(BankBase.build(), "bank", "money")
				.child(TransferOwner.build(), "transferowner", "tranfser")
				.child(SocialSpy.build(), "socialspy", "ss", "spy")
				.build();
	}

	public static CommandSpec help() {
		return CommandSpec.builder()
				.permission(Permissions.BASE)
				.executor(new Base())
				.build();
	}
}
