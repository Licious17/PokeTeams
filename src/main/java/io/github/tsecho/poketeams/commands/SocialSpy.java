package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

public class SocialSpy implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(!(src instanceof Player))
            ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

        if(ChatUtils.inSocialSpyOff(src.getName())) {
            ChatUtils.removeSocialSpyOff(src.getName());
            src.sendMessage(TechnicalMessages.SOCIALSPY_ON.getText(src));
        } else {
            ChatUtils.addSocialSpyOff(src.getName());
            src.sendMessage(TechnicalMessages.SOCIALSPY_OFF.getText(src));
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission(Permissions.SOCIAL_SPY)
                .executor(new SocialSpy())
                .build();
    }
}
