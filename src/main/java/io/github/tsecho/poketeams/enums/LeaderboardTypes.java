package io.github.tsecho.poketeams.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeaderboardTypes {

    RATING, CAUGHT, LEGENDS, KILLS, BAL, RECORD;

    public String getTitleName() {
        switch(this) {
            case RATING:
                return "Team Rating";
            case CAUGHT:
                return "Caught Pokemon";
            case LEGENDS:
                return "Caught Legendaries";
            case KILLS:
                return "Pokemon Kill";
            case BAL:
                return "Bank Balance";
            case RECORD:
                return "Record";
        }
        return "";
    }

    public String getMessageName() {
        switch(this) {
            case RATING:
                return "Team Rating";
            case CAUGHT:
                return "Caught Pokemon";
            case LEGENDS:
                return "Caught Legendaries";
            case KILLS:
                return "Pokemon Defeated";
            case BAL:
                return "Bank Balance";
        }
        return "";
    }

    public static Map<String, String> getChoices() {
        HashMap<String, String> choices = new HashMap();
        for(LeaderboardTypes type : LeaderboardTypes.values())
            choices.put(type.name().toLowerCase(), type.name().toLowerCase());

        return choices;
    }
}
