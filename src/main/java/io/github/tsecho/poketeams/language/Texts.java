package io.github.tsecho.poketeams.language;

import io.github.tsecho.poketeams.apis.PlaceholderAPI;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Texts {

	/**
	 * Factors in PAPI placeholders
	 * @param string to be formatted into a text
	 * @param src to base the formatting off of placeholders on
	 * @return a formatted text object
	 */
	public static Text of(String string, CommandSource src) {
		return PlaceholderAPI.getInstance().replace(string, src);
	}

	/**
	 * Factors in local placeholders
	 * @param string to be formatted into a text
	 * @param src to base the formatting off of placeholders on
	 * @return a formatted text object
	 */
	public static Text of(String string, String src) {
		return PlaceholderAPI.getInstance().replace(string, src);
	}

	/**
	 *
	 * @param string to change into a text
	 * @return a formatted Text object
	 */
	public static Text of(String string) {
		return TextSerializers.FORMATTING_CODE.deserialize(string);
	}

	/**
	 *
	 * @param text to serialize into a String
	 * @return a formatted string
	 */
	public static String getString(Text text) {
		return TextSerializers.FORMATTING_CODE.serialize(text);
	}
}
