package io.github.tsecho.poketeams.utilities;

import io.github.tsecho.poketeams.enums.messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.messages.QueueMessages;
import io.github.tsecho.poketeams.enums.messages.TechnicalMessages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

public class ErrorCheck {

    /**
     *
     * @param src to base the message off of
     * @param problem to send to the player
     * @return a successful command result to end the command
     */
    public static CommandResult test(CommandSource src, ErrorMessages problem) {
        src.sendMessage(problem.getText(src));
        return CommandResult.success();
    }

    /**
     *
     * @param src to base the message off of
     * @param problem to send to the player
     * @return a successful command result to end the command
     */
    public static CommandResult test(CommandSource src, TechnicalMessages problem) {
        src.sendMessage(problem.getText(src));
        return CommandResult.success();
    }

    /**
     *
     * @param src to base the message off of
     * @param problem to send to the player
     * @return a successful command result to end the command
     */
    public static CommandResult test(CommandSource src, QueueMessages problem) {
        src.sendMessage(problem.getText(src));
        return CommandResult.success();
    }
}
