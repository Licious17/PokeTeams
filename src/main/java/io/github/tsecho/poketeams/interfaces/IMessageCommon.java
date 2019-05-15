package io.github.tsecho.poketeams.interfaces;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public interface IMessageCommon {

    /**
     *
     * @param src to get the string from
     * @return a string based off of a config message
     */
    String getString(CommandSource src);

    /**
     *
     * @param src to get this placeholder from
     * @return a text that is properly formatted to have placeholders replaced
     */
    Text getText(CommandSource src);
}
