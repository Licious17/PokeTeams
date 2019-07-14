package io.github.tsecho.poketeams.configuration.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class AllySettings {
    @Setting("Chat-Settings") public Chat chat = new Chat();

    @ConfigSerializable
    public static class Chat {
        @Setting("Chat-Color") public String chatColor;
        @Setting("Prefix") public String prefix;
    }
}