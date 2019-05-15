package io.github.tsecho.poketeams.enums;

import io.github.tsecho.poketeams.configuration.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public enum Ranks {

    GRUNT, MEMBER, OFFICER, CAPTAIN, OWNER;

    /**
     * Options: grunt, member, officer, owner in any caps
     * @param string to get the enum from
     * @return
     */
    public static Ranks getEnum(String string) {
        return Ranks.valueOf(string.toUpperCase());
    }

    /**
     *
     * @param place (0-3) to get an Enum from
     * @return a rank from its hierarchical order
     */
    public static Ranks getEnum(int place) {
        switch(place) {
            default:
                return Ranks.GRUNT;
            case 1:
                return Ranks.MEMBER;
            case 2:
                return Ranks.OFFICER;
            case 3:
                return Ranks.CAPTAIN;
            case 4:
                return Ranks.OWNER;
        }
    }

    public static Map<String, String > getList() {
        Map<String, String> list = new HashMap();
        list.put(Ranks.GRUNT.getName(), Ranks.GRUNT.getName());
        list.put(Ranks.MEMBER.getName(), Ranks.MEMBER.getName());
        list.put(Ranks.OFFICER.getName(), Ranks.OFFICER.getName());
        list.put(Ranks.CAPTAIN.getName(), Ranks.CAPTAIN.getName());
        list.put(Ranks.OWNER.getName(), Ranks.OWNER.getName());
        return list;
    }

    /**
     * Is the name of the enum with standard english capitalization
     * @return the name of this rank for internal use
     */
    public String getName() {
        switch(this) {
            case GRUNT:
                return "Grunt";
            case MEMBER:
                return "Member";
            case OFFICER:
                return "Officer";
            case CAPTAIN:
                return "Captain";
            case OWNER:
                return "Owner";
            default:
                return "";
        }
    }

    /**
     * Is best used for output and player interaction
     * @return Configured value set in the config for this role
     */
    public String getTranslatedName() {
        return ConfigManager.getLangNode("Roles", this.name()).getString();
    }

    /**
     * Ranks from 0-3 from grunt to owner
     * @return the place in the rank hierachy (0-3)
     */
    public int getHierarchyPlace() {
        switch(this) {
            default:
                return 0;
            case MEMBER:
                return 1;
            case OFFICER:
                return 2;
            case CAPTAIN:
                return 3;
            case OWNER:
                return 4;
        }
    }
}
