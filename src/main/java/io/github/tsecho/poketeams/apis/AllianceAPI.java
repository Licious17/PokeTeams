package io.github.tsecho.poketeams.apis;

import io.github.tsecho.poketeams.enums.AllyRanks;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import java.util.Map;

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
     *
     * @return an instance of the PokeTeamsAPI that this alliance is based on
     */
    public PokeTeamsAPI getTeam() {
        return teamsAPI;
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
        if(inAlliance() && addedTeam.teamExists()) {
            getAllyNode("Allies", alliance, "Teams", addedTeam.team).setValue(AllyRanks.getEnum(place).getName());
            save();
        }
    }

    /**
     * @param removeTeam to add to the alliance
     */
    public void removeTeam(PokeTeamsAPI removeTeam) {
        if(inAlliance() && removeTeam.teamExists()) {
            getAllyNode("Allies", alliance, "Teams", removeTeam.team).setValue("Member");
            save();
        }
    }

    /**
     * @param removeTeam to add to the alliance
     */
    public void setRole(PokeTeamsAPI removeTeam) {
        if(inAlliance() && removeTeam.teamExists()) {
            getAllyNode("Allies", alliance, "Teams", removeTeam.team).setValue("Member");
            save();
        }
    }
}
