package io.github.tsecho.poketeams.language;

import com.google.common.reflect.TypeToken;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;

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

		if(word.contains("$"))
			FAILED = true;

		if(!isTag && ConfigManager.getConfNode("Team-Settings", "Name", "Use-Censor").getBoolean())
			if (ConfigManager.getCensorNode("Partial-Censor").getBoolean())
				partialCensor();
			else
				fullWord();
		else if(isTag && ConfigManager.getConfNode("Team-Settings", "NameTag", "Use-Censor").getBoolean())
			if (ConfigManager.getCensorNode("Partial-Censor").getBoolean())
				partialCensor();
			else
				fullWord();

		if(!isTag && ConfigManager.getConfNode("Team-Settings", "Name", "Max-Length").getInt() < word.length())
			FAILED = true;
		else if(isTag && ConfigManager.getConfNode("Team-Settings", "NameTag", "Max-Length").getInt() < word.replace("&[0123456789abcdefklmnorABCDEFKLMNOR]", "").length())
			FAILED = true;

		if(!isTag && word.contains("&"))
			FAILED = true;

		if(isTag) {
			if(word.contains("&")) {
				if(containsStyle() && !ConfigManager.getConfNode("Team-Settings", "NameTag", "Allow-Style").getBoolean()) {
					FAILED = true;
				}
				if(!containsStyle() && !ConfigManager.getConfNode("Team-Settings", "NameTag", "Allow-Colors").getBoolean()) {
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
