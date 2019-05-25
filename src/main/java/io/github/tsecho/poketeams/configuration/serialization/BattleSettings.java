package io.github.tsecho.poketeams.configuration.serialization;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class BattleSettings {

    @Setting("Arena")
    public Arena arena = new Arena();

    @Setting("Competitive")
    public Competitive competitive = new Competitive();

    @Setting("Queue-Fee")
    public QueueFee queueFee = new QueueFee();

    @Setting("Winner-Rewards")
    public List<String> rewards;

    @Setting("Record-All-Battles")
    public boolean recordAllBattles;

    @Setting("Record-Only-Queue")
    public boolean recordOnlyQueue;

    @Setting("Give-Winner-Rewards")
    public boolean giveWinnerRewards;

    @Setting("Message-Losers")
    public boolean messageLosers;

    @Setting("Message-Winners")
    public boolean messageWinners;

    @Setting("Queue-Timer")
    public int queueTimer;

    @ConfigSerializable
    public static class Arena {

        @Setting("Enabled")
        public boolean isEnabled;

        @Setting("LocationA")
        public Location locA = new Location();

        @Setting("LocationB")
        public Location locB = new Location();

        @Setting("World")
        public String world;

        @ConfigSerializable
        public static class Location {

            public Vector3d getVector() {
                return new Vector3d(X,Y,Z);
            }

            @Setting("X")
            private double X;

            @Setting("Y")
            private double Y;

            @Setting("Z")
            private double Z;
        }
    }

    @ConfigSerializable
    public static class Competitive {

        @Setting("Rules")
        public Rules rules = new Rules();

        @ConfigSerializable
        public static class Rules {

            @Setting("TurmTime")
            public int turnTime;

            @Setting("LevelCap")
            public int levelCap;

            @Setting("RaiseToCap")
            public boolean raiseToCap;

            @Setting("FullHeal")
            public boolean fullHeal;
        }
    }

    @ConfigSerializable
    public static class QueueFee {

        @Setting("Enabled")
        public boolean isEnabled;

        @Setting("Price")
        public int price;
    }
}
