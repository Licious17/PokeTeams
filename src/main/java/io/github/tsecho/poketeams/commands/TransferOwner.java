package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
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
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class TransferOwner implements CommandExecutor {

    private PokeTeamsAPI role, roleOther;
    private User newOwner;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        newOwner = args.<User>getOne(Text.of("player")).get();

        if(!(src instanceof Player))
            return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

        role = new PokeTeamsAPI(src);
        roleOther = new PokeTeamsAPI(newOwner.getName(), false);

        if((!role.inTeam() || !roleOther.inTeam()) || (!role.getTeam().equals(roleOther.getTeam())))
            return ErrorCheck.test(src, ErrorMessages.BOTH_NOT_IN_TEAM);
        if(role.getPlace() != 4)
            return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);
        if(src.getName().equals(newOwner.getName()))
            return ErrorCheck.test(src, ErrorMessages.CANT_DO_THAT);

        role.setRole(3);
        roleOther.setRole(4);

        src.sendMessage(SuccessMessages.DEMOTED.getText(src));
        src.sendMessage(Texts.of(SuccessMessages.PROMOTE_SEND.getString().replaceAll("%player%", newOwner.getName())));
        newOwner.getPlayer().ifPresent(p -> p.sendMessage(SuccessMessages.PROMOTED.getText(p)));

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.TRANSFEROWNER)
                .arguments(GenericArguments.onlyOne(GenericArguments.user(Text.of("player"))))
                .executor(new TransferOwner())
                .build();
    }
}
