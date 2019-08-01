package io.github.tsecho.poketeams.configuration;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Settings {

    @Setting("Base-Settings") public BaseSettings base = new BaseSettings();
    @Setting("Ally-Settings") public AllySettings ally = new AllySettings();
    @Setting("Battle-Settings") public BattleSettings battle = new BattleSettings();
    @Setting("Chat-Settings") public ChatSettings chat = new ChatSettings();
    @Setting("Placeholder-Settings") public PlaceholderSettings placeholders = new PlaceholderSettings();
    @Setting("Leaderboard-Settings") public LeaderboardSettings leaderboard = new LeaderboardSettings();
    @Setting("Team-Settings") public TeamSettings team = new TeamSettings();

    @ConfigSerializable
    public static class ChatSettings {
        @Setting("Chat-Color") public String chatColor;
        @Setting("SocialSpy-Message") public String spyMessage;
        @Setting("Prefix") public String prefix;
        @Setting("Console-SocialSpy") public boolean useConsoleSpy;
        @Setting("Player-SocialSpy") public boolean usePlayerSpy;
    }

    @ConfigSerializable
    public static class BaseSettings {
        @Setting("Only-Default-World") public boolean onlyDefaultWorld;
    }

    @ConfigSerializable
    public static class LeaderboardSettings {
        @Setting("CAUGHT") public int caughtAverage;
        @Setting("LEGENDS") public int legendsAverage;
        @Setting("KILLS") public int killsAverage;
        @Setting("BAL") public int balAverage;
        @Setting("RECORD") public int recordAverage;
    }


    @ConfigSerializable
    public static class AllySettings {
        @Setting("Chat-Settings") public Chat chat = new Chat();

        @ConfigSerializable
        public static class Chat {
            @Setting("Chat-Color") public String chatColor;
            @Setting("Prefix") public String prefix;
        }
    }

    @ConfigSerializable
    public static class PlaceholderSettings {
        @Setting("Default-TeamName") public String defaultTeamName;
        @Setting("Default-TeamTag") public String defaultTeamTag;
        @Setting("Formatted-TeamTag") public String formattedTeamTag;
        @Setting("Default-AllianceName") public String defaultAllyName;
    }

    @ConfigSerializable
    public static class TeamSettings {

        @Setting("Money") public Money money = new Money();
        @Setting("Name") public Name name = new Name();
        @Setting("NameTag") public Tag tag = new Tag();
        @Setting("Delete-When-Empty") public boolean deleteWhenEmpty;
        @Setting("Default-Team-Bal") public int defaultTeamBal;
        @Setting("Max-Members") public int maxMembers;

        @ConfigSerializable
        public static class Money {
            @Setting("Cost-Enabled") public boolean costEnabled;
            @Setting("Tax-Enabled") public boolean taxEnabled;
            @Setting("Creation-Cost") public int creationCost;
            @Setting("Tax") public int tax;
            @Setting("Tax-Timer") public List<String> taxTimes;
        }

        @ConfigSerializable
        public static class Name {
            @Setting("Max-Length") public int maxLength;
            @Setting("Use-Censor") public boolean useCensor;
        }

        @ConfigSerializable
        public static class Tag {
            @Setting("Max-Length") public int maxLength;
            @Setting("Use-Censor") public boolean useCensor;
            @Setting("Allow-Colors") public boolean allowColors;
            @Setting("Allow-Style") public boolean allowStyle;
        }
    }

    @ConfigSerializable
    public static class BattleSettings {

        @Setting("Arena") public Arena arena = new Arena();
        @Setting("Competitive") public Competitive competitive = new Competitive();
        @Setting("Queue-Fee") public QueueFee queueFee = new QueueFee();
        @Setting("Winner-Rewards") public List<String> rewards;
        @Setting("Record-All-Battles") public boolean recordAllBattles;
        @Setting("Record-Only-Queue") public boolean recordOnlyQueue;
        @Setting("Give-Winner-Rewards") public boolean giveWinnerRewards;
        @Setting("Message-Losers") public boolean messageLosers;
        @Setting("Message-Winners") public boolean messageWinners;
        @Setting("Queue-Timer") public int queueTimer;

        @ConfigSerializable
        public static class Arena {
            @Setting("Enabled") public boolean isEnabled;
            @Setting("LocationA") public Location locA = new Location();
            @Setting("LocationB") public Location locB = new Location();
            @Setting("World") public String world;

            @ConfigSerializable
            public static class Location {
                public Vector3d getVector() {
                    return new Vector3d(X,Y,Z);
                }

                @Setting("X") private double X;
                @Setting("Y") private double Y;
                @Setting("Z") private double Z;
            }
        }

        @ConfigSerializable
        public static class Competitive {
            @Setting("Rules") public Rules rules = new Rules();

            @ConfigSerializable
            public static class Rules {
                @Setting("TurmTime") public int turnTime;
                @Setting("LevelCap") public int levelCap;
                @Setting("RaiseToCap") public boolean raiseToCap;
                @Setting("FullHeal") public boolean fullHeal;
            }
        }

        @ConfigSerializable
        public static class QueueFee {
            @Setting("Enabled") public boolean isEnabled;
            @Setting("Price") public int price;
        }
    }

}
