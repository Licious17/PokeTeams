package io.github.tsecho.poketeams.configuration.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChatSettings {

    @Setting("Chat-Color")
    public String chatColor;

    @Setting("SocialSpy-Message")
    public String spyMessage;

    @Setting("Prefix")
    public String prefix;

    @Setting("Console-SocialSpy")
    public boolean useConsoleSpy;

    @Setting("Player-SocialSpy")
    public boolean usePlayerSpy;
}
