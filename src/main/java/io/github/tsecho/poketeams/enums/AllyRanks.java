package io.github.tsecho.poketeams.enums;

import io.github.tsecho.poketeams.configuration.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public enum AllyRanks {

    MEMBER, OWNER;

    /**
     * Options: member or owner in any caps
     * @param string to get the enum from
     * @return enum based
     */
    public static AllyRanks getEnum(String string) {
        return AllyRanks.valueOf(string.toUpperCase());
    }

    /**
     *
     * @param place (0-1) to get an Enum from
     * @return a rank from its hierarchical order
     */
    public static AllyRanks getEnum(int place) {
        switch(place) {
            default:
                return AllyRanks.MEMBER;
            case 1:
                return AllyRanks.OWNER;
        }
    }

    public static Map<String, String > getList() {
        Map<String, String> list = new HashMap();
        list.put(AllyRanks.MEMBER.getName(), AllyRanks.MEMBER.getName());
        list.put(AllyRanks.OWNER.getName(), AllyRanks.OWNER.getName());
        return list;
    }

    /**
     * Is the name of the enum with standard english capitalization
     * @return the name of this rank for internal use
     */
    public String getName() {
        switch(this) {
            case MEMBER:
                return "Grunt";
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
        return ConfigManager.getLangNode("Ally-Roles", this.name()).getString();
    }

    /**
     * Ranks from 0-1 from grunt to owner
     * @return the place in the rank hierarchy (0-1)
     */
    public int getHierarchyPlace() {
        switch(this) {
            default:
                return 0;
            case OWNER:
                return 1;
        }
    }
}
