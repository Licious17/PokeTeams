package io.github.tsecho.poketeams.commands.banks;

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

public class BankBase implements CommandExecutor {

	private ArrayList<Text> help;
	private CommandSource src;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		this.src = src;
		this.help = new ArrayList<>();

		addIfPermissible(Permissions.BANK_BASE, Texts.of("&6/teams bank help"));
		addIfPermissible(Permissions.BANK_BALANCE, Texts.of("&6/teams bank balance"));
		addIfPermissible(Permissions.BANK_ADD, Texts.of("&6/teams bank add <amount>"));
		addIfPermissible(Permissions.BANK_WITHDRAW, Texts.of("&e/teams bank withdraw <amount>"));

		PaginationList.builder()
			.title(Texts.of("&6PokeTeams Bank"))
			.padding(Texts.of("&7="))
			.contents(help)
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
				.permission(Permissions.BANK_BASE)
				.child(BankBase.help(), "help")
				.child(Balance.build(), "balance", "bal")
				.child(Add.build(), "add", "deposit")
				.child(Withdraw.build(), "withdraw", "take")
				.executor(new BankBase())
				.build();
	}

	private static CommandSpec help() {
		return CommandSpec.builder()
				.permission(Permissions.BANK_BASE)
				.executor(new BankBase())
				.build();
	}
}
