package io.github.tsecho.poketeams.commands.alliance;

import io.github.tsecho.poketeams.apis.AllianceAPI;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.AllyRanks;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
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

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(!(src instanceof Player))
            return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

        String team = args.<String>getOne(Text.of("team")).get();
        PokeTeamsAPI role = new PokeTeamsAPI(src);
        PokeTeamsAPI roleOther = new PokeTeamsAPI(team, true);

        if(!role.inTeam())
            return ErrorCheck.test(src, ErrorMessages.NOT_IN_TEAM);
        if(!roleOther.teamExists())
            return ErrorCheck.test(src, ErrorMessages.NOT_EXISTS);

        AllianceAPI alliance = new AllianceAPI(role);
        AllianceAPI allianceOther = new AllianceAPI(roleOther);

        if(role.getTeam().equals(roleOther.getTeam()))
            return ErrorCheck.test(src, ErrorMessages.CANT_BE_SAME_TEAM);
        if((!alliance.inAlliance() || !allianceOther.inAlliance()) || !alliance.getAlliance().equals(allianceOther.getAlliance()))
            return ErrorCheck.test(src, ErrorMessages.BOTH_NOT_IN_ALLIANCE);
        if(!role.canAllianceCommands() || !alliance.canDelete())
            return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);

        alliance.setRole(roleOther, AllyRanks.OWNER.getHierarchyPlace());
        alliance.setRole(role, AllyRanks.MEMBER.getHierarchyPlace());

        alliance.getAllTeams().stream()
                .forEach(newAlliance -> newAlliance.getAllMembers()
                .forEach(user -> user.getPlayer().ifPresent(player -> player.sendMessage(SuccessMessages.TRANSFERRED_ALLIANCE.getText(src)))));

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.ALLY_TRANSFER)
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("team")))
                .executor(new TransferOwner())
                .build();
    }
}