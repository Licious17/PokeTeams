package io.github.tsecho.poketeams.language;

import io.github.tsecho.poketeams.enums.ChatTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatUtils {

    private static Map<String, ChatTypes> chatRegistry = new HashMap<>();
    private static ArrayList<String> socialSpy = new ArrayList<>();

    /**
     * Adds players to the chat registry
     * @param name of a player to add to a specific chat
     */
    public static void setChat(String name, ChatTypes type) {
        chatRegistry.put(name, type);
    }

    /**
     * @param name of a player to check for team chat
     * @return ChatType the player is in
     */
    public static ChatTypes getChatType(String name) {
        return chatRegistry.get(name);
    }

    /**
     * Adds players to the SocialSpy toggle
     * @param name of a player to add to the SocialSpy toggle
     */
    public static void addSocialSpyOff(String name) {
        if(!inSocialSpyOff(name))
            socialSpy.add(name);
    }

    /**
     * Removes a player from the SocialSpy toggle
     * @param name of a player to remove from the SocialSpy toggle
     */
    public static void removeSocialSpyOff(String name) {
        if(inSocialSpyOff(name))
            socialSpy.remove(name);
    }

    /**
     * @param name of a player to check for SocialSpy
     * @return true if a player is in the SocialSpy
     */
    public static boolean inSocialSpyOff(String name) {
        return socialSpy.contains(name);
    }
}
