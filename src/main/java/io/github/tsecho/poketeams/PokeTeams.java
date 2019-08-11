package io.github.tsecho.poketeams;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.tsecho.poketeams.apis.PlaceholderAPI;
import io.github.tsecho.poketeams.commands.Base;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.eventlisteners.*;
import io.github.tsecho.poketeams.services.UserStorage;
import io.github.tsecho.poketeams.utilities.Tasks;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;
import java.util.UUID;

@Plugin(id = PokeTeams.ID, 
		name = PokeTeams.NAME, 
		authors = PokeTeams.AUTHORS, 
		description = PokeTeams.DESCRIPTION,
		version = PokeTeams.VERSION,
		dependencies = {@Dependency(id = Pixelmon.MODID, version = Pixelmon.VERSION), 
					    @Dependency(id = "placeholderapi", optional = true)})

public class PokeTeams {

	public static final String ID = "poketeams";
	public static final String NAME = "PokeTeams";
	public static final String VERSION = "4.0.8";
	public static final String AUTHORS = "TSEcho";
	public static final String DESCRIPTION = "Teams plugin with Pixelmon Reforged Support";

	@Inject private Logger logger;
	@Inject @ConfigDir(sharedRoot = false) private Path dir;
	@Inject private PluginContainer container;
	private static PokeTeams instance;
	private static UUID worldUUID;

    @Listener
	public void onPreInit(GamePreInitializationEvent e) {
		instance = this;
		ConfigManager.setup(dir);
	}
	
	@Listener
	public void onInit(GameInitializationEvent e) {
		Sponge.getCommandManager().register(instance, Base.build(), "poketeams", "teams", "team");
		Sponge.getEventManager().registerListener(this, MessageChannelEvent.Chat.class, Order.FIRST, new ChatListener());
		Sponge.getEventManager().registerListeners(this, new GameListener());
		Pixelmon.EVENT_BUS.register(new CatchPokemonListener());
		Pixelmon.EVENT_BUS.register(new BattleListener());
	}

	@Listener
    public void onPostInit(GamePostInitializationEvent e) {
        PlaceholderAPI.getOrCreate();
        UserStorage.getOrCreate();
    }

	@Listener
	public void onStart(GameStartedServerEvent e) {
		worldUUID = Sponge.getServer().getDefaultWorld().get().getUniqueId();
		Tasks.start();
	}

	public static PokeTeams getInstance() {
		return instance;
	}

	public static Logger getLogger() {
        return instance.logger;
    }

    public static PluginContainer getContainer() {
        return instance.container;
    }

	public static UUID getBaseWorld() {
		return worldUUID;
	}
}
