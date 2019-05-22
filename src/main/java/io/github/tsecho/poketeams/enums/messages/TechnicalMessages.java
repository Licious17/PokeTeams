package io.github.tsecho.poketeams.enums.messages;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.interfaces.IMessageCommon;
import io.github.tsecho.poketeams.language.Texts;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

public enum TechnicalMessages implements IMessageCommon {

    RELOADED, NOT_PLAYER, CONFIRM, SOCIALSPY_ON, SOCIALSPY_OFF;

    public String getString(CommandSource src) {
        return ConfigManager.getLangNode("Technical", this.name()).getString();
    }

    public String getString() {
        return ConfigManager.getLangNode("Technical", this.name()).getString();
    }

    public Text getText(CommandSource src) {
        return Texts.of(this.getString(), src);
    }
}
