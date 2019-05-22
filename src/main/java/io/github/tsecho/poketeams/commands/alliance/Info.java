package io.github.tsecho.poketeams.commands.alliance;

import io.github.tsecho.poketeams.apis.AllianceAPI;
import io.github.tsecho.poketeams.apis.InfoBuilderAPI;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessages;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.List;
import java.util.stream.Collectors;

public class Info implements CommandExecutor {

    private CommandSource src;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        this.src = src;

        if(!(src instanceof Player))
            return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

        PokeTeamsAPI role = new PokeTeamsAPI(src);

        if(!role.teamExists())
            return ErrorCheck.test(src, ErrorMessages.NOT_EXISTS);

        AllianceAPI alliance = new AllianceAPI(role);

        if(!alliance.inAlliance())
            return ErrorCheck.test(src, ErrorMessages.NOT_IN_ALLIANCE);

        List<Text> members = alliance.getAllTeams().stream()
                .map(team -> Texts.of(" &f- &a" + team.getTeam()).toBuilder()
                    .onHover(TextActions.showText(Texts.of("&b&oClick me to show information for this team!")))
                    .onClick(TextActions.executeCallback(callback -> sendInfo(team)))
                    .build())
                .collect(Collectors.toList());

        PaginationList.builder()
                .title(Texts.of("&a" + alliance.getAlliance()))
                .padding(Texts.of("&2="))
                .contents(members)
                .sendTo(src);

        return CommandResult.success();
    }

    private void sendInfo(PokeTeamsAPI team) {
        InfoBuilderAPI.builder(team.getTeam(), src)
                .addName()
                .addRating()
                .addBase()
                .addWinLoss()
                .addKills()
                .addCaught()
                .addLegendCaught()
                .addBank()
                .addPlayerList()
                .send();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.ALLY_INFO)
                .executor(new Info())
                .build();
    }
}