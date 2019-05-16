package io.github.tsecho.poketeams.apis;

import io.github.tsecho.poketeams.enums.AllyRanks;
import io.github.tsecho.poketeams.language.Texts;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.tsecho.poketeams.configuration.ConfigManager.*;

public class AllianceAPI {

    private PokeTeamsAPI teamsAPI;
    private String alliance;
    private String role;

    /**
     * All methods cannot return null and will give default values if none are present
     *
     * @param teamsAPI to base the alliance of of
     */
    public AllianceAPI(PokeTeamsAPI teamsAPI) {
        this.teamsAPI = teamsAPI;

        if(!teamsAPI.teamExists())
            return;

        for (Map.Entry<Object, ? extends CommentedConfigurationNode> allys : getAllyNode("Allies").getChildrenMap().entrySet()) {

            String allyTemp = allys.getKey().toString();

            if(!getAllyNode("Allies", allyTemp, "Teams", teamsAPI.team).isVirtual()) {
                alliance = allyTemp;
                role = getAllyNode("Allies", allyTemp, "Teams", teamsAPI.team).getString();
                break;
            }
        }
    }

    /**
     * Gets all the teams in this alliance
     */
    public List<PokeTeamsAPI> getAllTeams() {
        if(!inAlliance())
            return Arrays.asList();
        else
            return getAllyNode("Allies", alliance, "Teams").getChildrenMap().entrySet().stream()
                        .map(key -> new PokeTeamsAPI(key.getKey().toString(), true))
                        .collect(Collectors.toList());
    }

    /**
     *
     * @return an instance of the PokeTeamsAPI that this alliance is based on
     */
    public PokeTeamsAPI getTeam() {
        return teamsAPI;
    }

    public String getAlliance() {
        return inAlliance() ? alliance : getConfNode("Placeholder-Settings", "Default-AllianceName").getString();
    }

    /**
     * @return true if this team has an alliance
     */
    public boolean inAlliance() {
        return alliance != null;
    }

    /**
     * @return the rank of this team in the alliance
     */
    public String getRank() {
        return role;
    }

    /**
     * @return {@code true} if the team has the invite permission in config based on the teams rank
     */
    public boolean canInvite() {
        return inAlliance() && getConfNode("Ally-Settings", "Roles", role, "Invite").getBoolean();
    }

    /**
     * @return {@code true} if the team has the delete permission in config based on the teams rank
     */
    public boolean canDelete() {
        return inAlliance() && getConfNode("Ally-Settings", "Roles", role, "Delete").getBoolean();
    }

    /**
     * @param addedTeam to add to the alliance
     */
    public void addTeam(PokeTeamsAPI addedTeam, int place) {
        if(addedTeam.teamExists() && inAlliance()) {
            getAllyNode("Allies", alliance, "Teams", addedTeam.team).setValue(AllyRanks.getEnum(place).getName());
            save();
        }
    }

    /**
     * Deletes this alliance
     */
    public void delete() {
        if(inAlliance()) {
            getAllyNode("Allies", alliance).setValue(null);
            save();
        }
    }

    /**
     * @param removeTeam to add to the alliance
     */
    public void removeTeam(PokeTeamsAPI removeTeam) {
        if(inAlliance() && removeTeam.teamExists()) {
            getAllyNode("Allies", alliance, "Teams", removeTeam.team).setValue(null);
            save();
        }
    }

    /**
     * @param team to add to the alliance
     */
    public void setRole(PokeTeamsAPI team, int role) {
        if(inAlliance() && team.teamExists()) {
            getAllyNode("Allies", alliance, "Teams", team.team).setValue(AllyRanks.getEnum(role).getName());
            save();
        }
    }
}
