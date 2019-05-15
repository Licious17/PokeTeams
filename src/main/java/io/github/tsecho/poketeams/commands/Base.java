package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.AllianceAPI;
import io.github.tsecho.poketeams.commands.admin.AdminBase;
import io.github.tsecho.poketeams.commands.alliance.AllianceBase;
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
		this.help = new ArrayList<>();
		
		addIfPermissible(Permissions.LEADERBOARD, Texts.of("&c/teams leaderboard"));
		addIfPermissible(Permissions.LIST, Texts.of("&c/teams list"));
		addIfPermissible(Permissions.CREATE, Texts.of("&c/teams create <team>"));
		addIfPermissible(Permissions.DELETE, Texts.of("&c/teams delete"));
		addIfPermissible(Permissions.INFO, Texts.of("&c/teams info [<team>]"));
		addIfPermissible(Permissions.INVITE, Texts.of("&c/teams invite"));
		addIfPermissible(Permissions.KICK, Texts.of("&c/teams kick <player>"));
		addIfPermissible(Permissions.TAG, Texts.of("&c/teams tag <tag>"));
		addIfPermissible(Permissions.CHAT, Texts.of("&c/teams chat [<message>]"));
		addIfPermissible(Permissions.BASE_TELEPORT, Texts.of("&c/teams base"));
		addIfPermissible(Permissions.BASE_TELEPORT, Texts.of("&c/teams base set"));
		addIfPermissible(Permissions.QUEUE_BASE, Texts.of("&c/teams queue"));
		addIfPermissible(Permissions.BANK_BASE, Texts.of("&c/teams bank"));
		addIfPermissible(Permissions.TRANSFER, Texts.of("&c/teams transferowner <player>"));
        addIfPermissible(Permissions.ALLY_BASE, Texts.of("&c/teams ally"));
        addIfPermissible(Permissions.ADMIN_BASE, Texts.of("&c/teams admin"));
		addIfPermissible(Permissions.SOCIAL_SPY, Texts.of("&c/teams socialspy"));

		PaginationList.builder()
			.title(Texts.of("&cPokeTeams"))
			.contents(help)
			.padding(Texts.of("&b="))
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
                .child(AllianceBase.build(), "alliance", "ally", "alliances", "allies")
				.build();
	}

	private static CommandSpec help() {
		return CommandSpec.builder()
				.permission(Permissions.BASE)
				.executor(new Base())
				.build();
	}
}
