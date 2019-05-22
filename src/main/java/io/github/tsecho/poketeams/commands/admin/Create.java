package io.github.tsecho.poketeams.commands.admin;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.SuccessMessage;
import io.github.tsecho.poketeams.language.Texts;
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
import org.spongepowered.api.text.Text;

public class Create implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String team = args.<String>getOne(Text.of("team")).get();

        if(Utils.teamExists(team))
            return ErrorCheck.test(src, ErrorMessage.AlREADY_EXISTS);

        ConfigManager.getStorNode("Teams", team, "Record", "Wins").setValue(0);
        ConfigManager.getStorNode("Teams", team, "Record", "Losses").setValue(0);
        ConfigManager.getStorNode("Teams", team, "Stats", "Kills").setValue(0);
        ConfigManager.getStorNode("Teams", team, "Stats", "Caught").setValue(0);
        ConfigManager.getStorNode("Teams", team, "Stats", "Legends").setValue(0);
        ConfigManager.getStorNode("Teams", team, "Stats", "Bal").setValue(
                ConfigManager.getConfNode("Team-Settings", "Default-Team-Bal"));
        ConfigManager.save();

        src.sendMessage(Texts.of(SuccessMessage.CREATED_TEAM.getString().replaceAll("%teamname%", team)));

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.ADMIN_CREATE)
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("team")))
                .executor(new Create())
                .build();
    }
}
