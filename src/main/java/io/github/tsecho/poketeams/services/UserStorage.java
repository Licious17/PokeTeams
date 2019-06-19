package io.github.tsecho.poketeams.services;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Optional;
import java.util.UUID;

public class UserStorage {

    private static UserStorage INSTANCE;
    private static UserStorageService service;

    private UserStorage() {}

    public static UserStorage getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new UserStorage();

            INSTANCE.service = Sponge.getServiceManager().provide(UserStorageService.class).get();
        }

        return INSTANCE;
    }

    /**
     * @param uuid to get the user from
     * @return the optional user if the UUID belonged to a player
     */
    public Optional<User> getUserFromUUID(UUID uuid) {
        return service.get(uuid);
    }

    /**
     * @param name from the user
     * @return the optional User from the username
     */
    public Optional<User> getUserFromName(String name) {
        return service.get(name);
    }

    /**
     * @param name from the user
     * @return the UUID of the player if it is present
     */
    public Optional<UUID> getUUIDFromName(String name) {
        if(service.get(name).isPresent()) {
            return Optional.of(service.get(name).get().getUniqueId());
        } else {
            return Optional.empty();
        }
    }
}
