package io.github.tsecho.poketeams.enums.messages;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.interfaces.IMessageCommon;
import io.github.tsecho.poketeams.language.Texts;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

public enum SuccessMessages implements IMessageCommon {

    CREATED_TEAM, DELETED_TEAM, TELEPORTED, SET_BASE, SEND_INVITE,
    LEFT, ADDED_CHAT, REMOVED_CHAT, RESET_TEAM, DISBANDED, TAX_TAKEN,
    DEMOTED_ADMIN, INVITED, INVITE_ACCEPTED, JOINED_TEAM, INVITE_CLICK,
    KICK_RECEIVE, KICK_SEND, PROMOTED, DEMOTED, PROMOTE_SEND, DEMOTE_SEND,
    TAG_CHANGE, RENAMED, TEAM_CHANGE_RECEIVE, TEAM_CHANGE_SEND, MONEY_DEPOSIT,
    MONEY_WITHDRAW, BANK_BALANCE, MONEY_REWARD, TAX_WARNING, ALLY_CREATED,
    ALLY_DELETED, ALLY_INVITED, JOINED_ALLIANCE, TRANSFERRED_ALLIANCE,
    ALLY_REMOVED_CHAT, ALLY_ADDED_CHAT;

    public String getString(CommandSource src) {
        return ConfigManager.getLangNode("Success", this.name()).getString();
    }

    public String getString() {
        return ConfigManager.getLangNode("Success", this.name()).getString();
    }

    public Text getText(CommandSource src) {
        return Texts.of(this.getString(), src);
    }
}
