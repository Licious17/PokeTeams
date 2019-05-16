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
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

public class Leave implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(!(src instanceof Player))
            return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

        PokeTeamsAPI role = new PokeTeamsAPI(src);

        if(!role.inTeam())
            return ErrorCheck.test(src, ErrorMessages.NOT_IN_TEAM);

        AllianceAPI alliance = new AllianceAPI(role);

        if(!alliance.inAlliance())
            return ErrorCheck.test(src, ErrorMessages.NOT_IN_ALLIANCE);
        if(!role.canAllianceCommands())
            return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);

        int size = alliance.getAllTeams().size();

        if(size > 1) {
            if(alliance.getRank().equals(AllyRanks.OWNER.getName())) {
                src.sendMessage(ErrorMessages.ALLY_NEEDS_LEADER.getText(src));
            } else {
                alliance.removeTeam(role);
            }
        } else {
            src.sendMessage(SuccessMessages.ALLY_DELETED.getText(src));
            alliance.delete();
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.ALLY_LEAVE)
                .executor(new Leave())
                .build();
    }
}
