package io.github.tsecho.poketeams.commands.alliance;

import io.github.tsecho.poketeams.apis.AllianceAPI;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.AllyRanks;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.SuccessMessage;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessage;
import io.github.tsecho.poketeams.language.CensorCheck;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import io.github.tsecho.poketeams.utilities.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getAllyNode;
import static io.github.tsecho.poketeams.configuration.ConfigManager.save;

public class Create implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(!(src instanceof Player))
            return ErrorCheck.test(src, TechnicalMessage.NOT_PLAYER);

        PokeTeamsAPI team = new PokeTeamsAPI(src);
        String allianceName = args.<String>getOne(Text.of("name")).get();
        CensorCheck check = new CensorCheck(allianceName);

        if(!team.inTeam())
            return ErrorCheck.test(src, ErrorMessage.NOT_IN_TEAM);
        if(Utils.allianceExists(allianceName))
            return ErrorCheck.test(src, ErrorMessage.ALLY_ALREADY_EXISTS);

        AllianceAPI alliance = new AllianceAPI(team);

        if(alliance.inAlliance())
            return ErrorCheck.test(src, ErrorMessage.ALLY_ALREADY_IN_TEAM);
        if(check.failsCensor(false))
            return ErrorCheck.test(src, ErrorMessage.INNAPROPRIATE);
        if(!team.canAllianceCommands())
            return ErrorCheck.test(src, ErrorMessage.INSUFFICIENT_RANK);

        getAllyNode("Allies", allianceName, "Teams", team.getTeam()).setValue(AllyRanks.OWNER.getName());
        save();
        src.sendMessage(SuccessMessage.ALLY_CREATED.getText(src));

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.ALLY_CREATE)
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("name")))
                .executor(new Create())
                .build();
    }
}
