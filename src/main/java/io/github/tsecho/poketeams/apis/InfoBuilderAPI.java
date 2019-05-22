package io.github.tsecho.poketeams.apis;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.Ranks;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.language.Texts;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public InfoBuilderAPI(String team, Player src) {
        super(team, true);
        this.src = src;
        contents = new ArrayList<>();
    }

    /**
     *
     * @param member for the info generator to attempt to create
     * @param src for the info to be sent to
     */
    public InfoBuilderAPI(Player member, Player src) {
        super(member);
        this.src = src;
        contents = new ArrayList<>();
    }

    /**
     *
     * @param team for the info generator to attempt to create
     * @param src for the info to be sent to
     */
    public InfoBuilderAPI(String team, CommandSource src) {
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
        HashMap<String, String> owners = new HashMap<>();
        HashMap<String, String> captains = new HashMap<>();
        HashMap<String, String> officers = new HashMap<>();
        HashMap<String, String> members = new HashMap<>();
        HashMap<String, String> grunts = new HashMap<>();

        if(teamExists()) {
            for (Map.Entry<Object, ? extends CommentedConfigurationNode> players : ConfigManager
                    .getStorNode("Teams", team, "Members").getChildrenMap().entrySet()) {

                String rank = players.getValue().getString();
                UUID uuid = UUID.fromString(players.getKey().toString());
                String player = Sponge.getServiceManager().provide(UserStorageService.class).get().get(uuid).get().getName();

                if(rank.equals(Ranks.OWNER.getName()))
                    owners.put(player, rank);
                else if(rank.equals(Ranks.CAPTAIN.getName()))
                    captains.put(player, rank);
                else if(rank.equals(Ranks.OFFICER.getName()))
                    officers.put(player, rank);
                else if(rank.equals(Ranks.MEMBER.getName()))
                    members.put(player, rank);
                else
                    grunts.put(player, rank);
            }
        }

        if(!owners.isEmpty()) {
            String line = "&c" + Ranks.OWNER.getTranslatedName() + "s: ";
            for(String member: owners.keySet())
                line += "&e" + member + ", ";
            this.contents.add(Texts.of(line.substring(0, line.length() - 2)));
        }
        if(!captains.isEmpty()) {
            String line = "&c" + Ranks.CAPTAIN.getTranslatedName() + "s: ";
            for(String member: captains.keySet())
                line += "&e" + member + ", ";
            this.contents.add(Texts.of(line.substring(0, line.length() - 2)));
        }
        if(!officers.isEmpty()) {
            String line = "&c" + Ranks.OFFICER.getTranslatedName() + "s: ";
            for(String member: officers.keySet())
                line += "&e" + member + ", ";
            this.contents.add(Texts.of(line.substring(0, line.length() - 2)));
        }
        if(!members.isEmpty()) {
            String line = "&c" + Ranks.MEMBER.getTranslatedName() + "s: ";
            for(String member: members.keySet())
                line += "&e" + member + ", ";
            this.contents.add(Texts.of(line.substring(0, line.length() - 2)));
        }
        if(!grunts.isEmpty()) {
            String line = "&c" + Ranks.GRUNT.getTranslatedName() + "s: ";
            for(String member: grunts.keySet())
                line += "&e" + member + ", ";
            this.contents.add(Texts.of(line.substring(0, line.length() - 2)));
        }

        return this;
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
