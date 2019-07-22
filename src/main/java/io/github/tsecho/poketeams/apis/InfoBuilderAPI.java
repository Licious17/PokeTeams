package io.github.tsecho.poketeams.apis;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.Ranks;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.services.UserStorage;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import java.util.*;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getStorNode;

public class InfoBuilderAPI extends PokeTeamsAPI {

    private final Text PADDING = Texts.of("&6=");
    private CommandSource src;
    private ArrayList<Text> contents;
    private Text title;

    /**
     *
     * @param team for the information to be based off of
     * @param src to send the info to
     * @return a builder to send information to a player as a pagination list
     */
    public static InfoBuilderAPI builder(String team, Player src) {
        return new InfoBuilderAPI(team, src);
    }

    /**
     *
     * @param member for the team to be based off of
     * @param src to send the info to
     * @return a builder to send information to a player as a pagination list
     */
    public static InfoBuilderAPI builder(Player member, Player src) {
        return new InfoBuilderAPI(member, src);
    }

    /**
     *
     * @param team for the team to be based off of
     * @param src to send the info to
     * @return a builder to send information to a player as a pagination list
     */
    public static InfoBuilderAPI builder(String team, CommandSource src) {
        return new InfoBuilderAPI(team, src);
    }


    /**
     *
     * @param team for the info generator to attempt to create
     * @param src for the info to be sent to
     */
    private InfoBuilderAPI(String team, Player src) {
        super(team, true);
        this.src = src;
        contents = new ArrayList<>();
    }

    /**
     *
     * @param member for the info generator to attempt to create
     * @param src for the info to be sent to
     */
    private InfoBuilderAPI(Player member, Player src) {
        super(member);
        this.src = src;
        contents = new ArrayList<>();
    }

    /**
     *
     * @param team for the info generator to attempt to create
     * @param src for the info to be sent to
     */
    private InfoBuilderAPI(String team, CommandSource src) {
        super(team, true);
        this.src = src;
        contents = new ArrayList<>();
    }

    /**
     * Adds a title to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addName() {
        this.title = Texts.of("&e" + getTeam());
        return this;
    }

    /**
     * Adds the team rating to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addRating() {
        String prefix = ConfigManager.getLangNode("Info", "RATING").getString();
        this.contents.add(Texts.of(prefix + getRating()));
        return this;
    }

    /**
     * Adds win loss record to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addWinLoss() {
        String prefix = ConfigManager.getLangNode("Info", "RECORD").getString();
        this.contents.add(Texts.of(prefix + getWins() + "/" + (getLosses() + getWins()) + " ["  + getRatio() + "%]"));
        return this;
    }

    /**
     * Adds a rank to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addRank() {
        String prefix = ConfigManager.getLangNode("Info", "RANK").getString();
        if(!getRank().equals(""))
            this.contents.add(Texts.of(prefix + getTranslatedRank()));
        return this;
    }

    /**
     * Adds a kill counter to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addKills() {
        String prefix = ConfigManager.getLangNode("Info", "DEFEATED").getString();
        this.contents.add(Texts.of(prefix + getKills()));
        return this;
    }

    /**
     * Adds a caught pokemon counter to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addCaught() {
        String prefix = ConfigManager.getLangNode("Info", "CAUGHT").getString();
        this.contents.add(Texts.of(prefix + getCaught()));
        return this;
    }

    /**
     * Adds a legendary caught pokemon counter to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addLegendCaught() {
        String prefix = ConfigManager.getLangNode("Info", "LEGENDARIES").getString();
        this.contents.add(Texts.of(prefix + getLegends()));
        return this;
    }

    /**
     * Adds bank balance to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addBank() {
        String prefix = ConfigManager.getLangNode("Info", "BALANCE").getString();
        this.contents.add(Texts.of(prefix + getBal()));
        return this;
    }

    public InfoBuilderAPI addBase() {
        String prefix = ConfigManager.getLangNode("Info", "BASE").getString();
        String notSet = ConfigManager.getLangNode("Info", "NOT_SET").getString();

        if(hasBase())
            this.contents.add(Texts.of(prefix + (int) getX() + ", " + (int) getY() + ", " + (int) getZ()));
        else
            this.contents.add(Texts.of(prefix + notSet));

        return this;
    }

    /**
     * Adds a list of all team members to the pagination builder
     * @return the current builder
     */
    public InfoBuilderAPI addPlayerList() {
        List<String> owners = new ArrayList<>();
        List<String> captains = new ArrayList<>();
        List<String> officers = new ArrayList<>();
        List<String> members = new ArrayList<>();
        List<String> grunts = new ArrayList<>();

        for(User member : getAllMembers()) {
            String name = member.getName();
            UUID uuid = member.getUniqueId();
            String rank = getStorNode("Teams", team, "Members", uuid.toString()).getString();

            if(rank.equals(Ranks.OWNER.getName()))
                owners.add(name);
            else if(rank.equals(Ranks.CAPTAIN.getName()))
                captains.add(name);
            else if(rank.equals(Ranks.OFFICER.getName()))
                officers.add(name);
            else if(rank.equals(Ranks.MEMBER.getName()))
                members.add(name);
            else
                grunts.add(name);
        }

        addMembers(owners, Ranks.OWNER);
        addMembers(captains, Ranks.CAPTAIN);
        addMembers(officers, Ranks.OFFICER);
        addMembers(members, Ranks.MEMBER);
        addMembers(grunts, Ranks.GRUNT);

        return this;
    }

    private void addMembers(List<String> group, Ranks rank) {
        if(!group.isEmpty()) {
            String line = "&c" + rank.getTranslatedName() + "s: ";
            for(String member: group)
                line += "&e" + member + ", ";
            this.contents.add(Texts.of(line.substring(0, line.length() - 2)));
        }
    }

    /**
     * Sends the pagination list to the source declared in {@link #InfoBuilderAPI(String, Player)}
     */
    public void send() {
        if(teamExists()) {
            PaginationList.builder()
                    .title(title)
                    .padding(PADDING)
                    .contents(contents).
                    sendTo(src);
        }
    }

    /**
     * Sends the pagination list to the source declared in {@link #InfoBuilderAPI(String, Player)}
     * Will messages the source if they are not in a team
     */
    public void sendWithFeedback() {
        if(inTeam()) {
            PaginationList.builder()
                    .title(title)
                    .padding(PADDING)
                    .contents(contents).
                    sendTo(src);
        } else if(teamExists()) {
            PaginationList.builder()
                    .title(title)
                    .padding(PADDING)
                    .contents(contents).
                    sendTo(src);
        } else {
            src.sendMessage(ErrorMessage.NOT_IN_TEAM.getText(src));
        }
    }

    /**
     * @param player to send the pagination list to
     * Sends the pagination list to the designated player
     */
    public void sendTo(Player player) {
        if(teamExists()) {
            PaginationList.builder()
                    .title(title)
                    .padding(PADDING)
                    .contents(contents).
                    sendTo(player);
        }
    }
}
