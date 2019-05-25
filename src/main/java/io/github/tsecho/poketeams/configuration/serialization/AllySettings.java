package io.github.tsecho.poketeams.configuration.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class AllySettings {

    @Setting
    public Roles roles = new Roles();

    @ConfigSerializable
    public static class Roles {

        @Setting
        public Chat chat = new Chat();

        @ConfigSerializable
        public static class Chat {

            @Setting("Chat-Color")
            public String chatColor;

            @Setting("Prefix")
            public String prefix;
        }
    }
}