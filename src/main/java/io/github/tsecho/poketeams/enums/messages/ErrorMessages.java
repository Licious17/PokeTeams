package io.github.tsecho.poketeams.enums.messages;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.interfaces.IMessageCommon;
import io.github.tsecho.poketeams.language.Texts;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

public enum ErrorMessages implements IMessageCommon {

    NOT_IN_TEAM, ALREADY_IN_TEAM, INSUFFICIENT_RANK, INNAPROPRIATE,
    AlREADY_EXISTS, BOTH_NOT_IN_TEAM, CANT_PROMOTE, CANT_DEMOTE, MAX_MEMBERS,
    KICK_YOURSELF, NO_BASE, NEEDS_LEADER, NOT_EXISTS, OTHER_IN_TEAM,
    NOT_ONLINE, INSUFFICIENT_FUNDS, NOT_POSITIVE, BAD_WORLD, CANT_DO_THAT,
    ALLY_ALREADY_EXISTS, NOT_IN_ALLIANCE, OTHER_NOT_IN_TEAM, ALLY_ALREADY_IN_TEAM,
    ALLY_NEEDS_LEADER, BOTH_NOT_IN_ALLIANCE, CANT_BE_SAME_TEAM;

    public String getString(CommandSource player) {
        return ConfigManager.getLangNode("Errors", this.name()).getString();
    }

    public String getString() {
        return ConfigManager.getLangNode("Errors", this.name()).getString();
    }

    public Text getText(CommandSource src) {
        return Texts.of(this.getString(), src);
    }
}
