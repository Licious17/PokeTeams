package io.github.tsecho.poketeams.configuration.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class PlaceholderSettings {

    @Setting("Default-TeamName") public String defaultTeamName;
    @Setting("Default-TeamTag") public String defaultTeamTag;
    @Setting("Formatted-TeamTag") public String formattedTeamTag;
    @Setting("Default-AllianceName") public String defaultAllyName;
}
