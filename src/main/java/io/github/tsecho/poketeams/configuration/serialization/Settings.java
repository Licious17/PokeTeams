package io.github.tsecho.poketeams.configuration.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Settings {

    @Setting("Base-Settings")
    public BaseSettings base = new BaseSettings();

    @Setting("Ally-Settings")
    public AllySettings ally = new AllySettings();

    @Setting("Battle-Settings")
    public BattleSettings battle = new BattleSettings();

    @Setting("Chat-Settings")
    public ChatSettings chat = new ChatSettings();

    @Setting("Placeholder-Settings")
    public PlaceholderSettings placeholders = new PlaceholderSettings();

    @Setting("Leaderboard-Settings")
    public LeaderboardSettings leaderboard = new LeaderboardSettings();

    @Setting("Team-Settings")
    public TeamSettings team = new TeamSettings();
}
