package io.github.tsecho.poketeams.enums.messages;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.interfaces.IMessageCommon;
import io.github.tsecho.poketeams.language.Texts;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

public enum QueueMessages implements IMessageCommon {

    ALREADY_IN_QUEUE, NOT_IN_QUEUE, ADDED_QUEUE_COST, ADDED_QUEUE,
    LEAVE_QUEUE, START_BATTLE, BATTLE_WON, BATTLE_LOST;

    public String getString(CommandSource src) {
        return ConfigManager.getLangNode("queue", this.name()).getString();
    }

    public String getString() {
        return ConfigManager.getLangNode("queue", this.name()).getString();
    }

    public Text getText(CommandSource src) {
        return Texts.of(this.getString(), src);
    }
}

