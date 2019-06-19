package io.github.tsecho.poketeams.utilities;

import java.util.ArrayList;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getAllyNode;
import static io.github.tsecho.poketeams.configuration.ConfigManager.getStorNode;

public class Utils {

	private static ArrayList<String> inQueueSystem = new ArrayList<>();

	public static void removeQueue(String player) {
		inQueueSystem.remove(player);
	}
	
	public static void addQueue(String player) {
		inQueueSystem.add(player);
	}
	
	public static boolean inQueue(String player) {
		return inQueueSystem.contains(player);
	}

	public static boolean teamExists(String team) {
		return !getStorNode("Teams", team).isVirtual();
	}

	public static boolean allianceExists(String alliance) {
		return !getAllyNode("Allies", alliance).isVirtual();
	}
}
