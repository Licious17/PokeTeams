package io.github.tsecho.poketeams.commands.banks;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.economy.EconManager;
import io.github.tsecho.poketeams.enums.messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessages;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

public class Add implements CommandExecutor {

	private int addedMoney;
	private PokeTeamsAPI role;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

		addedMoney = args.<Integer>getOne(Text.of("amount")).get();
		role = new PokeTeamsAPI(src);

		if(!role.inTeam())
			return ErrorCheck.test(src, ErrorMessages.NOT_IN_TEAM);
		if(!role.canAddBank())
			return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);
		if(addedMoney < 0)
			return ErrorCheck.test(src, ErrorMessages.NOT_POSITIVE);

		EconManager econ = new EconManager((Player) src);
		BigDecimal cost = BigDecimal.valueOf(args.<Integer>getOne(Text.of("amount")).get());

		if(econ.withdraw(cost).getResult() != ResultType.SUCCESS)
			src.sendMessage(ErrorMessages.INSUFFICIENT_FUNDS.getText(src));

		role.addBal(addedMoney);
		src.sendMessage(SuccessMessages.MONEY_DEPOSIT.getText(src));

		return CommandResult.success();
	}

	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.BANK_ADD)
				.arguments(GenericArguments.integer(Text.of("amount")))
				.executor(new Add())
				.build();
	}
}