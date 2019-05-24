package io.github.tsecho.poketeams.pixelmon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.github.tsecho.poketeams.PokeTeams;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.messages.ErrorMessage;
import io.github.tsecho.poketeams.enums.messages.QueueMessage;
import io.github.tsecho.poketeams.utilities.Utils;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getSettings;

public class QueueManager {

	@Getter private static ArrayList<String> queue = new ArrayList();
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

				player1.sendMessage(QueueMessage.START_BATTLE.getText(player1));
				player2.sendMessage(QueueMessage.START_BATTLE.getText(player2));

				removeQueue();
			
				Task.builder()
					.delay(5, TimeUnit.SECONDS)
					.execute(this::forceBattle)
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

			if(getSettings().battle.arena.isEnabled)
				warp(player1, player2);

			BattleRegistry.startBattle(new BattleParticipant[]{battler1}, new BattleParticipant[]{battler2}, new BattleSettings().getRules());

		} else {
			Sponge.getServer().getPlayer(playerName1).ifPresent(player -> player.sendMessage(ErrorMessage.NOT_ONLINE.getText(player)));
			Sponge.getServer().getPlayer(playerName2).ifPresent(player -> player.sendMessage(ErrorMessage.NOT_ONLINE.getText(player)));
		}
	}

	private void removeQueue() {
		queue.remove(playerName1);
		queue.remove(playerName2);
		Utils.addQueue(playerName1);
		Utils.addQueue(playerName2);
	}

	private void warp(Player a, Player b) {

		String worldName = getSettings().battle.arena.world;
		UUID uuid = PokeTeams.getWorldUUID();

		if(Sponge.getServer().getWorld(worldName).isPresent())
			uuid = Sponge.getServer().getWorld(worldName).get().getUniqueId();
		else if(!worldName.equalsIgnoreCase("default"))
			PokeTeams.getInstance().getLogger().error("The world name in the configuration is not a valid world name!");

		a.setLocation(getSettings().battle.arena.locA.getVector(), uuid);
		b.setLocation(getSettings().battle.arena.locB.getVector(), uuid);
	}
}
