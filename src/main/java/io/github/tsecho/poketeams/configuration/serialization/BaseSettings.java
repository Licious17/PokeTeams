package io.github.tsecho.poketeams.configuration.serialization;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class BaseSettings {

    @Setting("Only-Default-World")
    public boolean onlyDefaultWorld;
}
