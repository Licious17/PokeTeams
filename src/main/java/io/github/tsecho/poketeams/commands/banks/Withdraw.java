package io.github.tsecho.poketeams.commands.banks;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.economy.EconManager;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.SuccessMessage;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessage;
import io.github.tsecho.poketeams.language.Texts;
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
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

public class Withdraw implements CommandExecutor {

    private PokeTeamsAPI role;
    private int requested;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(!(src instanceof Player))
            return ErrorCheck.test(src, TechnicalMessage.NOT_PLAYER);

        role = new PokeTeamsAPI(src);

        if(!role.inTeam())
            return ErrorCheck.test(src, ErrorMessage.NOT_IN_TEAM);
        if(!role.canTakeBank())
            return ErrorCheck.test(src, ErrorMessage.INSUFFICIENT_RANK);

        requested = args.<Integer>getOne(Text.of("amount")).get();

        if(requested < 0)
            return ErrorCheck.test(src, ErrorMessage.NOT_POSITIVE);
        if(role.takeBal(requested))
            src.sendMessage(Texts.of(SuccessMessage.MONEY_REWARD.getString(src).replaceAll("%price%", String.valueOf(requested))));
        else
            return ErrorCheck.test(src, ErrorMessage.INSUFFICIENT_FUNDS);

        EconManager econ = new EconManager((Player) src);
        econ.pay(BigDecimal.valueOf(requested));

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.BANK_WITHDRAW)
                .executor(new Withdraw())
                .arguments(GenericArguments.integer(Text.of("amount")))
                .build();
    }
}
