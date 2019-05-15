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

import java.util.ArrayList;

public class Base implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		ArrayList<Text> help = new ArrayList<Text>();
		help.add(Texts.of("&c/teams leaderboard"));
		help.add(Texts.of("&c/teams list"));
		help.add(Texts.of("&c/teams create <team>"));
		help.add(Texts.of("&c/teams delete"));
		help.add(Texts.of("&c/teams info [<team>]"));
		help.add(Texts.of("&c/teams invite"));
		help.add(Texts.of("&c/teams kick <player>"));
		help.add(Texts.of("&c/teams tag <tag>"));
		help.add(Texts.of("&c/teams chat [<message>]"));
		help.add(Texts.of("&c/teams base"));
		help.add(Texts.of("&c/teams base set"));
		help.add(Texts.of("&c/teams queue"));
		help.add(Texts.of("&c/teams bank"));
		help.add(Texts.of("&c/teams transferowner <player>"));
		
		if(src.hasPermission(Permissions.ADMIN_BASE))
			help.add(Texts.of("&c/teams admin"));
		if(src.hasPermission(Permissions.SOCIAL_SPY))
			help.add(Texts.of("&c/teams socialspy"));
		
		PaginationList.builder()
		.title(Texts.of("&cPokeTeams"))
		.contents(help)
		.padding(Texts.of("&b="))
		.sendTo(src);
		
		return CommandResult.success();
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
