package io.github.tsecho.poketeams.language;

import com.google.common.reflect.TypeToken;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getSettings;

public class CensorCheck {

	private boolean FAILED = false;
	private String word;
	private ArrayList<String> words;

	/**
	 *
	 * @param word to be checked for (includes team)
	 */
	public CensorCheck(String word) {
		words = new ArrayList();
		this.word = word;

		try {
			words.addAll(ConfigManager.getCensorNode("Censored-Words").getList(TypeToken.of(String.class)));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checking if a word does not pass the filter
	 * @param isTag if the word to look for is a tag
	 * @return true if the censor gets caught
	 */
	public boolean failsCensor(boolean isTag) {

		if(!isTag && getSettings().team.name.useCensor)
			if (ConfigManager.getCensorNode("Partial-Censor").getBoolean())
				partialCensor();
			else
				fullWord();
		else if(isTag && getSettings().team.tag.useCensor)
			if (ConfigManager.getCensorNode("Partial-Censor").getBoolean())
				partialCensor();
			else
				fullWord();

		if(!isTag && getSettings().team.name.maxLength < word.length())
			FAILED = true;
		else if(isTag && getSettings().team.tag.maxLength < word.replaceAll("&[abcdefklmnorABCDEFKLMNOR]", "").length())
			FAILED = true;

		if(!isTag && word.contains("&"))
			FAILED = true;

		if(isTag) {
			if(word.contains("&")) {
				if(containsStyle() && !getSettings().team.tag.allowStyle) {
					FAILED = true;
				}
				if(!containsStyle() && !getSettings().team.tag.allowColors) {
					FAILED = true;
				}
			}
		}

		return FAILED;
	}

	private boolean containsStyle() {
		return word.toUpperCase().contains("&l".toUpperCase()) || word.toUpperCase().contains("&n".toUpperCase())
			|| word.toUpperCase().contains("&o".toUpperCase()) || word.toUpperCase().contains("&m".toUpperCase())
			|| word.toUpperCase().contains("&k".toUpperCase());
	}
	
	private void partialCensor() {
		for(String swear : words)
			if (word.toLowerCase().contains(swear.toLowerCase().trim()))
				FAILED = true;
	}
	
	private void fullWord() {
		for(String swear : words)
			if (swear.equalsIgnoreCase(word))
				FAILED = true;
	}
}
