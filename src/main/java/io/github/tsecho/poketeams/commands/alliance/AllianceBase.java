package io.github.tsecho.poketeams.commands.alliance;

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

public class AllianceBase implements CommandExecutor {

    private ArrayList<Text> help;
    private CommandSource src;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        this.src = src;
        this.help = new ArrayList<>();

        addIfPermissible(Permissions.ALLY_CREATE, Texts.of("&b/teams ally create <name>"));
        addIfPermissible(Permissions.ALLY_DELETE, Texts.of("&b/teams ally delete"));
        addIfPermissible(Permissions.ALLY_INVITE, Texts.of("&b/teams ally invite <player>"));
        addIfPermissible(Permissions.ALLY_INFO, Texts.of("&b/teams ally info [<alliance>]"));
        addIfPermissible(Permissions.ALLY_CHAT, Texts.of("&b/teams ally chat [<messages>]"));
        addIfPermissible(Permissions.ALLY_LEAVE, Texts.of("&b/teams ally leave"));
        addIfPermissible(Permissions.ALLY_TRANSFER, Texts.of("&b/teams ally transfer <team>"));

        PaginationList.builder()
                .title(Texts.of("&bPokeTeams Alliances"))
                .contents(help)
                .padding(Texts.of("&9="))
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
                .executor(new AllianceBase())
                .permission(Permissions.ALLY_BASE)
                .child(AllianceBase.help(), "help")
                .child(Chat.build(), "chat")
                .child(Create.build(), "create")
                .child(Delete.build(), "delete")
                .child(Info.build(), "info")
                .child(Invite.build(), "invite")
                .child(Leave.build(), "leave")
                .child(TransferOwner.build(), "transfer", "transferowner")
                .build();
    }

    private static CommandSpec help() {
        return CommandSpec.builder()
                .permission(Permissions.ALLY_BASE)
                .executor(new AllianceBase())
                .build();
    }
}
