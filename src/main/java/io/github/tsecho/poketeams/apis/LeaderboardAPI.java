package io.github.tsecho.poketeams.apis;

import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.LeaderboardTypes;
import io.github.tsecho.poketeams.language.Texts;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.text.DecimalFormat;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class LeaderboardAPI {

    private static final DecimalFormat df = new DecimalFormat("##.#");
    private LeaderboardTypes type;
    private HashMap<String, Double> data;
    private ArrayList<Text> contents;

    /**
     * @see LeaderboardTypes
     * @param type of leaderboard to send
     */
    public LeaderboardAPI(LeaderboardTypes type) {
        this.type = type;
        this.contents = new ArrayList<>();
        this.data = new HashMap<>();
        this.type = type;

        if(type == LeaderboardTypes.RECORD)
            buildRecord();
        else if(type == LeaderboardTypes.BAL)
            buildBalance();
        else if(type == LeaderboardTypes.RATING)
            buildRating();
        else
            buildStatistic();
    }

    private void buildRecord() {
        for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigManager.getStorNode("Teams")
                .getChildrenMap().entrySet()) {

            PokeTeamsAPI team = new PokeTeamsAPI(teams.getKey().toString(), true);
            data.put(teams.getKey().toString(), (double) team.getRatio());
        }
        sort();
        int num = 1;
        for(String i : data.keySet()) {
            contents.add(Texts.of("&a" + num + ".) " + i + "  -  [W/L] " + df.format(data.get(i)) + "%"));
            num++;
        }
    }

    private void buildStatistic() {
        for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigManager.getStorNode("Teams")
                .getChildrenMap().entrySet()) {

            String team = teams.getKey().toString();
            String value = type.name().substring(0, 1) + type.name().substring(1).toLowerCase();
            double stat = ConfigManager.getStorNode("Teams", team, "Stats", value).getInt();
            data.put(teams.getKey().toString(), stat);
        }
        sort();
        int num = 1;
        for(String i : data.keySet()) {
            contents.add(Texts.of("&a" + num + ".) " + i + "  -  " + data.get(i).intValue() + " " + type.getMessageName()));
            num++;
        }
    }

    private void buildBalance() {
        for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigManager.getStorNode("Teams")
                .getChildrenMap().entrySet()) {

            String team = teams.getKey().toString();
            double stat = ConfigManager.getStorNode("Teams", team, "Stats", "Bal").getInt();
            data.put(teams.getKey().toString(), stat);
        }
        sort();
        int num = 1;
        for(String i : data.keySet()) {
            contents.add(Texts.of("&a" + num + ".) " + i + "  -  $" + data.get(i)));
            num++;
        }
    }

    private void buildRating() {
        for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigManager.getStorNode("Teams")
                .getChildrenMap().entrySet()) {

            String name = teams.getKey().toString();
            PokeTeamsAPI team = new PokeTeamsAPI(name, true);
            data.put(teams.getKey().toString(), team.getRating());
        }
        sort();
        int num = 1;
        for(String i : data.keySet()) {
            contents.add(Texts.of("&a" + num + ".) " + i + "  -  " + df.format(data.get(i)) + " " + type.getMessageName()));
            num++;
        }
    }

    private void sort() {
        data = data
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
    }

    /**
     *
     * @param player to send the leaderboard to
     */
    public void sendTo(Player player) {
        PaginationList.builder()
                .title(Text.of(TextColors.GREEN, type.getTitleName() + " Leaderboard"))
                .padding(Text.of(TextColors.YELLOW, "="))
                .linesPerPage(12)
                .contents(contents)
                .sendTo(player);
    }
}
