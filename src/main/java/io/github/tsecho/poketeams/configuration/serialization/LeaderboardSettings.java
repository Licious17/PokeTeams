package io.github.tsecho.poketeams.configuration.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class LeaderboardSettings {

    @Setting("CAUGHT")
    public int caughtAverage;

    @Setting("LEGENDS")
    public int legendsAverage;

    @Setting("KILLS")
    public int killsAverage;

    @Setting("BAL")
    public int balAverage;

    @Setting("RECORD")
    public int recordAverage;
}
