package io.github.tsecho.poketeams.pixelmon;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.QueueMessages;
import io.github.tsecho.poketeams.utilities.WorldInfo;
import io.github.tsecho.poketeams.utilities.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

import io.github.tsecho.poketeams.PokeTeams;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.world.World;

public class QueueManager {

	public static ArrayList<String> queue = new ArrayList<String>();
	private static final Random RANDOM = new Random();
	private String playerName1, playerName2;

	public static void choosePlayers() {
		if(queue.size() > 1) {
			String playerName1 = queue.get(0);
			String playerName2;
			if(queue.size() > 2) {
				playerName2 = queue.get(RANDOM.nextInt(queue.size() - 1) + 1);
			} else {
				playerName2 = queue.get(1);
			}
			new QueueManager(playerName1, playerName2);
		}
	}
	
	public QueueManager(String playerName1, String playerName2) {

		this.playerName1 = playerName1;
		this.playerName2 = playerName2;

		if(Sponge.getServer().getPlayer(playerName1).isPresent() && Sponge.getServer().getPlayer(playerName2).isPresent()) {
			
			Player player1 = Sponge.getServer().getPlayer(playerName1).get();
			Player player2 = Sponge.getServer().getPlayer(playerName2).get();			
			PokeTeamsAPI role1 = new PokeTeamsAPI(player1);
			PokeTeamsAPI role2 = new PokeTeamsAPI(player2);

			if((role1.inTeam() && role2.inTeam()) && (!role1.getTeam().equals(role2.getTeam()))) {

				player1.sendMessage(QueueMessages.START_BATTLE.getText(player1));
				player2.sendMessage(QueueMessages.START_BATTLE.getText(player2));

				removeQueue();
			
				Task.builder()
					.delay(5, TimeUnit.SECONDS)
					.execute(() -> forceBattle())
					.submit(PokeTeams.getInstance());
			}			
		}
	}
	
	private void forceBattle() {
		if(Sponge.getServer().getPlayer(playerName1).isPresent() && Sponge.getServer().getPlayer(playerName2).isPresent()) {

			Player player1 = Sponge.getServer().getPlayer(playerName1).get();
			Player player2 = Sponge.getServer().getPlayer(playerName2).get();

			EntityPixelmon poke1 = Pixelmon.storageManager.getParty(player1.getUniqueId()).getTeam().get(0).getOrSpawnPixelmon((Entity) player1);
			PlayerParticipant battler1 = new PlayerParticipant((EntityPlayerMP) player1, poke1);	
			
			EntityPixelmon poke2 = Pixelmon.storageManager.getParty(player2.getUniqueId()).getTeam().get(0).getOrSpawnPixelmon((Entity) player2);
			PlayerParticipant battler2 = new PlayerParticipant((EntityPlayerMP) player2, poke2);

			if(WorldInfo.useArena())
				warp(player1, player2);

			BattleRegistry.startBattle(new BattleParticipant[]{battler1}, new BattleParticipant[]{battler2}, new BattleSettings().getSettings());

		} else {
			Sponge.getServer().getPlayer(playerName1).ifPresent(player -> player.sendMessage(ErrorMessages.NOT_ONLINE.getText(player)));
			Sponge.getServer().getPlayer(playerName2).ifPresent(player -> player.sendMessage(ErrorMessages.NOT_ONLINE.getText(player)));
		}
	}

	private void removeQueue() {
		queue.remove(playerName1);
		queue.remove(playerName2);
		Utils.addQueue(playerName1);
		Utils.addQueue(playerName2);
	}

	private void warp(Player a, Player b) {

		String worldName = ConfigManager.getConfNode("Battle-Settings", "Arena", "World").getString();

		if(worldName.equalsIgnoreCase("default")) {
			a.setLocation(WorldInfo.getPosA(), WorldInfo.getWorldUUID());
			b.setLocation(WorldInfo.getPosB(), WorldInfo.getWorldUUID());
		} else if (Sponge.getServer().getWorld(worldName).isPresent()) {
			World world =  Sponge.getServer().getWorld(worldName).get();
			a.setLocation(WorldInfo.getPosA(), world.getUniqueId());
			b.setLocation(WorldInfo.getPosB(), world.getUniqueId());
		} else {
			a.setLocation(WorldInfo.getPosA(), WorldInfo.getWorldUUID());
			b.setLocation(WorldInfo.getPosB(), WorldInfo.getWorldUUID());
			PokeTeams.getLogger().error("The world name in the configuration is not a valid world name!");
		}
	}
}
