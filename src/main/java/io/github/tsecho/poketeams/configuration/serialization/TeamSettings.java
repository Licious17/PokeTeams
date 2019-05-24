package io.github.tsecho.poketeams.configuration.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class TeamSettings {

    @Setting("Money")
    public Money money = new Money();

    @Setting("Name")
    public Name name = new Name();

    @Setting("NameTag")
    public Tag tag = new Tag();

    @Setting("Delete-When-Empty")
    public boolean deleteWhenEmpty;

    @Setting("Default-Team-Bal")
    public int defaultTeamBal;

    @Setting("Max-Members")
    public int maxMembers;

    @ConfigSerializable
    public static class Money {

        @Setting("Cost-Enabled")
        public boolean costEnabled;

        @Setting("Tax-Enabled")
        public boolean taxEnabled;

        @Setting("Creation-Cost")
        public int creationCost;

        @Setting("Tax")
        public int tax;

        @Setting("Tax-Timer")
        public List<String> taxTimes;
    }

    @ConfigSerializable
    public static class Name {

        @Setting("Max-Length")
        public int maxLength;

        @Setting("Use-Censor")
        public boolean useCensor;
    }

    @ConfigSerializable
    public static class Tag {

        @Setting("Max-Length")
        public int maxLength;

        @Setting("Use-Censor")
        public boolean useCensor;

        @Setting("Allow-Colors")
        public boolean allowColors;

        @Setting("Allow-Style")
        public boolean allowStyle;
    }
}
