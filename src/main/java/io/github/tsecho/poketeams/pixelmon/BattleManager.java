package io.github.tsecho.poketeams.pixelmon;

import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.economy.EconManager;
import io.github.tsecho.poketeams.enums.Messages.QueueMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.PokeTeams;
import io.github.tsecho.poketeams.utilities.Utils;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.TypeTokens;

import java.math.BigDecimal;
import java.util.Map;

public class BattleManager {

	private BattleEndEvent event;
	private Player winner, loser;
	private boolean winnerBool, loserBool;
	private PokeTeamsAPI role, roleOther;

	public BattleManager(BattleEndEvent e) {
		
		event = e;
		
		if(isHuman()) {

			//Possibly redundant but error persists so check is in place \_(0_0)_/
			if(winner != null && loser != null) {

				role = new PokeTeamsAPI(winner);
				roleOther = new PokeTeamsAPI(loser);

				if(areBattleTeams() || recordBattles()) {
					if(inQueue()) {
						addStats();
						sendCommands();
						messagePlayers();
						removeQueue();
					}
				}
			}

		} else if(isWildWin()) {
			role = new PokeTeamsAPI(winner);
			addWildStats();
		}
	}
	
	//adding stats after battle
	private void addWildStats() {
		if(role.inTeam())
			role.addAmountKilled(1);
	}
	
	//adding stats after battle
	private void addStats() {
		role.addWin(1);
		roleOther.addLoss(1);
	}
	
	//sending message results
	private void messagePlayers() {
		if(ConfigManager.getConfNode("Battle-Settings", "Message-Winners").getBoolean())
			winner.sendMessage(QueueMessages.BATTLE_WON.getText(winner));

		if(ConfigManager.getConfNode("Battle-Settings", "Message-Losers").getBoolean())
			loser.sendMessage(QueueMessages.BATTLE_LOST.getText(loser));
	}

	//sending commands from config
	private void sendCommands() {
		if(ConfigManager.getConfNode("Battle-Settings", "Give-Winner-Rewards").getBoolean()) {
			try {
				for(String i : ConfigManager.getConfNode("Battle-Settings", "Winner-Rewards").getList(TypeTokens.STRING_TOKEN)) {
					if(i.contains("[MONEY=")) {

						EconManager econ = new EconManager(winner);

						if(econ.isEnabled()) {

							BigDecimal cost = BigDecimal.valueOf(Integer.valueOf(i.replaceAll("\\D+","")));
							econ.pay(cost);
							winner.sendMessage(Texts.of(SuccessMessages.MONEY_REWARD.getString(winner).replaceAll("%price%", cost.toPlainString()), winner));

						} else {
							PokeTeams.getLogger().error("Economy plugin is not available! Please add one for rewards to work properly");
						}

					} else {
						Sponge.getCommandManager().process(Sponge.getServer().getConsole(), i.replaceAll("%player%", winner.getName()));
					}
				}
			} catch (ObjectMappingException e) {
				e.printStackTrace();
			}
		}
	}
	
	//removes player from external queue
	private void removeQueue() {
		Utils.removeQueue(winner.getName());
		Utils.removeQueue(loser.getName());
	}
	
	private boolean inQueue() {
		
		//if the option is on, make sure they are in the queue. If its off, send commands
		if(ConfigManager.getConfNode("Battle-Settings", "Record-Only-queue").getBoolean())
			return Utils.inQueue(winner.getName()) && Utils.inQueue(loser.getName());
		return true;
	}
	
	//checking if they are players
	private boolean isHuman() {
		
		//looping through battle participants to get the loser and winner
        for (Map.Entry<BattleParticipant, BattleResults> entry : event.results.entrySet()) {
			if((entry.getKey().getEntity() instanceof Player)) {
				if (entry.getValue() == BattleResults.VICTORY) {
					winner = (Player) entry.getKey().getEntity();
					winnerBool = true;
				} else if (entry.getValue() == BattleResults.DEFEAT) {
					loser = (Player) entry.getKey().getEntity();
					loserBool = true;
				}
			}
        }

        //if there was a loser and winner, set their points
        if(loserBool && winnerBool)
        	return true;
        else
        	return winnerBool = loserBool = false;
	}
	
	//checking if there is a winner of a wild battle
	private boolean isWildWin() {
		
		for (Map.Entry<BattleParticipant, BattleResults> entry : event.results.entrySet()) {
            
        	if ((entry.getValue() == BattleResults.VICTORY) && (entry.getKey().getEntity() instanceof Player)) {
				winner = (Player) entry.getKey().getEntity();
				winnerBool = true;
			} else if((entry.getValue() == BattleResults.DEFEAT) && (entry.getKey().getEntity() instanceof EntityPixelmon)) {
            	loserBool = true;
        	}
        }

		return (winnerBool && loserBool);
	}
	
	//if it should record all battles not just teams
	private boolean recordBattles() {
		return ConfigManager.getConfNode("Battle-Settings", "Record-All-Battles").getBoolean();
	}
	
	//if both players are on different teams
	private boolean areBattleTeams() {
		return (role.inTeam() && roleOther.inTeam()) && (!role.getTeam().equals(roleOther.getTeam()));
	}
}
