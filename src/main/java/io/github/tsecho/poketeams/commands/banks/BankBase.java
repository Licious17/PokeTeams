package io.github.tsecho.poketeams.commands.banks;

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

public class BankBase implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		PaginationList.builder()
			.title(Text.of(TextColors.GOLD, "Team Bank"))
			.padding(Text.of(TextColors.GRAY, "="))
			.contents(Text.of(TextColors.GOLD, "/team bank"),
					  Text.of(TextColors.GOLD, "/team bank balance"),
					  Text.of(TextColors.GOLD, "/team bank add <amount>"),
					  Text.of(TextColors.GOLD, "/team bank withdraw <amount>"))
			.sendTo(src);
		
		return CommandResult.success();
	}

	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.BANK_BASE)
				.child(Balance.build(), "balance", "bal")
				.child(Add.build(), "add", "deposit")
				.child(Withdraw.build(), "withdraw", "take")
				.executor(new BankBase())
				.build();
	}
}
