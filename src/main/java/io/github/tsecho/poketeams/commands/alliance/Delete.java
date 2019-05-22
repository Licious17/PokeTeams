package io.github.tsecho.poketeams.commands.alliance;

import io.github.tsecho.poketeams.apis.AllianceAPI;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.SuccessMessage;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessage;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

public class Delete implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(!(src instanceof Player))
            return ErrorCheck.test(src, TechnicalMessage.NOT_PLAYER);

        PokeTeamsAPI role = new PokeTeamsAPI(src);

        if(!role.inTeam())
            return ErrorCheck.test(src, ErrorMessage.NOT_IN_TEAM);

        AllianceAPI alliance = new AllianceAPI(role);

        if(!alliance.inAlliance())
            return ErrorCheck.test(src, ErrorMessage.NOT_IN_ALLIANCE);
        if(!role.canAllianceCommands() || !alliance.canDelete())
            return ErrorCheck.test(src, ErrorMessage.INSUFFICIENT_RANK);

        src.sendMessage(SuccessMessage.ALLY_DELETED.getText(src));

        alliance.getAllTeams()
            .forEach(team -> team.getAllMembers()
            .forEach(user -> user.getPlayer().ifPresent(player -> player.sendMessage(SuccessMessage.ALLY_DELETED.getText(player)))));

        alliance.delete();

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.ALLY_DELETE)
                .executor(new Delete())
                .build();
    }
}
