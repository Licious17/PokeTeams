package io.github.tsecho.poketeams.language;

import java.util.ArrayList;

public class ChatUtils {

    private static ArrayList<String> chatRegistery = new ArrayList();
    private static ArrayList<String> socialSpy = new ArrayList();

    /**
     * Adds players to the team chat registery
     * @param name of a player to add to the teams chat
     */
    public static void addChat(String name) {
        if(!inChat(name))
            chatRegistery.add(name);
    }

    /**
     * Removes a player from the team chat registery
     * @param name of a player to remove from team chat
     */
    public static void removeChat(String name) {
        if(inChat(name))
            chatRegistery.remove(name);
    }

    /**
     * @param name of a player to check for team chat
     * @return true if a player is in the teams chat
     */
    public static boolean inChat(String name) {
        return chatRegistery.contains(name);
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
