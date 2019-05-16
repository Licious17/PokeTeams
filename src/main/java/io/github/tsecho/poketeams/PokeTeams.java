package io.github.tsecho.poketeams;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import io.github.tsecho.poketeams.apis.PlaceholderAPI;
import io.github.tsecho.poketeams.commands.Base;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.ChatTypes;
import io.github.tsecho.poketeams.eventlisteners.ChatListener;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.pixelmon.BattleManager;
import io.github.tsecho.poketeams.pixelmon.CatchingManager;
import io.github.tsecho.poketeams.pixelmon.QueueManager;
import io.github.tsecho.poketeams.utilities.Tasks;
import io.github.tsecho.poketeams.utilities.Utils;
import io.github.tsecho.poketeams.utilities.WorldInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

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
	public static final String VERSION = "4.0.0-BETA1";
	public static final String AUTHORS = "TSEcho";
	public static final String DESCRIPTION = "Teams plugin with Pixelmon Reforged Support";
	
	private static PokeTeams instance;
	
	@Inject 
	private Logger logger;

	@Inject 
	@ConfigDir(sharedRoot = false)
	private Path dir;
	
	@Inject
	private PluginContainer container;

	@Listener
	public void onPreInit(GamePreInitializationEvent e) {
		instance = this;
		ConfigManager.setup(dir);
		ConfigManager.load();
		ConfigManager.update();
	}
	
	@Listener
	public void onInit(GameInitializationEvent e) {
		PlaceholderAPI.getInstance();
		Sponge.getCommandManager().register(instance, Base.build(), "poketeams", "teams", "team");
		Sponge.getEventManager().registerListener(this, MessageChannelEvent.Chat.class, Order.FIRST, new ChatListener());
		Pixelmon.EVENT_BUS.register(this);
	}
	
	@Listener
	public void onStart(GameStartedServerEvent e) {
		WorldInfo.init();
		/* Temporary method here to transition from player names to UUIDs */
		Utils.moveToUUID();
		new Tasks();
	}
	
	@Listener
	public void onReload(GameReloadEvent e) {
		ConfigManager.load();
		logger.info("PokeTeams has been reloaded!");
	}
	
	@Listener
	public void onLeave(ClientConnectionEvent.Disconnect e, @Root Player player) {
		ChatUtils.setChat(player.getName(), ChatTypes.PUBLIC);
		if(QueueManager.queue.contains(player.getName())) QueueManager.queue.remove(player.getName());
	}

	@Listener
	public void onJoin(ClientConnectionEvent.Join e, @Root Player player) {
		ChatUtils.setChat(player.getName(), ChatTypes.PUBLIC);
	}

	@SubscribeEvent
	public void onEndBattle(BattleEndEvent e) {
		new BattleManager(e);
	}

	@SubscribeEvent
	public void onCatch(CaptureEvent.SuccessfulCapture e) {
		new CatchingManager(e);
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
}
